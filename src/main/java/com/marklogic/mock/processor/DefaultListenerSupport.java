package com.marklogic.mock.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.stereotype.Component;

@Component
public class DefaultListenerSupport extends RetryListenerSupport {
    private static final Logger logger = LoggerFactory.getLogger(DefaultListenerSupport.class);
    @Override
    public <T, E extends Throwable> void close(RetryContext context,
                                               RetryCallback<T, E> callback, Throwable throwable) {
        //logger.info("onClose");
        super.close(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> void onError(RetryContext context,
                                                 RetryCallback<T, E> callback, Throwable throwable) {
        logger.info("onError, attempt no:"+context.getRetryCount());
        logger.info("exception occured:"+throwable);
        super.onError(context, callback, throwable);
    }

    @Override
    public <T, E extends Throwable> boolean open(RetryContext context,
                                                 RetryCallback<T, E> callback) {
        logger.info("onOpen: retry activated, first attempt");

        return super.open(context, callback);
    }
}
