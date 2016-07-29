package com.djunior.eel418trabalho2;

import com.djunior.eel418trabalho2.DAO.SearchDAO;
import com.djunior.eel418trabalho2.DTO.ListaDeReferencias;
import com.djunior.eel418trabalho2.DTO.SearchQuery;
import com.djunior.eel418trabalho2.DTO.SearchResponseMessage;
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

@Path("search")
public class BuscaResource {

    @Context
    private UriInfo context;

    public BuscaResource() {
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String buscar(String s) {
        JsonObject obj = createJSONObject(s);
        SearchQuery sq = new SearchQuery(obj);
        ListaDeReferencias lista = new ListaDeReferencias();
        SearchResponseMessage resposta;
        
        try {
            SearchDAO search = new SearchDAO();
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
}
