package com.bitlogic.sociallbox.notification.service.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.bitlogic.Constants;
import com.bitlogic.sociallbox.data.model.EventType;
import com.bitlogic.sociallbox.notification.service.business.TestService;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

@RestController
@RequestMapping("/api/public/test")
public class TestController extends BaseController implements Constants {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TestController.class);

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Autowired
	private TestService testService;

	@RequestMapping("/async")
	@Async
	public DeferredResult<ResponseEntity<?>> async() {
		String LOG_PREFIX = "Async API";
		logRequestStart(LOG_PREFIX, PUBLIC_REQUEST_START_LOG, LOG_PREFIX);
		final DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
		
		logInfo(LOG_PREFIX, "Before Calling Test Service Timestamp {}",
				System.currentTimeMillis());
		ListenableFuture<List<EventType>> futureResponse = testService
				.getallTypes();
		logInfo(LOG_PREFIX, "Just after Calling Test Service Timestamp {}",
				System.currentTimeMillis());
		futureResponse
				.addCallback(new ListenableFutureCallback<List<EventType>>() {
					public void onSuccess(java.util.List<EventType> types) {
						for (EventType eventType : types) {
							logInfo("Async API", "Event Type {}", eventType);
						}
						ResponseEntity<List<EventType>> responseEntity = new ResponseEntity<>(
								types, HttpStatus.OK);
						deferredResult.setResult(responseEntity);
					};

					public void onFailure(Throwable arg0) {
						logError("Async API",
								"Failure in getting all event types", arg0);
					};
				});
		logInfo(LOG_PREFIX, "Returning response of async api at {}", System.currentTimeMillis());
		return deferredResult;
	}

}
