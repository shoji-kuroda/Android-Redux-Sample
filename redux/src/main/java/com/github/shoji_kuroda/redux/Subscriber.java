package com.github.shoji_kuroda.redux;

/**
 * Subscriber
 * <p>
 * Storeの変更を受け取る
 * ActivityやFragment、Viewなどで実装する
 * <p>
 * Created by kuroda02 on 2016/07/07.
 */
public interface Subscriber {
    void onStateChanged();
}
