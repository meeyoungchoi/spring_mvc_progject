<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<jsp:include page="../include/header.jsp" />
<style>
header.masthead {
	
	display: none;
}	
</style>
<br/><br/>
<div class="container">

<div class="row">
  <div class="col-lg-12">
    <div class="card">
      <div class="card-header text-white" style="background-color: #643691;">${article.boardNo}번 게시물 내용</div>
      <div class="card-body">

        
        
          <div class="form-group">
            <label>작성자</label>
            <input type="text" class="form-control" name='writer' value="${article.writer}" readonly>
          </div>
          
          <div class="form-group">
            <label>제목</label>
            <input type="text" class="form-control" name='title' value="${article.title}" readonly>
          </div>

          <div class="form-group">
            <label>내용</label>
            <textarea class="form-control" rows="5" name='content' readonly>${article.content}</textarea>
          </div>

         <form id="formObj" role="form" action="<c:url value='/board/delete' />" method="post">
         
         	  <input type="hidden" name="boardNo" value="${article.boardNo}">
         
	          <input id="list-btn" class="btn" type="button" value="목록" 
			style="background-color: #643691; margin-top: 0; height: 40px; color: white; border: 0px solid #388E3C; opacity: 0.8">&nbsp;&nbsp;
	          
	          <input id="modBtn" type="button" value="수정" class="btn btn-warning" 
			style="background-color: orange; margin-top: 0; height: 40px; color: white; border: 0px solid #388E3C; opacity: 0.8">&nbsp;&nbsp;
	          
	          <input  type="submit" value="삭제" class="btn btn-danger" onclick="return confirm('정말로 삭제하시겠습니까?')"
			style="background-color: red; margin-top: 0; height: 40px; color: white; border: 0px solid #388E3C; opacity: 0.8">&nbsp;&nbsp;
       
        </form>



      </div>
    </div>
  </div>
</div>
</div>


<script>
	
	//제이쿼리의 시작
	$(function() {
		
		//변수는 let, 상수는 const로 선언 (ES6 -> 2015)
		const formElement = $("#formObj");
		
		
		//목록 버튼 클릭 이벤트 처리
		//페이지 정보와 게시물의 개수까지 넘어간다
		${"#list-btn"}click(function() {
			console.log("목록버튼이 클릭되었습니다");
			location.href="/board/list?page=${p.paging}"
			+"&countPerPage=${p.countPerPage}";
		});
		
		
		
		//수정 버튼 클릭 이벤트
		//var modifyBtn = document.getElementById("modBtn") //vanilla js
		var modifyBtn = $("#modBtn"); //jQuery
		modifyBtn.click(function() {
			console.log("수정 버튼이 클릭됨!");
			formElement.attr("action", "/board/modify");
			formElement.attr("method", "get");
			formElement.submit();
		});
	});
	
</script>






<jsp:include page="../include/footer.jsp" />











