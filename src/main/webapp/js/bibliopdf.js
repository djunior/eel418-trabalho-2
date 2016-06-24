/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var resultadosBusca = [];

function doAjaxRequest(jsonObject,callback){
    console.log("doAjaxRequest called!");
    var stringJSON = JSON.stringify(jsonObject);
    var ajaxRequestObj = new XMLHttpRequest();
    ajaxRequestObj.open("POST", "controller");
    ajaxRequestObj.setRequestHeader("Content-Type","application/json;charset=UTF-8");
    
    // Prepara recebimento da resposta: tipo da resposta JSON!
    ajaxRequestObj.responseType = 'json';
    ajaxRequestObj.onreadystatechange =
        function() {
            if(ajaxRequestObj.readyState===4 && ajaxRequestObj.status===200){
                // A resposta 'response' já é avaliada para json!
                // Ao contraro da resposta responseText.
                callback(ajaxRequestObj.response);
            };
        };
        
    // Envio do pedido
    console.log("Calling ajaxRequestObj.send");
    ajaxRequestObj.send(stringJSON);
};

var setNumberOfResults = function(n) {
    document.getElementById("idNroRows").textContent = n;
};

var clearResultList = function() {
    resultadosBusca = [];
    var tabelaResultados = document.getElementById("idTabelaResultados");
    while (tabelaResultados.firstChild) {
        tabelaResultados.removeChild(tabelaResultados.firstChild);
    }
};

var onSearchResult = function(resultList) {
    resultadosBusca = resultList;
    tabelaResultados = document.getElementById("idTabelaResultados");
    
    clearResultList();
    
    for (i = 0; i < resultList.length; i++) {
        var ref = resultList[i];
        var link = document.createElement("a");
        link.setAttribute("class","result");
        link.setAttribute("href","#");
        link.setAttribute("onclick","loadCatalogForReference(" + i + ");");
        
        var node = document.createTextNode(ref.titulo + " - " + ref.autoria);
        link.appendChild(node);
        
        link.appendChild(document.createElement("br"));
        
        tabelaResultados.appendChild(link);
    }
    
    setNumberOfResults(resultList.length);
};

var clear = function () {
    
    document.getElementById("idpatrimonio2").value = "";
    document.getElementById("idtitulo2").value = "";
    document.getElementById("idautoria2").value = "";
    document.getElementById("idveiculo2").value = "";
    document.getElementById("iddatapublicacao21").value = "";
    document.getElementById("iddatapublicacao22").value = "";
    document.getElementById("idpalchave2").value = "";
    
    setNumberOfResults(0);
    clearResultList();
};

var search = function () {
    console.log("search called!");
    console.log("creating requestObj");
    var requestObj = {
        patrimonio: document.getElementById("idpatrimonio2").value,
        titulo: document.getElementById("idtitulo2").value,
        autoria: document.getElementById("idautoria2").value,
        veiculo: document.getElementById("idveiculo2").value,
        dataPublicacaoInicio: document.getElementById("iddatapublicacao21").value,
        dataPublicacaoFim: document.getElementById("iddatapublicacao22").value,
        palChave: document.getElementById("idpalchave2").value
    };
    console.log("calling doAjaxRequest");
    doAjaxRequest(requestObj,onSearchResult);
};

var edit = function () {
    document.getElementById("idpatrimonio3").readOnly = false;
    document.getElementById("idtitulo3").readOnly = false;
    document.getElementById("idautoria3").readOnly = false;
    document.getElementById("idveiculo3").readOnly = false;
    document.getElementById("iddatapublicacao3").readOnly = false;
    document.getElementById("idpalchave3").readOnly = false;
};

var loadCatalogForReference = function (index) {
    var ref = resultadosBusca[index];
    
    document.getElementById("idpatrimonio3").value = ref.serialno;
    document.getElementById("idtitulo3").value = ref.titulo;
    document.getElementById("idautoria3").value = ref.autoria;
//    document.getElementById("idveiculo3").value = ref.veiculo;
//    document.getElementById("iddatapublicacao3").value = ref.titulo;
    document.getElementById("idpalchave3").value = ref.palchave;
    
    goToCatalog();
};

var goToSearch = function () {
    document.getElementById("menuCatalogacao").style.display = "initial";
    document.getElementById("menuSair").style.display = "initial";
    document.getElementById("menuEntrar").style.display = "none";
    document.getElementById("menuBusca").style.display = "none";
    
    document.getElementById("idDivBusca").style.display = "initial";
    document.getElementById("idDivCatalogacao").style.display = "none";
    document.getElementById("idDivLogin").style.display = "none";
};

var goToCatalog = function (){
    document.getElementById("menuCatalogacao").style.display = "none";
    document.getElementById("menuSair").style.display = "initial";
    document.getElementById("menuEntrar").style.display = "none";
    document.getElementById("menuBusca").style.display = "initial";
    
    document.getElementById("idDivBusca").style.display = "none";
    document.getElementById("idDivCatalogacao").style.display = "initial";
    document.getElementById("idDivLogin").style.display = "none";
    
    document.getElementById("idpatrimonio3").readOnly = true;
    document.getElementById("idtitulo3").readOnly = true;
    document.getElementById("idautoria3").readOnly = true;
    document.getElementById("idveiculo3").readOnly = true;
    document.getElementById("iddatapublicacao3").readOnly = true;
    document.getElementById("idpalchave3").readOnly = true;
};

var logout = function () {
    document.getElementById("menuEntrar").style.display = "initial";
    document.getElementById("menuCatalogacao").style.display = "none";
    document.getElementById("menuBusca").style.display = "none";
    document.getElementById("menuSair").style.display = "none";
    
    document.getElementById("idDivLogin").style.display = "initial";
    document.getElementById("idDivBusca").style.display = "none";
    document.getElementById("idDivCatalogacao").style.display = "none";
};

var onload = function () {
    document.getElementById("menuCatalogacao").style.display = "none";
    document.getElementById("menuBusca").style.display = "none";
    document.getElementById("menuSair").style.display = "none";
    
    document.getElementById("idDivBusca").style.display = "none";
    document.getElementById("idDivCatalogacao").style.display = "none";
    
    document.getElementById("menuEntrar").onclick = goToSearch;
    document.getElementById("menuBusca").onclick = goToSearch;
    document.getElementById("menuSair").onclick = logout;
    document.getElementById("menuCatalogacao").onclick = goToCatalog;
    document.getElementById("idBuscar").onclick = search;
    document.getElementById("idEditar").onclick = edit;
    document.getElementById("idLimparBusca").onclick = clear;
    
    setNumberOfResults(0);
};

window.onload(onload);
