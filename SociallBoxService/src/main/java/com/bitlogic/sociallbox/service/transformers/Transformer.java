package com.bitlogic.sociallbox.service.transformers;

import com.bitlogic.sociallbox.service.exception.ServiceException;

public interface Transformer<T,V> {
	
	public T transform(V v) throws ServiceException;
}
