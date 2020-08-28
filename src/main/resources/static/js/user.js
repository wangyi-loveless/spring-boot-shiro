/**
 * 用户列表绑定数据
 * @returns
 */
layui.use(['table','form'], function() {
  	//lay table对象
	var table = layui.table,
	form = layui.form;
	
	//行id
	var user_id, title;
	
	//table绑定数据
	table.render({
		elem: '#user_table'
		,url:'/api/user/page'
		,cols: [[
			{type:'checkbox', fixed:'left', width:'3%'}
		    ,{field:'id', align:'center', width:'5%', title: 'ID', sort: true}
		    ,{field:'username',align:'center', width:'10%', title: '账号'}
		    ,{field:'phoneNo',align:'center', width:'15%', title: '手机号', sort: true}
		    ,{field:'nickName',align:'center', width:'10%', title: '姓名', minWidth: 150}
		    ,{field:'gender',align:'center', width:'10%', title: '性别', sort: true}
		    ,{field:'headUrl',align:'center', width:'15%', title: '用户图像', sort: true}
		    ,{field:'createTime',align:'center', width:'10%', title: '创建时间', sort: true}
		    ,{align:'center', titile:'操作', toolbar: '#barUser'}
		]]
		,page: true
	});
	
	//监听工具条
	table.on('tool(filter)', function(obj) {
		var data = obj.data;
		if(obj.event === 'grant') {
			user_id = data.id;
			title = '用户授权角色';
			fn_open_layer_win_4role();
		} else if(obj.event === 'del') {
			user_id = data.id;
			lay_confirm('该行将被删除，请确定', fn_ajax_delete_user);
		} else if(obj.event === 'edit'){
			user_id = data.id;
			title = '编辑用户';
			fn_open_layer_win();
		}
	});

	//行选择事件
	var $ = layui.$, active = {
		add:function() {
	    	//新增
			title = '新增用户';
			user_id = '';
	    	fn_open_layer_win();
	    },del:function() {
	    	var checkStatus = table.checkStatus('user_table'), data = checkStatus.data;
	    	var len = data.length;
	    	if(len == 0) {
	    		layer.msg('请选择操作的行');
	    		return false;
	    	}
	    	var idArr = new Array();
	    	for (var i = 0; i < len; i++) {
				var array_element = data[i];
				idArr.push(array_element.id);
			}
	    	user_id = idArr.join(',');
	    	lay_confirm('所选行将被删除，请确定', fn_ajax_delete_user);
	    },reload:function() {
	    	var search_value = $('#search_name').val();
	    	fn_reload_user_table(search_value);
	    }
	};
  
	//选择操作
	$('.userTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	
	/** 删除用户 */
	var fn_ajax_delete_user = function() {
		var url = '/api/user?id=' + user_id;
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('success');
				//刷新列表
				fn_reload_user_table('');
			}else {
				var msg = data.msg;
				if(msg) {
					layer.msg(msg);
				}
			}
		}
		ajax(url,'DELETE',success);
	};

	/** 提交表单 */
	var fn_ajax_submit_user_form = function(data, index) {
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('Success');
				
				//关闭窗口
				layer.close(index);
				
				// 刷新表格
				fn_reload_user_table('');
			}else {
				layer.msg(data.msg);
			}
		}
		ajax('/api/user', 'POST', success, JSON.stringify(data));
	};

	/** 刷新表格 */
	var fn_reload_user_table = function(key) {
		//刷新列表
		table.reload('user_table', {
			where: {
				//设定异步数据接口的额外参数，任意设
				'queryKey': key
			}
			,page: {
				//重新从第 1 页开始
				curr: 1 
			}
		});
	};

	/** 查询用户 */
	var fn_get_edit_user_data = function() {
		var user = null;
		var success = function(data) {
			if(null != data && data.result){
				user = data.data;
			}
		}
		var data = {};
		var url = '/api/user?id=' + user_id;
		ajax(url, 'GET', success, data, false);
		return user;
	};

	/**
	 * 打开layer Windows
	 */
	var fn_open_layer_win = function() {
		//表单数据
		var content = fn_user_add_or_edit_form_html();
		//按钮事件
		var cancel = function(){ 
			//右上角关闭回调
			layer.msg('cancel');
		},
		yes = function(index) {
			var id = $('input[name=id]').val();
			var username = $('input[name=username]').val();
			var password = $('input[name=password]').val();
			var phoneNo = $('input[name=phoneNo]').val();
			var nickName = $('input[name=nickName]').val();
			var gender = $('input[name=gender]:checked').val();
			var headUrl = $('input[name=headUrl]').val();
			var data = {
				'id': id,
				'username':username,
				'password':password,
				'phoneNo':phoneNo,
				'nickName':nickName,
				'gender':gender,
				'headUrl':headUrl
			}
			$('#user_submit').trigger("click");
			if(username && password) {
				fn_ajax_submit_user_form(data, index);
			}
		}
		//打开窗口
		layerWin(title, content, cancel, yes);
		
		//动态表单重新绑定表单对象
		form.render();
		
		//监听提交
		form.on('submit(user)', function(data){
		    return false;
		});
	};
	
	/**
	 * 打开选择框，选择角色
	 */
	var fn_open_layer_win_4role = function() {
		// 弹框标题
		var title = '选择角色';
		// 弹框类型
		var type = 2;
		//表单数据
		var content = '/userrole?userId=' + user_id;
		//按钮事件
		var cancel = function(){ 
			//右上角关闭回调
			layer.msg('cancel');
		},
		yes = function(index, layero) {
			var roleIds = window["layui-layer-iframe" + index].getRoleData();
			var data = {
				'userId': user_id,
				'roleIds':roleIds
			}
			// 提交数据
			fn_ajax_submit_role_grant(data, index);
		}
		
		// 个性窗口
		layer.open({
			id:'user_role_table',
			area: ['820px', '620px'],
			type: type,
			skin: 'layui-layer-molv', 
			closeBtn: type, 
			title: title,
			anim: 4,
			shadeClose: true, 
			content: content,
			btn: ['确定'],
			cancel: cancel,
			yes: yes
		});
	};
	
	/** 提交角色授权 */
	var fn_ajax_submit_role_grant = function(data, index) {
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('Success');
				
				//关闭窗口
				layer.close(index);
				
				// 刷新表格
				fn_reload_user_table('');
			}else {
				layer.msg(data.msg);
			}
		}
		var url = '/api/user/grant';
		ajax(url, 'POST', success, JSON.stringify(data), false);
	};
	
	/**
	 * 构造表单数据
	 */
	var fn_user_add_or_edit_form_html = function() {
		var id = '';
		var username = '';
		var password = '';
		var phoneNo = '';
		var nickName = '';
		var gender = '';
		var headUrl = '';
		if(user_id) {
			var edit_user = fn_get_edit_user_data();
			id = edit_user.id;
			username = edit_user.username==null?'':edit_user.username;
			password = edit_user.password==null?'':edit_user.password;
			phoneNo = edit_user.phoneNo==null?'':edit_user.phoneNo;
			nickName = edit_user.nickName==null?'':edit_user.nickName;
			gender = edit_user.gender==null?'':edit_user.gender;
			headUrl = edit_user.headUrl==null?'':edit_user.headUrl;
		}
		var user_html_arr = new Array();
		user_html_arr.push('<form class="layui-form layui-form-pane" style="margin:5px;" id="user_form">');
		
		// 隐藏id
		if(id) {
			user_html_arr.push('<input name="id" value="'+ id +'" style="display:none;"/>');
		}else {
			user_html_arr.push('<input name="id" style="display:none;"/>');
		}
		
		//用户账号
		user_html_arr.push('<div class="layui-form-item">');
		user_html_arr.push('<label class="layui-form-label">账号</label>');
		user_html_arr.push('<div class="layui-input-block">');
		user_html_arr.push('<input type="text" lay-verify="required" name="username" autocomplete="off"');
		user_html_arr.push(' value="'+ username +'" placeholder="请输入汉字、下划线、字母" class="layui-input">');
		user_html_arr.push('</div>');
		user_html_arr.push('</div>');
		
		//用户密码
		user_html_arr.push('<div class="layui-form-item">');
		user_html_arr.push('<label class="layui-form-label">密码</label>');
		user_html_arr.push('<div class="layui-input-block">');
		user_html_arr.push('<input type="password" lay-verify="required" name="password" autocomplete="off"');
		user_html_arr.push(' value="'+ password +'" placeholder="请输入密码" class="layui-input">');
		user_html_arr.push('</div>');
		user_html_arr.push('</div>');
		
		// 用户手机号
		user_html_arr.push('<div class="layui-form-item">');
		user_html_arr.push('<label class="layui-form-label">手机号</label>');
		user_html_arr.push('<div class="layui-input-block">');
		user_html_arr.push('<input type="text" name="phoneNo" autocomplete="off"');
		user_html_arr.push(' value="'+ phoneNo +'" placeholder="请输入手机号" class="layui-input">');
		user_html_arr.push('</div>');
		user_html_arr.push('</div>');
		
		// 用户名称
		user_html_arr.push('<div class="layui-form-item">');
		user_html_arr.push('<label class="layui-form-label">用户名称</label>');
		user_html_arr.push('<div class="layui-input-block">');
		user_html_arr.push('<input type="text" name="nickName" autocomplete="off"');
		user_html_arr.push(' value="'+ nickName +'" placeholder="请输入数字" class="layui-input">');
		user_html_arr.push('</div>');
		user_html_arr.push('</div>');
		
		// 用户性别
		user_html_arr.push('<div class="layui-form-item">');
		user_html_arr.push('<label class="layui-form-label">性别</label>');
		user_html_arr.push('<div class="layui-input-block">');
		user_html_arr.push('<input type="radio" name="gender" value="男" title="男"')
		if('男' == gender){
			user_html_arr.push(' checked');
		}
		user_html_arr.push(' >');
		user_html_arr.push('<input type="radio" name="gender" value="女" title="女"');
		if('女' == gender){
			user_html_arr.push(' checked');
		}
		user_html_arr.push(' >');
		user_html_arr.push('</div>');
		user_html_arr.push('</div>');
		
		// 用户头像
		user_html_arr.push('<div class="layui-form-item">');
		user_html_arr.push('<label class="layui-form-label">用户头像</label>');
		user_html_arr.push('<div class="layui-input-block">');
		user_html_arr.push('<input type="text" name="headUrl" autocomplete="off"');
		user_html_arr.push(' value="'+ nickName +'" placeholder="请输入用户头像" class="layui-input">');
		user_html_arr.push('</div>');
		user_html_arr.push('</div>');
		
		//提交按钮
		user_html_arr.push('<button class="layui-btn " style="display:none;" name="user_submit" ');
		user_html_arr.push(' id="user_submit" lay-submit="" lay-filter="user"></button>');
		user_html_arr.push('</form>');
		return user_html_arr.join('');
	};

});
