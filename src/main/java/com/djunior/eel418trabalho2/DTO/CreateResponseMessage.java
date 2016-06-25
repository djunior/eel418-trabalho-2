/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2.DTO;

import javax.json.JsonObject;

/**
 *
 * @author davidbrittojr
 */
public class CreateResponseMessage extends ResponseMessage{
    private long patrimonio;
    
    public CreateResponseMessage() {
        
    }
    
    public CreateResponseMessage(Boolean s, String m){
        super(s,m);
    }
    
    public CreateResponseMessage(Boolean s, String m, Long p) {
        super(s,m);
        patrimonio = p;
    }
    
    public void setPatrimonio(Long p){
        patrimonio = p;
    }
    
    public Long getPatrimonio() {
        return patrimonio;
    }
    
    @Override
    public JsonObject toJSON() {
        return getJsonObjectBuilder()
                .add("patrimonio",patrimonio)
                .build();
    }
    
    @Override
    public String toString() {
        return toJSON().toString();
    }
}
