package com.spring.mvc.board.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spring.mvc.board.repository.IBoardMapper;
import com.spring.mvc.commons.PageVO;
import com.spring.mvc.commons.SearchVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/mvc-config.xml"})
public class PageAlgorithmTest {
	
	/*
	 * ***페이징 알고리즘 만들기***
	 * 
	 * #1. 사용자가 보게될 페이지 화면
	 * -한 화면에 페이지를 10개씩 끊어서 보여준다면? (버튼을 열개 배치하겠다)
	 * ex) 1 2 3 4 5 6 7 8 9 10 [다음] // [이전] 31 32 33 34 35 36 37 38 39 40 [다음] 
	 * 몇개의 버튼을 배치할지를 알려면 리스트에 띄우려는 총 개시물의 개수를 알아야 한다
	 * 
	 * -만약에 총 게시물의 갯구가 67개라면? 7번까지 띄우면 된다
	 * 1 2 3 4 5 6 7
	 * 
	 * 
	 * - 총 개수물 수가 142개이고 현재 12페이지에 사용자가 머물러 있다면?
	 * - [이전] 11 12 13 14 15 
	 * 
	 * 
	 * #개시물의 총개수를 알야아 버튼을 배치할수 있다
	 * 2.총 게시물의 수를 조회해야 한다
	 * -총 게시물 수는 DB로부터 수를 조회하는 sql문을 작성
	 * 
	 * #3.사용자가 현재 위치한 페이지를 기준으로 끝페이지 번호를 계산하는 로직 작성.
	 *예)11페이에 있을 때 맨 끝 페이지 번호: 20 
	 * -만약 현재 사용자가 보고 있는 페이지가 3페이지이고 한 화면에 보여줄 페이지가 10페이지씩이라면???
	 * ->끝페이지번호>> 10페이지
	 *- 만약 현재 페이지가 36페이지 이고 한 화면에 보여줄 페이지수가 20페이지 라면
	 *-> 끝페이지 번호: 40 (1~20) (21 ~ 40)
	 *==>공식화를 시키자
	 *
	 *                13              10                      10
	 * 공식: (현재 사용자가 위치한 페이지 번호 / 한 화면당 보여줄 페이지수) * 한 화면당 보여질 페이지 수 => 20
	 *    36 / 20 = 1.~~~~를 올림한다 => 2
	 *    
	 * 올리는법 :
	 *실수를 무조건 올려줄수 있다
	 * Math.ceil(현재 사용자가 위치한 페이지 번호 / 한 화면당 보여줄 페이지수) * 한 화면당 보여질 페이지 수)  
	 *    
	 *  
	 * #4. 시작페이지 번호 계산하기
	 * -현재 위치한 페이지15 한 화면에 보여줄 페이지가 10페이지씩이라면?
	 * ->시작 페이지 번호는 11번
	 * 
	 *-현재 위치한 페이지가 73페이지이고 한 화면에 20페이지씩 보여준다면
	 *-> 시작페이지번호:61
	 *    
	 *
	 * 공식: (끝페에지번호 - 한화면에보여줄 페이지수) + 1;
	 * 
	 * 
	 * #5. 끝페이지 보정
	 * -총 게시물 수가 324개이고 한 페이지당 10개의 게시물을 보여준다
	 * -그리고 이사람은 31페이지를 현재 보고 있다
	 * -그리고 한 화면에 게시물은 10개가 배치된다
	 * -그렇다면 위공식에 의한 끝 페이지는 몇 번으로 계산되는가> 40번
	 * 
	 * -하지만 실제 끝 페이지는 몇번에서 끝나는가> 33번
	 * 
	 * #5-1. 이전 버튼 활성 여부 설정
	 * 첫화면안에서 돌아다닐때만 해제
	 * 시작페이지가 1이면 비활성화 아니면 활성화
	 * -언제 이전버튼을 비활성화 할 것인가-> 시작페이지가 1인 부분에서 비활성
	 * 공식: 시작페이지 번호가 1로 구해진 시점에서 비활성 처리, 나머지는 활성
	 * 
	 * #5-2. 다음 버튼 활성 여부 설정
	 * -언제 다음 버튼을 비활성화 할 것인가?
	 * -끝번호보다 총 게시물수가 적을때 
	 * 공식: 보정 전 끝 페이지 번호 * 한 페이지에 들어갈 게시물 수 >= 총 게시물수 -> 비활성
	 * 324게의 글
	 * 마지막 버튼 33
	 * 지금 31페이지
	 * 보정전 끝번호 40
	 * 한페이지당 10개의 버튼 배치
	 * 
	 * 예) 총 250글
	 * 버튼 10개
	 * 한화면 개시물수 10
	 * 지금 22번
	 * 
	 * 보정전: 30(끝번호)
	 * 
	 * 
	 * #5-3.끝페이지 값 보정
	 * -다음 버튼이 비활성화 되었을때 총 게시물 수에 맟줘 끝 페이지 번호를 재보정한다.
	 * 공식: Math.ceil(총 게시물 수 / 한 페이지에 보여줄 게시물 수)
	 * 355 / 10 = 35.5 => 35
	 * 다음페이지 버튼이 비활성화 된경우만 끝페이지를 보정한다
	 *    
	 *    
	 * */
	@Autowired
	private IBoardMapper mapper;
	
	@Test
	public void pagingAlgorithmTest() {
		//총 게시물 구하는 수 테스트
		System.out.println("===================================");
		System.out.println("총 게시물수 : " + mapper.countArticles());
		System.out.println("-----------------------------------");
		
		
		
		//끝 페이지 번호 계산 테스트
		PageVO paging = new PageVO();
		paging.setPage(26);
		int displayPage = 10;//한 화면당 보여줄 페이지번호 개수
		//마지막 페이지번호는 40
//		int endPage = (int) (Math.ceil(paging.getPage() / displayPage) * displayPage);
		int endPage = (int) Math.ceil(paging.getPage() / (double) displayPage) * displayPage;//실수를 줘야 올림을 할수 있다 리턴타입이 double이기때문에 int로 형변환 해줘야 한다
		System.out.println("끝 페이지번호 " + endPage + "번");//80
		
		
		//시작페이지 번호 계산 테스트
		int beginPage = (endPage - displayPage) + 1;
		System.out.println("시작 페이지번호: " + beginPage +"번");
		
		
		//이전버튼 활성, 비활성 여부
		//시작페이지가 1인 여부에 따라
		boolean isPrev = (beginPage == 1) ? false : true;
		System.out.println("이전 버튼 활성화 여부: " + isPrev);
		
		
		//다음 버튼 활성, 비활성 여부
		//총게시물수:321 보정전 11 끝번호:20  한페이지당 10를 보여준다
		boolean isNext = (mapper.countArticles() <= (endPage * paging.getCountPerPage())) ? false : true;
		System.out.println("다음 버튼 비활성 여부: " + isNext);
		
		
		//끝 페이지 보정.
		if (!isNext) {
			endPage = (int) Math.ceil(mapper.countArticles() / (double)paging.getCountPerPage());
	
		}
		
		System.out.println("보정후 끝페이지 번회 " + endPage + "번");
		System.out.println("=================================================");
		
		
	}
	
	
	@Test
	public void searchTest() {
		
		SearchVO search = new SearchVO();
		search.setPage(4);
		search.setKeyword("9");
		
		System.out.println("==================================");
		mapper.getArticleListByTitle(search)
		.forEach(vo -> System.out.println(vo));
		System.out.println("==================================");
		
		
	}
	
	
	

}
