package com.meawallet.sdkregistry.in.http.webfilter;

public class WebFilterOrder {

    public static final int TIMEOUT = Integer.MIN_VALUE;
    public static final int TRACING = TIMEOUT + 1;
    public static final int REQUEST_LOGGING = TRACING + 100;
    public static final int AUTH = REQUEST_LOGGING + 100;

}
