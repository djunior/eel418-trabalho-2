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
        if (book.getTitulo().equals(""))
            return;
        
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        PreparedStatement pstmt_title = null;
        PreparedStatement pstmt_author = null;
        PreparedStatement pstmt_veiculo = null;
        PreparedStatement pstmt_palchave = null;
        
        Boolean hasAutoria = ! book.getAutoria().equals("");
        Boolean hasVeiculo = ! book.getVeiculo().equals("");
        Boolean hasDataPublicacao = ! book.getDataPublicacao().equals("");
        Boolean hasPalChave = ! book.getPalchave().equals("");
        
        try {
            con = getConnection();
            
            String columns = "titulo";
            String values = "?";
            Integer count = 1;
            Integer autoria_index = 0, veiculo_index = 0, data_publicacao_index = 0;
            
            if (hasAutoria){
                columns += ",autoria";
                values += ",?";
                autoria_index = ++count;
            }
            
            if (hasVeiculo) {
                columns += ",veiculo";
                values += ",?";
                veiculo_index = ++count;
            }
            
            if (hasDataPublicacao) {
                columns += ",data_publicacao";
                values += ",?";
                data_publicacao_index = ++count;
            }
            
            String statement = "INSERT INTO dadoscatalogo (" + columns + ") VALUES(" + values + ") RETURNING patrimonio;";
            
            pstmt = con.prepareStatement(statement);
            
            pstmt.setString(1, book.getTitulo());
            
            if (hasAutoria)
                pstmt.setString(autoria_index, book.getAutoria());
            
            if (hasVeiculo)
                pstmt.setString(veiculo_index, book.getVeiculo());
            
            if (hasDataPublicacao) {
                Timestamp t = Timestamp.valueOf(book.getDataPublicacao());
                pstmt.setTimestamp(data_publicacao_index,t);
            }

            System.out.println("EXECUTING CATALOG CREATE QUERY");
            
            rst = pstmt.executeQuery();
            rst.next();
            book.setSerialno(rst.getLong("patrimonio"));
            
            System.out.println("DONE!");
            
            String[] titleFragments = Utils.removeDiacriticals(book.getTitulo()).split(" ");
            for (String fragment : titleFragments) {
                pstmt_title = con.prepareStatement("INSERT INTO palavrastitulonormal (palavra_titulo_normal,patrimonio) VALUES (?,?) RETURNING serialpaltitulo;");
                pstmt_title.setString(1, fragment);
                pstmt_title.setLong(2, book.getSerialno());
                pstmt_title.executeQuery();
                pstmt_title.close();
            }
            
            if (hasAutoria) {
                String[] authorFragments = Utils.removeDiacriticals(book.getAutoria()).split(" ");
                for (String fragment : authorFragments) {
                    pstmt_author = con.prepareStatement("INSERT INTO palavrasautorianormal (palavra_autoria_normal,patrimonio) VALUES (?,?) RETURNING serialpalautoria;");
                    pstmt_author.setString(1, fragment);
                    pstmt_author.setLong(2, book.getSerialno());
                    pstmt_author.executeQuery();
                    pstmt_author.close();
                }
            }

            if (hasVeiculo) {
                String[] veiculoFragments = Utils.removeDiacriticals(book.getVeiculo()).split(" ");
                for (String fragment : veiculoFragments) {
                    pstmt_veiculo = con.prepareStatement("INSERT INTO palavrasveiculonormal (palavra_veiculo_normal,patrimonio) VALUES (?,?) RETURNING serialpalveiculo;");
                    pstmt_veiculo.setString(1, fragment);
                    pstmt_veiculo.setLong(2, book.getSerialno());
                    pstmt_veiculo.executeQuery();
                    pstmt_veiculo.close();
                }
            }
            
            if (hasPalChave) {
                //
                //TODO: Quebrar as palavras-chave com ; e adicionar cada palavra chave na tabela
                // 
                pstmt_palchave = con.prepareStatement("INSERT INTO palavras_chave (palchave,patrimonio,palchavenormal) VALUES(?,?,?) RETURNING serialpalchave;");
                pstmt_palchave.setString(1, book.getPalchave());
                pstmt_palchave.setLong(2, book.getSerialno());
                pstmt_palchave.setString(3, Utils.removeDiacriticals((book.getPalchave())));
                pstmt_palchave.executeQuery();
            }
            
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
        
        Boolean hasAutoria = ! book.getAutoria().equals("");
        Boolean hasPalchave = ! book.getPalchave().equals("");
        Boolean hasVeiculo = ! book.getVeiculo().equals("");
        Boolean hasDataPublicacao = ! book.getDataPublicacao().equals("");
        
        Connection con = null;
        PreparedStatement pstmt = null;
        PreparedStatement pstmt_title = null;
        PreparedStatement pstmt_author = null;
        PreparedStatement pstmt_veiculo = null;
        PreparedStatement pstmt_palchave = null;
        
        try {
            con = getConnection();
            
            String columns = "titulo=?";
            String delete_commands = "DELETE FROM palavrastitulonormal WHERE patrimonio=?;";
            Integer count = 1;
            Integer delete_patromonio_count=1;
            Integer autoria_index = 0, veiculo_index = 0, data_publicacao_index = 0;
            
            if (hasAutoria) {
                columns += ",autoria=?";
                autoria_index = ++count;
            }
            
            if (hasVeiculo) {
                columns += ",veiculo=?";
                veiculo_index = ++count;
            }
            
            if (hasDataPublicacao) {
                columns += ",data_publicacao=?";
                data_publicacao_index = ++count;
            }
            
            delete_patromonio_count = count + 1; // patrimonio do UPDATE 
            delete_patromonio_count++; // patrimonio do DELETE titulo
            
            if (hasAutoria) {
                delete_commands += "DELETE FROM palavrasautorianormal WHERE patrimonio=?;";
                delete_patromonio_count++;
            }
            
            if (hasVeiculo) {
                delete_commands += "DELETE FROM palavrasveiculonormal WHERE patrimonio=?;";
                delete_patromonio_count++;
            }
            
            if(hasPalchave) {
                delete_commands += "DELETE FROM palavras_chave WHERE patrimonio=?;";
                delete_patromonio_count++;
            }
            
            String statement = "UPDATE dadoscatalogo SET " + columns + " WHERE patrimonio=?; " + delete_commands;
            
//            System.out.println("Update statement:");
//            System.out.println(statement);
            
            pstmt = con.prepareStatement(statement);
            
//            System.out.println("Autoria index:" + autoria_index);
//            System.out.println("Veiculo index:" + veiculo_index);
//            System.out.println("Data publicacao index:" + data_publicacao_index);
            
            pstmt.setString(1, book.getTitulo());
            
            if (hasAutoria)
                pstmt.setString(autoria_index, book.getAutoria());
            
            if (hasVeiculo)
                pstmt.setString(veiculo_index, book.getVeiculo());
            
            if (hasDataPublicacao) {
                Timestamp t = Timestamp.valueOf(book.getDataPublicacao());
                pstmt.setTimestamp(data_publicacao_index, t);
            }
            
            pstmt.setLong(++count, book.getSerialno() );
            
//            System.out.println("Count: " + count);
//            System.out.println("delete_patromonio_count: " + delete_patromonio_count);
            
            for (int i = count; i < delete_patromonio_count; i++) {
//                System.out.println("pstmt.setLong(" + (i+1) + ", book.getSerialno());" );
                pstmt.setLong(i+1, book.getSerialno());
            }
            
            pstmt.execute();
            
            String[] titleFragments = Utils.removeDiacriticals(book.getTitulo()).split(" ");
            for (String fragment : titleFragments) {
                pstmt_title = con.prepareStatement("INSERT INTO palavrastitulonormal (palavra_titulo_normal,patrimonio) VALUES (?,?) RETURNING serialpaltitulo;");
                pstmt_title.setString(1, fragment);
                pstmt_title.setLong(2, book.getSerialno());
                pstmt_title.executeQuery();
                pstmt_title.close();
            }
            
            if (hasAutoria) {
                String[] authorFragments = Utils.removeDiacriticals(book.getAutoria()).split(" ");
                for (String fragment : authorFragments) {
                    pstmt_author = con.prepareStatement("INSERT INTO palavrasautorianormal (palavra_autoria_normal,patrimonio) VALUES (?,?) RETURNING serialpalautoria;");
                    pstmt_author.setString(1, fragment);
                    pstmt_author.setLong(2, book.getSerialno());
                    pstmt_author.executeQuery();
                    pstmt_author.close();
                }
            }

            
            if (hasVeiculo) {    
                String[] veiculoFragments = Utils.removeDiacriticals(book.getVeiculo()).split(" ");
                for (String fragment : veiculoFragments) {
                    pstmt_veiculo = con.prepareStatement("INSERT INTO palavrasveiculonormal (palavra_veiculo_normal,patrimonio) VALUES (?,?) RETURNING serialpalveiculo;");
                    pstmt_veiculo.setString(1, fragment);
                    pstmt_veiculo.setLong(2, book.getSerialno());
                    pstmt_veiculo.executeQuery();
                    pstmt_veiculo.close();
                }
            }
            
            if (hasPalchave) {
                pstmt_palchave = con.prepareStatement("INSERT INTO palavras_chave (palchave,patrimonio,palchavenormal) VALUES(?,?,?) RETURNING serialpalchave;");
                pstmt_palchave.setString(1, book.getPalchave());
                pstmt_palchave.setLong(2, book.getSerialno());
                pstmt_palchave.setString(3, Utils.removeDiacriticals((book.getPalchave())));
                pstmt_palchave.executeQuery();
            }

        
        } finally {
            try { pstmt.close(); } catch(Exception e) {}
            try { pstmt_title.close(); } catch(Exception e) {}
            try { pstmt_author.close(); } catch(Exception e) {}
            try { pstmt_veiculo.close(); } catch(Exception e) {}
            try { pstmt_palchave.close(); } catch(Exception e) {}
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
    
    public void saveUploadFileData(ReferenciaBibliografica book) throws SQLException {
        
        Connection con = null;
        PreparedStatement pstmt = null;
        
        try {
            con = getConnection(); 
            pstmt = con.prepareStatement("UPDATE dadoscatalogo SET nomearquivo=?,nomeoriginalarquivo=? WHERE patrimonio=?;");
            
            pstmt.setString(1,book.getFilePath());
            pstmt.setString(2,book.getFileName());
            pstmt.setLong(3,book.getSerialno());
            
            pstmt.execute();
        } finally {
            try {pstmt.close();} catch(Exception e) {}
            try {con.close();} catch(Exception e) {}
        }
    }
    
}
