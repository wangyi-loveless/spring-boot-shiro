/**
 * 字典类型列表绑定数据
 * @returns
 */
layui.use(['table','form'], function() {
  	//lay table对象
	var table = layui.table,
	form = layui.form;
	//table绑定数据
	table.render({
		elem: '#dictype_table'
		,url:'/api/dictype/page/'
		,cols: [[
			{type:'checkbox', fixed:'left', width:'3%'}
		    ,{field:'id', align:'center', width:'5%', title: 'ID', sort: true}
		    ,{field:'typeCode',align:'center', width:'15%', title: '编码'}
		    ,{field:'typeName',align:'center', width:'30%', title: '名称', sort: true}
		    ,{field:'typeSort',align:'center', width:'10%', title: '排序', sort: true}
		    ,{align:'center', titile:'操作', toolbar: '#barDictype'}
		]]
		,page: true
	});
	

	//行id
	var dictype_id, title;
	//监听工具条
	table.on('tool(dictype_table_filter)', function(obj) {
		var data = obj.data;
		if(obj.event === 'del') {
			dictype_id = data.id;
			lay_confirm('该行将被删除，请确定', fn_ajax_delete_dictype);
		} else if(obj.event === 'edit'){
			dictype_id = data.id;
			title = '编辑字典类型';
			fn_open_layer_win();
		}
	});

	//行选择事件
	var $ = layui.$, active = {
		add:function() {
	    	//新增
			title = '新增字典类型';
			dictype_id = '';
	    	fn_open_layer_win();
	    },del:function() {
	    	var checkStatus = table.checkStatus('dictype_table'), data = checkStatus.data;
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
	    	dictype_id = idArr.join(',');
	    	lay_confirm('所选行将被删除，请确定', fn_ajax_delete_dictype);
	    },reload:function() {
	    	var search_value = $('#search_name').val();
	    	fn_reload_dictype_table(search_value);
	    }
	};
  
	//选择操作
	$('.dictypeTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});
	
	/** 删除字典类型 */
	var fn_ajax_delete_dictype = function() {
		var url = '/api/dictype?id=' + dictype_id;
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('success');
				//刷新列表
				fn_reload_dictype_table('');
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
	var fn_ajax_submit_dictype_form = function(data, index) {
		var success = function(data) {
			if(null != data && data.result){
				//操作提示
				layer.msg('Success');
				
				//关闭窗口
				layer.close(index);
				
				// 刷新表格
				fn_reload_dictype_table('');
			}else {
				layer.msg(data.msg);
			}
		}
		ajax('/api/dictype','POST',success,JSON.stringify(data));
	};

	/** 刷新表格 */
	var fn_reload_dictype_table = function(key) {
		//刷新列表
		table.reload('dictype_table', {
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

	/** 查询字典类型 */
	var fn_get_edit_dictype_data = function() {
		var dictype;
		var success = function(data) {
			if(null != data && data.result){
				dictype = data.data;
			}
		}
		var data ={};
		var url = '/api/dictype?id=' + dictype_id;
		ajax(url, 'GET', success, data, false);
		return dictype;
	};

	/** 打开layer Windows */
	var fn_open_layer_win = function() {
		//表单数据
		var content = fn_dictype_add_or_edit_form_html();
		//按钮事件
		var cancel = function(){ 
			//右上角关闭回调
			layer.msg('cancel');
		},
		yes = function(index) {
			var id = $('input[name=id]').val();
			var typeCode = $('input[name=typeCode]').val();
			var typeName = $('input[name=typeName]').val();
			var typeSort = $('input[name=typeSort]').val();
			
			var data = {
				'id': id,
				'typeCode':typeCode,
				'typeName':typeName,
				'typeSort':typeSort
			}
			$('#dictype_submit').trigger("click");
			if(typeCode && typeName && typeSort) {
				fn_ajax_submit_dictype_form(data, index);
			}
		}
		//打开窗口
		layerWin(title, content, cancel, yes);
		
		//动态表单重新绑定表单对象
		form.render();
		
		//监听提交
		form.on('submit(dictype)', function(data){
		    return false;
		});
	};
	
	/** 构造表单数据 */
	var fn_dictype_add_or_edit_form_html = function() {
		var id;
		var typeCode = '';
		var typeName = '';
		var typeSort = '';
		
		// id不为空，则为编辑
		if(dictype_id) {
			var dictype = fn_get_edit_dictype_data();
			if(dictype != undefined){
				id = dictype.id;
				typeCode = dictype.typeCode == null?'':dictype.typeCode;
				typeName = dictype.typeName == null?'':dictype.typeName;
				typeSort = dictype.typeSort == null?'':dictype.typeSort;
			}
		}
		
		// 构造HTML数据
		var dictype_html_arr = new Array();
		dictype_html_arr.push('<form class="layui-form layui-form-pane" style="margin:5px;" id="dictype_form">');
		
		// 隐藏id
		if(id) {
			dictype_html_arr.push('<input name="id" value="'+ id +'" style="display:none;"/>');
		}else {
			dictype_html_arr.push('<input name="id" style="display:none;"/>');
		}
		
		// 字典类型编码
		dictype_html_arr.push('<div class="layui-form-item">');
		dictype_html_arr.push('<label class="layui-form-label">编码</label>');
		dictype_html_arr.push('<div class="layui-input-block">');
		dictype_html_arr.push('<input type="text" lay-verify="required" name="typeCode" autocomplete="off"');
		dictype_html_arr.push(' value="'+ typeCode +'" placeholder="请输入汉字、下划线、字母" class="layui-input">');
		dictype_html_arr.push('</div>');
		dictype_html_arr.push('</div>');
		
		// 字典类型名称
		dictype_html_arr.push('<div class="layui-form-item">');
		dictype_html_arr.push('<label class="layui-form-label">名称</label>');
		dictype_html_arr.push('<div class="layui-input-block">');
		dictype_html_arr.push('<input type="text" name="typeName" autocomplete="off"');
		dictype_html_arr.push(' value="'+ typeName +'" placeholder="请输入名称" class="layui-input">');
		dictype_html_arr.push('</div>');
		dictype_html_arr.push('</div>');
		
		//字典类型顺序
		dictype_html_arr.push('<div class="layui-form-item">');
		dictype_html_arr.push('<label class="layui-form-label">顺序</label>');
		dictype_html_arr.push('<div class="layui-input-block">');
		dictype_html_arr.push('<input type="text" lay-verify="required|number" name="typeSort" autocomplete="off"');
		dictype_html_arr.push(' value="'+ typeSort +'" placeholder="请输入数字" class="layui-input">');
		dictype_html_arr.push('</div>');
		dictype_html_arr.push('</div>');
		
		//提交按钮
		dictype_html_arr.push('<button class="layui-btn " style="display:none;" name="dictype_submit" ');
		dictype_html_arr.push(' id="dictype_submit" lay-submit="" lay-filter="dictype"></button>');
		dictype_html_arr.push('</form>');
		return dictype_html_arr.join('');
	};
});
