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
		elem: '#user_role_table'
		,url:'/api/role/page/'
		,cols: [[
			{type:'checkbox', fixed:'left', width:'10%'}
		    ,{field:'id', align:'center', width:'10%', title: 'ID', sort: true}
		    ,{field:'roleName',align:'center', width:'40%', title: '角色'}
		    ,{field:'createTime',align:'center', width:'40%', title: '创建时间', sort: true}
		]]
		,page: true,
		parseData:function(res) {
			var selectedRoleIds = document.getElementById('roleIds').value;
			var checkedSet = [];
			if(selectedRoleIds){
				checkedSet=selectedRoleIds.split(',');
			}
			//res 即为原始返回的数据
	    	for(var i in res.data) {
	    		var roleId = res.data[i].id + '';
	    		if(checkedSet.indexOf(roleId)>-1) {
	    			//如果set集合中有的话，给rows添加check属性选中
	    			res.data[i]["LAY_CHECKED"] = true;
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
	var $ = layui.$, active = {
		reload:function() {
	    	var search_value = $('#search_name').val();
	    	fn_reload_role_table(search_value);
	    }
	};
  
	// 选择操作
	$('.roleTable .layui-btn').on('click', function() {
		var type = $(this).data('type');
		active[type] ? active[type].call(this) : '';
	});

	/** 刷新表格 */
	var fn_reload_role_table = function(key) {
		//刷新列表
		table.reload('user_role_table', {
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
var getRoleData = function(){
	var checkStatus = table.checkStatus('user_role_table'), data = checkStatus.data;
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
