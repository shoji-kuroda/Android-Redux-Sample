package com.github.shoji_kuroda.redux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Store
 * <p>
 * Created by kuroda02 on 2016/07/05.
 */
public abstract class Store<A extends Action, S extends State> {

    static public <A extends Action, S extends State> CoreStore<A, S> create(
            S initialState, Reducer<A, S> reducer, Middleware<A, S>... middlewares) {
        return new CoreStore<>(initialState, reducer, middlewares);
    }

    public abstract Subscription subscribe(Subscriber subscriber);

    public abstract S getState();

    public abstract void dispatch(A action);

    static class CoreStore<A extends Action, S extends State> extends Store<A, S> {

        private static final int LISTENERS_INITIAL_CAPACITY = 100;

        private final List<Subscriber> subscribers;
        private final Reducer<A, S> reducer;
        private final AtomicBoolean isReducing;
        private final List<Dispatcher<A>> next = new ArrayList<>();

        private S currentState;

        CoreStore(S initialState, Reducer<A, S> reducer, Middleware<A, S>... middlewares) {
            this.reducer = reducer;
            this.currentState = initialState;
            this.subscribers = new ArrayList<>(LISTENERS_INITIAL_CAPACITY);
            this.isReducing = new AtomicBoolean(false);

            // deafult dispatch
            this.next.add(new Dispatcher<A>() {
                @Override
                public void dispatch(A action) {
                    defaultDispatch(action);
                }
            });
            // add middleware
            List<Middleware<A, S>> reversedMiddlewares = Arrays.asList(middlewares);
            Collections.reverse(reversedMiddlewares);
            final CoreStore store = this;
            for (final Middleware middleware : reversedMiddlewares) {
                final Dispatcher<A> n = next.get(0);
                next.add(0, new Dispatcher<A>() {
                    @Override
                    public void dispatch(A action) {
                        middleware.dispatch(store, action, n);
                    }
                });
            }
        }

        @Override
        public Subscription subscribe(Subscriber subscriber) {
            subscribers.add(subscriber);
            return Subscription.create(subscribers, subscriber);
        }

        @Override
        public S getState() {
            return currentState;
        }

        public void defaultDispatch(final A action) {
            checkState(!isReducing.get(), "Can not dispatch an action when an other action is being processed");

            isReducing.set(true);
            currentState = reduce(action, currentState);
            isReducing.set(false);

            notifyStateChanged();
        }

        @Override
        public void dispatch(final A action) {
            this.next.get(0).dispatch(action);
        }

        private S reduce(A action, S state) {
            return reducer.call(action, state);
        }

        private void notifyStateChanged() {

            // TODO: 2016/07/07 Subscriberをセットし直したほうがいい？

            for (int i = 0, size = subscribers.size(); i < size; i++) {
                subscribers.get(i).onStateChanged();
            }
        }

        private void checkState(boolean expression, Object errorMessage) {
            if (!expression) {
                throw new IllegalStateException(String.valueOf(errorMessage));
            }
        }
    }
}
