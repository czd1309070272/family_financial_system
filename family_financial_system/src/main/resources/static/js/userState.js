$(function (){
    getUserState();
})
function getUserState(){
    var refreshState_client=function (){
        $.ajax({
            url: "chatMessage/getUserState",
            dataType: "json",
            data:{

            },
            type: "post",
            scriptCharset: 'UTF-8',
            success: function(message) {
                localStorage.removeItem("userState");
                // console.log(message)
                localStorage.setItem("userState",JSON.stringify(message));

            },
            error:function(){  //请求失败的回调方法
                console.log("信息请求失败，请重试");
            }
        });
    }
    refreshState_client();
    setInterval(refreshState_client,120000);
}
