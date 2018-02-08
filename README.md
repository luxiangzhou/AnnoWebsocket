# AnnoWebsocket
websocket请求用自定义注解方式访问，类似于springmvc @RequestMapping注解方式访问。
 源码github地址：[https://github.com/luxiangzhou/AnnoWebsocket](https://github.com/luxiangzhou/AnnoWebsocket)
### 1、ajax长轮询
web异步请求一般用ajax实现，但是如果后端请求返回时间慢，而web异步请求又非常多，如果浏览器有超过6个ajax请求不能返回处于pending状态，就会导致浏览器卡死。这时又想有多个异步请求同时发出，又不想浏览器卡死，可以让后端立马返回一个token给ajax，然后js定时循环掉直到返回数据，这样可以保证请求不在pending状态，浏览器不会卡死。用token方式可以解决浏览器请求过多问题，但是还是要先返回token、还要轮询，很是麻烦，也不是真正的异步解决方法。可以用websocket代替。

### 2、websocket
WebSocket是HTML5开始提供的一种浏览器与服务器间进行全双工通讯的网络技术。
依靠这种技术可以实现客户端和服务器端的长连接，双向实时通信。
websocket的优越性不言自明，长连接的连接资源（线程资源）随着连接数量的增多，必会耗尽，
客户端轮询会给服务器造成很大的压力，而websocket是在物理层非网络层建立一条客户端至服务器的长连接，
以此来保证服务器向客户端的即时推送，既不耗费线程资源，又不会不断向服务器轮询请求。

### 3、spring+websocket整合
在实际开发中websocket请求到controller层按照websocket原生态方式访问还是比较麻烦的，
可以参照spring mvc注解方式进行访问、返回数据。

### 4、websocket请求用自定义注解方式访问，类似于springmvc @RequestMapping注解方式访问
#### 4.1Controller访问例子

```
@RestController
@RequestMapping(value = { "/api/springmvc" })
@WSRequestMapping(value = { "/api/websocket" })
public class TestController {
	@Autowired
	private TestService testService;

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
```

其中@RequestMapping(value = { "/api/springmvc" })是spring mvc用于get/post的访问，
@WSRequestMapping(value = { "/api/websocket" })是仿springmvc @RequestMapping用于websocket的访问。

#### 4.2大体思想
1. 先写一个@WSRequestMapping注解用于写在类和方法上面；
2. 项目启动的时候扫描所有的@WSRequestMapping注解，将扫描到的@WSRequestMapping注解及对应的类、方法保存到一个Map里面；
3. 用拦截器拦截websocket请求，到Map里面查看是否有这个websocket请求，有这个webscoket请求就用反射调用controlller层类、方法；
4. 最后返回数据到前端websocket。
