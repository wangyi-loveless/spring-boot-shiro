//获取菜单数据
var success = function(data) {
	if(null != data && data.result){
		var html = fn_generator_menu(data);
		$('#right_menu').html(html);
	}else {
		layer.alert(data.msg);
	}
}
ajax('/oper/menu/user','GET',success);

/**
 * 初始化layui组件
 * @returns
 */
layui.use(['element','layer'], function() {
	//框架初始化
	var element = layui.element,
	layer = layui.layer;
	//退出登录
	$('#logout').bind('click',fn_logout);
});

/** 菜单点击事件 */
var fn_menu_click = function(url) {
	if(url && url.length > 0) {
//	$('#main_content').empty();
		$('#main_content').load(url);
	}
}

/** 退出登录 */
var fn_logout = function() {
	var msg = '退出登录，请确定';
	logout(msg);
}

/** 生成菜单 */
var fn_generator_menu = function(data) {
	if(data && data.result) {
		var menu_data = data.data;
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
		layer.msg(data.msg);
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
	if(!url){
		url = '';
	}
	var id = menu.id;
	var name = menu.name;
	menu_html_array.push('<a id="' + id + '"');
	menu_html_array.push(' onclick="fn_menu_click(\''+ url +'\')"');
	menu_html_array.push(' style="cursor: pointer;">'+ name +'</a>');
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
	var menu_html_array = new Array();
	var len = data.length;
	menu_html_array.push('<dl class="layui-nav-child">');
	for (var i = 0; i < len; i++) {
		var menu = data[i];
		menu_html_array.push('<dd>');
		var id = menu.id;
		var name = menu.name;
		var url = menu.url;
		menu_html_array.push('<a id="' + id + '"');
		menu_html_array.push(' onclick="fn_menu_click(\''+ url +'\')"');
		menu_html_array.push(' style="cursor: pointer;">'+ name +'</a>');
		menu_html_array.push('</dd>');
	}
	menu_html_array.push('</dl>');
	return menu_html_array.join('');
}