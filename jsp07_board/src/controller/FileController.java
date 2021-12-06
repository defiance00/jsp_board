package controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/file/*")
public class FileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		System.out.println(uri);
		if(uri.contains("filedown")) {
			//파일다운로드
			String saveDirectory = getServletContext().getInitParameter("savedir");
			String filename=request.getParameter("filename");
			//마임타입 파일의 종류
			String mimeType=getServletContext().getMimeType(filename);
			if(mimeType==null) {
				
				mimeType = "application/octet-stream;charset=utf-8";
			}
			response.setContentType(mimeType);
			//첨부파일로 파일을 보낼때
			
			response.setHeader("Content-Disposition", "attachment;filename=" +URLEncoder.encode(filename, "utf-8"));
			
			
			System.out.println(filename);
			//읽어올파일 경로명
			String fileurl= saveDirectory +"/"+ filename;
			System.out.println(fileurl);
			//입력스트림
			FileInputStream fis = new FileInputStream(fileurl);
			//출력스트림
			ServletOutputStream outs = response.getOutputStream();
			byte[] b = new byte[4096]; //4kb
			int numRead = 0;
			while((numRead = fis.read(b,0,b.length)) != -1) {
				outs.write(b,0,numRead);
			}
			outs.flush();
			outs.close();
			fis.close();
			
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
