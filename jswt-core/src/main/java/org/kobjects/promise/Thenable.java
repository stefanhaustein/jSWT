package org.kobjects.promise;

public interface Thenable<T> {
    <U> Promise<U> then(Function<T, U> onFulfilled, Function<Exception, Void> onRejected);
}
