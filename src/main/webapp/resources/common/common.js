/**
 * 
 */
function changeVerifyCode(img) {
	img.src = "../Kaptcha?" + Math.floor(Math.random() * 100);
}

// localhost:8080/o2o/shopadmin/shopoperation?shopId=2
// 这个程序能将上边的name=xxx中的值，即xxx取出来
function getQueryString(name) {
	/*
	 * 这个正则是寻找&+url参数名字=值+&，&可以不存在。
	 * (^|&)表示以'&'开头或直接以变量名开头
	 * =([^&]*)表示等号后有0个或多个'&',即我们可以将'shopId='这样的参数放在url中,也会被匹配出来
	 * (&|$)表明可以以'&'结尾,也可以直接结尾,即可以直接以'shopId=1'结尾,可以以'shopId=1&'结尾
	 */
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	//window.location 对象所包含的属性  search从问号 (?) 开始的 URL（查询部分）
	//substr():返回一个从指定位置开始的指定长度的子字符串,这里设置为1，是为了把url中的?号去掉
	var r = window.location.search.substr(1).match(reg);
	//match返回的r是什么样的仍不清楚
	if (r != null) {
		return decodeURIComponent(r[2]);
	}
	return '';
}