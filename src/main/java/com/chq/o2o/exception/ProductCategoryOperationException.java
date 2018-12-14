package com.chq.o2o.exception;

public class ProductCategoryOperationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1026725998728442784L;

	public ProductCategoryOperationException(String errMsg){
		super(errMsg);
	}
}
