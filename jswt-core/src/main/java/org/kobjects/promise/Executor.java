package org.kobjects.promise;

public interface Executor<T> {
    void run(Function<T, Void> resolve, Function<Exception, Void> reject);
}
