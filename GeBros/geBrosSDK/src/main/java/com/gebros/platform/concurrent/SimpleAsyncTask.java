package com.gebros.platform.concurrent;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * Created by nairs77@joycity.com on 6/8/16.
 */
public class SimpleAsyncTask<Result> {

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    private ExecutorService executorService;

    private Thread asyncThread;

    // FutureTask
    private FutureTask asyncFutureTask;

    // In Background
    private ISimpleAsyncTask.AsyncFunc runOnBackgroundFunc;
    // In UIThread
    private ISimpleAsyncTask.AsyncUIFunc runOnUIThreadFunc;

    private Result result;

    public static void doRunUIThread(final ISimpleAsyncTask.OnUIThreadTask onUIThreadTask) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                onUIThreadTask.doRunUIThread();
            }
        });
    }

    public static void doInBackground(final ISimpleAsyncTask.OnBackgroundTask onBackgroundTask) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                onBackgroundTask.doOnBackgroundThread();
            }
        }).start();
    }

    public static FutureTask doInBackground(final ISimpleAsyncTask.OnBackgroundTask onBackgroundTask, ExecutorService executor) {
        FutureTask task = (FutureTask) executor.submit(new Runnable() {
            @Override
            public void run() {
                onBackgroundTask.doOnBackgroundThread();
            }
        });

        return task;
    }

    public void start() {
        if(runOnBackgroundFunc != null) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    result = (Result)runOnBackgroundFunc.doAsync();
                    onResult();
                }
            };

            if(getExecutorService() != null) {
                asyncFutureTask = (FutureTask) getExecutorService().submit(runnable);
            } else {
                asyncThread = new Thread(runnable);
                asyncThread.start();
            }
        }
    }

    public void cancel() {
        if(runOnBackgroundFunc != null) {
            if(executorService != null) {
                asyncFutureTask.cancel(true);
            } else {
                asyncThread.interrupt();
            }
        }
    }


    private void onResult() {
        if (runOnUIThreadFunc != null) {
            uiHandler.post(new Runnable() {
                @Override
                public void run() {
                    runOnUIThreadFunc.onResult(result);
                }
            });
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public ISimpleAsyncTask.AsyncFunc getBackgroundFunc() {
        return runOnBackgroundFunc;
    }

    public void setActionInBackground(ISimpleAsyncTask.AsyncFunc runOnBackgroundFunc) {
        this.runOnBackgroundFunc = runOnBackgroundFunc;
    }

    public ISimpleAsyncTask.AsyncUIFunc getUIFunc() {
        return runOnUIThreadFunc;
    }

    public void setUIFunc(ISimpleAsyncTask.AsyncUIFunc runOnUIThreadFunc) {
        this.runOnUIThreadFunc = runOnUIThreadFunc;
    }
}
