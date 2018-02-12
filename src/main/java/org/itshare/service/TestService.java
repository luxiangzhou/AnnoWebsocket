package org.itshare.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestService {
	public String helloWebscoket(String parm) {
		String result = "";
		if (StringUtils.isNoneBlank(parm)) {
			result = parm + "!";
		}
		return "Hi, " + result;
	}
}
