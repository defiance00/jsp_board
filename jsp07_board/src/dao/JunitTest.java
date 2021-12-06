package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dto.Board;


class JunitTest {
	BoardDAO bdao= new BoardDAOImpl();
	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testMBConn() {
		MBConn.getSession();
		
	}
	@Test
	void testInsert() {
		Board board = new Board();
		board.setEmail("vibes@gmail.com");
		board.setSubject("제목");
		board.setContent("내용");
		board.setIp("192.168.0.100");
		int cnt = bdao.insert(board);
		System.out.println(cnt +"건 추가");
		
	}
	@Test
	void testUpdate() {
		Board board = new Board();
		board.setEmail("vibes@gmail.com");
		board.setSubject("제목");
		board.setContent("내용");
		board.setIp("192.168.0.100");
		board.setBnum(2);
		int cnt = bdao.update(board);
		System.out.println(cnt +"건 수정");
		
	}
	@Test
	void testDelete() {
		int cnt = bdao.delete(3);
		System.out.println(cnt +"건 삭제");
	}
	@Test
	void testSelectOne() {
		
		Board board = bdao.selectOne(4);
		
		System.out.println(board);
	}
	@Test
	void testSelectList() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("findkey", "subcon");
		map.put("findvalue", "제");
		List<Board> blist =bdao.selectList(map);
		System.out.println(blist);
	}

}
