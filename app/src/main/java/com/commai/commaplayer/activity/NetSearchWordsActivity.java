package com.commai.commaplayer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.commai.commaplayer.Entity.SearchMusicResponse;
import com.commai.commaplayer.R;
import com.commai.commaplayer.adapter.OnlineMusicSearchItemAdapter;
import com.commai.commaplayer.listener.ClickItemTouchListener;
import com.commai.commaplayer.listener.SearchWords;
import com.commai.commaplayer.netservices.ApiMethod;
import com.commai.module_baselib.network.DefaultObserver;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NetSearchWordsActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnTouchListener, SearchWords {

    private SearchView mSearchView;
    private InputMethodManager mImm;
    private String queryString;

    private RecyclerView searchRV;

    private List<SearchMusicResponse.ResultBean.SongsBean> songsBeanList=new ArrayList<>();
    private OnlineMusicSearchItemAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_search);

        searchRV=findViewById(R.id.seach_rv);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        initView();

    }

    private void initView(){
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayout.VERTICAL);
        searchRV.setLayoutManager(manager);
        searchRV.setItemAnimator(new DefaultItemAnimator());
        mAdapter=new OnlineMusicSearchItemAdapter(this,songsBeanList);
        searchRV.setAdapter(mAdapter);
        searchRV.addOnItemTouchListener(new ClickItemTouchListener(searchRV) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {

                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                return false;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getResources().getString(R.string.search_net_music));

        mSearchView.setIconifiedByDefault(true);
        mSearchView.setIconified(true);

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.menu_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });

        menu.findItem(R.id.menu_search).expandActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {

        hideInputManager();

        if (query.equals(queryString)) {
            return true;
        }
        if (songsBeanList!=null){
            songsBeanList.clear();
        }
        queryString = query;
        if (!queryString.trim().equals("")) {
            ApiMethod.searchMusic(queryString, 1, 1, new DefaultObserver<SearchMusicResponse>(this) {
                @Override
                public void onSuccess(SearchMusicResponse response) {
                    songsBeanList.addAll(response.getResult().getSongs());
                    mAdapter.notifyDataSetChanged();
                }
            });
        } else {
            songsBeanList.clear();
            mAdapter.notifyDataSetChanged();
        }

        return true;
    }


    @Override
    public boolean onQueryTextChange(final String newText) {
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideInputManager();
        return false;
    }

    public void hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
            mSearchView.clearFocus();

//            SearchHistory.getInstance(this).addSearchString(mSearchView.getQuery().toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0,0);
        super.onPause();
    }

    @Override
    public void onSearch(String t) {
        mSearchView.setQuery(t, true);
    }
}
