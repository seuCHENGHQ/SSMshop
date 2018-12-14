package com.chq.o2o.web.superadmin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chq.o2o.entity.Area;
import com.chq.o2o.service.AreaService;

@Controller
@RequestMapping("/superadmin")
public class AreaController {
	Logger logger = LoggerFactory.getLogger(AreaController.class);
	@Autowired
	private AreaService areaService;

	/*
	 * @responseBody注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，
	 * 写入到response对象的body区，通常用来返回JSON数据或者是XML
	 * 数据，需要注意的呢，在使用此注解之后不会再走视图处理器，而是直接将数据写入到输入流中，他的效果等同于通过response对象输出指定格式的数据。
	 */
	@RequestMapping(value = "/listarea", method = RequestMethod.GET)
	@ResponseBody
	// 这里之所以把返回值设定为Map，是因为控制器返回的ModelAndView底层是使用Map来实现的
	private Map<String, Object> listArea() {
		logger.info("===start===");
		long startTime = System.currentTimeMillis();
		Map<String, Object> modelAndView = new HashMap<>();
		List<Area> list = new ArrayList<>();
		try {
			list = areaService.getAreaList();
			// 这里之所以用rows和total，是因为之后我们会使用一个分页插件，这是其中的两个元素的名字
			modelAndView.put("rows", list);
			modelAndView.put("total", list.size());
		} catch (Exception e) {
			// 这里会发生Java中的自动装箱现象
			modelAndView.put("success", false);
			modelAndView.put("errMsg", e.toString());
		}
		logger.error("error test");
		long endTime = System.currentTimeMillis();
		// {}表示占位符，因为debug涉及到程序的调优，因此我们需要看到程序的执行时间，来对程序做针对性的优化
		logger.debug("costTime:[{}ms]", endTime - startTime);
		logger.info("===end===");
		return modelAndView;
	}
}
