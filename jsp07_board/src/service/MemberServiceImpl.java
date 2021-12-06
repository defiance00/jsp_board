package service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import dao.MemberDAO;
import dao.MemberDAOImpl;
import dto.Member;

public class MemberServiceImpl implements MemberService {
	private MemberDAO mdao = new MemberDAOImpl();
	@Override
	public String insert(Member member) {
		//비밀번호를 암호화해서 저장 
		String salt = saltmake(); //salt생성
		String secretpw = sha256(member.getPasswd(), salt);
		member.setPasswd(secretpw);
		member.setSalt(salt);
		int cnt = mdao.insert(member);
		if(cnt>0)
			
			return "추가성공";
		else
			return "추가실패";
	}
	
	//salt를 랜덤하게 만들기
	public String saltmake() {
		
		String salt = null;
		try {
			SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
			byte[] bytes = new byte[16];
			sr.nextBytes(bytes); //랜덤한 값을 bytes에 만들어서
			//byte데이터를 String형으로 변환 
			salt = new String(Base64.getEncoder().encode(bytes));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salt;
	}

	//평문을 암호문으로 반환
	public String sha256(String passwd, String salt) {
		StringBuffer sb = new StringBuffer();
		try {
			//sha-256 단방향암호기법, 복호화 불가능 256bit(16진수 64자리)
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(passwd.getBytes()); //암호화된 바이트배열(32byte)
			md.update(salt.getBytes()); //솔트를 
			byte[] data = md.digest();
			System.out.println("암호화된 바이트배열" + Arrays.toString(data));
			//16진수 문자열로 변경 sb변수에 추가 
			for(byte b : data) {
				sb.append(String.format("%02x", b));
				
				
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public Map<String, String> login(String email, String passwd) {
		//  한건조회
		Map<String, String> map = new HashMap<>();
		
		String msg= null;
		String rcode = null;
		
		
		Member member = mdao.selectOne(email);
		if(member==null) {
			msg = "이메일이 존재하지 않습니다.";
			rcode= "1";
		}else {
			
			String dbpw=member.getPasswd();
			String salt = member.getSalt();
			//암호화된 비밀번호 생성 
			String secretpw=sha256(passwd, salt);
			if(dbpw.equals(secretpw)) {
				msg="로긴성공";
				rcode="0";
			}else {
				msg="패스워드가 일치하지않습니다. 다시 입력해주세요.";
				rcode="2";
			}
		}
		map.put("msg", msg);
		map.put("rcode", rcode);
		
		System.out.println(map);
		return map;
	}

	@Override
	public Member selectOne(String email) {
		
		return mdao.selectOne(email);
	}

	@Override
	public String update(Member member, String changepw) {
		//한건조회
		String msg=null;
		
		Member dbmember = mdao.selectOne(member.getEmail());
		String dbpw = dbmember.getPasswd();
		String secretpw = sha256(member.getPasswd(), dbmember.getSalt());
		if (dbpw.equals(secretpw)) {
			if(!changepw.equals("")) {
			
			//솔트구하기
			String salt=saltmake();
			secretpw=sha256(changepw, salt);
			member.setPasswd(secretpw);
			member.setSalt(salt);
		}else {
			member.setPasswd(dbpw);
			member.setSalt(dbmember.getSalt());
		}
		System.out.println(member);
		mdao.update(member);
		msg="수정완료";
	}else {
			msg="비밀번호가 일치하지않음";
	}
		
		return msg;
	}

	
	
	
	
}
