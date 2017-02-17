package com.gebros.platform.sample;

/**
 * Created by gebros.nairs77@gmail.com on 2017. 2. 16..
 */

public abstract class RunnableWithArgs<T> implements Runnable {

    private T _runnableArg = null;

    public void setRunnableArgument(T arg)
    {
        _runnableArg = arg;
    }
    public T getRunnableArgument()
    {
        return _runnableArg;
    }

    @Override
    public void run()
    {
        runWithArgument(_runnableArg);
    }

    public abstract void runWithArgument(T runnableArgument);
}
