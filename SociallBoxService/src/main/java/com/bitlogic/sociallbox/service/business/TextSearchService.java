package com.bitlogic.sociallbox.service.business;

import com.bitlogic.sociallbox.data.model.ext.Places;
import com.bitlogic.sociallbox.data.model.requests.TextSearchRequest;

public interface TextSearchService {

	public Places search(TextSearchRequest textSearchRequest) ;
}
