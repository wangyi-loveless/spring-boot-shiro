/**
 * layui组件初始化
 * @returns
 */
layui.use(['form', 'layedit'], function(){
  	var form = layui.form,layer = layui.layer;
	
	//自定义验证规则
	form.verify({
		pass: [/(.+){1,12}$/, '密码必须1到12位']
	});
	
	//监听提交
	form.on('submit(login)', function(data){
	    return false;
	});
	
	$("button[name=submit]").bind("click", fn_submit);
});

/**
 * 绑定提交事件
 */
var fn_bind_submit = function() {
	$("button[name=submit]").bind("click", fn_submit);
}

/** 提交表单 */
var fn_submit = function() {
	var username = $('input[name=username]').val();
	var password = $('input[name=password]').val();
	var rememberMe = $("input[type='checkbox']").is(':checked');
	//提交数据
	var formData = {
			'username':username,
			'password':password,
			'rememberMe':rememberMe
		};
	//成功事件
	var success = function(data) {
		if(null != data && data.result){
			window.location.href = '/index';
		}else {
			layer.msg(data.msg);
		}
	}
	ajax('/login','POST',success,JSON.stringify(formData));
}
