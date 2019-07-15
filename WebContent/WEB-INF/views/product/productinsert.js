$(function(){
	$(".submit-btn").click(function(){
		//获取表单中的数据
		var valueTemp=$("#productForm").serializeArray();
		$.ajax({
			url : "/product/productinsert.json",
			data : valueTemp,
			type : 'POST',
			success : function(result) {
				//数据执行成功返回的消息
//				alert(666);
				if(result.ret){
					showMessage("新增材料", result.msg,
							true);
				}else{
					 showMessage("新增材料", result.msg, false);
				}
			}
		});
	});

});