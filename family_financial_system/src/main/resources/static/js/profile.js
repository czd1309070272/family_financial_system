$(function (){
    var msg=JSON.parse(localStorage.getItem("contactsList"));
    var mainUserID=getCookie("userID");
    var loc = location.href;
    var n1=loc.length;
    var n2=loc.indexOf("?");
    var parameter=loc.substring(n2+9, n1);
    if(msg==null){
        $.ajax({
            url: "getInfo/getMainUserInfo",
            dataType: "json",
            async:false,
            data:{
                id:mainUserID
            },
            type: "post",
            scriptCharset: 'UTF-8',
            success: function(user) {
                msg=user.mainUserInfo;

            },
            error:function(){  //请求失败的回调方法
                console.log("信息请求失败，请重试");
            }
        });
    }
    if (loc.indexOf("?")==-1){
        if(msg.length==1){
            parameter=0;
        }else{
            for(var i=0;i<msg.length;i++){
                if(msg[i].id==mainUserID){
                    parameter=i;
                    break;
                }
            }
        }

    }else{
        parameter=loc.substring(n2+9, n1);
    }

    if(msg[parameter].id==mainUserID){
        $("#user-button").css("display","none");
    }else{
        $("#user-button").css("display","block");
    }
    $("#realname").html(msg[parameter].realname);

})
function getCookie(cookie_name) {
    var allcookies = document.cookie;
    //索引长度，开始索引的位置
    var cookie_pos = allcookies.indexOf(cookie_name);


    // 如果找到了索引，就代表cookie存在,否则不存在
    if (cookie_pos != -1) {
        // 把cookie_pos放在值的开始，只要给值加1即可
        //计算取cookie值得开始索引，加的1为“=”
        cookie_pos = cookie_pos + cookie_name.length + 1;
        //计算取cookie值得结束索引
        var cookie_end = allcookies.indexOf(";", cookie_pos);


        if (cookie_end == -1) {
            cookie_end = allcookies.length;


        }
        //得到想要的cookie的值
        var value = unescape(allcookies.substring(cookie_pos, cookie_end));
    }
    return value;
}

$("#send").click(function (){
    if($("#chatbox",window.parent.document).attr("class")=="small-chat-box fadeInRight animated active"){
        return;
    }

    var userState=localStorage.getItem("userState");
    var map = eval("("+userState+")"); //r为String类型的数据



    var a=window.parent.document.getElementById("content");
    a.innerHTML="";
    var msg=JSON.parse(localStorage.getItem("contactsList"));
    var indexChatPage=window.parent.document;
    var receiver;
    var sender;
    indexChatPage.getElementById("chatbox").className="small-chat-box fadeInRight animated active";
    indexChatPage.getElementById("chatTitle").innerHTML=" 与 "+$("#realname").html()+" 聊天中";
    for(var i=0;i<msg.length;i++){
        if($("#realname").html()==msg[i].realname){
            receiver=msg[i].id;

        }
        if ($("#username",window.parent.document).html()==msg[i].realname){
            sender=msg[i].id;

        }
    }






    //连接
    conectWebSocket(sender,sender,receiver);

    if(map[receiver]=="online"){
        indexChatPage.getElementById("userState").className="pull-right label label-primary";
        indexChatPage.getElementById("userState").innerHTML="在线";
    }else{
        indexChatPage.getElementById("userState").className="pull-right label";
        indexChatPage.getElementById("userState").innerHTML="离线";
    }

    //获取历史聊天记录
    $.ajax({
        url: "chatMessage/getHistoryChat",
        dataType: "json",
        async:false,
        data:{
            sender:sender,
            receiver:receiver
        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(message) {
            for(var i=0;i<message.length;i++){
                createMsg(message[i].message,message[i].sender);
            }
            getFullYearTime(message,0)

        },
        error:function(){  //请求失败的回调方法
            console.log("信息请求失败，请重试");
        }
    });





});



function getFullYearTime(message,i){
    return message[i].msgtime.year
    console.log(message[i].msgtime.date)
}


function createMsg(msg,sender){
    var str="";
    var contactsList=JSON.parse(localStorage.getItem("contactsList"));
    var a=window.parent.document.getElementById("content");
    var activeUser;
    for(var i=0;i<contactsList.length;i++){
        if ($("#username",window.parent.document).html()==contactsList[i].realname){
            activeUser=contactsList[i].id;
        }
    }
    if(sender==activeUser){
        str+='<div id="right" class="right">'
        str+=    '<div id="message-right" class="chat-message">'
        str+=        ''+msg+''
        str+=    '</div>'
        str+='</div>'
        a.innerHTML+=str;
    }else{
        str+='<div id="left" class="left">'
        str+=    '<div id="message-left" class="chat-message active">'
        str+=        ''+msg+''
        str+=    '</div>'
        str+='</div>'
        a.innerHTML+=str;
    }
    $("#msgText",window.parent.document).val("");


}


var websocket = null;
function conectWebSocket(nickname,sender,receiver){
    //判断当前浏览器是否支持WebSocket
    try{
        if ('WebSocket'in window) {
            websocket = new WebSocket("ws://localhost:8080/websocket/"+nickname+":"+sender+"."+receiver);
        } else {
            alert('Not support websocket')
        }
    }catch (e){
        websocketReconnect(nickname,sender,receiver);
    }

    //连接发生错误的回调方法
    websocket.onerror = function() {
        setMessageInnerHTML("error");
    };
    //连接成功建立的回调方法
    websocket.onopen = function(event) {
        setConnectMessageInHTML("成功建立连接");
        // setMessageInnerHTML("Loc MSG: 成功建立连接");
    }
    //接收到消息的回调方法
    websocket.onmessage = function(event) {
        // setConnectMessageInHTML(event.data);
        setMessageInnerHTML(event.data);
    }
    //连接关闭的回调方法
    websocket.onclose = function() {
        $.ajax({
            url: "chatMessage/inputHistoryChat",
            dataType: "json",
            async:false,
            data:{

            },
            type: "post",
            scriptCharset: 'UTF-8',
            success: function(message) {
                console.log(message);
            },
            error:function(){  //请求失败的回调方法
                console.log("信息请求失败，请重试");
            }
        });
        setConnectMessageInHTML("关闭连接");
        // setMessageInnerHTML("Loc MSG:关闭连接");
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function() {
        websocket.close();
    }
}
var lockReconnect=false;
//websocket重连
function websocketReconnect(nickname,sender,receiver){
    if (lockReconnect) {       // 是否已经执行重连
        return;
    }
    lockReconnect = true;
    //没连接上会一直重连，设置延迟避免请求过多
    tt && clearTimeout(tt);
    var tt = setTimeout(function () {
        conectWebSocket(nickname,sender,receiver);
        lockReconnect = false;
        console.log("正在重连");
        heartCheck.start();
    }, 5000)
}


function setConnectMessageInHTML(innerHTML){
    console.log(innerHTML)
    // $("#connectMsg").html(innerHTML);
}

//将消息显示在网页上
function setMessageInnerHTML(innerHTML) {
    if(innerHTML.charAt(0)=="恭"){

    }else{
        var obj = JSON.parse(''+innerHTML+'');
        createMsg(obj.message,obj.Sender);
    }

}
//关闭连接
function closeWebSocket() {
    websocket.close();
}

//服务器心跳检测
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
            websocket.send('hello');
            self.serverTimeoutObj = setTimeout(function () {
                //websocket.close();
            }, self.timeout);
        }, this.timeout)
    }
}



//发送消息
$("#sendButton",window.parent.document).click(function (){
    var message = window.parent.document.getElementById('msgText').value;
    if(message!==""){
        websocket.send(message);
    }
})
$("#openOrClose",window.parent.document).click(function (){
    if($("#chatbox",window.parent.document).attr("class")=="small-chat-box fadeInRight animated"){
        closeWebSocket();
    }

})

