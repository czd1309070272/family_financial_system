document.getElementById("loginButton").setAttribute("type","button");
document.getElementById("animation_control").style.display="none";
document.getElementById("animation_control2").style.display="none";
document.getElementById("animation_login").className="";
$("#username").focus(function (){
    $("#msg").text("");
})
$("#password").focus(function (){
    $("#msg").text("");
})
$("#username").blur(function (){
    var uname=$("#username").val();
    if(isEmpty(uname))
    {
        $("#umsg").html("用户名/QQ邮箱不能为空!");
        document.getElementById("animation_login").className="";
        document.getElementById("animation_control").style.display="";
        document.getElementById("loginButton").setAttribute("type","button");
        return;
    }
    $("#umsg").html("");
    document.getElementById("animation_control").style.display="none";

});
$("#password").blur(function (){
    var upwd=$("#password").val();
    if(isEmpty(upwd))
    {
        $("#pmsg").html("密码不能为空!");
        document.getElementById("animation_login").className="";
        document.getElementById("animation_control2").style.display="";
        document.getElementById("loginButton").setAttribute("type","button");
        return;
    }
    $("#pmsg").html("");
    document.getElementById("animation_control2").style.display="none";

});
$("#loginButton").click(function (){
    var uname=$("#username").val();
    var upwd=$("#password").val();
    if(!isEmpty(upwd)&&!isEmpty(uname))
    {
        document.getElementById("loginButton").setAttribute("type","submit");
    }
    document.getElementById("animation_login").className="animated bounce";
});
function isEmpty(str){
    if(str==null||str.trim()==""){
        return true;
    }
    return false;
}