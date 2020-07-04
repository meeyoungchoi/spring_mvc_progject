package com.spring.mvc.board.controller;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.spring.mvc.board.model.BoardVO;
import com.spring.mvc.board.service.IBoardService;
import com.spring.mvc.commons.PageCreator;
import com.spring.mvc.commons.PageVO;
import com.spring.mvc.commons.SearchVO;

@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Inject
	private IBoardService service;
	
	/*
	//게시글 목록 불러오기 요청
	@GetMapping("/list")
	public String list(Model model) {
		List<BoardVO> list = service.getArticleList();
		
		System.out.println("URL: /board/list GET -> result: " + list.size());
		model.addAttribute("articles", list);
		
		return "board/list";
	}
	*/
	
	
	//페이징 처리 이후 게시글 목록 불러오기 요청.
	//한 화면에 띄울 게시물 개수도 같이 받겠다
	//처음실행했을때는 paging객체의 값이 0이 되어 sql문이 실행되지 않느다
	//그래서 paging생성자를 사용해서 기본값을 설정해놔야 한다
	//http://localhost/mvc/board/list?page=2&countPerPage=20
/*	@GetMapping("/list")
	public String list(PageVO paging, Model model) {
		List<BoardVO> list = service.getArticleListPaging(paging);
		
		System.out.println("URL: /board/list GET -> result: " + list.size());
		System.out.println("parameter(페이지 번호): " + paging.getPage());
		
		//pageCreator객체가 제대로 계산하고 있는지 확인
		PageCreator pc = new PageCreator();
		pc.setPaging(paging);//사용자가 보려고하는 페에징이 몇페이지 인지를 보여줘야 한다
		pc.setArticleTotalCount(service.countArticles());//총 게시물 수 를 구한다
		//System.out.println(pc);
		
		model.addAttribute("articles", list);
		model.addAttribute("pc", pc);
		
		
		return "board/list";
	}
	*/
	
	
	//검색 처리 이후 게시물 목록 불러오기 요청
	@GetMapping("/list")
	public String list(SearchVO search, Model model) {
		
		System.out.println("검색어: " + search.getKeyword());
		
		List<BoardVO> list = service.getArticleListByTitle(search);
		
		System.out.println("URL: /board/list GET -> result: " + list.size());
		System.out.println("parameter(페이지 번호): " + search.getPage());
		
		//pageCreator객체가 제대로 계산하고 있는지 확인
		PageCreator pc = new PageCreator();
		pc.setPaging(search);//사용자가 보려고하는 페에징이 몇페이지 인지를 보여줘야 한다
		pc.setArticleTotalCount(service.countArticlesByTitle(search));//검색결과에 따른 게시물 개수를 가져와야 한다
		//System.out.println(pc);
		
		model.addAttribute("articles", list);
		model.addAttribute("pc", pc);
		
		
		return "board/list";
	}
	
	
	
	
	
	
	//게시글 작성 페이지 요청
	@GetMapping("/write")
	public void write() {
		System.out.println("URL: /board/write -> GET");
	}
	
	//게시글 DB 등록 요청
	@PostMapping("/write")
	public String write(BoardVO article, RedirectAttributes ra) {
		System.out.println("URL: /board/write -> POST");
		System.out.println("Controller parameter: " + article);
		service.insert(article);
		ra.addFlashAttribute("msg", "regSuccess");
		return "redirect:/board/list";
	}
	
	
	//게시물 상세 조회 요청
	@GetMapping("/content/{boardNo}")
	public String content(@PathVariable int boardNo, Model model,
			@ModelAttribute("p") PageVO paging) {
		System.out.println("URL: /board/content -> GET");
		System.out.println("parameter(글 번호): " + boardNo);
		BoardVO vo = service.getArticle(boardNo);
		System.out.println("Result Data: " + vo);
		model.addAttribute("article", vo);
		
		
		return "board/content";
	}
	
	
	
	//게시물 삭제 처리 요청
	@PostMapping("/delete")
	public String remove(int boardNo, RedirectAttributes ra 
			, @ModelAttribute("p") PageVO paging) {
		System.out.println("URL: /board/delete -> POST");
		System.out.println("parameter(글 번호): " + boardNo);
		service.delete(boardNo);
		ra.addFlashAttribute("msg", "delSuccess");	
		//메서드 체이닝을 통해 한번에 넘겨줄수 있다
//		.addAttribute("page", paging.getPage());
//		.addAttribute("countPerPage", paging.getCountPerPage());
		//리다이렉트가 될때 파라미터 값을 그대로 넘겨줄수 있다
		ra.addAttribute("page", paging.getPage());
		ra.addAttribute("countPerPage", paging.getCountPerPage());
		
		
		return "redirect:/board/list";
	}
	
	
	//게시물 수정 페이지 요청
	@GetMapping("/modify")
	public String modify(int boardNo, Model model, @ModelAttribute("p") PageVO paging) {
		System.out.println("URL: /board/modify -> GET");
		System.out.println("parameter(글 번호): " + boardNo);
		
		BoardVO vo = service.getArticle(boardNo);
		System.out.println("Result Data: " + vo);
		model.addAttribute("article", vo);
		
		return "board/modify";
	}
	
	//게시물 수정 요청
	@PostMapping("/modify")
	public String modify(BoardVO article, RedirectAttributes ra) {
		System.out.println("URL: /board/modify -> POST");
		System.out.println("parameter(게시글): " + article);
		service.update(article);
		
		ra.addFlashAttribute("msg", "modSuccess");
		
		return "redirect:/board/content/" + article.getBoardNo();
	}
	
	
	
	

}




















