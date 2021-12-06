package service;

import dto.Board;

public interface ReplyService {
	String insert(Board board);

	Board selectOne(int bnum);

	String update(Board board);
	
	
}
