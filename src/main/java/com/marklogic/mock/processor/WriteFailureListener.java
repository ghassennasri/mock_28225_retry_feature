package com.marklogic.mock.processor;

import com.marklogic.client.document.DocumentWriteOperation;
import com.marklogic.client.ext.batch.WriteListener;

import java.util.List;

public class WriteFailureListener implements WriteListener {
    public Throwable getErrorCaught() {
        return errorCaught;
    }

    private Throwable errorCaught;
    @Override
    public void onWriteFailure(Throwable ex, List<? extends DocumentWriteOperation> items) {
        errorCaught=ex;
    }


}
