package com.powerinbox.bern.webservice.service;

import com.powerinbox.bern.webservice.model.Split;
import com.powerinbox.bern.webservice.model.WebRequestBodyModel;

/**
 * PersistDataRunnable is a Runnable Class for inserting data
 * in parallel threads. Submitted to ExecutorService
 */
public class PersistDataRunnable implements Runnable {

    private PersistenceService persistenceService;
    private WebRequestBodyModel requestBodyModel;
    private Split split;

    /**
     * Constructor for PersistDataRunnable
     *
     * @param persistenceService PersistenceService
     * @param requestBodyModel The WebRequestBodyModel to be persisted
     * @param split The Split enum value for the related table
     */
    public PersistDataRunnable(PersistenceService persistenceService, WebRequestBodyModel requestBodyModel, Split split) {
        this.persistenceService = persistenceService;
        this.requestBodyModel = requestBodyModel;
        this.split = split;
    }

    @Override
    public void run() {
        if (split.equals(Split.COUNTRY)) {
            persistenceService.persistToCountry(requestBodyModel);
        } else if (split.equals(Split.ITEM_ID)) {
            persistenceService.persistToItem(requestBodyModel);
        } else if (split.equals(Split.DEVICE)) {
            persistenceService.persistToDevice(requestBodyModel);
        }

    }
}
