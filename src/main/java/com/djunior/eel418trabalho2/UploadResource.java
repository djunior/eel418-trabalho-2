package com.djunior.eel418trabalho2;

import com.djunior.eel418trabalho2.DAO.CatalogDAO;
import com.djunior.eel418trabalho2.DTO.ReferenciaBibliografica;
import com.djunior.eel418trabalho2.DTO.ResponseMessage;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("upload")
public class UploadResource {

    @Context
    private UriInfo context;

//--------------------------------------------------------
    public UploadResource() {
    }

//--------------------------------------------------------
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String postFazerUpload(@Context HttpServletRequest request) {
        System.out.println("UpdateResources::postFazerUpload called!");
        String filePath = "";
        String nomeArq = "";
        Part part = null;
        ResponseMessage resposta;
        try {
            System.out.println("Calling request.getParts()");
            ArrayList parts = (ArrayList) request.getParts();
            System.out.println("Getting request parameter");
            //--- DEPOIS DE ***getParts()***, pode usar o request.getParameter() ---
            System.out.println("--- nome_1: "+request.getParameter("upload_patrimonio"));
            //---
            String patrimonio = request.getParameter("upload_patrimonio");
            
            if (patrimonio.equals("")) {
                resposta = new ResponseMessage(false,"Erro! Crie ou selecione uma obra antes de enviar um arquivo para o servidor!");
                return resposta.toString();
            }
            
            Iterator itr = parts.iterator();
            while (itr.hasNext()) {
                part = (Part) itr.next();
                if (part.getName().compareTo("fileUpload") == 0) {
                    nomeArq = extractFileName(part);
                    filePath = obterCaminhoPastaWeb() + patrimonio;
                    part.write(filePath);
                }
            }
            
            try {
                CatalogDAO catalog = new CatalogDAO();

                ReferenciaBibliografica r = new ReferenciaBibliografica();
                r.setSerialno(new Long(patrimonio));
                r.setFilePath(filePath);
                r.setFileName(nomeArq);

                catalog.saveUploadFileData(r);

                resposta = new ResponseMessage(true,"Arquivo \"" + nomeArq + "\" enviado com sucesso!");
            } catch (SQLException e) {
                e.printStackTrace();
                resposta = new ResponseMessage(false,"Falha na comunicação com o banco de dados.");
            }
            return resposta.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        resposta = new ResponseMessage(false, "Falha ao enviar o arquivo \"" + nomeArq + "\" para o servidor." );
        return resposta.toString();
    }
//--------------------------------------------------------
    private String obterCaminhoPastaWeb(){
        Class esteServlet = getClass();
        ClassLoader cl = esteServlet.getClassLoader();
        URL url = cl.getResource("../../arquivos/");
        String caminhoDosArquivos = url.getPath();
        System.out.println("caminhoDosArquivos = " + caminhoDosArquivos);
        caminhoDosArquivos = caminhoDosArquivos.replace("%20"," ");
        return caminhoDosArquivos;
    }
//--------------------------------------------------------
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
//==============================================================================
}
