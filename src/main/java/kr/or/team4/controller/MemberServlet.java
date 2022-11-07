package kr.or.team4.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.or.team4.action.Action;
import kr.or.team4.action.ActionForward;
import kr.or.team4.dao.MemberDao;
import kr.or.team4.dto.MemberDto;
import kr.or.team4.service.alllist;
import kr.or.team4.service.registerOk;

@WebServlet("*.do")
public class MemberServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;
       
    public MemberServlet() {
        super();
       
    }
   private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      //1. 한글 처리
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            
            //2. 데이터 받기 (입력데이터 , command)
            String requestURI = request.getRequestURI();
            String contextPath = request.getContextPath();
            String urlcommand = requestURI.substring(contextPath.length());
            
            /*
             *    주소가 http://192.168.0.22:8090/WebServlet/register.do 라면
             * requestURI       : /WebServlet/register.do
             * contextPath    : WebServlet
             * urlcommand   : /register.do
             * 
             * URL 마지막 주소를 추출하고 판단의 근거로 삼기
             */
            
            String viewpage = "";
            Action action = null;
            ActionForward forward = null;
            
            HttpSession session = request.getSession();
            PrintWriter out = response.getWriter();
            //3. 요청 서비스 판단 (command 값을 비교해서)
            //4. 데이터 저장 
            if(urlcommand.equals("/login.do")) {
               //로그인
               viewpage="/WEB-INF/views/login.jsp";
               
               //session에 id 설정
            }else if(urlcommand.equals("/loginok.do")) {
               // 로그인 여부 확인
               String id = request.getParameter("id");
               String pwd = request.getParameter("pwd");
               
               MemberDao dao = new MemberDao();
               System.out.println(1);
               
               if(dao.getMemberDtoListById(id) != null) {
                  if(dao.getMemberDtoListById(id).getPwd().equals(pwd)) {
                      System.out.println(22);
                     session.setAttribute("id", id);
                     viewpage="/WEB-INF/views/main.jsp";
                  }else {
                      System.out.println(11);
                     viewpage="/WEB-INF/views/login.jsp";
                  }
               }else {
                  viewpage="/WEB-INF/views/register.jsp";
               }
            }else if (urlcommand.equals("/logout.do")) {
            	request.getSession().invalidate();
            	out.print("<script>alert('로그아웃');</script>");
                viewpage="/WEB-INF/views/main.jsp";
            }
            else if(urlcommand.equals("/register.do")) {
               // 회원가입
               viewpage="/WEB-INF/views/register.jsp";
            } else if(urlcommand.equals("/registerok.do")) {
               // 회원가입
               action = new registerOk();
               forward = action.execute(request, response);
            } else if(urlcommand.equals("/alllist.do")) {
               // 전체조회
            	action = new alllist();
            	forward=action.execute(request, response);
               // request.setAttribute("list",여기에값)
            } else if (urlcommand.equals("/main.do")) {
                viewpage = "/WEB-INF/views/main.jsp";
             } 
            else if(urlcommand.equals("/search.do")) {
               // like조회
               MemberDao dao = new MemberDao();
               
               request.setAttribute("list", dao.getMemberDtoByLikeEmail(request.getParameter("search")));
               
               viewpage = "/WEB-INF/views/list.jsp";
               // request.setAttribute("list",여기에값)
            } else if(urlcommand.equals("/update.do")) {
               // 수정
            	MemberDao dao = new MemberDao();
            	
            	request.setAttribute("member", dao.getMemberDtoListById(request.getParameter("id")));
            	
               viewpage =  "/WEB-INF/views/edit.jsp";
               //수정 후 전체조회로
            } else if(urlcommand.equals("/updateok.do")) {
               // 수정
               MemberDao dao = new MemberDao();
                              
               MemberDto dto = new MemberDto();
               
               dto.setId(request.getParameter("id"));
               dto.setName(request.getParameter("name"));
               dto.setAge(Integer.parseInt(request.getParameter("age")));
               dto.setGender(request.getParameter("gender"));
               dto.setEmail(request.getParameter("email"));
               
               int row = 0;
               
               row = dao.updateMemberDto(dto);
               if(row>0) {
                  out.print("<script>alert('수정됨')</script>");
               }else {
                  out.print("<script>alert('실패')</script>");
               }
               
               viewpage = "/alllist.do";
               //수정 후 전체조회로
            } else if(urlcommand.equals("/delete.do")) {
               // 삭제
               MemberDao dao = new MemberDao();
               
               
               dao.deleteMemberDto(request.getParameter("id"));
               //삭제 후 전체조회로
               viewpage = "/alllist.do";
            } else if(urlcommand.equals("/detail.do")){

            } else if (urlcommand.equals("/test.do")){
            	// like조회
                MemberDao dao = new MemberDao();
                
                request.setAttribute("list", dao.getMemberDtoByLikeEmail(request.getParameter("search")));
                
                viewpage = "/WEB-INF/test/test.jsp";
                // request.setAttribute("list",여기에값)
            }
            // ... else if 반복
            //5. View 지정
            RequestDispatcher dis = request.getRequestDispatcher(viewpage);
            
            dis.forward(request, response);
            //forward
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doProcess(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doProcess(request, response);
   }

}
