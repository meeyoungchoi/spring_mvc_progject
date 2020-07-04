package com.spring.mvc.commons;

public class SearchVO extends PageVO {

	//검색 + 페이징까지 처리하기위해 PageVO클래스를 상속받았다
	
	private String keyword;
	private String condition;//어떤 부류의 검색이지
	
	//검색을 안하고싶을떄를 대비
	public SearchVO() {
		this.keyword = "";
		this.condition = "";
	}
	
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@Override
	public String toString() {
		return "SearchVO [keyword=" + keyword + ", condition=" + condition + "]";
	}
	
	
	
	

}
