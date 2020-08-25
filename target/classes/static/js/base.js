/**
 * 公共函数，变量
 */
var ajax_settings = {
	
	/**
	 * 类型：Boolean
	 * 默认值: true。默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。
	 * 注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
	 */
	async: true,
	
	/**
	 * 类型：Function
	 * 发送请求前可修改 XMLHttpRequest 对象的函数，如添加自定义 HTTP 头。
	 * XMLHttpRequest 对象是唯一的参数。
	 * 这是一个 Ajax 事件。如果返回 false 可以取消本次 ajax 请求。
	 */
	beforeSend:function(XHR) {
		
	},
	
	/**
	 * 类型：Boolean
	 * 默认值: true，dataType 为 script 和 jsonp 时默认为 false。设置为 false 将不缓存此页面。
	 * jQuery 1.2 新功能。
	 */
	cache:true,
	
	/**
	 * 类型：Function
	 * 请求完成后回调函数 (请求成功或失败之后均调用)。
	 * 参数： XMLHttpRequest 对象和一个描述请求类型的字符串。
	 * 这是一个 Ajax 事件。
	 */
	complete: function(XHR, TS) {
		
	},
	
	/**
	 * 类型：String
	 * 默认值: "application/x-www-form-urlencoded"。发送信息至服务器时内容编码类型。
	 * 默认值适合大多数情况。如果你明确地传递了一个 content-type 给 $.ajax() 那么它必定会发送给服务器（即使没有数据要发送）。
	 */
	contentType:'',

    /**
     * 类型：Number
     * 设置请求超时时间（毫秒）。此设置将覆盖全局设置。
     */
	timeout: 30000,
	
	/**
	 * 类型：Function
	 * 默认值: 自动判断 (xml 或 html)。请求失败时调用此函数。
	 * 有以下三个参数：XMLHttpRequest 对象、错误信息、（可选）捕获的异常对象。
	 * 如果发生了错误，错误信息（第二个参数）除了得到 null 之外，还可能是 "timeout", "error", "notmodified" 和 "parsererror"。
	 */
	error: function(XMLHttpRequest, textStatus, errorThrown) {
		
	},
	
	/**
	 * 类型：Boolean
	 * 是否触发全局 AJAX 事件。默认值: true。设置为 false 将不会触发全局 AJAX 事件，如 ajaxStart 或 ajaxStop 可用于控制不同的 Ajax 事件。
	 */
	global:true
}

/** ajax请求数据 */
var ajax = function(url, type, success, data, async) {
	ajax_settings.url = url;
	ajax_settings.type = type;
	ajax_settings.success = success;
	ajax_settings.contentType = 'application/json;charset=UTF-8';
	if(data) {
		ajax_settings.data = data;
	}
	if(async != undefined) {
		ajax_settings.async = async;
	}
	$.ajax(ajax_settings);
}

/** Ajax 属性设置 */
$.ajaxSetup({
	// 请求成功后触发
	success: function () {},
	// 请求失败遇到异常触发
	error: function (xhr, msg, thrownError) {
		layer.msg(msg);
	},
	// 完成请求后触发。即在success或error触发后触发
	complete: function (xhr,status) {
		if(xhr.status == 401) {
			var msg = '会话过期，请确定重新登录';
			logout(msg);
		}
	}
});

/** 退出登录 */
var logout = function(msg) {
	var confirm = function() {
		window.location.href='/logout';
	}
	lay_confirm(msg, confirm);
};

/** layer confirm */
var lay_confirm = function(msg, confirm) {
	layer.confirm(msg, {icon: 3, title:'系统提醒'}, function(yes) {
		if(yes) {
			confirm();
		}
	});
}

/**
 * 打开Layer窗口
 */
var layerWin = function(title, content, cancel, yes) {
	layerOpenWin(title, 1, content, cancel, yes)
};


/**
 * 打开Layer窗口
 */
var layerOpenWin = function(title, type, content, cancel, yes) {
	layer.open({
		id:'add_menu_form',
		//area: ['820px', '400px'],
		//layer提供了5种层类型。可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）
		type: type,
		skin: 'layui-layer-molv', //样式类名
		closeBtn: 2, //关闭按钮
		title:title,
		// anim: 0-平滑放大-默认，1-从上掉落 ， 2-从最底部往上滑入 ，3-从左滑入，4-从左翻滚，5-渐显，6-抖动
		anim: 4,
		shadeClose: true, //开启遮罩关闭
		content: content,
		btn: ['确定'],
		cancel: cancel,
		yes: yes
	});
};

/**
 * layer alert
 */
var layer_alert = function(text) {
	// 第三方扩展皮肤
	layer.alert(text, {
		icon : 1,
		// 该皮肤由layer.seaning.com友情扩展。关于皮肤的扩展规则，去这里查阅
		skin : 'layer-ext-moon'
	});
};
