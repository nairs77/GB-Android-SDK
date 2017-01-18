package com.gebros.platform.exception;

/**
 * Created by Joycity-Platform on 5/25/16.
 */
public class GBAPIException extends Exception implements BaseException {

    @Override
    public int getErrorCode() {
        return 0;
    }

    @Override
    public String getDetailError() {
        return "";
    }
}
