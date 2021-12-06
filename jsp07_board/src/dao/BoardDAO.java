package dao;

import java.util.List;


import dto.Board;
import dto.Page;

public interface BoardDAO {
	int insert(Board board);
	int update(Board board);
	int delete(int bnum);
	Board selectOne(int bnum);
	List<Board> selectList(Page page);
	
	void cntplus(int bnum);
	int select_totcnt(Page page);
	//댓글 순서 변경
	void restepplus(Board board);
	//댓글조회
	List<Board> select_reply(int ref);
	
}
