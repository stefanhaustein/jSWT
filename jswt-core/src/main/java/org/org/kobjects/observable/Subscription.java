package org.org.kobjects.observable;

public interface Subscription {
    boolean isUnsubscribed();
    void unsubscribe();
}
