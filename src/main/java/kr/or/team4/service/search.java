package kr.or.team4.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.team4.action.Action;
import kr.or.team4.action.ActionForward;
import kr.or.team4.dao.MemberDao;

public class search implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {

		ActionForward forward = new ActionForward();
		
        MemberDao dao = new MemberDao();
        
        request.setAttribute("list", dao.getMemberDtoByLikeId(request.getParameter("search")));
        
        forward.setPath("/WEB-INF/createview/memberlisttable.jsp");
        forward.setRedirect(false);
        
		return forward;
	}

}
