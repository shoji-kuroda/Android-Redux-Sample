package com.github.shoji_kuroda.redux;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * StoreTest
 * <p>
 * Created by kuroda02 on 2016/07/07.
 */
@RunWith(JUnit4.class)
public class StoreTest {

    private class TestAction implements Action {
        public String type;

        public TestAction() {
            this.type = "initial state";
        }

        public TestAction(String type) {
            this.type = type;
        }
    }

    private class TestState implements State {
        public String state;

        public TestState() {
            this.state = "unknown";
        }

        public TestState(String state) {
            this.state = state;
        }
    }

    private class TestSubscriber implements Subscriber {

        public boolean called = false;

        @Override
        public void onStateChanged() {
            this.called = true;
        }
    }

    private class TestMiddleware implements Middleware<TestAction, TestState> {

        public boolean called = false;

        @Override
        public void dispatch(Store<TestAction, TestState> store, TestAction action, Dispatcher<TestAction> next) {
            this.called = true;
            next.dispatch(action);
        }
    }

    @Test
    public void actionShouldBeReduced() throws Exception {
        TestAction action = new TestAction("to reducer");
        TestState state = new TestState();

        Reducer<TestAction, TestState> reducer = new Reducer<TestAction, TestState>() {
            @Override
            public TestState call(TestAction action, TestState state) {
                if (action.type.equals("to reducer")) {
                    return new TestState("reduced");
                } else {
                    return state;
                }
            }
        };
        Store<TestAction, TestState> store = Store.create(state, reducer);
        store.dispatch(action);
        assertThat("reduced", is(store.getState().state));
    }

    @Test
    public void storeShouldNotifySubscribers() {
        Store store = Store.create(
                new TestState(),
                new Reducer<TestAction, TestState>() {
                    @Override
                    public TestState call(TestAction action, TestState state) {
                        return new TestState();
                    }
                });
        TestSubscriber subscriber1 = new TestSubscriber();
        TestSubscriber subscriber2 = new TestSubscriber();

        store.subscribe(subscriber1);
        store.subscribe(subscriber2);
        store.dispatch(new TestAction("check"));

        assertThat(true, is(subscriber1.called));
        assertThat(true, is(subscriber2.called));
    }

    @Test
    public void storeShouldNotNotifyWhenUnsubscribed() {
        Store store = Store.create(
                new TestState(),
                new Reducer<TestAction, TestState>() {
                    @Override
                    public TestState call(TestAction action, TestState state) {
                        return new TestState();
                    }
                });
        TestSubscriber subscriber1 = new TestSubscriber();
        TestSubscriber subscriber2 = new TestSubscriber();

        store.subscribe(subscriber1);
        Subscription subscription = store.subscribe(subscriber2);
        subscription.unsubscribe();
        store.dispatch(new TestAction("check"));

        assertThat(true, is(subscriber1.called));
        assertThat(false, is(subscriber2.called));
    }

    @Test
    public void middlewareShouldBeReduced() {
        TestMiddleware middleware = new TestMiddleware();
        Store<TestAction, TestState> store = Store.create(
                new TestState(),
                new Reducer<TestAction, TestState>() {
                    @Override
                    public TestState call(TestAction action, TestState state) {
                        return new TestState("called");
                    }
                },
                middleware);
        store.dispatch(new TestAction());

        assertThat(true, is(middleware.called));
        assertThat("called", is(store.getState().state));
    }
}