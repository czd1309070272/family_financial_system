websocket=null;
conectWebSocket($("#userid").val())
function conectWebSocket(user){
    //判断当前浏览器是否支持WebSocket
    try{
        if ('WebSocket'in window) {
            websocket = new WebSocket("ws://localhost:8080/webService/"+user);
        } else {
            alert('服务器已关闭')
        }
    }catch (e){
        websocketReconnect(user);
    }

    //连接发生错误的回调方法
    websocket.onerror = function() {
        setMessageInnerHTML("error");
    };
    //连接成功建立的回调方法
    websocket.onopen = function(event) {
        setConnectMessageInHTML("主服务器成功建立连接");
        // setMessageInnerHTML("Loc MSG: 成功建立连接");
    }
    //接收到消息的回调方法
    websocket.onmessage = function(event) {
        // setConnectMessageInHTML(event.data);
        setMessageInnerHTML(event.data);
    }
    //连接关闭的回调方法
    websocket.onclose = function() {
        setConnectMessageInHTML("主服务器关闭连接");
        // setMessageInnerHTML("Loc MSG:关闭连接");
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function() {
        websocket.close();
    }
}
var lockReconnect=fals;
//websocket重连
function websocketReconnect(user){
    if (lockReconnect) {       // 是否已经执行重连
        return;
    }
    lockReconnect = true;
    //没连接上会一直重连，设置延迟避免请求过多
    tt && clearTimeout(tt);
    var tt = setTimeout(function () {
        conectWebSocket(user);
        lockReconnect = false;
        console.log("正在重连");
        heartCheck.start();
    }, 5000)
}
var heartCheck = {
    timeout: 120000,
    timeoutObj: null,
    serverTimeoutObj: null,
    start: function () {
        console.log('start');
        var self = this;
        this.timeoutObj && clearTimeout(this.timeoutObj);
        this.serverTimeoutObj && clearTimeout(this.serverTimeoutObj);
        this.timeoutObj = setTimeout(function () {
            //发送测试信息，后端收到后，返回一个消息，
            websocket.send('HEALTH');
            self.serverTimeoutObj = setTimeout(function () {
                //websocket.close();
            }, self.timeout);
        }, this.timeout)
    }
}
function setMessageInnerHTML(innerHTML) {
    console.log(innerHTML);

}
function setConnectMessageInHTML(innerHTML){
    console.log(innerHTML)
    // $("#connectMsg").html(innerHTML);
}
