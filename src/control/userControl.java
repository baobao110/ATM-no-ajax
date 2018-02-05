package control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import AccountFlow.Account;
import BankCard.Card;
import annotation.Annotation;
import fenye.Fenye;
import service.Service;
import service.UserService;
import user.User;
@Annotation("user")
public class userControl extends control {
	

	private Service service=new Service();
	
	private UserService userservice=new UserService();
	
	public String load(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// Set factory constraints
		factory.setSizeThreshold(10240);
		
		String src = req.getServletContext().getRealPath("/");
		
		factory.setRepository(new File(src + "WEB-INF/load-tmp"));

		// Create a new file upload handler
		ServletFileUpload load = new ServletFileUpload(factory);

		// Set overall request size constraint
		load.setSizeMax(1024 * 500);
		// Parse the request
		try {
			List<FileItem> items = load.parseRequest(req);
			Iterator<FileItem> iter = items.iterator();
			while (iter.hasNext()) {
			    FileItem item = iter.next();

			    if (item.isFormField()) {
			    	 String name = item.getFieldName();
			    	 String value = item.getString();
			    } else {
			    	 HttpSession session = req.getSession();
			    	 User user = (User)session.getAttribute("user");
			    	 String fileName = "" + user.getUsername();
			    	 File loadedFile = new File(src + "WEB-INF/load/" +  fileName);
			    	 item.write(loadedFile);
			    }
			}
			return toUsercenter(req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/*
	 * 文件的上传可以模仿Apache的fileupload
	 */
	
	public void showPicture(HttpServletRequest req, HttpServletResponse resp) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		String src=req.getServletContext().getRealPath("/");
		HttpSession session=req.getSession();//这里的一个知识点就是session和request的区别一个是全局变量一个是局部变量
		User user=(User)session.getAttribute("user");
		try (FileInputStream in = new FileInputStream(src+ "WEB-INF/load/" + user.getUsername());
				OutputStream out = resp.getOutputStream()) {
			byte[] data = new byte[1024];
			int length = -1;
			while((length=in.read(data))!=-1) {
				out.write(data, 0, length);
				out.flush();
			}
		}
	}
	
	public String  toRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		return "register";
	
	}
	
	public String Register(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();//如果有进行赋值就是原来的Session，如果没有创建新的Session
		String username= req.getParameter("username");
		String psssword= req.getParameter("password");
		User user= userservice.register(username,psssword);
		if(user==null) {
			return "register";
		}
		else {
			session.setAttribute("user",user);
			return "login";
		}
	}
	
	public String toLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		return "login";																																								
	}

	public String  login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		javax.servlet.http.HttpSession session = req.getSession();
		String username= req.getParameter("username");
		String psssword= req.getParameter("password");
		User user=userservice.login(username, psssword);
		if(user==null) {
			return "login";	
		}
		else {
			session.setAttribute("user", user);
			session.setAttribute("username",username);
			return toUsercenter(req, resp);
		}
	}

}
