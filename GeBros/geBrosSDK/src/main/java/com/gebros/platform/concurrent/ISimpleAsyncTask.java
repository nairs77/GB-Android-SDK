package com.gebros.platform.concurrent;

/**
 * Created by gebros.nairs77@gmail.com on 6/8/16.
 */
public interface ISimpleAsyncTask {

    public interface AsyncFunc<AsyncResult> {
        public AsyncResult doAsync();
    }

    public interface AsyncUIFunc<AsyncResult> {
        public void onResult(AsyncResult result);
    }

    public interface OnUIThreadTask {
        public void doRunUIThread();
    }

    public interface OnBackgroundTask {
        public void doOnBackgroundThread();
    }
}
