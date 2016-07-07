package com.github.shoji_kuroda.redux;

import java.util.List;

/**
 * Subscription
 * <p>
 * Storeの変更を受け取る
 * <p>
 * Created by kuroda02 on 2016/07/05.
 */
public abstract class Subscription {

    private static final Subscription EMPTY = new Subscription() {
        @Override
        public void unsubscribe() {
        }
    };

    public abstract void unsubscribe();

    public static Subscription create(final List<Subscriber> subscribers, final Subscriber subscriber) {
        return new Subscription() {
            @Override
            public void unsubscribe() {
                subscribers.remove(subscriber);
            }
        };
    }

    public static Subscription empty() {
        return EMPTY;
    }
}
