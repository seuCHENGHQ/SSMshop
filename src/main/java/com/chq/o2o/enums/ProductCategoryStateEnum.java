package com.chq.o2o.enums;

public enum ProductCategoryStateEnum {

	SUCCESS("创建成功",1),NULL_CURRENT_SHOP("currentShop为空", -1),INNER_ERROR("操作失败",-1001),EMPTY_LIST("添加失败,list为空",-1002);

	private String state;
	private int stateCode;

	private ProductCategoryStateEnum(String state, int stateCode) {
		this.state = state;
		this.stateCode = stateCode;
	}

	public String getState() {
		return state;
	}

	public int getStateCode() {
		return stateCode;
	}
	
	//根据错误码查询对应的错误信息
	public static String stateOf(int index){
		for(ProductCategoryStateEnum item : values()){
			if(item.getStateCode()==index){
				return item.getState();
			}
		}
		return null;
	}
}
