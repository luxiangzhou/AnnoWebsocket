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
	public String test() {
		return "test";
	}

	@RequestMapping(value = { "/test1" }, method = RequestMethod.GET)
	@WSRequestMapping(value = { "/test1" })
	public String test1(String param1) {
		return testService.helloWebscoket(param1);
	}

	@RequestMapping(value = { "/test2" }, method = RequestMethod.GET)
	public String test2(String param1) {
		return testService.helloWebscoket(param1);
	}

	@RequestMapping(value = { "/test3" }, method = RequestMethod.GET)
	public String test3(String param1) {
		return testService.helloWebscoket(param1);
	}

}
