package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fenye.Fenye;
import service.CardService;

public abstract class control {
	
	private CardService cardservice=new CardService();

	public String toUsercenter(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		String username= (String) session.getAttribute("username");
		System.out.println(username);
		String currentPage = req.getParameter("currentPage");
		currentPage=currentPage ==null ? "1" : currentPage;//注意这里的分页后台实现
		Fenye list=cardservice.list(username, Integer.parseInt(currentPage));
		req.setAttribute("fenye", list);
		req.setAttribute("currentPage", Integer.parseInt(currentPage));
		req.setAttribute("username", username);
		return "usercenter";
	}

}
