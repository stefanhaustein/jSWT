package org.org.kobjects.observable;

public interface Observer<T> {
    void onNext(T value);
    void onError(Throwable error);
    void onCompleted();
}
