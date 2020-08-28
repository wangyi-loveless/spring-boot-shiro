var menus;

/** 获取父级菜单数据 */
var fn_get_parent_menus = function() {
	var success = function(data) {
		if(null != data){
			menus = data;
		}
	}
	var data = {};
	var url = '/api/menu/parent';
	ajax(url, 'GET', success, data, false);
	return menus;
};
/**
 * 菜单列表绑定数据
 * @returns
 */
layui.use(['table','form'], function() {
	// 获取父级菜单
	fn_get_parent_menus();
	
	//lay table对象
	var table = layui.table,
	form = layui.form;
	
	//table绑定数据
	table.render({
		elem: '#menu_table'
		,url:'/api/menu/'
		,cols: [[
			{type:'checkbox', fixed:'left', width:'3%'}
		    ,{field:'id', align:'center', width:'5%', title: 'ID', sort: true}
		    ,{field:'pid',align:'center', width:'15%', title: '父级菜单', sort: true}
		    ,{field:'name',align:'center', width:'15%', title: '菜单'}
		    ,{field:'url',align:'center', width:'20%', title: 'URL'}
		    ,{field:'icon',align:'center', width:'10%', title: '图标'}
		    ,{field:'sequence',align:'center', width:'6%', title: '排序', sort: true}
		    ,{field:'status',align:'center', width:'6%', title: '状态', minWidth: 150}
		    ,{align:'center', titile:'操作', toolbar: '#barMenu'}
		]]
		,page: true
	});
	
	//行id
	var menu_id, title;
	//监听工具条
	table.on('tool(filter)', function(obj) {
		var data = obj.data;
		if(obj.event === 'detail') {
			layer.msg('ID：'+ data.id + ' 的查看操作');
		} else if(obj.event === 'del') {
			menu_id = data.id;
			lay_confirm('该行将被删除，请确定', fn_ajax_delete_menu);
		} else if(obj.event === 'edit'){
			menu_id = data.id;
			title = '编辑菜单';
			fn_open_layer_win();
		}
	});

	//行选择事件
	var $ = layui.$, active = {
		add:function() {
	    	//新增
			title = '新增菜单';
			menu_id = '';
	    	fn_open_layer_win();
	    },del:function() {
	    	var checkStatus = table.checkStatus('menu_table'), data = checkStatus.data;
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
	    	menu_id = idArr.join(',');
	    	lay_confirm('所选行将被删除，请确定', fn_ajax_delete_menu);
	    },reload:function() {
	    	var search_value = $('#search_name').val();
	    	fn_reload_menu_table(search_value);
	    }
	};
  
	//选择操作
	$('.menuTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	
	/** 删除菜单 */
	var fn_ajax_delete_menu = function() {
		var url = '/api/menu?id=' + menu_id;
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('success');
				//刷新列表
				fn_reload_menu_table('');
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
	var fn_ajax_submit_menu_form = function(data, index) {
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('Success');
				
				//关闭窗口
				layer.close(index);
				
				// 刷新表格
				fn_reload_menu_table('');
			}else {
				layer.msg(data.msg);
			}
		}
		ajax('/api/menu','POST',success,JSON.stringify(data));
	};

	/** 刷新表格 */
	var fn_reload_menu_table = function(key) {
		//刷新列表
		table.reload('menu_table', {
			where: {
				//设定异步数据接口的额外参数，任意设
				'queryKey': key
			}
			,page: {
				curr: 1 //重新从第 1 页开始
			}
		});
	};

	/** 查询菜单 */
	var fn_get_edit_menu_data = function() {
		var menu = null;
		var success = function(data) {
			if(null != data && data.result){
				menu = data.data;
			}
		}
		var data ={};
		var url = '/api/menu/one?id=' + menu_id;
		ajax(url, 'GET', success, data, false);
		return menu;
	};

	/**
	 * 打开layer Windows
	 */
	var fn_open_layer_win = function() {
		//表单数据
		var content = fn_menu_add_or_edit_form_html();
		//按钮事件
		var cancel = function(){ 
			//右上角关闭回调
			layer.msg('cancel');
		},
		yes = function(index) {
			var pid = $('select[name=pid]').val();
			if(!pid) {
				pid = 0;
			}
			var icon = $('select[name=icon]').val();
			if(icon == undefined || null == icon) {
				icon = '';
			}
			var id = $('input[name=id]').val();
			var name = $('input[name=name]').val();
			var url = $('input[name=url]').val();
			var sequence = $('input[name=sequence]').val();
			var status = $('input:checked').val();
			if(!status){
				status = 0;
			}
			var data = {
				'id': id,
				'pid':pid,
				'name':name,
				'icon':icon,
				'url':url,
				'sequence':sequence,
				'status':status
			}
			$('#menu_submit').trigger("click");
			if(name && sequence) {
				fn_ajax_submit_menu_form(data, index);
			}
		}
		
		var area = ['820px', '660px'];
		layerOpenArea(title, 1, content, cancel, yes, area);
		
		//动态表单重新绑定表单对象
		form.render();
		
		//监听提交
		form.on('submit(menu)', function(data){
		    return false;
		});
	};
	
	/**
	 * 构造表单数据
	 */
	var fn_menu_add_or_edit_form_html = function() {
		var edit_menu_id = '';
		var edit_menu_name = '';
		var edit_menu_icon = '';
		var edit_menu_url = '';
		var edit_menu_pid = 0;
		var edit_menu_sequence = '';
		var edit_menu_status = '';
		if(menu_id) {
			var edit_menu = fn_get_edit_menu_data();
			edit_menu_id = edit_menu.id;
			edit_menu_name = edit_menu.name==null?'':edit_menu.name;
			edit_menu_url = edit_menu.url==null?'':edit_menu.url;
			edit_menu_icon = edit_menu.icon==null?'':edit_menu.icon;
			edit_menu_pid = edit_menu.pid;
			edit_menu_sequence = edit_menu.sequence;
			edit_menu_status = edit_menu.status;
		}
		var menu_html_arr = new Array();
		menu_html_arr.push('<form class="layui-form layui-form-pane" style="margin:5px;" id="menu_form">');
		//隐藏id
		if(edit_menu_id) {
			menu_html_arr.push('<input name="id" value="'+ edit_menu_id +'" style="display:none;"/>');
		}else {
			menu_html_arr.push('<input name="id" style="display:none;"/>');
		}
		//父级菜单
		menu_html_arr.push('<div class="layui-form-item">');
		menu_html_arr.push('<label class="layui-form-label">父级菜单</label>');
		menu_html_arr.push('<div class="layui-input-block">');
		menu_html_arr.push('<select name="pid" lay-filter="">');
		menu_html_arr.push('<option value=""></option>');
		var data = fn_get_parent_menus();
		var len = data.length;
		for (var i = 0; i < len; i++) {
			var menu = data[i];
			var id = menu.id;
			var name = menu.name;
			if(edit_menu_pid == id) {
				 menu_html_arr.push('<option value="'+ id +'" selected="selected">'+ name +'</option>');
			}else {
				menu_html_arr.push('<option value="'+ id +'">'+ name +'</option>');
			}
		}
		menu_html_arr.push('</select>');
		menu_html_arr.push('</div>');
		menu_html_arr.push('</div>');
		
		//菜单名称
		menu_html_arr.push('<div class="layui-form-item">');
		menu_html_arr.push('<label class="layui-form-label">菜单名称</label>');
		menu_html_arr.push('<div class="layui-input-block">');
		menu_html_arr.push('<input type="text" lay-verify="required" name="name"  autocomplete="off"');
		menu_html_arr.push(' value="'+ edit_menu_name +'" placeholder="请输入汉字、下划线、字母" class="layui-input">');
		menu_html_arr.push('</div>');
		menu_html_arr.push('</div>');
		
		//菜单URL
		menu_html_arr.push('<div class="layui-form-item">');
		menu_html_arr.push('<label class="layui-form-label">URL</label>');
		menu_html_arr.push('<div class="layui-input-block">');
		menu_html_arr.push('<input type="text" name="url" autocomplete="off"');
		menu_html_arr.push(' value="'+ edit_menu_url +'" placeholder="请输入URL" class="layui-input">');
		menu_html_arr.push('</div>');
		menu_html_arr.push('</div>');
		
		// 图标
		menu_html_arr.push('<div class="layui-form-item">');
		menu_html_arr.push('<label class="layui-form-label">图标</label>');
		menu_html_arr.push('<div class="layui-input-block">');
		menu_html_arr.push('<select name="icon" lay-filter="">');
		menu_html_arr.push('<option value=""></option>');
		var icon_data = fn_get_icon();
		if(icon_data != undefined){
			var icon_len = icon_data.length;
			for (var j = 0; j < icon_len; j++) {
				var icon = icon_data[j];
				var id = icon.id;
				var value = icon.value;
				if(edit_menu_icon == id) {
					menu_html_arr.push('<option value="'+ id +'" selected="selected">'+ value +'</option>');
				}else {
					menu_html_arr.push('<option value="'+ id +'">'+ value +'</option>');
				}
			}
		}
		menu_html_arr.push('</select>');
		menu_html_arr.push('</div>');
		menu_html_arr.push('</div>');
		
		//菜单顺序
		menu_html_arr.push('<div class="layui-form-item">');
		menu_html_arr.push('<label class="layui-form-label">顺序</label>');
		menu_html_arr.push('<div class="layui-input-block">');
		menu_html_arr.push('<input type="text" lay-verify="required|number" name="sequence" autocomplete="off"');
		menu_html_arr.push(' value="'+ edit_menu_sequence +'" placeholder="请输入数字" class="layui-input">');
		menu_html_arr.push('</div>');
		menu_html_arr.push('</div>');
		
		//菜单状态
		menu_html_arr.push('<div class="layui-form-item" pane="">');
		menu_html_arr.push('<label class="layui-form-label">状态</label>');
		menu_html_arr.push('<div class="layui-input-block">');
		menu_html_arr.push('<input type="checkbox" value="1" name="status" lay-text="启用|禁用"');
		
		if(edit_menu_status == 1) {
			menu_html_arr.push(' checked="" lay-skin="switch" lay-filter="menu_status">');
		}else {
			menu_html_arr.push(' lay-skin="switch" lay-filter="menu_status">');
		}
		menu_html_arr.push('</div>');
		menu_html_arr.push('</div>');
		
		//提交按钮
		menu_html_arr.push('<button class="layui-btn " style="display:none;" name="menu_submit" ');
		menu_html_arr.push(' id="menu_submit" lay-submit="" lay-filter="menu"></button>');
		menu_html_arr.push('</form>');
		return menu_html_arr.join('');
	};
	
	/** 获取图标JSON数据 */
	var fn_get_icon = function(){
		var icon;
		var success = function(data) {
			if(null != data){
				icon = data;
			}
		}
		var data = {};
		var url = '../json/icon.json';
		ajax(url, 'GET', success, data, false);
		return icon;
	};
});
