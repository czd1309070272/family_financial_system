function getButtonEvent(){
    var clickCheckbox = document.querySelector('.js-check-click');
    if(clickCheckbox.checked){
        $("#bestIncom").html('家庭全年总收入￥ *')
        $("#lowerIncomPanel").css('display','none');
    }else{
        $("#bestIncom").html('最高收入￥ *')
        $("#lowerIncomPanel").css('display','block');
    }
}