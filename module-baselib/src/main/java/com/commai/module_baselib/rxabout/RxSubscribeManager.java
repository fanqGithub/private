package com.commai.module_baselib.rxabout;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * Created by fanqi on 2018/4/27.
 * Description:
 */

public interface RxSubscribeManager<T> {

    void addSubscribe(T tag, Subscription subscription);

    void removeSubscribe(T tag);

    void cancel(T tag);

    void cancelAll();

}
