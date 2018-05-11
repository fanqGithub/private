package com.commai.module_baselib.testuse;

import android.util.Log;

/**
 * Created by fanqi on 2018/5/7.
 * Description:
 */

public class UserImp implements Action {


    @Override
    public void say(String s) {
        Log.d("Tag_USERIMP","我想大声对你说,动态代理学习"+s);
    }
}
