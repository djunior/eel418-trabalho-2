/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BaseDAO {

    private DataSource ds;

    public BaseDAO() throws SQLException {
        try {
            InitialContext cxt = new InitialContext();
            ds = (DataSource) cxt.lookup("java:comp/env/jdbc/bibliopdf");
        } catch (Exception e) {
            throw new SQLException("Falha ao comunicar com o banco de dados");
        }
    }

    public Connection getConnection() throws SQLException {
        if(ds!=null){
            return ds.getConnection();
        }else{
            return null;
        }
    }
}
