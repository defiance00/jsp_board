package controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;

import dto.Board;
import dto.Page;
import service.BoardService;
import service.BoardServiceImpl;


@WebServlet("/board/*")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private BoardService bservice = new BoardServiceImpl();
  
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri=request.getRequestURI();
		System.out.println(uri);
		//context path
		String path = request.getContextPath();
		
		if(uri.contains("list")) {
			String findkey= request.getParameter("findkey");
			String findvalue=request.getParameter("findvalue");
			String curpage_s = request.getParameter("curpage");
			
			int curpage=1;
			if (curpage_s !=null)
				curpage =Integer.parseInt(curpage_s);
			
			Page page = new Page();
			System.out.println(findkey);
			System.out.println(findvalue);
			
			page.setFindkey(findkey);
			page.setFindvalue(findvalue);
			page.setCurpage(curpage);
			System.out.println("cont" + page);
			List<Board> blist =bservice.selectList(page);
			System.out.println(blist);
			
			//forward 
			
			request.setAttribute("blist", blist);
			request.setAttribute("page", page);
			request.getRequestDispatcher("/views/board/list.jsp").forward(request, response);
			
			
		}else if(uri.contains("add")) {
			//등록
			String saveDirectory = getServletContext().getInitParameter("savedir");
			int size = 1024*1024*10;
			MultipartRequest multi = new MultipartRequest(request,saveDirectory,size,"utf-8",new DefaultFileRenamePolicy());
			String email = multi.getParameter("email");
			String subject = multi.getParameter("subject");
			String content =multi.getParameter("content");
	
			Board board = new Board();
			board.setEmail(email);
			board.setSubject(subject);
			board.setContent(content);
			board.setIp(request.getRemoteAddr()); //요청아이피
			
			System.out.println(board);
			
			List<String> filenames = new ArrayList<>();
			Enumeration<String> files = multi.getFileNames();
			while(files.hasMoreElements()) {
				String name=files.nextElement();
				System.out.println(name);
				String filename = multi.getFilesystemName(name);
				filenames.add(filename);
				
			}
			System.out.println(filenames);
			String msg = bservice.insert(board,filenames);
			//redirect방식, msg
			response.sendRedirect(path+"/board/list?msg=" + URLEncoder.encode(msg, "utf-8"));
		}else if(uri.contains("detail")) {
			//상세조회
			int bnum = Integer.parseInt(request.getParameter("bnum"));
			System.out.println(bnum);
			//게시물 조회
			String cntplus=request.getParameter("cntplus");
			System.out.println(cntplus);
			if(cntplus !=null) {
				bservice.cntplus(bnum);
			}
			//파일 조회
			Map<String, Object> map = bservice.selectOne(bnum);
			
			//조회수 +1
			
			
			//forward방식 detail.jsp
			request.setAttribute("map", map);
			request.getRequestDispatcher("/views/board/detail.jsp").forward(request, response);
			
			
		}else if(uri.contains("remove")) {
			int bnum = Integer.parseInt(request.getParameter("bnum"));
			String msg = bservice.delete(bnum);
			System.out.println(msg);
			response.sendRedirect(path +"/board/list?msg=" + URLEncoder.encode(msg, "utf-8"));
		}else if(uri.contains("modiform")) {
			int bnum = Integer.parseInt(request.getParameter("bnum"));
			Map<String, Object> map = bservice.selectOne(bnum);
			
			request.setAttribute("map", map);
			request.getRequestDispatcher("/views/board/modify.jsp").forward(request, response);
		}else if(uri.contains("modify")) {
			String saveDirectory = getServletContext().getInitParameter("savedir");
			int size = 1024*1024*10;
			MultipartRequest multi = new MultipartRequest(request,saveDirectory,size,"utf-8",new DefaultFileRenamePolicy());
			int bnum=Integer.parseInt(multi.getParameter("bnum"));
			String email = multi.getParameter("email");
			String subject = multi.getParameter("subject");
			String content =multi.getParameter("content");
			Board board = new Board();
			board.setBnum(bnum);
			board.setEmail(email);
			board.setSubject(subject);
			board.setContent(content);
			board.setIp(request.getRemoteAddr());
			System.out.println("수정할 board" + board);
			
			String[] filedels=multi.getParameterValues("filedel");
			System.out.println("기존파일 삭제리스트" + Arrays.toString(filedels));
			
			
			List<String> filenames = new ArrayList<>();
			Enumeration<String> files = multi.getFileNames();
			while(files.hasMoreElements()) {
				String name=files.nextElement();
				System.out.println(name);
				String filename = multi.getFilesystemName(name);
				if(filename!=null) filenames.add(filename);
				
			}
			System.out.println("신규파일 추가리스트" + filenames);
			
			//서비스에 업데이트요청
			String msg = bservice.update(board, filedels, filenames);
			//redirect방식 board/detail
			response.sendRedirect(path+"/board/detail?bnum=" +bnum + "&msg=" + URLEncoder.encode(msg, "utf-8"));
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
