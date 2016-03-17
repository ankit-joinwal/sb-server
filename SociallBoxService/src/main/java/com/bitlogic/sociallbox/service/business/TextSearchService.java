package com.bitlogic.sociallbox.service.business;

import com.bitlogic.sociallbox.data.model.ext.google.GooglePlaces;
import com.bitlogic.sociallbox.data.model.requests.TextSearchRequest;

public interface TextSearchService {

	public GooglePlaces search(TextSearchRequest textSearchRequest) ;
}
