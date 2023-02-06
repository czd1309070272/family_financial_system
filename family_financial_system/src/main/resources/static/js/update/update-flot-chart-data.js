var dataSet=[
    {"houseid":1,"money":4.50,"moneytype":"￥","name":"餐饮美食","payway":"支付宝","id":1,"time":"2022-09-10 15:42:30","title":"雪碧","realname":"小明"},
    {"houseid":1,"money":12.00,"moneytype":"￥","name":"餐饮美食","payway":"微信支付","id":2,"time":"2022-09-10 15:42:32","title":"汉堡","realname":"小明"},
    {"houseid":1,"money":6799.00,"moneytype":"￥","name":"数码电器","payway":"支付宝","id":3,"time":"2022-09-10 15:42:35","title":"iphone14","realname":"小明"},
    {"houseid":1,"money":8999.90,"moneytype":"￥","name":"数码电器","payway":"支付宝","id":8,"time":"2022-09-21 17:12:37","title":"华硕幻16笔记本","realname":"小曾"},
    {"houseid":1,"money":1.50,"moneytype":"￥","name":"餐饮美食","payway":"支付宝","id":9,"time":"2022-09-24 11:02:28","title":"矿泉水","realname":"小曾"},
    {"houseid":1,"money":2399.00,"moneytype":"￥","name":"数码电器","payway":"微信支付","id":10,"time":"2022-09-24 17:00:06","title":"AR眼镜","realname":"小曾"},
    {"houseid":1,"money":400.00,"moneytype":"￥","name":"数码电器","payway":"微信支付","id":11,"time":"2022-09-27 17:00:06","title":"手机","realname":"小曾"}
];
function countJsonData(dataSet){
    var temp={};
    var count=0;
    var sum=0;
    var arrayNum=0;
    for(var i=0;i<dataSet.length;i++){
        temp[arrayNum]={};
        if(i+1!=dataSet.length){
            if((dataSet[i].time.substring(0,10))==(dataSet[i+1].time.substring(0,10))){
                for(var j=0;j<dataSet.length;j++){
                    if((dataSet[i].time.substring(0,10))==(dataSet[j].time.substring(0,10))){
                        sum=dataSet[j].money+sum;
                        count++;
                    }
                }
                temp[arrayNum]['time']=(dataSet[i].time).substring(0,10);
                temp[arrayNum]['money']=sum;
                arrayNum++;
                sum=0;
                i=i+count-1;
                count=0;

            }else {
                temp[arrayNum]['time']=(dataSet[i].time).substring(0,10);
                temp[arrayNum]['money']=dataSet[i].money;
                arrayNum++;
            }
        }else{
            temp[arrayNum]['time']=(dataSet[i].time).substring(0,10);
            temp[arrayNum]['money']=dataSet[i].money;
        }
    }
    return temp;
}
function length(o){
    var t=typeof o;
    if(t=='string'){
        return o.length;
    }else if(t=='object'){
        var n=0;
        for (var i in o){
            n++;
        }
        return n;
    }
    return false;
}
countJsonData(dataSet);

var testArray=[1,4,5,6,10];
function getMonthDay(year, month) {
    let days = new Date(year, month + 1, 0).getDate()
    return days
}

function updateTableData(msg){
    var date=new Date();
    var mBillNum=0;
    var yBillNum=0;
    var last_yBillNum=0;
    for(var i=0;i<msg.length;i++){
        if((msg[i].time).substring(0,4)==date.getFullYear()&&(msg[i].time).substring(5,7)==checkTime(date.getMonth()+1)){
            mBillNum++;
        }
        if((msg[i].time).substring(0,4)==date.getFullYear()){
            yBillNum++;
        }
        if((msg[i].time).substring(0,4)==date.getFullYear()-1){
            last_yBillNum++;
        }
    }
    $("#year_bill_num").html(yBillNum);
    $("#year-percent").html(Math.round(last_yBillNum/yBillNum*10000)/100.00+"% ");
    $("#m_bill_num").html(mBillNum);
}

// for(var i=0;i<num.length;i++){
//     if(temp[num[i]]!=undefined){
//         temp[num[i]]=temp[num[i]]+num[i]
//         console.log(temp)
//     }else{
//         temp[num[i]]=num[i];
//
//     }
// }
// console.log(temp)