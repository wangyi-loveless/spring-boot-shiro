//获取菜单数据
var form, layer;
var success = function(data) {
	if(null != data && data.result) {
		var msg = data.msg;
		var html = fn_generator_menu(data);
		$('#right_menu').html(html);
		
		/** 初始化layui组件 */
		layui.use(['element','layer','form'], function() {
			// 框架初始化
			var element = layui.element,
			layer = layui.layer;
			form = layui.form;
			
			// 退出登录
			$('#logout').bind('click',fn_logout);
			
			// 重置密码
			$('#reset').bind('click',fn_open_layer_win);
		});
	}else {
		/** 初始化layui组件 */
		layui.use(['element','layer','form'], function() {
			// 框架初始化
			var element = layui.element,
			layer = layui.layer;
			form = layui.form;
			
			// 退出登录
			$('#logout').bind('click',fn_logout);
			
			// 重置密码
			$('#reset').bind('click',fn_open_layer_win);
			
			layer.msg(data.msg);
		});
	}
}

// 获取用户菜单
ajax('/api/menu/user','GET',success);

/** 菜单点击事件 */
var fn_menu_click = function(url) {
	if(url && url.length > 0) {
		$('#main_content').load(url);
	}
}

// 重置密码提交
var fn_ajax_submit_reset_password_form = function(data, index) {
	var success = function(data) {
		if(null != data && data.result){
			//操作提示
			layer.msg('Success');
			
			//关闭窗口
			layer.close(index);
		}else {
			layer.msg(data.msg);
		}
	}
	var url = '/api/user/reset/';
	ajax(url, 'POST', success, JSON.stringify(data), false);
	
}

/** 打开layer Windows  */
var fn_open_layer_win = function() {
	//表单数据
	var content = fn_reset_password_form_html();
	
	//按钮事件
	var cancel = function(){ 
		//右上角关闭回调
		layer.msg('cancel');
	},
	yes = function(index) {
		var id = $('#user_id').val();
		var password = $('input[name=password]').val();
		var newPassword1 = $('input[name=newPassword1]').val();
		var newPassword2 = $('input[name=newPassword2]').val();
		if(newPassword1 && newPassword2){
			if(newPassword1 != newPassword2){
				layer.msg('两次输入密码不相同');
				return;
			}
		} 
		
		var data = {
			'id': id,
			'oldPassword': password,
			'newPassword': newPassword1
		}
		$('#reset_password_submit').trigger("click");
		if(id && password && newPassword1) {
			fn_ajax_submit_reset_password_form(data, index);
		}
	}
	var title = '重置密码';
	
	//打开窗口
	layerWin(title, content, cancel, yes);
	
	//监听提交
	form.on('submit(reset_password)', function(data){
	    return false;
	});
};

/**
 * 构造表单数据
 */
var fn_reset_password_form_html = function() {
	var reset_password_html_arr = new Array();
	reset_password_html_arr.push('<form class="layui-form layui-form-pane" style="margin:5px;" id="reset_password_form">');
	// 历史密码
	reset_password_html_arr.push('<div class="layui-form-item">');
	reset_password_html_arr.push('<label class="layui-form-label">旧密码</label>');
	reset_password_html_arr.push('<div class="layui-input-block">');
	reset_password_html_arr.push('<input type="password" lay-verify="required" name="password"  autocomplete="off"');
	reset_password_html_arr.push(' placeholder="请输入原密码" class="layui-input">');
	reset_password_html_arr.push('</div>');
	reset_password_html_arr.push('</div>');
	
	//新密码1
	reset_password_html_arr.push('<div class="layui-form-item">');
	reset_password_html_arr.push('<label class="layui-form-label">第一次新密码</label>');
	reset_password_html_arr.push('<div class="layui-input-block">');
	reset_password_html_arr.push('<input type="password" lay-verify="required" name="newPassword1"  autocomplete="off"');
	reset_password_html_arr.push(' placeholder="请输入新密码" class="layui-input">');
	reset_password_html_arr.push('</div>');
	reset_password_html_arr.push('</div>');
	
	//新密码2
	reset_password_html_arr.push('<div class="layui-form-item">');
	reset_password_html_arr.push('<label class="layui-form-label">重复输入新密码</label>');
	reset_password_html_arr.push('<div class="layui-input-block">');
	reset_password_html_arr.push('<input type="password" lay-verify="required" name="newPassword2"  autocomplete="off"');
	reset_password_html_arr.push(' placeholder="请输入新密码" class="layui-input">');
	reset_password_html_arr.push('</div>');
	reset_password_html_arr.push('</div>');
	
	//提交按钮
	reset_password_html_arr.push('<button class="layui-btn " style="display:none;" name="reset_password_submit" ');
	reset_password_html_arr.push(' id="reset_password_submit" lay-submit="" lay-filter="reset_password"></button>');
	reset_password_html_arr.push('</form>');
	return reset_password_html_arr.join('');
};

/** 退出登录 */
var fn_logout = function() {
	var msg = '退出登录，请确定';
	logout(msg);
}

/** 生成菜单 */
var fn_generator_menu = function(data) {
	if(data && data.result) {
		var menu_data = data.data;
		if(null == menu_data || menu_data == undefined) {
			layer.msg('用户暂无授权');
			return;
		}
		var len = menu_data.length;
		var html_array = new Array();
		for (var i = 0; i < len; i++) {
			var isfirst = false;
			if(i == 0){
				isfirst = true;
			}else{
				isfirst = false;
			}
			var menu = menu_data[i];
			html_array.push(fn_data_2_html(menu, isfirst));
		}
		return html_array.join('');
	}else {
		layer_alert(data.msg);
	}
};

/** 菜单数据转换为html标签 */
var fn_data_2_html = function(menu, isfirst) {
	var menu_html_array = new Array();
	if(isfirst) {
		menu_html_array.push('<li class="layui-nav-item layui-nav-itemed">');
	}else {
		menu_html_array.push('<li class="layui-nav-item">');
	}
	var url = menu.url;
	if(url == undefined || null == url){
		url = '';
	}
	var id = menu.id;
	var name = menu.name;
	var icon = menu.icon;
	if(icon == undefined || null == icon){
		icon = '';
	}
	menu_html_array.push('<a class="layui-icon" id="' + id + '" onclick="fn_menu_click(\''+ url +'\')" style="cursor: pointer;">');
	menu_html_array.push('<i class="layui-icon '+ icon +'">&nbsp;&nbsp;</i>'+ name +'</a>');
	var child = menu.child;
	if(null != child && child.length > 0) {
		var child_html = fn_child_data_2_html(child);
		menu_html_array.push(child_html);
	}
	menu_html_array.push('</li>');
	return menu_html_array.join('');
}

/** 构造子菜单 */
var fn_child_data_2_html = function(data) {
	if(null == data || data.length < 1){
		return '';
	}
	var child_menu_html_array = new Array();
	var len = data.length;
	child_menu_html_array.push('<dl class="layui-nav-child">');
	for (var i = 0; i < len; i++) {
		var child_menu = data[i];
		var id = child_menu.id;
		var name = child_menu.name;
		var url = child_menu.url;
		var icon = child_menu.icon;
		if(icon == undefined || null == icon){
			icon = '';
		}
		child_menu_html_array.push('<dd>');
		child_menu_html_array.push('<a id="' + id + '" onclick="fn_menu_click(\''+ url +'\')" style="cursor: pointer; margin-left:10%;">');
		child_menu_html_array.push('<i class="iconfont '+ icon +'">&nbsp;&nbsp;</i>'+ name +'</a>');
		child_menu_html_array.push('</dd>');
	}
	child_menu_html_array.push('</dl>');
	return child_menu_html_array.join('');
}