/**
 * 其实js看起来就像一个脚本，当访问到该页面时，加载该脚本并获取一些信息
 */

/*
 * 当加载这个js文件的时候，就运行这个function程序 $(function(){…}); 也可以写作 jQuery(function($) {…}); ，
 * 其实就是$(document).ready(funcrtion{...}); 的简写。
 */
$(function() {
	var shopId = getQueryString('shopId');
	//标识符，如果访问shopadmin/shopoperation时不带shopId参数，说明是要注册店铺，带了shopId参数，说明是要修改店铺信息
	//这里三目运算符设计到了js中字符串变量转换为boolean变量的操作
	var isEdit = shopId?true:false;
	// 获取店铺的初始信息
	var initUrl = '/o2o/shopadmin/getshopinitinfo';
	// 注册店铺
	var registerShopUrl = '/o2o/shopadmin/registershop';
	var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId='+shopId;
	var editShopUrl = '/o2o/shopadmin/modifyshop';
	
	if(!isEdit){
		getShopInitInfo();
	}else{
		getShopInfo(shopId);
	}
	
	//获取店铺的类别和区域等信息，并填充到网页中，该方法于下边定义
	//当修改店铺信息的时候，要先使用该函数去获取已经存在数据库内的店铺信息
	function getShopInfo(shopId){
		$.getJSON(shopInfoUrl,function(data){
			//这里的data时controller层返回的modelMap对象，其实就是modelAndMap对象
			if(data.success){
				var shop = data.shop;
				$('#shop-name').val(shop.shopName);
				$('#shop-addr').val(shop.shopAddr);
				$('#shop-phone').val(shop.phone);
				$('#shop-desc').val(shop.shopDesc);
				var shopCategory = '<option data-id="'
					+ shop.shopCategory.shopCategoryId + '" selected>'
					+ shop.shopCategory.shopCategoryName + '</option>';
				var tempAreaHtml = '';
				data.areaList.map(function(item,index){
					tempAreaHtml += '<option data-id="' + item.areaId +'">'
						+ item.areaName + '</option>';
				});
				$('#shop-category').html(shopCategory);
				//在修改店铺信息时，我们不允许修改店铺的类别，因此修改option控件的disabled属性，将该属性设置为disabled和false均可达到同样效果
				$('#shop-category').attr('disabled','disabled');
				$('#shop-area').html(tempAreaHtml);
				//这里让shop-area下拉列表默认显示数据库中注册的area
				$('#shop-area option[data-id="' + shop.area.areaId + '"]').attr('selected','selected');
			}
		});
	}
	function getShopInitInfo() {
		/*
		 * 这个是ajax操作函数
		 * 通过 HTTP GET 请求载入 JSON 数据。 语法
		 * jQuery.getJSON(url,data,success(data,status,xhr)) 
		 * url: 必需。规定将请求发送的哪个URL。 
		 * data: 可选。规定连同请求发送到服务器的数据。 
		 * success(data,status,xhr): 可选。规定当请求成功时运行的函数。 额外的参数：response:包含来自请求的结果数据; status:包含请求的状态; xhr:包含 XMLHttpRequest 对象
		 * 
		 * 此处的function(data)是一个回调函数，当从initUrl取回json数据之后，执行该回调函数
		 * 其中的data是程序从initUrl中取回的json对象
		 */
		$.getJSON(initUrl, function(data) {
			var tempHtml = '';
			var tempAreaHtml = '';
			// 生成店铺区域信息和店铺种类信息的下拉列表中的标签项
			/*
			 * 这里data.shopCategoryList是JavaScript中的array对象
			 * array对象的map方法的语法: array.map(function(currentValue,index,arr), thisValue)
			 * currentValue:必须。当前元素的值       index:可选。当期元素的索引值      arr:可选。当期元素属于的数组对象
			 * thisValue: 可选。对象作为该执行回调时使用，传递给函数，用作 "this" 的值。   如果省略了 thisValue ，"this" 的值为 "undefined"
			 * 
			 * 这里的item其实是ShopCategory的JSON对象
			 * 正是因为data中的数据已经被解析为了JSON对象，因此才能通过item.shopCategoryId的方式访问到其id，而在Java对象中，shopCategoryId是私有的，需要通过get方法才能访问到
			 */
			data.shopCategoryList.map(function(item, index) {
				tempHtml += '<option data-id="' + item.shopCategoryId + '">'
						+ item.shopCategoryName + '</option>';
			});
			/*
			 * HTML的option标签中的 data-* 属性用于存储页面或应用程序的私有自定义数据。
			 * data-* 属性包括两部分：(1)属性名不应该包含任何大写字母，并且在前缀 "data-" 之后必须有至少一个字符 (2)属性值可以是任意字符串
			 * 这表示用户选中该选项后，实际发送给后端服务器的值
			 */
			data.areaList.map(function(item, index) {
				tempAreaHtml += '<option data-id="' + item.areaId + '">'
						+ item.areaName + '</option>';
			});
			// $("#lastname") 选取id="lastname" 的元素 这里使用的是jQuery选择器
			// 语法 $(selector).html()
			// html() 方法返回或设置被选元素的内容 (inner HTML)。如果该方法未设置参数，则返回被选元素的当前内容。
			// 在这里就是设置id="shop-category"的标签的内容为tempHtml，area同理
			$('#shop-category').html(tempHtml);
			$('#shop-area').html(tempAreaHtml);
		});
	}
	$('#submit').click(function(){
		//这里定义一个json对象
		var shop = {};
		if(isEdit){
			shop.shopId = shopId;
		}
		//这里是从前端的表单中获取对应的变量值，并将其传入json对象中，准备发往后台服务器
		shop.shopName = $('#shop-name').val();
		shop.shopAddr = $('#shop-addr').val();
		shop.phone = $('#shop-phone').val();
		shop.shopDesc = $('#shop-desc').val();
		shop.shopCategory = {
				//先找到id=shop-category的标签，之后搜索其中的option元素，然后选中了表单中被选中的那个option元素，最后将这个option标签的值赋予名字'id'
				//find函数语法: .find(selector)  selector:字符串值，包含供匹配当前元素集合的选择器表达式。
				//not函数语法: .not(function(index)) function(index)	用于检测集合中每个元素的函数。this 是当前 DOM 元素。
				//data函数语法: $(selector).data(name)   name:可选。规定要取回的数据的名称。如果没有规定名称，则该方法将以对象的形式从元素中返回所有存储的数据。
				//因为data-*中存储的是我们自定义的字符串数据，因此data('id')应该是取出该标签中data-id中存储的数据
				shopCategoryId : $('#shop-category').find('option').not(function(){
					//函数返回true，则移除当前元素，返回false则保留当前元素
					return !this.selected;
				}).data('id')
		};
		shop.area = {
				areaId : $('#shop-area').find('option').not(function(){
					return !this.selected;
				}).data('id')
		};
		// 如果这里提交多个文件，就可以用.files[1]等将对应的文件取出来
		// 为什么$('#shop-img')[0]中，索引值是0呢？ 因为$('#shop-img')是一个jquery对象，[0]为Dom元素 可以用dom的所有属性和方法。然后使用files来取出文件。
		var shopImg = $('#shop-img')[0].files[0];
		// 如果我们需要上传文件，使用ajax进行提交的时候就需要formdata对象的帮助
		var formData = new FormData();
		formData.append('shopImg',shopImg);
		formData.append('shopStr',JSON.stringify(shop));
		
		//在提交之前，先要校验一下验证码是否正确
		var varifyCodeActual = $('#j_captcha').val();
		/*
		 * !varifyCodeActual: varifyCodeActual是一个字符串，加!就是将其先转换为布尔型的变量，再取非
		 * 如果变量是undefined,null,空字符串,0，转换为布尔值就是false
		 * 因此这里如果没有输入验证码，!varifyCodeActual就是true
		 */
		if(!varifyCodeActual){
			$.toast('请输入验证码!');
			return;
		}
		formData.append('varifyCodeActual',varifyCodeActual);
		
		//这里进行异步提交
		$.ajax({
			url:isEdit?editShopUrl:registerShopUrl,
			type:'POST',
			data:formData,
			//因为这里除了json，还要提交文件，因此contentType为false
			contentType:false,
			processData:false,
			cache:false,
			//提交完成后，要执行的回调函数
			success:function(data){
				//这里的data是服务器端传回来的ShopExecution对象，里面存放了success和errMsg两个键值对，用于判断店铺注册是否成功，以及失败的原因
				if(data.success){
					$.toast('提交成功 !');
				}else{
					$.toast('提交失败!'+data.errMsg);
				}
			}
		});
	})
})