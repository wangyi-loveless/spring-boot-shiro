//lay table对象
var table ;

/**
 * LayUI对象
 * @returns
 */
layui.use(['table','form'], function() {
  	// lay table对象
	table = layui.table,
	form = layui.form;
	
	//table绑定数据
	table.render({
		elem: '#role_menu_table'
		,url:'/api/menu/'
		,cols: [[
			{type:'checkbox', fixed:'left', width:'10%'}
		    ,{field:'id', align:'center', width:'10%', title: 'ID', sort: true}
		    ,{field:'name',align:'center', width:'20%', title: '菜单'}
		    ,{field:'sequence',align:'center', width:'10%', title: '排序', sort: true}
		    ,{field:'status',align:'center', width:'10%', title: '状态', minWidth: 150}
		    ,{field:'url',align:'center', width:'30%', title: 'URL', sort: true}
		    ,{field:'pid',align:'center', width:'10%', title: 'pid', sort: true}
		]]
		,page: true,
		parseData:function(res) {
			var selectedMenuIds = document.getElementById('menuIds').value;
			var checkedSet = [];
			if(selectedMenuIds){
				checkedSet=selectedMenuIds.split(',');
			}
			//res 即为原始返回的数据
	    	for(var i in res.data) {
	    		var menuId = res.data[i].id + '';
	    		if(checkedSet.indexOf(menuId)>-1) {
	    			//如果set集合中有的话，给rows添加check属性选中
	    			res.data[i]["LAY_CHECKED"] = true;
	    			$("input[type=checkbox]").prop('checked',true);
	    		}
	    	}
		    return {
		    	//解析接口状态
		        "code": res.code, 
		        //解析数据长度
		        "count": res.count, 
		        //解析数据列表
		        "data": res.data 
		    };  
		}
	});
	
	// 行选择事件
//	table.on('checkbox(role_menu_table_filter)', function(obj) {
//		getAllcheckedData();
//	});
	
	// 行选择事件
	var $ = layui.$, active = {
		reload:function() {
	    	var search_value = $('#search_name').val();
	    	fn_reload_menu_table(search_value);
	    }
	};
  
	// 选择操作
	$('.menuTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});

	/** 刷新表格 */
	var fn_reload_menu_table = function(key) {
		//刷新列表
		table.reload('role_menu_table', {
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
});

/**
 * 获取菜单选择数据
 */
var getMemuData = function(){
	var checkStatus = table.checkStatus('role_menu_table'), data = checkStatus.data;
	var len = data.length;
	if(len == 0) {
//		layer.msg('请选择要授权的菜单');
//		return false;
	}
	var idArr = new Array();
	for (var i = 0; i < len; i++) {
		var array_element = data[i];
		idArr.push(array_element.id);
	}
	return idArr.join(',');
}
