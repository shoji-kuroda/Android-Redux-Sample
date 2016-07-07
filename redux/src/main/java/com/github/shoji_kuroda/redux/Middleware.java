package com.github.shoji_kuroda.redux;

/**
 * Middleware
 * <p>
 * Created by kuroda02 on 2016/07/05.
 */
public interface Middleware<A extends Action, S extends State> {
    void dispatch(Store<A, S> store, A action, Dispatcher<A> next);
}