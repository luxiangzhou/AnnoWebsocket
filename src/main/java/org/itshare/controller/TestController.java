package org.itshare.controller;

import org.itshare.service.TestService;
import org.itshare.websocket.annotation.WSRequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @WSRequestMapping是仿springmvc @RequestMapping用于websocket访问的注解
 * @author luxiangzhou
 *
 */
@RestController
@RequestMapping(value = { "/api/springmvc" })
@WSRequestMapping(value = { "/api/websocket" })
public class TestController {
	@Autowired
	private TestService testService;

	@RequestMapping(value = { "/test" }, method = RequestMethod.GET)
	@WSRequestMapping(value = { "/test" })
	public String test(String param) {
		return testService.helloWebscoket(param);
	}

}
