$(function() {
	var shopId = 1;
	var listUrl = '/o2o/shopadmin/getproductcategorylist?shopId=' + shopId;
	var addUrl = '/o2o/shopadmin/addproductcategorys';
	var deleteUrl = '/o2o/shopadmin/removeproductcategory';

	$
			.getJSON(
					listUrl,
					function(data) {
						if (data.success) {
							var dataList = data.data;
							//这里也进行了productCategory列表的刷新操作
							$('.category-wrap').html('');
							var tempHtml = '';
							dataList
									.map(function(item, index) {
										//注意，这里如果是从数据库中读出来的数据，其class是带now的，而我们的批量提交功能，客户新增的条目，其class是带temp的
										tempHtml += ''
												+ '<div class="row row-product-category now">'
												+ '<div class="col-33 product-category-name">'
												+ item.productCategoryName
												+ '</div>'
												+ '<div class="col-33">'
												+ item.priority
												+ '</div>'
												+ '<div class="col-33"><a href="#" class="button delete" data-id="'
												+ item.productCategoryId
												+ '">删除</a></div>' + '</div>';
									});
							$('.category-wrap').append(tempHtml);
						}
					});

	function getList() {
		$
				.getJSON(
						listUrl,
						function(data) {
							if (data.success) {
								var dataList = data.data;
								//这里刷新列表的内容，因此在下边提交过新增的productCategory之后，也会在这里进行刷新
								$('.category-wrap').html('');
								var tempHtml = '';
								dataList
										.map(function(item, index) {
											tempHtml += ''
													+ '<div class="row row-product-category now">'
													+ '<div class="col-33 product-category-name">'
													+ item.productCategoryName
													+ '</div>'
													+ '<div class="col-33">'
													+ item.priority
													+ '</div>'
													+ '<div class="col-33"><a href="#" class="button delete" data-id="'
													+ item.productCategoryId
													+ '">删除</a></div>'
													+ '</div>';
										});
								$('.category-wrap').append(tempHtml);
							}
						});
	}
	getList();

	$('#submit').click(function() {
		//$(".intro")	所有 class="intro" 的元素
		var tempArr = $('.temp');
		//创建JavaScript中的list对象
		var productCategoryList = [];
		tempArr.map(function(index, item) {
			//这里创建了JavaScript中的JSON对象，其实JSON对象和Map挺像的，都是键值对
			var tempObj = {};
			//item是<div class="row row-product-category temp">元素，通过find('.category')找到class=category的元素，这里是input元素，再用val()将其值取出来
			tempObj.productCategoryName = $(item).find('.category').val();
			//priority和上边同理
			tempObj.priority = $(item).find('.priority').val();
			//这里JavaScript会自动将productCategoryName和priority转换为boolean变量，并且null会转换为false
			if (tempObj.productCategoryName && tempObj.priority) {
				//push() 方法可向数组的末尾添加一个或多个元素，并返回新的长度。
				//语法：arrayObject.push(newelement1,newelement2,....,newelementX)  其中2...x个元素都是可选的，第一个是必须有的
				productCategoryList.push(tempObj);
			}
		});
		$.ajax({
			url : addUrl,
			type : 'POST',
			//将list对象转化为json字符串传给controller程序，因为controller程序使用了@RequestBody注解，因此可以将该JSON字符串再转换为list对象
			data : JSON.stringify(productCategoryList),
			//这里设置发送数据的类型
			contentType : 'application/json',
			// 这里设置返回值类型
			dataType:'json',
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					//当提交成功的时候，就将class中的temp
					getList();
				} else {
					$.toast('提交失败！');
				}
			}
		});
	});

	//这里绑定新增按钮的点击事件，实现了点击一次，就新增一行的功能
	$('#new')
			.click(
					function() {
						//注意这里，新增的条目，其class都是带temp的
						var tempHtml = '<div class="row row-product-category temp">'
								//和前面不同的是，这里新增的条目，其中包含文本和数字的输入框
								+ '<div class="col-33"><input class="category-input category" type="text" placeholder="分类名"></div>'
								+ '<div class="col-33"><input class="category-input priority" type="number" placeholder="优先级"></div>'
								+ '<div class="col-33"><a href="#" class="button delete">删除</a></div>'
								+ '</div>';
						//这里用append而不是 .html方法 来增加div中的html代码
						$('.category-wrap').append(tempHtml);
					});

	/*
	 * 当删除已经存储在数据库中的productCategory时，需要进行POST，调用service层的方法进行删除
	 * 这里的点击操作绑定到now的delete按钮下
	 * 
	 * on() 方法在被选元素及子元素上添加一个或多个事件处理程序。
	 * 语法：$(selector).on(event,childSelector,data,function)
	 * event	必需。规定要从被选元素移除的一个或多个事件或命名空间。由空格分隔多个事件值，也可以是数组。必须是有效的事件。
	 * childSelector	可选。规定只能添加到指定的子元素上的事件处理程序（且不是选择器本身，比如已废弃的 delegate() 方法）。
	 * data	可选。规定传递到函数的额外数据。
	 * function	可选。规定当事件发生时运行的函数。
	 * 
	 * $('#xxx') 查找页面上id=xxx的元素，并选中它
	 * $('.xxx') 查找页面上class=xxx的元素，并选中它
	 */
	$('.category-wrap').on('click', '.row-product-category.now .delete',
			function(e) {
				/*
				 * event.currentTarget 属性是在事件冒泡阶段内的当前 DOM 元素，通常等于 this。
				 * 
				 * 语法：event.currentTarget
				 * event	必需。event 参数来自事件绑定函数。
				 */
				var target = e.currentTarget;
				/*
				 * $.confirm
				 * if no buttons are specified, two buttons (Okay & cancel) will be added.
				 */
				$.confirm('确定么?', function() {
					$.ajax({
						url : deleteUrl,
						type : 'POST',
						data : {
							productCategoryId : target.dataset.id,
							shopId : shopId
						},
						dataType : 'json',
						success : function(data) {
							if (data.success) {
								$.toast('删除成功！');
								getList();
							} else {
								$.toast('删除失败！');
							}
						}
					});
				});
			});

	/*
	 * 对于在前台还没有提交给数据库的product Category，删除的时候直接删除就好了，无需进行数据库操作
	 * 这里的点击操作绑定到temp下面的delete按钮上
	 */
	$('.category-wrap').on('click', '.row-product-category.temp .delete',
			function(e) {
				console.log($(this).parent().parent());
				/*
				 * <div class="row row-product-category temp">
				 * 		<div class="col-33">
				 * 			<a href="#" class="button delete">删除</a>
				 * 		</div>
				 * </div>
				 * 
				 * 因此调用两次parent()找到class=xxx.temp的div，再使用remove()方法将<div class="row row-product-category temp">元素直接删除
				 */
				$(this).parent().parent().remove();

			});
});