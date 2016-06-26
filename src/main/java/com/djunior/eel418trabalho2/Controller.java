/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2;

import com.djunior.eel418trabalho2.DAO.CatalogDAO;
import com.djunior.eel418trabalho2.DAO.SearchDAO;
import com.djunior.eel418trabalho2.DTO.CreateResponseMessage;
import com.djunior.eel418trabalho2.DTO.ListaDeReferencias;
import com.djunior.eel418trabalho2.DTO.ReferenciaBibliografica;
import com.djunior.eel418trabalho2.DTO.ResponseMessage;
import com.djunior.eel418trabalho2.DTO.SearchQuery;
import com.djunior.eel418trabalho2.DTO.SearchResponseMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet {

    private JsonObject readJSONObject(HttpServletRequest request) throws ServletException, IOException {
                BufferedReader br = new BufferedReader(
                                  new  InputStreamReader(
                                           request.getInputStream(),"UTF8"));
        String textoDoJson = br.readLine();
        System.out.println("textoDoJson:");
        System.out.println(textoDoJson);
        
        JsonObject jsonObjectDeJava = null;
        // Ler e fazer o parsing do String para o "objeto json" java
        try (   //Converte o string em "objeto json" java
                // Criar um JsonReader.
                JsonReader readerDoTextoDoJson = 
                        Json.createReader(new StringReader(textoDoJson))) {
                // Ler e fazer o parsing do String para o "objeto json" java
                jsonObjectDeJava = readerDoTextoDoJson.readObject();
                // Acabou, então fechar o reader.
                System.out.println("Criou um JsonObject!");
                System.out.println(jsonObjectDeJava.toString());
        }catch(Exception e){
            System.out.println("Caught exception: " + e.getMessage());
            e.printStackTrace();
        }
        return jsonObjectDeJava;
    }
    
    private String search(JsonObject obj) throws ServletException, IOException {
        
        SearchQuery sq = new SearchQuery(obj);
        ListaDeReferencias lista = new ListaDeReferencias();
        SearchResponseMessage resposta;
        
        try {
            SearchDAO search = new SearchDAO();
//            search.getBookByQuery(sq);
            lista.setListaReferencias(search.getBookByQuery(sq));
            resposta = new SearchResponseMessage(true,"",lista);
        } catch (SQLException e) {
            e.printStackTrace();
            resposta = new SearchResponseMessage(false,"Falha na comunicação com o banco de dados",lista);
        }
        System.out.println("Enviando resposta:");
        System.out.println(resposta.toString());
        return resposta.toString();
    }
    
    private String create(JsonObject obj) {
        
        ReferenciaBibliografica ref = new ReferenciaBibliografica(obj);
        ResponseMessage resposta;
        try {
            CatalogDAO catalog = new CatalogDAO();
            catalog.create(ref);
            resposta = new CreateResponseMessage(true,"Patrimonio " + ref.getSerialno() + " criado.",ref.getSerialno());
        } catch(SQLException e) {
            e.printStackTrace();
            resposta = new CreateResponseMessage(false,"Falha na comunicação com o banco de dados.",ref.getSerialno());
        }
        
        return resposta.toString();
    }
    
    private String update(JsonObject obj){
        System.out.println("Controller.updtae called");
        ReferenciaBibliografica ref = new ReferenciaBibliografica(obj);
        ResponseMessage resposta;
        try {
            CatalogDAO catalog = new CatalogDAO();
            catalog.update(ref);
            resposta = new ResponseMessage(true,"Patrimonio " + ref.getSerialno() + " atualizado com sucesso");
        } catch (SQLException e) {
            resposta = new ResponseMessage(false,"Falha na comunicação com o banco de dados.");
        }
        return resposta.toString();
    }
    
    private String remove(JsonObject obj) {
        
        System.out.println("Controller.remove called!");
        ReferenciaBibliografica ref = new ReferenciaBibliografica(obj);
        ResponseMessage resposta;
        try {
            CatalogDAO catalog = new CatalogDAO();
            System.out.println("Calling CatalogDAO.remove");
            catalog.remove(ref);
            resposta = new ResponseMessage(true, "Patrimonio " + ref.getSerialno() + " removida com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
            resposta = new ResponseMessage(false,"Falha na comunicação com o banco de dados.");
        }
        
        return resposta.toString();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Controller.processRequest called!");
        
        JsonObject obj = readJSONObject(request);
        String resposta;
        String action = request.getParameter("action");
        switch(action){
            case "search":
                resposta = search(obj);
                break;
            case "create":
                resposta = create(obj);
                break;
            case "update":
                resposta = update(obj);
                break;
            case "remove":
                resposta = remove(obj);
                break;
            default:
                resposta = search(obj);
                break;
        }
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(resposta);
        out.flush();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Controller doGet");
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("Controller doPost");
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
