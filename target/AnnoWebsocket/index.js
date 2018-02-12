var websocket = null;
//判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
    websocket = new WebSocket("ws://localhost:8080"+projectPath+"/api/websocket/test");
} else {
    alert('当前浏览器 Not support websocket')
}

//连接发生错误的回调方法
websocket.onerror = function () {
    $(".ws").append("<div style='color:red;'>WebSocket连接发生错误</div>");
};

//连接成功建立的回调方法
websocket.onopen = function () {
    $(".ws").append("<div style='color:red;'>WebSocket连接成功</div>");
}

//接收到消息的回调方法
websocket.onmessage = function (event) {
    var data = event.data;
    $(".ws").append("<div style='color:red;'>"+data+"</div>");
}

//连接关闭的回调方法
websocket.onclose = function () {
    $(".ws").append("<div style='color:red;'>WebSocket连接关闭</div>");
}

//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function () {
    closeWebSocket();
}


//关闭WebSocket连接
function closeWebSocket() {
    websocket.close();
}

//发送消息
$(".ws button").click(function(){
	var message = $(".ws input").val();
    websocket.send(message);
});
