$(document).ready(function(){
    $("#delTempBill").click(function(){
        var $table=$("#billFormEvents");
        var obj=$table.bootstrapTable('getSelections');
        if(obj.length==0){
            swal({title:"请选择一个账单进行删除!",type:"warning"})
        }else{
            swal({title:"您确定要删除这条信息吗",text:"删除后将无法恢复，请谨慎操作！",type:"warning",showCancelButton:true,confirmButtonColor:"#DD6B55",confirmButtonText:"删除",closeOnConfirm:false
            },function(){
                var billIndex=[];
                for(var i=0;i<obj.length;i++){
                    billIndex[i]=obj[i].id;
                }
                $table.bootstrapTable('remove',{
                    field:"id",values:billIndex
                });
                swal("删除成功！","您已经永久删除了这"+billIndex.length+"条信息。","success")
            })
        }

    });});
$("#uploadbutton").click(function (){
    var $table=$("#billFormEvents");
    var obj=$table.bootstrapTable('getSelections');
    $.ajax({
        url: "/upload2DB",
        dataType: "json",
        data:{
            dataSet:JSON.stringify(obj)
        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(msg) {
            if(msg!=="400"){
                console.log("信息已录入")
                swal({title:"上传成功！",text:"您已上传"+msg+"条信息。",type:"success"
                },function(){
                    location.reload();
                })
            }

        },
        error:function(){  //请求失败的回调方法
            console.log("上传请求失败，请重试");
        }
    });
})