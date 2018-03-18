package com.commai.commaplayer.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.commai.commaplayer.utils.PermissionUtil;

/**
 * Created by fanqi on 2018/3/16.
 * Description:
 */

public class BaseFragment extends Fragment {

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
