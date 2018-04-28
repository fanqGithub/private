//package com.commai.module_baselib.rxabout;
//
//import android.annotation.TargetApi;
//import android.os.Build;
//import android.util.ArrayMap;
//
//
//import org.reactivestreams.Subscription;
//
//import java.util.Set;
//
///**
// * Created by fanqi on 2018/4/27.
// * Description:
// */
//
//public class RxSubscribeManagerImp implements RxSubscribeManager<Object> {
//    private static RxSubscribeManagerImp sInstance = null;
//
//    private ArrayMap<Object, Subscription> maps;
//
//    public static RxSubscribeManagerImp get() {
//
//        if (sInstance == null) {
//            synchronized (RxSubscribeManagerImp.class) {
//                if (sInstance == null) {
//                    sInstance = new RxSubscribeManagerImp();
//                }
//            }
//        }
//        return sInstance;
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private RxSubscribeManagerImp() {
//        maps = new ArrayMap<>();
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    @Override
//    public void addSubscribe(Object tag, Subscription subscription) {
//        maps.put(tag, subscription);
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    @Override
//    public void removeSubscribe(Object tag) {
//        if (!maps.isEmpty()) {
//            maps.remove(tag);
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    public void removeAll() {
//        if (!maps.isEmpty()) {
//            maps.clear();
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    @Override public void cancel(Object tag) {
//        if (maps.isEmpty()) {
//            return;
//        }
//        if (maps.get(tag) == null) {
//            return;
//        }
//        if (!maps.get(tag).isUnsubscribed()) {
//            maps.get(tag).unsubscribed();
//            maps.remove(tag);
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    @Override public void cancelAll() {
//        if (maps.isEmpty()) {
//            return;
//        }
//        Set<Object> keys = maps.keySet();
//        for (Object apiKey : keys) {
//            cancel(apiKey);
//        }
//    }
//
//}
