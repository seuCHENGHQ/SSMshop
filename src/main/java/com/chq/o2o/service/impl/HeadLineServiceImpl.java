package com.chq.o2o.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chq.o2o.dao.HeadLineDao;
import com.chq.o2o.entity.HeadLine;
import com.chq.o2o.service.HeadLineService;

@Service
public class HeadLineServiceImpl implements HeadLineService {
	
	@Autowired
	private HeadLineDao headLineDao;

	@Override
	//虽然这里Service层只是调用了Dao层已经实现的方法，好像Service层这里没什么别的作用，其实，在加入缓存之后，它能够让Controller层和Dao层解耦，无需关心Service层的变化
	public List<HeadLine> getHeadLineList(HeadLine headLineCondition) throws IOException {
		// TODO Auto-generated method stub
		return headLineDao.queryHeadLine(headLineCondition);
	}

}
