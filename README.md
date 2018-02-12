# AnnoWebsocket
websocket请求用自定义注解@WSRequestMapping访问，类似springmvc @RequestMapping访问。
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

### 4、自定义注解 @WSRequestMapping
使用@WSRequestMapping注解，让websocket请求类似于springmvc @RequestMapping注解方式访问

![ws](ws.jpg)

#### 4.1Controller访问例子

```
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
```

其中@RequestMapping(value = { "/api/springmvc" })是spring mvc用于get/post的访问，
@WSRequestMapping(value = { "/api/websocket" })是仿springmvc @RequestMapping用于websocket的访问。

#### 4.2大体思想
1. 先写一个@WSRequestMapping注解用于写在类和方法上面；

```
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface WSRequestMapping {
	public abstract String[] value();
}
```

2. 项目启动的时候扫描所有的@WSRequestMapping注解，将扫描到的@WSRequestMapping注解及对应的类、方法保存到一个Map里面；

```
public class WSApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
	private static final Log LOGGER = LogFactory.getLog(WSApplicationListener.class);
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		// 判断不为空，防止进来两次；保证spring启动好了再扫描WSRequestMapping注解
		if (event != null && context.getParent() != null) {
			LOGGER.info("AnnoWebsocket mvc注解扫描 begin...");
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			long beginTime = new Date().getTime();
			LOGGER.info("begin time : " + dateFormatter.format(new Date()));

			Set<Class<?>> clazzs = getClasses(WSConstant.WS_SCAN_PACKAGE);
			int beanIndex = 1;
			for (Class<?> clazz : clazzs) {
				boolean annoFlag = clazz.isAnnotationPresent(WSRequestMapping.class);
				if (annoFlag) {
					// 从spring容器里面获取bean，不要自己反射获取bean,因为自己反射获取的bean中的注解对象是null，比如controller里面的注解获取的service为null
					Object bean = context.getBean(clazz);
					Class<?> beanClazz = bean.getClass();
					// 获取类上WSRequestMapping注解url
					WSRequestMapping clazzAnno = clazz.getAnnotation(WSRequestMapping.class);
					String[] clazzWsUrls = clazzAnno.value();
					for (String clazzWsUrl : clazzWsUrls) {
						// 获取方法上WSRequestMapping注解url
						Method[] methodAnnos = beanClazz.getDeclaredMethods();
						for (int i = 0; i < methodAnnos.length; i++) {
							Method method = methodAnnos[i];
							WSRequestMapping methodAnno = method.getAnnotation(WSRequestMapping.class);
							if (methodAnno != null) {
								String[] methodWsUrls = methodAnno.value();
								for (String methodWsUrl : methodWsUrls) {
									WSRequestMappingBean clazzBean = new WSRequestMappingBean();
									clazzBean.setBean(bean);
									clazzBean.setClazzWsUrl(clazzWsUrl);
									clazzBean.setMethod(method);
									clazzBean.setMethodWsUrl(methodWsUrl);
									// 使用ApplicationContext获取项目根目录
									String wsUrl = event.getApplicationContext().getApplicationName() + clazzWsUrl
											+ methodWsUrl;
									WSConstant.WS_CLAZZ_MAP.put(wsUrl, clazzBean);
									LOGGER.info("AnnoWebsocket Bean-" + (beanIndex++) + ":" + wsUrl);
								}
							}
						}
					}
				}
			}

			long endTime = new Date().getTime();
			LOGGER.info("end time : " + dateFormatter.format(new Date()));
			LOGGER.info("总耗时 : " + (endTime - beginTime) + "ms");
			LOGGER.info("AnnoWebsocket mvc注解扫描 done!");
		}

	}
	...
}
```

3. 用拦截器拦截websocket请求，到Map里面查看是否有这个websocket请求，有这个webscoket请求就用反射调用controlller层类、方法；

```
public class WSHandler implements WebSocketHandler {
	...

	@Override
	public void handleMessage(final WebSocketSession session, final WebSocketMessage<?> message) throws Exception {
		LOGGER.info("AnnoWebsocket handleMessage begin ....");
		EXECUTOR_SERVICE.submit(new Runnable() {
			@Override
			public void run() {
				try {
					handle(session, message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		LOGGER.info("AnnoWebsocket handleMessage end!");
	}

	private void handle(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		// websocket url
		URI uri = session.getUri();
		String wsUrl = uri.toString();
		// websocket message
		String wsMessage = (String) message.getPayload();

		// 反射调用业务方法，并将业务方法返回的数据返回给websocket
		if (WSConstant.WS_CLAZZ_MAP.containsKey(wsUrl)) {
			WSRequestMappingBean wsBean = WSConstant.WS_CLAZZ_MAP.get(wsUrl);
			Object bean = wsBean.getBean();
			// 方法
			Method wsBeanMethod = wsBean.getMethod();

			// 方法参数
			Class<?>[] parameterTypes = wsBeanMethod.getParameterTypes();
			LocalVariableTableParameterNameDiscoverer paramterDiscover = new LocalVariableTableParameterNameDiscoverer();
			String[] parameterNames = paramterDiscover.getParameterNames(wsBeanMethod);
			Object[] args = new Object[parameterTypes.length];

			// websocket message值
			JSONObject jsonObject = null;
			boolean isJSON = false;
			try {
				jsonObject = JSONObject.fromObject(wsMessage);
				isJSON = true;
			} catch (Exception e) {
				isJSON = false;
			}

			for (int i = 0; i < parameterTypes.length; i++) {
				Class<?> pClazz = parameterTypes[i];
				Object pValue = null;
				if ("java.lang.String".equals(pClazz.getName())) {
					if (isJSON) {
						pValue = jsonObject.get(parameterNames[i]);
					} else {
						pValue = wsMessage;
					}
				} else if ("java.lang.Booealn".equals(pClazz.getName()) || "boolean".equals(pClazz.getName())
						|| "int".equals(pClazz.getName())) {
					pValue = jsonObject.get(parameterNames[i]);
				} else {
					pValue = JSONObject.toBean(jsonObject, pClazz);
				}
				args[i] = pValue;
			}
			// 反射调用业务方法
			Object resObj = wsBeanMethod.invoke(bean, args);
			// 将业务方法返回的数据返回给websocket
			WSUtils.sendMessage(wsUrl, resObj.toString());
		}
	}
	...
}
```

4. 最后返回数据到前端websocket。

```
// 将业务方法返回的数据返回给websocket
WSUtils.sendMessage(wsUrl, resObj.toString());
```

### 5、Spring @MessageMapping
后来发现Spring对于WebSocket请求也做了封装，提供了一个@MessageMapping注解，功能类似@RequestMapping，它是存在于Controller中的，定义一个消息的基本请求，功能也跟@RequestMapping类似。

