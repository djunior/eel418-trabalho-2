package com.djunior.eel418trabalho2.DTO;

import java.io.Serializable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ReferenciaBibliografica implements Serializable{
    private long serialno;
    private String titulo;
    private String autoria;
    private String palchave;

    public ReferenciaBibliografica() {
        
    }
    
    public ReferenciaBibliografica(JsonObject obj) {
        serialno = (long) obj.getInt("serialno");
        titulo = obj.getString("titulo");
        autoria = obj.getString("autoria");
        palchave = obj.getString("palchave");
    }
    
    public long getSerialno() {
        return serialno;
    }

    public void setSerialno(long serialno) {
        this.serialno = serialno;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutoria() {
        return autoria;
    }

    public void setAutoria(String autoria) {
        this.autoria = autoria;
    }

    public String getPalchave() {
        return palchave;
    }

    public void setPalchave(String palchave) {
        this.palchave = palchave;
    }
    
    public JsonObjectBuilder getJsonObjectBuilder() {
        return Json.createObjectBuilder()
                .add("serialno", serialno)
                .add("titulo", titulo)
                .add("autoria", autoria)
                .add("palchave", palchave);
    }
    
    public JsonObject toJSON(){
        JsonObject objetoJSON = getJsonObjectBuilder().build();
        return objetoJSON;
    }
    
    @Override
    public String toString() {
        return toJSON().toString();
    }
    

}
