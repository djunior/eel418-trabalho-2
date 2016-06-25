/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2.DTO;

import java.io.Serializable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author davidbrittojr
 */
public class ResponseMessage implements Serializable{
    private Boolean success;
    private String message;
    
    public ResponseMessage() {
        
    }
    
    public ResponseMessage(Boolean s, String m){
        success = s;
        message = m;
    }
    
    public void setSuccess(Boolean b){
        success = b;
    }
    
    public Boolean getSuccess(){
        return success;
    }
    
    public void setMessage(String m){
        message = m;
    }
    
    public String getMessage() {
        return message;
    }
    
    public JsonObjectBuilder getJsonObjectBuilder(){
        return Json.createObjectBuilder()
                .add("success",success)
                .add("message",message);
    }
    
    public JsonObject toJSON() {
        return getJsonObjectBuilder().build();
    }
    
    @Override
    public String toString(){
        return toJSON().toString();
    }
}
