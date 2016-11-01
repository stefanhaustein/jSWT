package org.kobjects.jswt;

import org.kobjects.promise.Promise;

public interface PromiseDialog<T> {
    Promise<T> openPromise();
}
