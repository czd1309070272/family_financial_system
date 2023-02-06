var $table=$("#exampleTableEvents");
var $ModyfiyButton=$("#modfiy");
$ModyfiyButton.click(function (){
    var obj=$table.bootstrapTable('getSelections');
    $("#orginGoodsName").val(obj[0].title);
    $("#orginGoodsPrice").val(obj[0].money);
    if(obj[0].remark!=null){
        $("#orginRemark").val(obj[0].remark);
    }
    var orginDate=(obj[0].time).substring(0,10);
    var orginTime=(obj[0].time).substring(10,(obj[0].time).length);
    $("#orginCreateDate").val(orginDate);
    $("#orginCreateTime").val(orginTime);


})
generateType();
function generateType(){
    $.ajax({
        url: "getSystemInfo/getType",
        dataType: "json",
        data:{

        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(msg) {
            for(var i=0;i<msg.length;i++){
                $(".chosen-select").append('<option value="'+(i+1)+'" hassubinfo="true">'+msg[i]+'</option>')
                $(".chosen-select").trigger("chosen:updated");
            }
        },
        error:function(){  //请求失败的回调方法
            console.log("信息请求失败，请重试");
        }
    });

}

var config={
    ".chosen-select-2":{},".chosen-select-deselect":
        {
            allow_single_deselect:!0
        },
    ".chosen-select-no-single":{disable_search_threshold:10},".chosen-select-no-results":{no_results_text:"Oops, nothing found!"},".chosen-select-width":{width:"95%"}};
for(var selector in config)$(selector).chosen(config[selector]);

