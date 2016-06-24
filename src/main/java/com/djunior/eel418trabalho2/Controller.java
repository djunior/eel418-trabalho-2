/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2;

import com.djunior.eel418trabalho2.DTO.ListaDeReferencias;
import com.djunior.eel418trabalho2.DTO.ReferenciaBibliografica;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        System.out.println("Controller.processRequest called!");
        // Não é um conjunto de pares nome-valor,
        // então tem que ler como se fosse um upload de arquivo...
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
        
        
        System.out.println("Titulo: " + jsonObjectDeJava.getString("titulo"));
        
        // Agora é só responder...
//        RespostaDTO dto = new RespostaDTO();
//        dto.setCampo1("Servidor recebeu:" 
//                                          + jsonObjectDeJava.getString("campo1"));
//        dto.setCampo2("Servidor recebeu:" + jsonObjectDeJava.getString("campo2"));
//        dto.setCampo3("Servidor recebeu:" + jsonObjectDeJava.getString("campo3"));
//        dto.setCampo4("Servidor recebeu:" + jsonObjectDeJava.getString("campo4"));
        
        ListaDeReferencias resposta = new ListaDeReferencias();

        for (int i = 0; i < 15; i++){
            ReferenciaBibliografica ref = new ReferenciaBibliografica();
            ref.setSerialno(i+1);
            ref.setTitulo("Titulo Obra " + (i+1) );
            ref.setAutoria(("David Britto Jr"));
            ref.setPalchave("web programming");
            resposta.add(ref);
        }
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(resposta.toString());
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
