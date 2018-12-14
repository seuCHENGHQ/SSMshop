/**
 * 
 */

$(function() {
	/*
	 * 当我们直接通过url的形式访问shopmanagement.html时，不带上shopId参数，这里的shopId为null
	 */
	var shopId = getQueryString('shopId');
	/*
	 * 因此，在访问对应的控制器时，控制器程序会判断用户是否有权限进行店铺修改操作
	 * 如果权限，则进行重定向跳转
	 * 有权限就可以停留在该页面上，并进行各种信息的修改操作
	 */
	var shopInfoUrl = '/o2o/shopadmin/getshopmanagementinfo?shopId=' + shopId;
	$.getJSON(shopInfoUrl,function(data){
		if(data.redirect){
			/*
			 * self.location.href="/url" 当前页面打开URL页面
			 * location.href="/url" 当前页面打开URL页面
			 * windows.location.href="/url" 当前页面打开URL页面，前面三个用法相同。
			 * this.location.href="/url" 当前页面打开URL页面
			 * parent.location.href="/url" 在父页面打开新页面
			 * top.location.href="/url" 在顶层页面打开新页面
			 */
			window.location.href = data.url;
		}else{
			if(data.shopId!=undefined && data.shopId!=null){
				shopId = data.shopId;
			}
			$('#shopInfo').attr('href','/o2o/shopadmin/shopoperation?shopId=' + shopId);
		}
	})
})