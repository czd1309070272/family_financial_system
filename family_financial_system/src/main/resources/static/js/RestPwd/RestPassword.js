$("#pwdmsg").css('display','none');
$("#UNmsg").css('display','none');
$("#confirmdPassword").blur(function (){
    if($("#confirmdPassword").val()!=$("#FirstPassword").val()){
        $("#pwdmsg").css('display','block');
        $("#pwdmsg").html("两次密码不一致！");
        return;
    }
    $("#pwdmsg").html("");
})
$("#username").blur(function (){
    if(isEmpty($("#username").val())){
        $("#UNmsg").css('display','block');
        $("#UNmsg").html("用户名不能为空！");
        return;
    }
    $("#UNmsg").html("");
})
$("#forgetpasswordBtn").click(function (){
    if(isEmpty($("#username").val())||isEmpty($("#confirmdPassword").val())||$("#pwdmsg").text()!=""){
        document.getElementById("forgetpasswordBtn").setAttribute("type","button");
        return;
    }
    document.getElementById("forgetpasswordBtn").setAttribute("type","submit");
})
function isEmpty(str){
    if(str==null||str.trim()==""){
        return true;
    }
    return false;
}