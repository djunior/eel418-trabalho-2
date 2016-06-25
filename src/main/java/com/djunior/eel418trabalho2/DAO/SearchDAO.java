/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2.DAO;

import com.djunior.eel418trabalho2.DTO.ReferenciaBibliografica;
import com.djunior.eel418trabalho2.utils.Utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author davidbrittojr
 */
public class SearchDAO extends BaseDAO {
    
    public SearchDAO() throws SQLException{
        super();
    }
    
    private ReferenciaBibliografica createBookData(ResultSet rst){
        ReferenciaBibliografica book = new ReferenciaBibliografica();
        try{
            book.setSerialno(rst.getLong("serialno"));
            book.setTitulo(rst.getString("titulo"));
            book.setAutoria(rst.getString("autoria"));
        } catch(Exception e){
            
        }
        return book;
    }
    
    public ArrayList<ReferenciaBibliografica> getAllBookData() throws SQLException{
        ArrayList<ReferenciaBibliografica> bookDataList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        try {
            con = getConnection();
            pstmt = con.prepareStatement("SELECT * FROM referencias;");
            rst = pstmt.executeQuery();
            while(rst.next()){
                bookDataList.add(createBookData(rst));
            }
        } finally {
            try { rst.close(); } catch(Exception e) {}
            try { pstmt.close(); } catch(Exception e) {}
            try { con.close(); } catch(Exception e) {}
        }
        
        return bookDataList;
    }
    
    public ReferenciaBibliografica getBookDataByTitle(String title) throws SQLException{
        ReferenciaBibliografica book;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement("SELECT * FROM referencias WHERE titulo=?;");
            pstmt.setString(1, title);

            rst = pstmt.executeQuery();
            rst.next();
            book = createBookData(rst);
        } finally {
            try { rst.close(); } catch(Exception e) {}
            try { pstmt.close(); } catch(Exception e) {}
            try { con.close(); } catch(Exception e) {}
        }
        return book;
    }
    
    public ArrayList<ReferenciaBibliografica> getBookDataByTitleFragment(String fragment) throws SQLException {
        ArrayList<ReferenciaBibliografica> bookDataList = new ArrayList<>();
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        try{
            con = getConnection();
            
            String statement = "SELECT T1.serialno, T1.titulo, T1.autoria, (count(*)) AS nrohits "
                                + "FROM referencias T1 "
                                + "INNER JOIN palavrasdotitulo T2 "
                                + "ON (T1.serialno = T2.serialno_referencias) "
                                + "WHERE ";
            String[] fragments = Utils.removeDiacriticals(fragment).split(" ");
            int count = 0;
            for(String frag: fragments) {
                statement = statement + "T2.palavra='" + frag + "'";
                count++;
                if(count < fragments.length)
                    statement = statement + " OR ";
            }
            statement = statement + " GROUP BY T1.serialno, T1.titulo, T1.autoria ORDER BY nrohits DESC, T1.titulo ASC;";

            System.out.println(statement);

            pstmt = con.prepareStatement(statement);
            rst = pstmt.executeQuery();
            while(rst.next()){
                bookDataList.add(createBookData(rst));
            }
        } finally {
            try { rst.close(); } catch ( Exception e) {}
            try { pstmt.close(); } catch( Exception e) {}
            try { con.close(); } catch(Exception e) {}
        }
        
        return bookDataList;
    }
    
    public ArrayList<ReferenciaBibliografica> getBookDataByAuthorFragment(String fragment) {
        ArrayList<ReferenciaBibliografica> bookDataList = new ArrayList<>();
        
        return bookDataList;
    }
}
