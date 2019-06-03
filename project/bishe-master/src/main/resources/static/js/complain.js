
$(function () {
    layui.use('layer', function () {
        var layer = layui.layer;
    });
});

function complain() {

    var manid=$("#manid").val();
    if(manid==''){
        window.location.href='/login';
        window.event.returnValue=false;
    }else{
        layer.prompt({
            formType: 2,
            value: '',
            title: '请写下您的投诉信息',
        }, function (value, index, elem) {
            $.get("/complain", {
                content: value,
                manid: manid
            }, function (data1, textStatus) {
                if (data1.msg === '200') {
                    layer.msg("发送成功", {icon: 1});
                    layer.close(index);
                } else {
                    layer.msg("发送失败", {icon: 2});
                    layer.close(index);
                }
            }, "json");
        });
    }

}











