package com.commai.commaplayer.base;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.commai.commaplayer.utils.PermissionUtil;

/**
 * @author:范启 Created on 2018/3/18.
 * Description:
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
