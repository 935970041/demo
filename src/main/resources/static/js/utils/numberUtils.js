//将阿拉伯数字转换大写汉字(繁体)
function arabicNumberToChinaNumber(n) {
    if (!/^(0|[1-9]\d*)(\.\d+)?$/.test(n))
        return "数据非法";
    var unit = "仟佰拾亿仟佰拾万仟佰拾元角分", str = "";
    n += "00";
    var p = n.indexOf('.');
    if (p >= 0)
        n = n.substring(0, p) + n.substr(p+1, 2);
    unit = unit.substr(unit.length - n.length);
    for (var i=0; i < n.length; i++)
        str += '零壹贰叁肆伍陆柒捌玖'.charAt(n.charAt(i)) + unit.charAt(i);
    return str.replace(/零(仟|佰|拾|角)/g, "零").replace(/(零)+/g, "零").replace(/零(万|亿|元)/g, "$1").replace(/(亿)万|壹(拾)/g, "$1$2").replace(/^元零?|零分/g, "").replace(/元$/g, "元整");
}

//格式化日期
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}




// 计算两个时间之间的时间差
function dateDiff(travelDateVal, returnDateVal) {  //sDate1和sDate2是yyyy-MM-dd格式
    travelDateVal=travelDateVal.Format("yyyy-MM-dd");
    returnDateVal=returnDateVal.Format("yyyy-MM-dd");
    if(travelDateVal>returnDateVal){
        //layer.msg('开始日期不能大于结束日期', {icon: 16,shade: [0.5, '#f5f5f5'],scrollbar: false,offset: '300px',time: 2000});
        layer.alert('开始日期不能大于结束日期');
        return -1;
    }
    var aDate, oDate1, oDate2, iDays;
    aDate = travelDateVal.split("-");
    oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);  //转换为yyyy-MM-dd格式
    aDate = returnDateVal.split("-");
    oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0]);
    iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24); //把相差的毫秒数转换为天数

    return Number(iDays)+Number(1);  //返回相差天数
}


// 调用window.open post的方式传递很多参数,而不是进行拼接
function openWindow(name) {
    window.open('about:blank', name, "width=1000,height=650,toolbar=no, menubar=no,  scrollbars=yes,resizable=yes,location=no, status=no");
};
var print=function openPostWindow(url, name) {  //url要跳转到的页面，data要传递的数据，name显示方式（可能任意命名）
    var tempForm = $("<form>");
    tempForm.attr("id", "tempForm1");
    tempForm.attr("style", "display:none");
    tempForm.attr("target", name);
    tempForm.attr("method", "post");
    tempForm.attr("action", url);
    var input = $("<input>");
    input.attr("type", "hidden");
    input.attr("name", "printDetailInfo");
    var str = JSON.stringify(_this.formInline);
    input.attr("value", str);
    tempForm.append(input);
    tempForm.on("submit", function () { openWindow(name); }); // 必须用name不能只用url，否则无法传值到新页面
    tempForm.trigger("submit");
    $("body").append(tempForm);//将表单放置在web中
    tempForm.submit();
    $("tempForm1").remove();
};
print("/reimburse/reimburse/printDetail","打印");