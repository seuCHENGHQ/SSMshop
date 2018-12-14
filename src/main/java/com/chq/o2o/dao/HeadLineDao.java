package com.chq.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.chq.o2o.entity.HeadLine;

public interface HeadLineDao {
	/**
	 * 根据查询条件，返回对应的头条轮播图对象
	 * @return
	 */
	List<HeadLine> queryHeadLine(
			@Param("headLineCondition") HeadLine headLineCondition);
}
