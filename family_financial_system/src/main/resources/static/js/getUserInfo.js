function getUserInfo(){
    var str="";
    var a=document.getElementById("tbodySpace");
    var b=document.getElementById("tableSpace");
    $.ajax({
        url: "getInfo/getUserSession",
        dataType: "json",
        data:{
            "houseid":1
        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(msg) {
            b.appendChild(a)
            for(var i=0;i<msg.userList.length;i++){
                (function (index){
                    setTimeout(function (){
                        str+='<tr class="animated bounceInUp">'
                        str+='  <td class="client-avatar"><img alt="image" src="img/a2.jpg"></td>'
                        str+='  <td><a data-toggle="tab" href="#contact-1" class="client-link">'+msg.userList[index].realname+'</a>'
                        str+='  </td>'
                        str+='  <td></td>'
                        str+='  <td class="contact-type"><i class="fa fa-envelope"> </i>'
                        str+='  </td>'
                        str+='  <td> '+msg.userList[index].email+'</td>'
                        // str+='  <td class="client-status"><span class="label label-primary"></span>'
                        str+='  <td class="client-status"><span class="label label-primary">'+reName(msg.userList[index].roleid)+'</span>'
                        str+='  </td>'
                        str+='</tr>'
                        document.getElementById("tbodySpace").innerHTML=str;
                    },600)
                })(i)
            }
            // document.getElementById("loadAnim").style.display="none";
        },
        error:function(){  //请求失败的回调方法
            console.log("信息请求失败，请重试");
        }
    });
}
getUserInfo()

function reName(i){
    switch (i){
        case 0:return "超级管理员";
        case 1:return "户主";
        case 2:return "家庭成员";
    }
}

