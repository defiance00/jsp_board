package dao;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dto.BoardFile;

class JunitTestF {
	BoardFileDAO bfdao= new BoardFileDAOImpl();
	
	@Test
	void testInsert() {
		BoardFile boardfile = new BoardFile();
		boardfile.setFilename("c.jpg");
		boardfile.setBnum(5);
		boardfile.setFnum(1);
		int cnt = bfdao.insert(boardfile);
		System.out.println(cnt+"건 추가");
	}

	@Test
	void testUpdate() {
		BoardFile boardfile = new BoardFile();
		boardfile.setFilename("d.jpg");
		boardfile.setBnum(5);
		boardfile.setFnum(3);
		int cnt = bfdao.update(boardfile);
		System.out.println(cnt+"건 수정");
	}

	@Test
	void testDelete() {
		int cnt = bfdao.delete(3);
		System.out.println(cnt +"건 삭제");
	}

	@Test
	void testSelectOne() {
		BoardFile boardfile = bfdao.selectOne(2);
		System.out.println(boardfile);
	}

	@Test
	void testSelectList() {
		List<BoardFile> bfList = bfdao.selectList(6);
		System.out.println(bfList);
	}

}
