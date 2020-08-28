/**
 * 角色列表绑定数据
 * @returns
 */
layui.use(['table','form'], function() {
  	//lay table对象
	var table = layui.table,
	form = layui.form;
	//table绑定数据
	table.render({
		elem: '#role_table'
		,url:'/api/role/page'
		,cols: [[
			{type:'checkbox', fixed:'left', width:'3%'}
		    ,{field:'id', align:'center', width:'5%', title: 'ID', sort: true}
		    ,{field:'roleName',align:'center', width:'20%', title: '角色'}
		    ,{field:'createTime',align:'center', width:'20%', title: '创建时间', sort: true}
		    ,{align:'center', titile:'操作', toolbar: '#barRole'}
		]]
		,page: true
	});

	//行id
	var role_id, title;
	//监听工具条
	table.on('tool(filter)', function(obj) {
		var data = obj.data;
		if(obj.event === 'grant') {
			role_id = data.id;
			title = '角色授权菜单';
			fn_open_layer_win_4menu();
		} else if(obj.event === 'del') {
			role_id = data.id;
			lay_confirm('该行将被删除，请确定', fn_ajax_delete_role);
		} else if(obj.event === 'edit'){
			role_id = data.id;
			title = '编辑角色';
			fn_open_layer_win();
		}
	});

	//行选择事件
	var $ = layui.$, active = {
		add:function() {
	    	//新增
			title = '新增角色';
			role_id = '';
	    	fn_open_layer_win();
	    },del:function() {
	    	var checkStatus = table.checkStatus('role_table'), data = checkStatus.data;
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
	    	role_id = idArr.join(',');
	    	lay_confirm('所选行将被删除，请确定', fn_ajax_delete_role);
	    },reload:function() {
	    	var search_value = $('#search_name').val();
	    	fn_reload_role_table(search_value);
	    }
	};
  
	//选择操作
	$('.roleTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	
	/** 删除角色 */
	var fn_ajax_delete_role = function() {
		var url = '/api/role?id=' + role_id;
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('success');
				//刷新列表
				fn_reload_role_table('');
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
	var fn_ajax_submit_role_form = function(data, index) {
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('Success');
				
				//关闭窗口
				layer.close(index);
				
				// 刷新表格
				fn_reload_role_table('');
			}else {
				layer.msg(data.msg);
			}
		}
		ajax('/api/role', 'POST', success, JSON.stringify(data));
	};

	/** 刷新表格 */
	var fn_reload_role_table = function(key) {
		//刷新列表
		table.reload('role_table', {
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

	/** 查询角色 */
	var fn_get_edit_role_data = function() {
		var role = null;
		var success = function(data) {
			if(null != data && data.result){
				role = data.data;
			}
		}
		var data = {};
		var url = '/api/role?id=' + role_id;
		ajax(url, 'GET', success, data, false);
		return role;
	};

	/**
	 * 打开选择框，选择菜单
	 */
	var fn_open_layer_win_4menu = function() {
		// 弹框标题
		var title = '选择菜单';
		// 弹框类型
		var type = 2;
		//表单数据
		var content = '/rolemenu?roleId=' + role_id;
		//按钮事件
		var cancel = function(){ 
			//右上角关闭回调
			layer.msg('cancel');
		},
		yes = function(index, layero) {
			var menuIds = window["layui-layer-iframe" + index].getMemuData();
			var data = {
				'roleId': role_id,
				'menuIds':menuIds
			}
			// 提交数据
			fn_ajax_submit_role_grant(data, index);
		}
		
		// 个性窗口
		layer.open({
			id:'role_menu_table',
			area: ['820px', '620px'],
			type: type,
			skin: 'layui-layer-molv', 
			closeBtn: 2, 
			title:'选择菜单',
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
				fn_reload_role_table('');
			}else {
				layer.msg(data.msg);
			}
		}
		var url = '/api/role/grant';
		ajax(url, 'POST', success, JSON.stringify(data), false);
	};
	
	// 获取菜单数据
	var fn_get_menu_html = function() {
		var menu_html_arr = new Array();
		menu_html_arr.push('<table class="layui-hide" id="role_menu_table" lay-filter="filter2" ></table>');
		menu_html_arr.push('<script type="text/html" ');
		//table绑定数据
		menu_html_arr.push('table.render({');
		menu_html_arr.push('elem: "#role_menu_table"');
		menu_html_arr.push(',url:"/api/menu/"');
		menu_html_arr.push(',cols: [[');
		menu_html_arr.push('{type:"checkbox", fixed:"left", width:"3%"}');
		menu_html_arr.push(',{field:"id", align:"center", width:"5%", title: "ID", sort: true}');
		menu_html_arr.push(',{field:"name",align:"center", width:"20%", title: "菜单"}');
		menu_html_arr.push(',{field:"sequence",align:"center", width:"6%", title: "排序", sort: true}');
		menu_html_arr.push(',{field:"status",align:"center", width:"10%", title: "状态", minWidth: 150}');
		menu_html_arr.push(',{field:"url",align:"center", width:"20%", title: "URL", sort: true}');
		menu_html_arr.push(',{field:"pid",align:"center", width:"6%", title: "pid", sort: true}');
		menu_html_arr.push(']]');
		menu_html_arr.push(',page: true');
		menu_html_arr.push('});');
		menu_html_arr.push('</script>');
		
		return menu_html_arr.join('');
	}
	
	/**
	 * 打开layer Windows
	 */
	var fn_open_layer_win = function() {
		//表单数据
		var content = fn_role_add_or_edit_form_html();
		//按钮事件
		var cancel = function(){ 
			//右上角关闭回调
			layer.msg('cancel');
		},
		yes = function(index) {
			var id = $('input[name=id]').val();
			var roleName = $('input[name=roleName]').val();
			var data = {
				'id': id,
				'roleName':roleName
			}
			$('#role_submit').trigger("click");
			if(roleName) {
				fn_ajax_submit_role_form(data, index);
			}
		}
		//打开窗口
		layerWin(title, content, cancel, yes);
		
		//动态表单重新绑定表单对象
		form.render();
		
		//监听提交
		form.on('submit(role)', function(data){
		    return false;
		});
	};
	
	/**
	 * 构造表单数据
	 */
	var fn_role_add_or_edit_form_html = function() {
		var id = '';
		var roleName = '';
		var roleMenuSet = '';
		if(role_id) {
			var edit_role = fn_get_edit_role_data();
			id = edit_role.id;
			roleName = edit_role.roleName==null?'':edit_role.roleName;
			
			// 角色菜单列表
			var roleMenus = edit_role.roleMenuSet;
			var roleMenuArray = new Array();
			for (let roleMenu of roleMenus) {
				roleMenuArray.push(roleMenu.menuId);
			}
			roleMenuSet = roleMenuArray.join(',');
		}
		var role_html_arr = new Array();
		role_html_arr.push('<form class="layui-form layui-form-pane" style="margin:5px;" id="role_form">');
		
		// 隐藏id
		if(id) {
			role_html_arr.push('<input name="id" value="'+ id +'" style="display:none;"/>');
		}else {
			role_html_arr.push('<input name="id" style="display:none;"/>');
		}
		
		//角色名称
		role_html_arr.push('<div class="layui-form-item">');
		role_html_arr.push('<label class="layui-form-label">角色</label>');
		role_html_arr.push('<div class="layui-input-block">');
		role_html_arr.push('<input type="text" lay-verify="required" name="roleName" autocomplete="off"');
		role_html_arr.push(' value="'+ roleName +'" placeholder="请输入汉字、下划线、字母" class="layui-input">');
		role_html_arr.push('</div>');
		role_html_arr.push('</div>');
		
		//提交按钮
		role_html_arr.push('<button class="layui-btn " style="display:none;" name="role_submit" ');
		role_html_arr.push(' id="role_submit" lay-submit="" lay-filter="role"></button>');
		role_html_arr.push('</form>');
		return role_html_arr.join('');
	};

});
