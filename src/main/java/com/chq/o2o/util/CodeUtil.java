package com.chq.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		String varifyCodeExpected = (String) request.getSession()
				.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		String varifyCodeActual = HttpServletRequestUtil.getString(request, "verifyCodeActual");
		if (varifyCodeActual == null || !varifyCodeActual.equals(varifyCodeExpected)) {
			return false;
		}
		return true;
	}
}
