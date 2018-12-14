package com.chq.o2o.exception;

public class ProductOperationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7164988869181372142L;

	public ProductOperationException(String errMsg) {
		super(errMsg);
	}
}
