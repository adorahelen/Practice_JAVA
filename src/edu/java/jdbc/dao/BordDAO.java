package edu.java.jdbc.dao;

import edu.java.jdbc.util.DBCon;
import edu.java.jdbc.vo.BordVO;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
게시물 등록
게시물 수정
게시물 삭제
게시물 하나 조회
게시물 전체 목록
게시물 조회 수 증가 ----->
로그인 기능, 댓글 기능 ,
* */


public class BordDAO {
    private Connection con;
    private Statement stmt;
    private PreparedStatement pstmt;
    private ResultSet rs;

    public BordDAO() {
        this.con = DBCon.getConnection();
    } // connect

    public boolean insert(BordVO vo) {

        String query = " INSERT INTO t_board(title, content, writer) VALUES(?, ?, ?) ";
// 쿼리문 입력 방식에 따른 에러 발생 가능성 있음
        // 타이틀 - 컨텐츠 - 라이터
        try {
            pstmt = con.prepareStatement(query); //쿼리문을 미리 준비
            pstmt.setString(1, vo.getTitle());         //쿼리의 물음표에 해당하는 값 바인딩
            pstmt.setString(2, vo.getContent());
            pstmt.setString(3, vo.getWriter());

            int result = pstmt.executeUpdate(); // DML 쿼리 실행 결과 행 수 저장
            if (result == 1) { //추가 성공인 경우
                DBCon.close(pstmt);
                return true;

            }
        } catch (SQLException e) {
//          throw new RuntimeException(e);
            System.err.println("INSERT 쿼리 실행 실패");
            System.out.println(e.getSQLState() + " | " +
                               e.getErrorCode() + " | " + e.getMessage());
        } finally {     //예외 발생 여부 관계없이 항상 실행
            DBCon.close(pstmt);
        }
        return false;
    }

    public boolean update(BordVO vo) {
        String query = " UPDATE t_board SET title = ?, content = ?  WHERE bno = ? ";
// mid 대신에 bno로 잡았는데, 문제의 원인이 될 수도?

        try {
            pstmt = con.prepareStatement(query); //쿼리문을 미리 준비
            pstmt.setString(1, vo.getTitle());         //쿼리의 물음표에 해당하는 값 바인딩
            pstmt.setString(2, vo.getContent());
            pstmt.setInt(3, vo.getBno()); // 근데 이건 자동 생성인데, 수정할 필요가 있나? 지정해서 수정하나?


            int result = pstmt.executeUpdate(); // DML 쿼리 실행 결과 행 수 저장
            if (result == 1) { //추가 성공인 경우
                DBCon.close(pstmt);
                return true;
            }
        } catch (SQLException e) {
 //           throw new RuntimeException(e);
            System.err.println("UPDATE 쿼리 실행 실패");
            System.out.println(e.getSQLState() + " | " +
                               e.getErrorCode() + " | " + e.getMessage());
        } finally {     //예외 발생 여부 관계없이 항상 실행
            DBCon.close(pstmt);
        }
        return false;
    }

    public boolean delete(int bno) {
        String query = " DELETE FROM t_board WHERE bno = ? ";

        try {
            pstmt = con.prepareStatement(query); //쿼리문을 미리 준비
            pstmt.setInt(1, bno);         //쿼리의 물음표에 해당하는 값 바인딩



            int result = pstmt.executeUpdate(); // DML 쿼리 실행 결과 행 수 저장
            if (result == 1) { //추가 성공인 경우
                DBCon.close(pstmt);
                return true;
            }
        } catch (SQLException e) {
            //           throw new RuntimeException(e);
            System.err.println("Delete 쿼리 실행 실패");
            System.out.println(e.getSQLState() + " | " +
                    e.getErrorCode() + " | " + e.getMessage());
        } finally {     //예외 발생 여부 관계없이 항상 실행
            DBCon.close(pstmt);
        }
        return false;


    }
    public BordVO select(int bno) {   //매개변수로 넘겨받은 회원아이디의 레코드 조회 ----
        String query = " SELECT * FROM t_board WHERE bno = ? ";
        BordVO vo = null;


        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, bno);         //쿼리의 물음표에 해당하는 값 바인딩
            rs = pstmt.executeQuery();    //쿼리 실행하고 결과 레코드 받기

            if (rs.next()) { //읽어올 값이 있는지 확인
                vo = new BordVO(); // 객체를 생성하여 읽어온 값들을 각 필드에 저장 시킨다.
                vo.setBno(rs.getInt("bno"));
                vo.setTitle(rs.getString("title"));
                vo.setContent(rs.getString("content"));
                vo.setWriter(rs.getString("writer"));
                vo.setHit(rs.getInt("hit"));
                vo.setWriteDate(rs.getDate("write_date"));
            }
        } catch (SQLException e) {
            //           throw new RuntimeException(e);
            System.err.println("Select 쿼리 실행 실패");
            System.out.println(e.getSQLState() + " | " +
                    e.getErrorCode() + " | " + e.getMessage());
        } finally { // 예외 다시
            DBCon.close(rs, pstmt);

            return vo;
        }

    }
// 아래는 아직 안바꿈
    public List<BordVO> selectAll() { //t_member 테이블의 모든 레코드 조회 ----------------
        String query = " SELECT * FROM t_board ";
        List<BordVO> list = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();    //쿼리 실행하고 결과 레코드 받기

            while (rs.next()) {        //더이상 읽을 값이 없을 때까지 반복
                BordVO vo = new BordVO(); // 객체를 생성하여 읽어온 값들을 각 필드에 저장 시킨다.
                vo.setBno(rs.getInt("bno"));
                vo.setTitle(rs.getString("title"));
                vo.setContent(rs.getString("content"));
                vo.setWriter(rs.getString("writer"));
                vo.setHit(rs.getInt("hit"));
                vo.setWriteDate(rs.getDate("write_date"));
                list.add(vo); // 생성된 mvo 객체를 list 에 저장
            }


        } catch (SQLException e) {
            //           throw new RuntimeException(e);
            System.err.println("All Select 쿼리 실행 실패");
            System.out.println(e.getSQLState() + " | " +
                    e.getErrorCode() + " | " + e.getMessage());
        } finally {
            DBCon.close(rs, pstmt);
        }
        return list;
    }


    // 게시물 조회수 증가시키기?
    public boolean updateHit(int bno) { // 조회 수를 장가시킬 게시물 번호 받기
        String query = " UPDATE t_board SET hit = +1  WHERE bno = ? ";
        try {
            pstmt = con.prepareStatement(query); //쿼리문을 미리 준비
            pstmt.setInt(1, bno);         //쿼리의 물음표에 해당하는 값 바인딩

            int result = pstmt.executeUpdate(); // DML 쿼리 실행 결과 행 수 저장
            if (result == 1) { //추가 성공인 경우
                DBCon.close(pstmt);
                return true;
            }
        } catch (SQLException e) {
            //           throw new RuntimeException(e);
            System.err.println("UPDATE 쿼리 실행 실패");
            System.out.println(e.getSQLState() + " | " +
                    e.getErrorCode() + " | " + e.getMessage());
        } finally {     //예외 발생 여부 관계없이 항상 실행
            DBCon.close(pstmt);
        }
        return false;
    }



}
