package org.kobjects.promise;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import org.eclipse.swt.graphics.Image;


public class Promise<T> implements Thenable<T> {

    enum State {
        PENDING, FULFILLED, REJECTED
    }

    private State state = State.PENDING;
    private T value;
    private Exception reason;
    private ArrayList<Runnable> onFullfilledList;
    private ArrayList<Runnable> onRejectedList;

    public Promise() {
    }

    public static<T> Promise<Void> all(Iterable<Promise<T>> promises) {
        final int[] expected = {0};
        final Promise<Void> result = new Promise<Void>();
        for (Promise<T> promise: promises) {
            expected[0]++;
            promise.then(new Function<T, Void>() {
                public Void call(T value) {
                    expected[0]--;
                    if (expected[0] == 0) {
                        result.resolve(null);
                    }
                    return null;
                }
            }, new Function<Exception, Void>() {
                @Override
                public Void call(Exception param) {
                    result.reject(param);
                    return null;
                }
            });
        }
        return result;
    }


    public Promise(Executor<T> executor) {
        executor.run(new Function<T, Void>() {
            @Override
            public Void call(T param) {
                resolve(param);
                return null;
            }
        }, new Function<Exception, Void>() {
            @Override
            public Void call(Exception reason) {
                reject(reason);
                return null;
            }
        });
    }

    synchronized public Promise<T> resolve(T value) {
        if (state != State.PENDING) {
            throw new IllegalStateException();
        }
        this.state = State.FULFILLED;
        this.value = value;
        if (onFullfilledList != null) {
            for (Runnable onFullfilled : onFullfilledList) {
                onFullfilled.run();
            }
        }
        onFullfilledList = null;
        onRejectedList = null;
        return this;
    }

    synchronized public Promise<T> reject(Exception reason) {
        if (state != State.PENDING) {
            throw new IllegalStateException();
        }
        this.reason = reason;
        if (onRejectedList != null) {
            for (Runnable onRejected : onRejectedList) {
                onRejected.run();
            }
        }
        onFullfilledList = null;
        onRejectedList = null;
        return this;
    }

    public <U> Promise<U> then(Function<T, U> onFulfilled) {
        return then(onFulfilled, null);
    }

    public synchronized <U> Promise<U> then(final Function<T, U> onFulfilled, final Function<Exception, Void> onRejected) {
        final Promise<U> result = new Promise<>();
        switch (state) {
            case FULFILLED:
                if (onFulfilled != null) {
                    try {
                        result.resolve(onFulfilled.call(value));
                    } catch (Exception e) {
                        result.reject(e);
                    }
                } else {
                    result.resolve(null);
                }
                break;
            case REJECTED:
                if (onRejected != null) {
                    try {
                        onRejected.call(reason);
                        result.reject(reason);
                    } catch (Exception e) {
                        result.reject(e);
                    }
                } else {
                    result.reject(reason);
                }
                break;
            case PENDING:
                if (onFullfilledList == null) {
                    onFullfilledList = new ArrayList<>();
                    onRejectedList = new ArrayList<>();
                }
                onFullfilledList.add(new Runnable() {
                    public void run() {
                        if (onFulfilled != null) {
                            try {
                                result.resolve(onFulfilled.call(value));
                            } catch (Exception e) {
                                result.reject(e);
                            }
                        } else {
                            result.resolve(null);
                        }
                    }
                });
                onRejectedList.add(new Runnable() {
                    @Override
                    public void run() {
                        if (onRejected == null) {
                            result.reject(reason);
                        } else {
                            try {
                               onRejected.call(reason);
                               result.reject(reason);
                            } catch(Exception e) {
                               result.reject(reason);
                            }
                        }
                }});
                break;
            default:
                throw new IllegalStateException();
        }
        return result;
    }

}
