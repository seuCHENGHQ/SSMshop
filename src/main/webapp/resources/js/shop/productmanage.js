$(function() {
	var shopId = 1;
	var listUrl = '/o2o/shopadmin/listproductsbyshop?pageIndex=1&pageSize=9999&shopId='
			+ shopId;
	var statusUrl = '/o2o/shopadmin/modifyproduct';

	/*
	 * 获取此店铺下的商品列表
	 */
	function getList() {
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var productList = data.productList;
				var tempHtml = '';
				/*
				 * 遍历每行的商品信息，并拼接成一行去显示
				 * 商品名称，优先级，上架/下架(含productId)，编辑按钮(含productId)
				 * 预览(含productId)
				 */
				productList.map(function(item, index) {
					var textOp = "下架";
					//表示当前商品的状态，默认是上架的
					//0表示目前是上架状态，1表示目前是下架状态
					//而数据库中0表示未上架，1表示上架
					//就是这样取反，在changeStatus函数中直接把contrayStatus放到数据库中就好了
					//所以变量名才叫contrayStatus，说明和数据库中的状态是相反的
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						//如果商品状态是0，即已经下架的商品，那么我们这个按钮的功能应该就是上架商品
						textOp = "上架";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					tempHtml += '' + '<div class="row row-product">'
							+ '<div class="col-30">'
							+ item.productName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.priority
							+ '</div>'
							+ '<div class="col-50">'
							+ '<a href="#" class="edit" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">编辑</a>'
							+ '<a href="#" class="status" data-id="'
							+ item.productId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">预览</a>'
							+ '</div>'
							+ '</div>';
				});
				$('.product-wrap').html(tempHtml);
			}
		});
	}

	getList();

	/**
	 * 改变商品的状态，即上架还是下架
	 * @param id
	 * @param enableStatus
	 * @returns
	 */
	function changeItemStatus(id, enableStatus) {
		var product = {};
		product.productId = id;
		product.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			$.ajax({
				url : statusUrl,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						//如果操作成功的话，就再次刷新列表
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}

	/**
	 * 为class为product-wrap的a标签绑定上点击事件
	 */
	$('.product-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						if (target.hasClass('edit')) {
							window.location.href = '/o2o/shopadmin/productoperation?productId='
									+ e.currentTarget.dataset.id;
						} else if (target.hasClass('status')) {
							/*
							 * e.currentTarget始终指向添加监听事件的那个对象，这里就是class=product-wrap的div对象
							 * target是指向触发监听事件的那个对象，这里就是各个a标签
							 */
							changeItemStatus(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						} else if (target.hasClass('preview')) {
							window.location.href = '/o2o/frontend/productdetail?productId='
									+ e.currentTarget.dataset.id;
						}
					});

	//绑定新增按钮的点击事件，如果点击新增，就不带productId参数直接访问productoperation页面，这样就是新增
	//当带productId访问productoperation页面时，代表要进行修改操作
	$('#new').click(function() {
		window.location.href = '/o2o/shopadmin/productoperation';
	});
});