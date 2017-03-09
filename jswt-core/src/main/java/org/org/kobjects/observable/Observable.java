package org.org.kobjects.observable;

import java.util.ArrayList;

public class Observable<T> {
    ArrayList<T> pending = new ArrayList<>();
    ArrayList<Entry<? super T>> subscriptions;

    public static<T> Observable<T> of(T... values) {
        Observable result = new Observable();
        for (T value : values) {
            result.pending.add(value);
        }
        return result;
    };

    public Subscription subscribe(Observer<? super T> observer) {
        Entry entry = new Entry(observer);
        synchronized (subscriptions) {
            subscriptions.add(entry);
        }
        return entry;
    }

    public Subscription subscribe(final Action1<? super T> onNext, final Action1<Throwable> onError, final Action0 onCompleted) {
        return subscribe(new Observer<T>() {
            @Override
            public void onNext(T value) {
                if (onNext != null) {
                    onNext.call(value);
                }
            }

            @Override
            public void onError(Throwable error) {
                if (onError != null) {
                    onError.call(error);
                }
            }

            @Override
            public void onCompleted() {
                if (onCompleted != null) {
                    onCompleted.call();
                }
            }
        });
    }


    private class Entry<T> implements Subscription {
        Observer<T> observer;
        boolean unsubscribed;
        Entry(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public boolean isUnsubscribed() {
            return unsubscribed;
        }

        @Override
        public void unsubscribe() {
            if (!isUnsubscribed()) {
                unsubscribed = true;
                synchronized (subscriptions) {
                    subscriptions.remove(this);
                }
            }
        }
    }


}
