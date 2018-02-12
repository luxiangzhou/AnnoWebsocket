package org.itshare.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service("testService")
public class TestService {
	public String helloWebscoket(String param) {
		String result = "";
		if (StringUtils.isNoneBlank(param)) {
			result = "Hi, " + param + "!";
		} else {
			result = "No Data!";
		}
		return result;
	}
}
