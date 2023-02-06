var date=new Date();
$("#defaultDateTime").val(date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate())
$("#defaultTime").val(checkTime(date.getHours())+":"+checkTime(date.getMinutes())+":"+checkTime(date.getSeconds()))
$("#defaultTime").blur(function (){
    var timeStr=$("#defaultTime").val();
    $("#defaultTime").val(timeStr.replaceAll("_","0"))
})
$("#orginCreateTime").blur(function (){
    var timeStr=$("#orginCreateTime").val();
    $("#orginCreateTime").val(timeStr.replaceAll("_","0"))
})
function checkTime(i){
    if (i < 10) {

        i = "0" + i;

    }

    return i;

}
var $button=$("#save");
$button.click(function (){
    var $table=$("#exampleTableEvents");
    var obj=$table.bootstrapTable('getSelections');
    var GoodsName;
    var GoodsMoney;
    var GoodsType;
    var GoodsPayType;
    if($("#orginGoodsName").val()!=obj[0].title&&$("#orginGoodsName").val()!=null){
        GoodsName=$("#orginGoodsName").val();
    }else{
        GoodsName="null";
    }
    if($("#orginGoodsPrice").val()!=obj[0].money&&$("#orginGoodsPrice").val()!=null){
        GoodsMoney=$("#orginGoodsPrice").val();
    }else{
        GoodsMoney="null";
    }
    if($("#orginGoodsType").val()!=""){
        GoodsType=$("#orginGoodsType").val();
    }else{
        GoodsType="null";
    }
    if($("#orginPayway").val()!=""){
        GoodsPayType=$("#orginPayway").val();
    }else{
        GoodsPayType="null";
    }
    // if($("#orginCreateDate").val()!=)
    var remark;
    var nowDateTime;
    var orginDate=(obj[0].time).substring(0,10);
    var orginTime=(obj[0].time).substring(10,(obj[0].time).length);
    if($("#orginCreateDate").val()!=""&&$("#orginCreateTime").val()!="00:00:00"&&
        orginDate!=$("#orginCreateDate").val()||orginTime!=$("#orginCreateTime").val()){
        nowDateTime=$("#orginCreateDate").val()+$("#orginCreateTime").val();
    }else{
        nowDateTime="null";
    }
    if(obj[0].remark!=null){
        if(obj[0].remark!=$("#orginRemark").val()){
            remark=$("#orginRemark").val();
        }else{
            remark="null";
        }
    }else{
        if($("#orginRemark").val()!=""){
            remark=$("#orginRemark").val();
        }else{
            remark="null";
        }

    }
    $.ajax({
        url: "updatebill",
        dataType: "json",
        data:{
            GoodsID:obj[0].id,
            GoodsName:GoodsName,
            GoodsType:GoodsType,
            GoodsPayType:GoodsPayType,
            GoodsMoney:GoodsMoney,
            nowDateTime:nowDateTime,
            remark:remark
        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(msg) {
            console.log(msg.msg)

        },
        error:function(){  //请求失败的回调方法
            console.log("信息请求失败，请重试");
        }
    });
})
var $button=$("#modfiy");
$button.click(function (){
    var $table=$("#exampleTableEvents");
    var obj=$table.bootstrapTable('getSelections');
    if(obj.length==0||obj.length>1){
        $("#modfiy").attr("href","");
        swal({title:"请选择一个账单进行修改!",type:"warning"})
    }else{
        $("#modfiy").attr("href","form_basic.html#modal-form_2");
    }
})
var $button=$("#addBill");
$button.click(function (){
    if($("#goodsName").val()==""||$("#goodsPrice").val()==""||$("#selectType").find("option:selected").text()=="类型"||$("#selectWay").find("option:selected").text()=="方式"){
        document.getElementById("addBill").setAttribute("type","button");
        // alert($("#selectType").find("option:selected").text());
        $('#goodsNamediv').attr('class','form-group has-warning');
        $('#goodsPricediv').attr('class','form-group has-warning');
        if($("#goodsName").val()!=""){
            $('#goodsNamediv').attr('class','form-group has-success');
        }
        if($("#goodsPrice").val()!=""){
            $('#goodsPricediv').attr('class','form-group has-success');
        }
        if($("#goodsName").val()==""||$("#goodsPrice").val()==""){
            swal({title:"请把信息填写完整!",type:"warning"})
        }else {
            swal({title:"请选择账单类型和支付方式!",type:"warning"})
        }

    }else{
        document.getElementById("addBill").setAttribute("type","submit");
        swal({title:"添加成功",type:"success"})
    }
})
