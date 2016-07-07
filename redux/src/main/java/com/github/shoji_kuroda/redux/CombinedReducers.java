package com.github.shoji_kuroda.redux;


import java.util.Arrays;
import java.util.List;

/**
 * Created by kuroda02 on 2016/07/05.
 */

public class CombinedReducers<A extends Action, S extends State> implements Reducer<A, S> {

    private final List<? extends Reducer<A, S>> reducers;

    @SafeVarargs
    public static <A extends Action, S extends State> CombinedReducers<A, S> from(Reducer<A, S>... reducers) {
        return new CombinedReducers<>(Arrays.asList(reducers));
    }

    public CombinedReducers(List<? extends Reducer<A, S>> reducers) {
        if (reducers.size() == 0) {
            throw new RuntimeException("CombinedReducer is empty. It needs one or more reducers.");
        }
        this.reducers = reducers;
    }

    @Override
    public S call(A action, S state) {
        for (Reducer<A, S> reducer : reducers) {
            state = reducer.call(action, state);
        }
        return state;
    }
}
