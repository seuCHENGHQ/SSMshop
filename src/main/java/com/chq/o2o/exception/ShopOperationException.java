package com.chq.o2o.exception;

public class ShopOperationException extends RuntimeException {
	/**
	 * 因为exception需要序列化，因此需要这个因子
	 */
	private static final long serialVersionUID = -2314754091096077734L;

	public ShopOperationException(String msg){
		super(msg);
	}
}
