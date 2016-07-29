package com.djunior.eel418trabalho2.DTO;

import com.djunior.eel418trabalho2.utils.Utils;
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
    private String filePath;
    private String fileName;

    public ReferenciaBibliografica() {
        
    }
    
    public ReferenciaBibliografica(JsonObject obj) {
        if (obj.isNull("patrimonio"))
            serialno = 0;
        else {
            if (! obj.getString("patrimonio").equals(""))
                serialno = Long.parseLong(obj.getString("patrimonio"));
            else
                serialno = 0;
        }
        
        if (obj.isNull("titulo"))
            titulo = "";
        else
            titulo = obj.getString("titulo");
        
        if (obj.isNull("autoria"))
            autoria = "";
        else
            autoria = obj.getString("autoria");
        
        if (obj.isNull("palchave"))
            palchave = "";
        else
            palchave = obj.getString("palchave");
        
        if (obj.isNull("veiculo"))
            veiculo = "";
        else
            veiculo = obj.getString("veiculo");
        
        if (obj.isNull("dataPublicacao"))
            dataPublicacao = "";
        else {
            dataPublicacao = Utils.fixTimestamp(obj.getString("dataPublicacao"));
        }
        
        if (obj.isNull("filePath"))
            filePath = "";
        else
            filePath = obj.getString("filePath");
        
        if (obj.isNull("fileName"))
            fileName = "";
        else
            fileName = obj.getString("fileName");
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
        if (titulo == null)
            this.titulo = "";
        else
            this.titulo = titulo;
    }

    public String getAutoria() {
        return autoria;
    }

    public void setAutoria(String autoria) {
        if (autoria == null)
            this.autoria = "";
        else
            this.autoria = autoria;
    }

    public String getPalchave() {
        return palchave;
    }

    public void setPalchave(String palchave) {
        if (palchave == null)
            this.palchave = "";
        else
            this.palchave = palchave;
    }
    
    public void setVeiculo(String v) {
        if (v == null)
            this.veiculo = "";
        else
            this.veiculo = v;
    }
    
    public String getVeiculo() {
        return veiculo;
    }
    
    public void setDataPublicacao(String d) {
        if (d == null)
            dataPublicacao = "";
        else
            dataPublicacao = d;
    }
    
    public String getDataPublicacao() {
        return dataPublicacao;
    }
    
    public void setFilePath(String fp) {
        if (fp == null)
            filePath = "";
        else
            filePath = fp;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFileName(String fn) {
        if (fn == null)
            fileName = "";
        else
            fileName = fn;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public JsonObjectBuilder getJsonObjectBuilder() {
        return Json.createObjectBuilder()
                .add("serialno", serialno)
                .add("titulo", titulo)
                .add("autoria", autoria)
                .add("palchave", palchave)
                .add("veiculo", veiculo)
                .add("dataPublicacao", dataPublicacao)
                .add("filePath",filePath)
                .add("fileName",fileName);
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
