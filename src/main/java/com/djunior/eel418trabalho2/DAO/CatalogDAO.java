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
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author davidbrittojr
 */
public class CatalogDAO extends BaseDAO {
    
    public CatalogDAO() throws SQLException {
        super();
    }
    
    public void create(ReferenciaBibliografica book) throws SQLException{
        if (book.getTitulo().equals("") || book.getAutoria().equals(""))
            return;
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        PreparedStatement pstmt_title = null;
        PreparedStatement pstmt_author = null;
        PreparedStatement pstmt_veiculo = null;
        PreparedStatement pstmt_palchave = null;
        
        try {
            con = getConnection();
            pstmt = con.prepareStatement("INSERT INTO dadoscatalogo (titulo,autoria,veiculo,data_publicacao) VALUES(?,?,?,?) RETURNING patrimonio;");
            
            pstmt.setString(1, book.getTitulo());
            pstmt.setString(2, book.getAutoria());
            pstmt.setString(3, book.getVeiculo());
            
            Timestamp t = Timestamp.valueOf(book.getDataPublicacao());
            pstmt.setTimestamp(4,t);

            rst = pstmt.executeQuery();
            rst.next();
            book.setSerialno(rst.getInt("patrimonio"));

            String[] titleFragments = Utils.removeDiacriticals(book.getTitulo()).split(" ");
            for (String fragment : titleFragments) {
                pstmt_title = con.prepareStatement("INSERT INTO palavrastitulonormal (palavra_titulo_normal,patrimonio) VALUES (?,?) RETURNING serialpaltitulo;");
                pstmt_title.setString(1, fragment);
                pstmt_title.setLong(2, book.getSerialno());
                pstmt_title.executeQuery();
                pstmt_title.close();
            }
            
            String[] authorFragments = Utils.removeDiacriticals(book.getAutoria()).split(" ");
            for (String fragment : authorFragments) {
                pstmt_author = con.prepareStatement("INSERT INTO palavrasautorianormal (palavra_autoria_normal,patrimonio) VALUES (?,?) RETURNING serialpalautoria;");
                pstmt_author.setString(1, fragment);
                pstmt_author.setLong(2, book.getSerialno());
                pstmt_author.executeQuery();
                pstmt_author.close();
            }

            String[] veiculoFragments = Utils.removeDiacriticals(book.getVeiculo()).split(" ");
            for (String fragment : veiculoFragments) {
                pstmt_veiculo = con.prepareStatement("INSERT INTO palavrasveiculonormal (palavra_veiculo_normal,patrimonio) VALUES (?,?) RETURNING serialpalveiculo;");
                pstmt_veiculo.setString(1, fragment);
                pstmt_veiculo.setLong(2, book.getSerialno());
                pstmt_veiculo.executeQuery();
                pstmt_veiculo.close();
            }
            
            pstmt_palchave = con.prepareStatement("INSERT INTO palavras_chave (palchave,patrimonio,palchavenormal) VALUES(?,?,?) RETURNING serialpalchave;");
            pstmt_palchave.setString(1, book.getPalchave());
            pstmt_palchave.setLong(2, book.getSerialno());
            pstmt_palchave.setString(3, Utils.removeDiacriticals((book.getPalchave())));
            pstmt_palchave.executeQuery();
            
        } finally {
            try { rst.close(); } catch(Exception e) { e.printStackTrace(); }
            try { pstmt.close(); } catch(Exception e) { e.printStackTrace(); }
            try { pstmt_title.close(); } catch(Exception e) { e.printStackTrace(); }
            try { pstmt_author.close(); } catch(Exception e) { e.printStackTrace(); }
            try { pstmt_veiculo.close(); } catch(Exception e) { e.printStackTrace(); }
            try { pstmt_palchave.close(); } catch(Exception e) { e.printStackTrace(); }
            try { con.close(); } catch(Exception e) { e.printStackTrace(); }
        }
    }
    
    public void update(ReferenciaBibliografica book) throws SQLException{
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement prepStatement = null;
        
        try {
            con = getConnection();
            
            pstmt = con.prepareStatement("UPDATE referencias"
                                      + " SET titulo=?,autoria=?"
                                      + " WHERE serialno=?;"
                                      + " DELETE FROM palavrasdotitulo"
                                      + " WHERE serialno_referencias=?;");
            
            pstmt.setString(1, book.getTitulo());
            pstmt.setString(2, book.getAutoria());
            pstmt.setLong(3, book.getSerialno());
            pstmt.setLong(4, book.getSerialno());
            
            pstmt.execute();
            
            String[] titleFragments = Utils.removeDiacriticals(book.getTitulo()).split(" ");
            for (String fragment : titleFragments) {
                prepStatement = con.prepareStatement("INSERT INTO palavrasdotitulo (palavra,serialno_referencias) VALUES (?,?) RETURNING serialno;");
                prepStatement.setString(1, fragment);
                prepStatement.setLong(2, book.getSerialno());
                prepStatement.executeQuery();
            }
        
        } finally {
            try { pstmt.close(); } catch(Exception e) {}
            try { prepStatement.close(); } catch(Exception e) {}
            try { con.close(); } catch(Exception e) {}
        }
    }
    
    public void remove(ReferenciaBibliografica book) throws SQLException{
        Connection con = null;
        PreparedStatement pstmt = null;
        
        try {
            con = getConnection();
            pstmt = con.prepareStatement(
                      "DELETE FROM dadoscatalogo WHERE patrimonio=?;"
                    + "DELETE FROM palavrastitulonormal WHERE patrimonio=?;"
                    + "DELETE FROM palavrasautorianormal WHERE patrimonio=?;"
                    + "DELETE FROM palavrasveiculonormal WHERE patrimonio=?;"
                    + "DELETE FROM palavras_chave WHERE patrimonio=?;");
            
            pstmt.setLong(1, book.getSerialno());
            pstmt.setLong(2, book.getSerialno());
            pstmt.setLong(3, book.getSerialno());
            pstmt.setLong(4, book.getSerialno());
            pstmt.setLong(5, book.getSerialno());
            
            pstmt.execute();
        
        } finally {
            try { pstmt.close(); } catch(Exception e) {}
            try { con.close(); } catch(Exception e) {}
        }
    }
    
}
