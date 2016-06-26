/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.djunior.eel418trabalho2.DTO;

import com.djunior.eel418trabalho2.utils.Utils;
import javax.json.JsonObject;

/**
 *
 * @author davidbrittojr
 */
public class SearchQueryPair {
    private String value;
    private Boolean active = false;
    private String operation;
    
    public SearchQueryPair() {
        
    }
    
    public SearchQueryPair(JsonObject obj) {
        value = obj.getString("value");
        operation = obj.getString("operation");
        active = true;
    }
    
    public SearchQueryPair(Boolean a){
        active = a;
    }
    
    public void setActive(Boolean a) {
        active = a;
    }
    
    public Boolean isActive() {
        return active;
    }
    
    public void setOperation(String o) {
        if (o.equals("and") || o.equals("or"))
            operation = o;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getCleanValue() {
        return Utils.removeDiacriticals(value);
    }
    
    public void setValue(String v) {
        value = v;
    }
}