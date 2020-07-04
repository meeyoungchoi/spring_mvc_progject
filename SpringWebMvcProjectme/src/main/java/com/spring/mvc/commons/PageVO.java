package com.spring.mvc.commons;

public class PageVO {
	
	private int page; //사용자가 요청한 페이지 번호 
	private int countPerPage;//한 페이지당 들어갈 게시물의 수 
	
	//처음 리스트요청을 통해들어왔을떄 글을 확인하기 위해 기본값을 설정해 놓겠다
	public PageVO() {
		this.page = 1;
		this.countPerPage = 10;
	}
	
	
	//클라이언트가 전달한 페이지 번호를 데이터베이스의 LIMIT절에 맞는 숫자로 변환해주는 메서드
	//언제 호출할 것인가? 서비스에서 부르지 않겠다
	public int getPageStart() {
		return (this.page - 1) * this.countPerPage;
		//      2 * 20 = 40 LIMIT 40 20
	}
	
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		//url로 이상한 값이 넘오는 것을 방지
		if (page <= 0 ) {
			this.page = 1;
			return;
		}
		
		
		this.page = page;
	}
	public int getCountPerPage() {
		return countPerPage;
	}
	public void setCountPerPage(int countPerPage) {
		//url로 이상한 값이 넘오는 것을 방지
		if (countPerPage <= 0 && countPerPage > 50) {
			this.countPerPage = 10;
			return;
		}
		
		this.countPerPage = countPerPage;
	}
	
	@Override
	public String toString() {
		return "PageVO [page=" + page + ", countPerPage=" + countPerPage + "]";
	}
	
	
	
	

}
