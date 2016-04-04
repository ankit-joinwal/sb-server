package com.bitlogic.sociallbox.notification.service.business;

import java.util.List;

import org.springframework.util.concurrent.ListenableFuture;

import com.bitlogic.sociallbox.data.model.EventType;

public interface TestService {

	public ListenableFuture<List<EventType>> getallTypes();
}
