$("#saveIncome").click(function (){
    var income=$("#totelIncome").val();
    var YearEstimatedExpenditure=$("#YearEstimatedExpenditure").val();
    var MonthEstimatedExpenditure=$("#MonthEstimatedExpenditure").val();
    var reg=/(?:^[1-9]([0-9]+)?(?:\.[0-9]{1,2})?$)|(?:^(?:0){1}$)|(?:^[0-9]\.[0-9](?:[0-9])?$)/
    if(YearEstimatedExpenditure!=""||MonthEstimatedExpenditure!=""){
        if(YearEstimatedExpenditure.match(reg)==null){
            $("#worn2").css("display","block");
        }
        if(MonthEstimatedExpenditure.match(reg)==null){
            $("#worn3").css("display","block");
        }
    }
    if(income.match(reg)===null||income===""){
        $("#worn").css("display","block");
    }else{
        $.ajax({
            url: "getSystemInfo/setIncome",
            dataType: "json",
            data:{
                totelIncome:income
            },
            type: "post",
            scriptCharset: 'UTF-8',
            success: function(msg) {
                $("#worn").css("display","none");
                location.reload();

            },
            error:function(){  //请求失败的回调方法
                console.log("信息请求失败，请重试");
            }
        });


    }

})
function getUserInfo(){
    $.ajax({
        url: "getSystemInfo/getIncom",
        dataType: "json",
        data:{

        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(msg) {
            if(msg.incomInfo.totelIncome==null){
                $("#year_income").html("SetIncome(Click)");
                $("#month_income").html("SetIncome");
            }else{
                $("#year_income").html(msg.incomInfo.totelIncome+",00");
                $("#month_income").html((parseInt(msg.incomInfo.totelIncome)/12).toFixed(2).toString())
            }

        },
        error:function(){  //请求失败的回调方法
            console.log("信息请求失败，请重试");
        }
    });
}
getUserInfo()
function getSpending(){
    $.ajax({
        url: "getSystemInfo/getSpending",
        dataType: "json",
        data:{

        },
        type: "post",
        scriptCharset: 'UTF-8',
        success: function(msg) {
            countCurrentMonth(msg)

        },
        error:function(){  //请求失败的回调方法
            console.log("信息请求失败，请重试");
        }
    });
}
getSpending()

function countCurrentMonth(msg){
    console.log(msg)
    var date = new Date();
    var monthSpending=0;
    var lastYearMonthSpending=0;
    var yearSpending=0;
    var lastYearSpending=0;
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    for(var i=0;i<msg.length;i++){
        if(msg[i].time.month+1==month&&(msg[i].time.year).toString().slice(-2)==year.toString().slice(-2)){
            monthSpending=msg[i].money+monthSpending;
        }else if(msg[i].time.month+1==month&&(msg[i].time.year).toString().slice(-2)==(year-1).toString().slice(-2)){
            lastYearMonthSpending=msg[i].money+lastYearMonthSpending;
        }

        if((msg[i].time.year).toString().slice(-2)==year.toString().slice(-2)){
            yearSpending=msg[i].money+yearSpending;
        }else if ((msg[i].time.year).toString().slice(-2)==(year-1).toString().slice(-2)){
            lastYearSpending=msg[i].money+lastYearSpending;
        }
    }
    var yearPecent=((yearSpending-lastYearSpending)/lastYearSpending*100).toFixed(2)+"% ";
    var monethPecent=((monthSpending-lastYearMonthSpending)/lastYearMonthSpending*100).toFixed(2)+"% "
    $("#monethSpending").html(monthSpending.toFixed(2).toString());
    $("#yearSpending").html(yearSpending.toFixed(2).toString());
    checkUpandDown(yearPecent,"yearPecentTip","yearPecentDiv");
    checkUpandDown(monethPecent,"monthPecentTip","monthPecentDiv");
    $("#yearPecentNum").html(yearPecent);
    $("#monthPecentNum").html(monethPecent);
    var totelincome=$("#year_income").html();
    buildChart(((yearSpending/parseInt($("#year_income").html()))*100).toFixed(2));
    echartsData(msg,yearSpending,lastYearSpending,monthSpending,lastYearMonthSpending);
}




function buildChart(payPart){
    var temp=0;
    var label1="可支配"
    var label2="已支出"
    if(payPart>100&&(100-payPart)<0){
        temp=100;
        label1="已超支"+(payPart-100).toFixed(2)+"%";
        label2="已超支"+(payPart-100).toFixed(2)+"%";
    }else{
        temp=payPart;
    }
    Morris.Donut({element:"morris-donut-chart",data:[{label:""+label1+"",value:100-temp},{label:""+label2+"",value:temp}],resize:!0,colors:["#FF0000","#FFCC00"]})




}
function echartsData(msg){
    var yeardata = [];
    var date = new Date();
    var year=date.getFullYear();
    var yearSpending=0;
    var monthSpending=0;
    var yearSpendingdata=[];
    var monethSpendingdata=[];
    var point=0;
    for(var k=0;k<4;k++){
        for(var i=0;i<msg.length;i++){
            if((msg[i].time.year).toString().slice(-2)==(date.getFullYear()-k).toString().slice(-2)) {
                yeardata.push((date.getFullYear() - k).toString())
                break;
            }
        }
        for(var y=(msg.length-1)-point;y>=0;y--){
            if((msg[y].time.year).toString().slice(-2)==(year-k).toString().slice(-2)){
                yearSpending=msg[y].money+yearSpending;
            }else{
                point=msg.length-(y+1);
                break;
            }
        }
        for(var m=msg.length-1;m>=0;m--) {
            if ((msg[m].time.year).toString().slice(-2) == (year - k).toString().slice(-2) && msg[m].time.month + 1 == date.getMonth() + 1) {
                monthSpending = msg[m].money + monthSpending;
            }
        }
        if(yearSpending!=0){
            yearSpendingdata.push(yearSpending.toFixed(2));
            monethSpendingdata.push(monthSpending.toFixed(2));
            yearSpending=0;
            monthSpending=0;
        }





    }



    var t=echarts.init(document.getElementById("echarts-bar-chart")),
        n={title:{text:""},tooltip:{trigger:"axis"},legend:{data:["年支出","当月支出"]},grid:{x:50,x2:60,y2:24},calculable:!0,xAxis:[
                {
                    type:"category",
                    data:yeardata}],yAxis:[{type:"value"}],series:[{name:"年支出",type:"bar",data:yearSpendingdata,markLine:{data:[{type:"average",name:"平均值"}]}},{name:"当月支出",type:"bar",data:monethSpendingdata,markLine:{data:[{type:"average",name:"平均值"}]}}]};t.setOption(n),window.onresize=t.resize;
}


function checkUpandDown(data,TipclassName,DivclassName){
    if(data.substring(0,1)=="-"){
        $("#"+TipclassName+"").attr('class','fa fa-level-down');
        $("#"+DivclassName+"").attr('class','stat-percent font-bold text-danger')
    }else{
        $("#"+TipclassName+"").attr('class','fa fa-level-up');
        $("#"+DivclassName+"").attr('class','stat-percent font-bold text-navy')
    }
}
function getCookie(cookie_name) {
    var allcookies = document.cookie;
    //索引长度，开始索引的位置
    var cookie_pos = allcookies.indexOf(cookie_name);


    // 如果找到了索引，就代表cookie存在,否则不存在
    if (cookie_pos != -1) {
        // 把cookie_pos放在值的开始，只要给值加1即可
        //计算取cookie值得开始索引，加的1为“=”
        cookie_pos = cookie_pos + cookie_name.length + 1;
        //计算取cookie值得结束索引
        var cookie_end = allcookies.indexOf(";", cookie_pos);


        if (cookie_end == -1) {
            cookie_end = allcookies.length;


        }
        //得到想要的cookie的值
        var value = unescape(allcookies.substring(cookie_pos, cookie_end));
    }
    return value;
}
