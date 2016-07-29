package com.djunior.eel418trabalho2;

import com.djunior.eel418trabalho2.DAO.CatalogDAO;
import com.djunior.eel418trabalho2.DTO.CreateResponseMessage;
import com.djunior.eel418trabalho2.DTO.ReferenciaBibliografica;
import com.djunior.eel418trabalho2.DTO.ResponseMessage;
import java.io.StringReader;
import java.sql.SQLException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("catalog")
public class CatalogResource {

    @Context
    private UriInfo context;

    public CatalogResource() {
    }
 
   private JsonObject createJSONObject(String str) {
        
        JsonObject jsonObjectDeJava = null;
        // Ler e fazer o parsing do String para o "objeto json" java
        try (   //Converte o string em "objeto json" java
                // Criar um JsonReader.
                JsonReader readerDoTextoDoJson = 
                        Json.createReader(new StringReader(str))) {
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
    
    @POST
    @Path("create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String create(String s) {
        JsonObject obj = createJSONObject(s);
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
    
    @POST
    @Path("update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String update(String s){
        System.out.println("Controller.update called");
        JsonObject obj = createJSONObject(s);
        ReferenciaBibliografica ref = new ReferenciaBibliografica(obj);
        ResponseMessage resposta;
        try {
            CatalogDAO catalog = new CatalogDAO();
            catalog.update(ref);
            resposta = new ResponseMessage(true,"Patrimonio " + ref.getSerialno() + " atualizado com sucesso");
        } catch (SQLException e) {
            e.printStackTrace();
            resposta = new ResponseMessage(false,"Falha na comunicação com o banco de dados.");
        }
        return resposta.toString();
    }
    
    @POST
    @Path("remove")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String remove(String s) {
        System.out.println("Controller.remove called!");
        JsonObject obj = createJSONObject(s);
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
}
