package com.gebros.platform.exception;

/**
 * Created by nairs on 2016-05-12.
 */
public interface BaseException {
    abstract int getErrorCode();
    abstract String getDetailError();
}
