/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2.DTO;

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author davidbrittojr
 */
public class SearchResponseMessage extends ResponseMessage {
    private ListaDeReferencias listRef;
    
    public void setListaDeReferencias(ListaDeReferencias l){
        listRef = l;
    }
    
    public ListaDeReferencias getListaDeReferencias() {
        return listRef;
    }
    
    public JsonObject toJSON() {
        return getJsonObjectBuilder()
                .add("resultList",listRef.toJSON())
                .build();
    }
    
    public String toString() {
        return toJSON().toString();
    }
}
