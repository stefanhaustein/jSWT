package org.kobjects.promise;

public interface Function<T, U> {
    U call(T param);
}
