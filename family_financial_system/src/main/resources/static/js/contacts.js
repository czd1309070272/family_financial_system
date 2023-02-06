function getContacts(){
    var str="";
    var a=document.getElementById("contactsList");
    $.ajax({
        url: "getInfo/getUserSession",
        dataType: "json",
        data:{

        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(msg) {
            localStorage.removeItem("contactsList");
            localStorage.setItem("contactsList",JSON.stringify(msg.userList));
            var userList=msg.userList;
            for(var i=0;i<userList.length;i++){
                str+='<div class="col-sm-4">'
                str+=    '<div class="contact-box">'
                str+=        '<a href="profile.html?user_id='+i+'">'
                str+=            '<div class="col-sm-4">'
                str+=                '<div class="text-center">'
                str+=                    '<img alt="image" class="img-circle m-t-xs img-responsive" src="'+userList[i].photo+'">'
                str+=                        '<div class="m-t-xs font-bold">CTO</div>'
                str+=                '</div>'
                str+=            '</div>'
                str+=            '<div class="col-sm-8">'
                str+=                '<h3><strong>'+userList[i].realname+'</strong></h3>'
                str+=                '<address>'
                str+=                    'E-mail:'+userList[i].email+'<br>'
                str+=                    '<abbr title="Phone">Tel:</abbr> '+userList[i].tel+''
                str+=                '</address>'
                str+=            '</div>'
                str+=            '<div class="clearfix"></div>'
                str+=        '</a>'
                str+=    '</div>'
                str+='</div>'
                document.getElementById("contactsList").innerHTML=str;
            }

        },
        error:function(){  //请求失败的回调方法
            console.log("信息请求失败，请重试");
        }
    });
}
getContacts()