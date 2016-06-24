package com.djunior.eel418trabalho2.DTO;

import java.io.Serializable;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

public class ListaDeReferencias implements Serializable{
    private ArrayList<ReferenciaBibliografica> listaReferencias = new ArrayList<>();

    public void add(ReferenciaBibliografica ref){
        listaReferencias.add(ref);
    }
    
    public ArrayList<ReferenciaBibliografica> getListaReferencias() {
        return listaReferencias;
    }

    public void setListaReferencias(ArrayList<ReferenciaBibliografica> listaReferencias) {
        this.listaReferencias = listaReferencias;
    }
    
    public JsonArray toJSON(){
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (ReferenciaBibliografica ref: listaReferencias){
            arrayBuilder.add(ref.getJsonObjectBuilder());
        }
        
        return arrayBuilder.build();
    }
    
    @Override
    public String toString(){
        return toJSON().toString();
    }


}
