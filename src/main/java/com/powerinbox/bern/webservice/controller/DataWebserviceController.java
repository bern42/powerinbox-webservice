package com.powerinbox.bern.webservice.controller;

import com.powerinbox.bern.webservice.model.QueryObject;
import com.powerinbox.bern.webservice.model.ResponseObject;
import com.powerinbox.bern.webservice.model.Split;
import com.powerinbox.bern.webservice.model.WebRequestBodyModel;
import com.powerinbox.bern.webservice.service.AsyncSubmissionService;
import com.powerinbox.bern.webservice.service.MapperService;
import com.powerinbox.bern.webservice.service.PersistenceService;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class DataWebserviceController {

    private static final Logger logger = LoggerFactory.getLogger(DataWebserviceController.class);

    @Autowired
    protected MapperService mapperService;
    @Autowired
    protected AsyncSubmissionService asyncSubmissionService;
    @Autowired
    protected PersistenceService persistenceService;

    /**
     * postRecords endpoint for POST request data
     * to be persisted asynchronously
     *
     * @param request
     * @return ResponseEntity contains String and HttpStatus.ACCEPTED
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    public ResponseEntity<String> postRecords(@RequestBody String request) {
        logger.debug("Request Received: " + request.toString());
        Validate.notNull(request, "Request must not be null");
        // Maps malformed JSON string into list of WebRequestBodyModel Objects
        List<WebRequestBodyModel> requestBodyModels = mapperService.mapStringRequestToListOfRequestModels(request);
        requestBodyModels.forEach(this::validateRequest);
        asyncSubmissionService.submitDataRequest(requestBodyModels);

        return new ResponseEntity<>("POST request received & is processing", HttpStatus.ACCEPTED);

    }

    /**
     * GET endpoint for querying for records by "spiltBy'
     *
     * @param startDay
     * @param startHour
     * @param endDay
     * @param endHour
     * @param splitByStr
     * @return List<ResponseObject>
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public ResponseEntity<List<ResponseObject>> getRecords(@RequestParam(value = "startDay") String startDay,
                                                           @RequestParam(value = "startHour") int startHour,
                                                           @RequestParam(value = "endDay") String endDay,
                                                           @RequestParam(value = "endHour") int endHour,
                                                           @RequestParam(value = "splitBy") String splitByStr) {
        Split splitBy = Split.findByDisplayName(splitByStr);
        if (splitBy == null) {
            return new ResponseEntity("Invalid splitBy value: " + splitByStr, HttpStatus.BAD_REQUEST);
        }
        QueryObject queryObject = mapperService.mapQueryObjectToUnixTime(new QueryObject(startDay, startHour, endDay, endHour, splitBy));
        logger.info("processing query: " + queryObject.toString());
        List<ResponseObject> results = persistenceService.processQueryRequest(queryObject);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    /**
     * Helper method to validate none of the fields except country are null.
     *
     * @param request
     */
    private void validateRequest(WebRequestBodyModel request) {
        Validate.notNull(request.getDevice(), "Request must contain device");
        Validate.notNull(request.getItemId(), "Request must contain item id");
        Validate.notNull(request.getTimestamp(), "Request must contain timestamp");
        Validate.notNull(request.getValue(), "Request must contain a value ");
    }
}
