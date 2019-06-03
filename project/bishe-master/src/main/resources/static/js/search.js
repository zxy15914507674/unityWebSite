//搜索框方法
function search() {
    var queryword=$("#queryword").val();
    //debugger;
    if(queryword.length==0){
        window.location.href='/';
        window.event.returnValue=false;
    }else{
        $("#queryfrom").attr("action","/g/search?queryword="+queryword);
        $("#queryfrom").attr("method","post").submit();
    }
}

function cdk(){
    if(event.keyCode ==13){
        search();
    }
}