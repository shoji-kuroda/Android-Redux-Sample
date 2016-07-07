package com.github.shoji_kuroda.redux;

/**
 * Storeの更新
 * <p>
 * Created by kuroda02 on 2016/07/07.
 */
public interface Dispatcher<A extends Action> {
    void dispatch(A action);
}
