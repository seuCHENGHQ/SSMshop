$(function() {

	/*
	 * e指的是事件：在firefox中，只能在事件现场使用window.event，所以只有把event传给函数使用。
	 * 所以，为了兼容FF和其它浏览器，一般会在函数里重新给e赋值：e = window.event || e;
	 * 也就是说，如果window.event存在，则该浏览器支持直接使用window.event，否在就是不支持，不支持就使用传进来的e。
	 */
	function getlist(e) {
		// 通过访问我们在shopManagementController中定义好的controller之后，得到该用户名下注册的店铺信息
		$.ajax({
			url : "/o2o/shopadmin/getshoplist",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.success) {
					handleList(data.shopList);
					handleUser(data.user);
				}
			}
		});
	}

	// 在左上角显示 “你好，用户名”
	function handleUser(data) {
		$('#user-name').text(data.name);
	}

	// 在列表中显示店铺信息
	function handleList(data) {
		var html = '';
		data.map(function(item, index) {
			html += '<div class="row row-shop"><div class="col-40">'
					+ item.shopName + '</div><div class="col-40">'
					+ shopStatus(item.enableStatus)
					+ '</div><div class="col-20">'
					+ goShop(item.enableStatus, item.shopId) + '</div></div>';

		});
		$('.shop-wrap').html(html);
	}

	// 生成前往指定shopId对应的shop的信息修改页面
	function goShop(status, id) {
		if (status != 0 && status != -1) {
			return '<a href="/o2o/shopadmin/shopmanagement?shopId=' + id
					+ '">进入</a>';
		} else {
			return '';
		}
	}

	// 显示店铺当前的状态
	function shopStatus(status) {
		if (status == 0) {
			return '审核中';
		} else if (status == -1) {
			return '店铺非法';
		} else {
			return '审核通过';
		}
	}

	// 退出系统按钮
	$('#log-out').click(function() {
		$.ajax({
			url : "/myo2o/shop/logout",
			type : "post",
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					window.location.href = '/myo2o/shop/ownerlogin';
				}
			},
			error : function(data, error) {
				alert(error);
			}
		});
	});

	// 当前往shop/shoplist.html页面的时候，就加载当前用户名下的店铺信息
	getlist();
});
