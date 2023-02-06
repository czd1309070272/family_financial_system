$(function (){
    $.ajax({
        url: "getInfo/getUserAuthor",
        dataType: "json",
        data:{

        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(msg) {
            if(msg!=="超级管理员"){
                $("#addMember").empty();
            }else{

            }

        },
        error:function(){  //请求失败的回调方法
            console.log("信息请求失败，请重试");
        }
    });
})
$(function (){
    $.ajax({
        url: "getInfo/getUserAvatar",
        dataType: "json",
        data:{
            username:$("#username").html()
        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(msg) {
            console.log(msg.avatar)
            if(msg.avatar!=="null"){
                $("#avatar").attr("src",msg.avatar);
            }else{
                $("#avatar").attr("src","/img/a8.jpg")
            }


        },
        error:function(){  //请求失败的回调方法
            console.log("信息请求失败，请重试");
        }
    });
})

