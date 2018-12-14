$(function() {
	var productId = getQueryString('productId');
	// var shopId = 1;
	var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
	var categoryUrl = '/o2o/shopadmin/getproductcategorylist';
	var productPostUrl = '/o2o/shopadmin/modifyproduct';
	// 标识符，用于标记本次操作是新增还是修改
	var isEdit = false;
	// 如果能从url中获取到productId，则说明本次操作是修改操作，否则为新增操作
	if (productId) {
		getInfo(productId);
		isEdit = true;
	} else {
		// 如果是添加，则需要获取店铺的所有商品类别，并添加到html页面中
		getCategory();
		productPostUrl = '/o2o/shopadmin/addproduct';
	}

	// 获取需要编辑的商品的商品信息，并赋值给表单
	function getInfo(id) {
		$
				.getJSON(
						infoUrl,
						function(data) {
							if (data.success) {
								// 从返回的JSON数据中获取商品信息，并填入对应的HTML控件中
								var product = data.product;
								$('#product-name').val(product.productName);
								$('#product-desc').val(product.productDesc);
								$('#priority').val(product.priority);
								$('#normal-price').val(product.normalPrice);
								$('#promotion-price').val(
										product.promotionPrice);
								// 获取原本的商品类别以及该店铺的所有商品类别列表
								var optionHtml = '';
								var optionArr = data.productCategoryList;
								// 默认选中数据库中存储的该商品所属的商品类别
								var optionSelected = product.productCategory.productCategoryId;
								optionArr
										.map(function(item, index) {
											/*
											 * 1、对于string,number等基础类型，==和===是有区别的
											 * 1）不同类型间比较，==之比较“转化成同一类型后的值”看“值”是否相等，===如果类型不同，其结果就是不等
											 * 2）同类型比较，直接进行“值”比较，两者结果一样
											 * 
											 * 2、对于Array,Object等高级类型，==和===是没有区别的
											 * 进行“指针地址”比较
											 * 
											 * 3、基础类型与高级类型，==和===是有区别的
											 * 1）对于==，将高级转化为基础类型，进行“值”比较
											 * 2）因为类型不同，===结果为false
											 * 
											 * 在这里涉及到String转换为Number，然后再进行比较两个数值是否相等
											 * 然后就是三目运算符的操作了
											 */
											var isSelect = optionSelected === item.productCategoryId ? 'selected'
													: '';
											optionHtml += '<option data-value="'
													+ item.productCategoryId
													+ '"'
													+ isSelect
													+ '>'
													+ item.productCategoryName
													+ '</option>';
										});
								$('#category').html(optionHtml);
							}
						});
	}

	// 获取某店铺下的所有商品类别信息
	// 和上边的getInfo相比，只是将所有的商品类别信息查询回来，无需设置默认选中的选项
	function getCategory() {
		$.getJSON(categoryUrl, function(data) {
			if (data.success) {
				var productCategoryList = data.productCategoryList;
				var optionHtml = '';
				productCategoryList.map(function(item, index) {
					optionHtml += '<option data-value="'
							+ item.productCategoryId + '">'
							+ item.productCategoryName + '</option>';
				});
				$('#category').html(optionHtml);
			}
		});
	}

	// 将change事件绑定到详情图的最后一个div上(.detail-img:last-child)
	// 如果最后一个详情图输入控件的值发生改变，就执行这里的function函数，即追加一个详情图上传控件
	/*
	 * 语法：$(selector).on(event,childSelector,data,function)
	 * 
	 * event 必需。规定要从被选元素移除的一个或多个事件或命名空间。由空格分隔多个事件值，也可以是数组。必须是有效的事件。
	 * childSelector 可选。规定只能添加到指定的子元素上的事件处理程序（且不是选择器本身，比如已废弃的 delegate() 方法）。
	 * data 可选。规定传递到函数的额外数据。 
	 * function 可选。规定当事件发生时运行的函数。
	 */
	$('.detail-img-div').on('change', '.detail-img:last-child', function() {
		// 最多只能提交6个商品详情图
		if ($('.detail-img').length < 6) {
			// 如果商品详情图少于6张，每次添加完一张图片后，就再增加一个上传图片的按钮
			$('#detail-img').append('<input type="file" class="detail-img">');
		}
	});

	$('#submit').click(
			function() {
				var product = {};
				product.productName = $('#product-name').val();
				product.productDesc = $('#product-desc').val();
				product.priority = $('#priority').val();
				product.normalPrice = $('#normal-price').val();
				product.promotionPrice = $('#promotion-price').val();
				product.productCategory = {
					productCategoryId : $('#category').find('option').not(
							function() {
								return !this.selected;
							}).data('value')
				};
				product.productId = productId;

				var thumbnail = $('#small-img')[0].files[0];
				//在浏览器的console上输出thumbnail
				console.log(thumbnail);
				
				//把表单信息封装在formdata中
				/*
				 * FormData对象用以将数据编译成键值对，以便用XMLHttpRequest来发送数据。
				 * 你可以自己创建一个FormData对象，然后调用它的append()方法来添加字段
				 */
				var formData = new FormData();
				formData.append('thumbnail', thumbnail);
				$('.detail-img').map(
						function(index, item) {
							//因为当上传图片不足6个时，会有一个空的上传控件，因此要验证文件的大小要大于0
							if ($('.detail-img')[index].files.length > 0) {
								//给每个上传的商品详情图定义一个不同的键，index从0开始
								formData.append('productImg' + index,
										$('.detail-img')[index].files[0]);
							}
						});
				//把product的JSON对象转换为字符串，再添加到formdata中，一起传输给后台
				formData.append('productStr', JSON.stringify(product));
				var verifyCodeActual = $('#j_captcha').val();
				//这里验证码不能为空，为空直接返回
				if (!verifyCodeActual) {
					$.toast('请输入验证码！');
					return;
				}
				formData.append("verifyCodeActual", verifyCodeActual);
				$.ajax({
					url : productPostUrl,
					type : 'POST',
					data : formData,
					contentType : false,
					processData : false,
					cache : false,
					success : function(data) {
						if (data.success) {
							$.toast('提交成功！');
							//提交成功，验证码要刷新一下
							$('#captcha_img').click();
						} else {
							$.toast('提交失败！');
							//提交失败，验证码也要刷新一下
							$('#captcha_img').click();
						}
					}
				});
			});

});