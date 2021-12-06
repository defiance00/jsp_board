package dao;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MBConn {
	

	public static SqlSession getSession() {
		SqlSession session = null;
		String resource="mybatis/mybatisConfig.xml";
		try {
			InputStream is =Resources.getResourceAsStream(resource);
			SqlSessionFactory sf =new SqlSessionFactoryBuilder().build(is);
			//세션을 만들수 있는 객체 (팩토리 만들기)
			session =sf.openSession();
			System.out.println("session생성 성공");
		} catch (IOException e) {
			System.out.println("mb환경파일 읽기 실패");
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return session;
		
		
	}
}
