/**
 * 字典列表绑定数据
 * @returns
 */
layui.use(['table','form'], function() {
  	//lay table对象
	var table = layui.table,
	form = layui.form;
	//table绑定数据
	table.render({
		elem: '#dictionary_table'
		,url:'/api/dictionary/page/'
		,cols: [[
			{type:'checkbox', fixed:'left', width:'3%'}
		    ,{field:'id', align:'center', width:'5%', title: 'ID', sort: true}
		    ,{field:'typeCode',align:'center', width:'20%', title: '类型'}
		    ,{field:'dicCode',align:'center', width:'20%', title: '编码'}
		    ,{field:'dicName',align:'center', width:'6%', title: '名称', sort: true}
		    ,{field:'dicSort',align:'center', width:'10%', title: '排序', minWidth: 150}
		    ,{align:'center', titile:'操作', toolbar: '#barDictionary'}
		]]
		,page: true
	});

	//行id
	var dictionary_id, title;
	//监听工具条
	table.on('tool(dictionary_filter)', function(obj) {
		var data = obj.data;
		if(obj.event === 'detail') {
			layer.msg('ID：'+ data.id + ' 的查看操作');
		} else if(obj.event === 'del') {
			dictionary_id = data.id;
			lay_confirm('该行将被删除，请确定', fn_ajax_delete_dictionary);
		} else if(obj.event === 'edit'){
			dictionary_id = data.id;
			title = '编辑字典';
			fn_open_layer_win();
		}
	});

	//行选择事件
	var $ = layui.$, active = {
		add:function() {
	    	//新增
			title = '新增字典';
			dictionary_id = '';
	    	fn_open_layer_win();
	    },del:function() {
	    	var checkStatus = table.checkStatus('dictionary_table'), data = checkStatus.data;
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
	    	dictionary_id = idArr.join(',');
	    	lay_confirm('所选行将被删除，请确定', fn_ajax_delete_dictionary);
	    },reload:function() {
	    	var search_value = $('#search_name').val();
	    	fn_reload_dictionary_table(search_value);
	    }
	};
  
	//选择操作
	$('.dictionaryTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	
	/** 删除字典 */
	var fn_ajax_delete_dictionary = function() {
		var url = '/api/dictionary?id=' + dictionary_id;
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('success');
				//刷新列表
				fn_reload_dictionary_table('');
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
	var fn_ajax_submit_dictionary_form = function(data, index) {
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('Success');
				
				//关闭窗口
				layer.close(index);
				
				// 刷新表格
				fn_reload_dictionary_table('');
			}else {
				layer.msg(data.msg);
			}
		}
		ajax('/api/dictionary','POST',success,JSON.stringify(data));
	};

	/** 刷新表格 */
	var fn_reload_dictionary_table = function(key) {
		//刷新列表
		table.reload('dictionary_table', {
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

	/** 编辑查询 */
	var fn_get_edit_dictionary_data = function() {
		var dictionary = null;
		var success = function(data) {
			if(null != data && data.result){
				dictionary = data.data;
			}
		}
		var data ={};
		var url = '/api/dictionary?id=' + dictionary_id;
		ajax(url, 'GET', success, data, false);
		return dictionary;
	};

	/**
	 * 打开layer Windows
	 */
	var fn_open_layer_win = function() {
		//表单数据
		var content = fn_dictionary_add_or_edit_form_html();
		//按钮事件
		var cancel = function(){ 
			//右上角关闭回调
			layer.msg('cancel');
		},
		yes = function(index) {
			var typeCode = $('select[name=typeCode]').val();
			if(typeCode == undefined) {
				typeCode = '';
			}
			var id = $('input[name=id]').val();
			var dicCode = $('input[name=dicCode]').val();
			var dicName = $('input[name=dicName]').val();
			var dicSort = $('input[name=dicSort]').val();
			
			var data = {
				'id': id,
				'typeCode':typeCode,
				'dicCode':dicCode,
				'dicName':dicName,
				'dicSort':dicSort
			}
			$('#dictionary_submit').trigger("click");
			if(typeCode && dicCode && dicName && dicSort) {
				fn_ajax_submit_dictionary_form(data, index);
			}
		}
		//打开窗口
		layerWin(title, content, cancel, yes);
		
		//动态表单重新绑定表单对象
		form.render();
		
		//监听提交
		form.on('submit(dictionary)', function(data){
		    return false;
		});
	};
	
	/**
	 * 构造表单数据
	 */
	var fn_dictionary_add_or_edit_form_html = function() {
		var id = '';
		var typeCode = '';
		var dicCode = '';
		var dicName = '';
		var dicSort = '';
		
		// 编辑状态
		if(dictionary_id) {
			var dictionary = fn_get_edit_dictionary_data();
			id = dictionary.id;
			typeCode = dictionary.typeCode==null?'':dictionary.typeCode;
			dicCode = dictionary.dicCode==null?'':dictionary.dicCode;
			dicName = dictionary.dicName==null?'':dictionary.dicName;
			dicSort = dictionary.dicSort==null?'':dictionary.dicSort;
		}
		
		// 构造表单HTML标签
		var dictionary_html_arr = new Array();
		dictionary_html_arr.push('<form class="layui-form layui-form-pane" style="margin:5px;" id="dictionary_form">');
		
		// 隐藏id
		if(id) {
			dictionary_html_arr.push('<input name="id" value="'+ id +'" style="display:none;"/>');
		}else {
			dictionary_html_arr.push('<input name="id" style="display:none;"/>');
		}
		
		// 字典类型
		dictionary_html_arr.push('<div class="layui-form-item">');
		dictionary_html_arr.push('<label class="layui-form-label">字典类型</label>');
		dictionary_html_arr.push('<div class="layui-input-block">');
		dictionary_html_arr.push('<select name="typeCode" lay-filter="">');
		dictionary_html_arr.push('<option value=""></option>');
		
		// 获取字典类型数据
		var data = fn_get_dictionary_type();
		if(data != undefined){
			var len = data.length;
			for (var i = 0; i < len; i++) {
				var dictionary_type = data[i];
				var selected_type_code = dictionary_type.typeCode;
				var selected_type_name = dictionary_type.typeName;
				if(typeCode == selected_type_code) {
					dictionary_html_arr.push('<option value="'+ selected_type_code +'" selected="selected">'+ selected_type_code +'</option>');
				}else {
					dictionary_html_arr.push('<option value="'+ selected_type_code +'">'+ selected_type_name +'</option>');
				}
			}
		}
		dictionary_html_arr.push('</select>');
		dictionary_html_arr.push('</div>');
		dictionary_html_arr.push('</div>');
		
		// 编码
		dictionary_html_arr.push('<div class="layui-form-item">');
		dictionary_html_arr.push('<label class="layui-form-label">编码</label>');
		dictionary_html_arr.push('<div class="layui-input-block">');
		dictionary_html_arr.push('<input type="text" lay-verify="required" name="dicCode" autocomplete="off"');
		dictionary_html_arr.push(' value="'+ dicCode +'" placeholder="请输入编码" class="layui-input">');
		dictionary_html_arr.push('</div>');
		dictionary_html_arr.push('</div>');
		
		// 名称
		dictionary_html_arr.push('<div class="layui-form-item">');
		dictionary_html_arr.push('<label class="layui-form-label">名称</label>');
		dictionary_html_arr.push('<div class="layui-input-block">');
		dictionary_html_arr.push('<input type="text" lay-verify="required" name="dicName" autocomplete="off"');
		dictionary_html_arr.push(' value="'+ dicName +'" placeholder="请输入名称" class="layui-input">');
		dictionary_html_arr.push('</div>');
		dictionary_html_arr.push('</div>');
		
		// 排序
		dictionary_html_arr.push('<div class="layui-form-item">');
		dictionary_html_arr.push('<label class="layui-form-label">排序</label>');
		dictionary_html_arr.push('<div class="layui-input-block">');
		dictionary_html_arr.push('<input type="text" lay-verify="required|number" name="dicSort" autocomplete="off"');
		dictionary_html_arr.push(' value="'+ dicSort +'" placeholder="请输入排序" class="layui-input">');
		dictionary_html_arr.push('</div>');
		dictionary_html_arr.push('</div>');
		
		//提交按钮
		dictionary_html_arr.push('<button class="layui-btn " style="display:none;" name="dictionary_submit" ');
		dictionary_html_arr.push(' id="dictionary_submit" lay-submit="" lay-filter="dictionary"></button>');
		dictionary_html_arr.push('</form>');
		return dictionary_html_arr.join('');
	};

	/**
	 * 获取父级字典数据
	 */
	var fn_get_dictionary_type = function() {
		var dictionary_type;
		var success = function(data) {
			if(null != data && data.result){
				dictionary_type = data.data;
			}
		}
		var data = {};
		var url = '/api/dictype/list';
		ajax(url, 'GET', success, data, false);
		return dictionary_type;
	};
});
