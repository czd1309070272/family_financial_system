
function loadJS() {
    var elem=document.querySelector(".js-switch")
    switchery= new Switchery( elem,{size: 'small',color : '#1AB394'})
    $("#custom_ionrange_1").ionRangeSlider({
        type: "double",
        grid: true,
        min: 0,
        max: 100000,
        from: 200,
        to: 800,
        step:100,
        prefix: "$"
    });

}
if(window.addEventListener) {
    window.addEventListener("load", loadJS, false)
}else if(window.attachEvent) {
    window.attachEvent("onload", loadJS)
}else {
    window.onload = loadJS
}










