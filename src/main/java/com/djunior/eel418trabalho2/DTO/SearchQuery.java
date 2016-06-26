/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2.DTO;

import java.io.Serializable;
import javax.json.JsonObject;

/**
 *
 * @author davidbrittojr
 */
public class SearchQuery implements Serializable{
    private SearchQueryPair patrimonio;
    private SearchQueryPair titulo;
    private SearchQueryPair autoria;
    private SearchQueryPair veiculo;
    private SearchQueryPair dataInicial;
    private SearchQueryPair dataFinal;
    private SearchQueryPair palchave;

    public SearchQuery(JsonObject obj) {
        System.out.println("Obj:");
        System.out.println(obj.toString());
        try{
            patrimonio = new SearchQueryPair(obj.getJsonObject("patrimonio"));
        } catch (NullPointerException np) {
            patrimonio = new SearchQueryPair(false);
            
            try { titulo = new SearchQueryPair(obj.getJsonObject("titulo")); } catch (NullPointerException e) { titulo = new SearchQueryPair(false); }
            try { autoria = new SearchQueryPair(obj.getJsonObject("autoria")); } catch (NullPointerException e) { autoria = new SearchQueryPair(false); }
            try { veiculo = new SearchQueryPair(obj.getJsonObject("veiculo")); } catch (NullPointerException e) { veiculo = new SearchQueryPair(false);}
            try { dataInicial = new SearchQueryPair(obj.getJsonObject("dataInicial")); } catch(NullPointerException e) { dataInicial = new SearchQueryPair(false); }
            try { dataFinal = new SearchQueryPair(obj.getJsonObject("dataFinal")); } catch (NullPointerException e) { dataFinal = new SearchQueryPair(false); }
            try { palchave = new SearchQueryPair(obj.getJsonObject("palchave")); } catch (NullPointerException e) { palchave = new SearchQueryPair(false); }
        }
    }
    
    public SearchQueryPair getTitulo() {
        return titulo;
    }
    
    public SearchQueryPair getAutoria() {
        return autoria;
    }
    
        public SearchQueryPair getPatrimonio() {
        return patrimonio;
    }

    public SearchQueryPair getVeiculo() {
        return veiculo;
    }

    public SearchQueryPair getDataInicial() {
        return dataInicial;
    }

    public SearchQueryPair getDataFinal() {
        return dataFinal;
    }

    public SearchQueryPair getPalchave() {
        return palchave;
    }
}
