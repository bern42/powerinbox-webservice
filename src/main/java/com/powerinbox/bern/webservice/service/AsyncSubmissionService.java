package com.powerinbox.bern.webservice.service;

import com.powerinbox.bern.webservice.model.WebRequestBodyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsyncSubmissionService {

    @Autowired
    protected PersistenceService persistenceService;

    @Async
    public void submitDataRequest(List<WebRequestBodyModel> requestList) {
        persistenceService.processDataRequest(requestList);
    }
}
