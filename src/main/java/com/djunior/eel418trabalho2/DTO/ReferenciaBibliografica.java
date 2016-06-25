package com.djunior.eel418trabalho2.DTO;

import java.io.Serializable;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ReferenciaBibliografica implements Serializable{
    private long serialno;
    private String titulo;
    private String autoria;
    private String veiculo;
    private String dataPublicacao;
    private String palchave;

    public ReferenciaBibliografica() {
        
    }
    
    public ReferenciaBibliografica(JsonObject obj) {
        if (! obj.isNull("patrimonio")){
            if (! obj.getString("patrimonio").equals(""))
                serialno = Long.parseLong(obj.getString("patrimonio"));
            else
                serialno = 0;
        }
        
        if (! obj.isNull("titulo"))
            titulo = obj.getString("titulo");
        
        if (! obj.isNull("autoria"))
            autoria = obj.getString("autoria");
        
        if (! obj.isNull("palchave"))
            palchave = obj.getString("palchave");
        
        if (! obj.isNull("veiculo"))
            veiculo = obj.getString("veiculo");
        
        if (! obj.isNull("dataPublicacao"))
            dataPublicacao = obj.getString("dataPublicacao");
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
    
    public void setVeiculo(String v) {
        this.veiculo = v;
    }
    
    public String getVeiculo() {
        return veiculo;
    }
    
    public void setDataPublicacao(String d) {
        dataPublicacao = d;
    }
    
    public String getDataPublicacao() {
        return dataPublicacao;
    }
    
    public JsonObjectBuilder getJsonObjectBuilder() {
        return Json.createObjectBuilder()
                .add("serialno", serialno)
                .add("titulo", titulo)
                .add("autoria", autoria)
                .add("palchave", palchave)
                .add("veiculo", veiculo)
                .add("dataPublicacao", dataPublicacao);
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
