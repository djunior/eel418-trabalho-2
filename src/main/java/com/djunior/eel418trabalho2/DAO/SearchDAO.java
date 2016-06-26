/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2.DAO;

import com.djunior.eel418trabalho2.DTO.ReferenciaBibliografica;
import com.djunior.eel418trabalho2.DTO.SearchQuery;
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
            book.setSerialno(rst.getLong("patrimonio"));
            book.setTitulo(rst.getString("titulo"));
            book.setAutoria(rst.getString("autoria"));
            book.setVeiculo(rst.getString("veiculo"));
            book.setDataPublicacao(rst.getString("data_publicacao"));
            book.setPalchave(rst.getString("palchave"));
            System.out.println("Livro criado: " + book.toString());
        } catch(Exception e){
            e.printStackTrace();
        }
        return book;
    }
    
    private String createQuery(SearchQuery sq) {
        String q = 
        "SELECT T1.patrimonio, T1.titulo, T1.autoria, T1.veiculo, T1.data_publicacao, T2.palchave, (count(*)) AS nrohits \n" +
        "FROM dadoscatalogo T1 \n" +
        "INNER JOIN palavras_chave T2 ON (T1.patrimonio = T2.patrimonio) \n";
        
        if (sq.getPatrimonio().isActive()) {
            q = q + "WHERE T1.patrimonio = '" + sq.getPatrimonio().getValue() + "'";
        } else {
            String searchFieldsAnd = "";
            String searchFieldsOr = "(";
            Boolean firstAnd = true;
            Boolean firstOr = true;
            
            if (sq.getPalchave().isActive()){
                String palavrasDaBusca = sq.getPalchave().getCleanValue();
                String comando = "T2.palchavenormal LIKE '" + palavrasDaBusca + "' ";
                
                if (sq.getPalchave().getOperation().equals("and")){
                    if (firstAnd) firstAnd = false; else searchFieldsAnd = searchFieldsAnd + " AND ";
                    searchFieldsAnd = searchFieldsAnd + comando;

                } else {
                    if (firstOr) firstOr = false; else searchFieldsOr = searchFieldsOr + " OR ";
                    searchFieldsOr = searchFieldsOr + comando;
                }
            }
            
            if (sq.getTitulo().isActive()){
                q = q + "INNER JOIN palavrastitulonormal T3 ON(T1.patrimonio = T3.patrimonio) \n";
                String[] palavrasDaBusca = sq.getTitulo().getCleanValue().split(" ");
                String comando = "(";
                for (int i=0; i < palavrasDaBusca.length; i++) {
                    comando = comando + "T3.palavra_titulo_normal LIKE '" + palavrasDaBusca[i] + "'";
                    if (i < (palavrasDaBusca.length-1)) comando = comando + " OR ";
                }
                comando = comando + ")";
                if (sq.getTitulo().getOperation().equals("and")){
                    if (firstAnd) firstAnd = false; else searchFieldsAnd = searchFieldsAnd + " AND ";
                    searchFieldsAnd = searchFieldsAnd + comando;

                } else {
                    if (firstOr) firstOr = false; else searchFieldsOr = searchFieldsOr + " OR ";
                    searchFieldsOr = searchFieldsOr + comando;
                }
            }
            
            if (sq.getAutoria().isActive()){
                q = q + "INNER JOIN palavrasautorianormal T4 ON(T1.patrimonio = T4.patrimonio) \n";
                String[] palavrasDaBusca = sq.getAutoria().getCleanValue().split(" ");
                String comando = "(";
                for (int i=0; i < palavrasDaBusca.length; i++) {
                    comando = comando + "T4.palavra_autoria_normal LIKE '" + palavrasDaBusca[i] + "' ";
                    if (i < (palavrasDaBusca.length-1)) comando = comando + " OR ";
                }
                comando = comando + ")";
                if (sq.getAutoria().getOperation().equals("and")){
                    if (firstAnd) firstAnd = false; else searchFieldsAnd = searchFieldsAnd + " AND ";
                    searchFieldsAnd = searchFieldsAnd + comando;

                } else {
                    if (firstOr) firstOr = false; else searchFieldsOr = searchFieldsOr + " OR ";
                    searchFieldsOr = searchFieldsOr + comando;
                }
            }
            
            if (sq.getVeiculo().isActive()){
                q = q + "INNER JOIN palavrasveiculonormal T5 ON(T1.patrimonio = T5.patrimonio) \n";
                String[] palavrasDaBusca = sq.getVeiculo().getCleanValue().split(" ");
                String comando = "(";
                for (int i=0; i < palavrasDaBusca.length; i++) {
                    comando = comando + "T5.palavra_veiculo_normal LIKE '" + palavrasDaBusca[i] + "' ";
                    if (i < (palavrasDaBusca.length-1)) comando = comando + " OR ";
                }
                comando = comando + ")";
                if (sq.getVeiculo().getOperation().equals("and")){
                    if (firstAnd) firstAnd = false; else searchFieldsAnd = searchFieldsAnd + " AND ";
                    searchFieldsAnd = searchFieldsAnd + comando;

                } else {
                    if (firstOr) firstOr = false; else searchFieldsOr = searchFieldsOr + " OR ";
                    searchFieldsOr = searchFieldsOr + comando;
                }
            }
            
            if (sq.getDataInicial().isActive()) {
                String comando = "";
                if (! sq.getDataInicial().getValue().equals("")) {
                    comando = "T1.data_publicacao > '" + sq.getDataInicial().getValue() + "' ";

                    if (sq.getDataInicial().getOperation().equals("and")){
                        if (firstAnd) firstAnd = false; else searchFieldsAnd = searchFieldsAnd + " AND ";
                        searchFieldsAnd = searchFieldsAnd + comando;

                    } else {
                        if (firstOr) firstOr = false; else searchFieldsOr = searchFieldsOr + " OR ";
                        searchFieldsOr = searchFieldsOr + comando;
                    }
                }
            }
            
            if (sq.getDataFinal().isActive()) {
                String comando = "";
                if (! sq.getDataFinal().getValue().equals(""))  {
                    comando = "T1.data_publicacao < '" + sq.getDataFinal().getValue() + "' ";
                
                    if (sq.getDataFinal().getOperation().equals("and")){
                        if (firstAnd) firstAnd = false; else searchFieldsAnd = searchFieldsAnd + " AND ";
                        searchFieldsAnd = searchFieldsAnd + comando;

                    } else {
                        if (firstOr) firstOr = false; else searchFieldsOr = searchFieldsOr + " OR ";
                        searchFieldsOr = searchFieldsOr + comando;
                    }
                }
            }
            
            searchFieldsOr = searchFieldsOr + ")";
            if (!searchFieldsAnd.equals("") || ! searchFieldsOr.equals("()"))
                q = q + " WHERE ";
            
            if (! searchFieldsAnd.equals("")) {
                q = q + searchFieldsAnd;
                if (! searchFieldsOr.equals("()")) q = q + " AND ";
            }
            if (! searchFieldsOr.equals("()")) q = q + searchFieldsOr;
        }
        
        String finalSelectExterno = 
        " GROUP BY T1.patrimonio, T1.titulo, T1.autoria, T1.veiculo, T1.data_publicacao, T2.palchave ORDER BY nrohits DESC, titulo ASC;";
        
        return q + finalSelectExterno;
    }
    
    public ArrayList<ReferenciaBibliografica> getAllBookData() throws SQLException{
        ArrayList<ReferenciaBibliografica> bookDataList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        try {
            con = getConnection();
            pstmt = con.prepareStatement("SELECT * FROM dadoscatalogo T1"
                                      + " INNER JOIN palavras_chave T2"
                                      + " ON (T1.patrimonio = T2.patrimonio);");
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
    
    public ArrayList<ReferenciaBibliografica> getBookByQuery(SearchQuery sq) throws SQLException {
        String query = createQuery(sq);
        System.out.println(query);
        ArrayList<ReferenciaBibliografica> bookList = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        
        try {
            con = getConnection();
            pstmt = con.prepareStatement(query);
            rst = pstmt.executeQuery();
            
            
            while(rst.next()){
                System.out.println("Adicionando livro para a lista");
                bookList.add(createBookData(rst));
            }
        } finally {
            try { rst.close(); } catch ( Exception e) {}
            try { pstmt.close(); } catch( Exception e) {}
            try { con.close(); } catch(Exception e) {}
        }
        
        return bookList;
    }
    
    public ReferenciaBibliografica getBookBySerialno(Long serialno) throws SQLException {
        ReferenciaBibliografica book;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rst = null;
        try {
            con = getConnection();
            
            pstmt = con.prepareStatement("SELECT * FROM dadoscatalogo WHERE patrimonio=?;");
            pstmt.setLong(1, serialno);
            rst = pstmt.executeQuery();
            
            book = createBookData(rst);
        } finally {
            try { rst.close(); } catch(Exception e) {}
            try { pstmt.close(); } catch(Exception e) {}
            try { con.close(); } catch(Exception e) {}
        }
        return book;
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
