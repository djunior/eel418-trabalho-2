/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var resultadosBusca = [];
var resultadosBuscaIndex = 0;
var currentPage = 0;
var resultsPerPage = 10;

function doAjaxRequest(action,jsonObject,callback){
    console.log("doAjaxRequest called!");
    var stringJSON = JSON.stringify(jsonObject);
    var ajaxRequestObj = new XMLHttpRequest();
    ajaxRequestObj.open("POST", "controller?action="+action);
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

function ReferenciaBibliografica() {
    this.patrimonio = document.getElementById("idpatrimonio3").value;
    this.titulo = document.getElementById("idtitulo3").value;
    this.autoria = document.getElementById("idautoria3").value;
    this.veiculo = document.getElementById("idveiculo3").value;
    this.dataPublicacao = document.getElementById("iddatapublicacao3").value;
    this.palchave = document.getElementById("idpalchave3").value;
};

function SearchQuery() {
    var query = {};
    if (document.getElementById("idcheckpatrimonio").checked == true) {
        query.patrimonio = {
            value: document.getElementById("idpatrimonio2").value,
            operation: "and"
        };
    } else {
        if (document.getElementById("idchecktituloOU").checked || document.getElementById("idchecktituloE").checked) {
            query.titulo = {
                value: document.getElementById("idtitulo2").value,
                operation: (document.getElementById("idchecktituloOU").checked == true ? "or" : "and")
            };
        }
        if (document.getElementById("idcheckautoriaOU").checked || document.getElementById("idcheckautoriaE").checked) {
            query.autoria = {
                value: document.getElementById("idautoria2").value,
                operation: (document.getElementById("idcheckautoriaOU").checked == true ? "or" : "and")
            };
        }
        if (document.getElementById("idcheckveiculoOU").checked || document.getElementById("idcheckveiculoE").checked) {
            query.veiculo = {
                value: document.getElementById("idveiculo2").value,
                operation: (document.getElementById("idcheckveiculoOU").checked == true ? "or" : "and")
            };
        }
        if (document.getElementById("idcheckpalchaveOU").checked || document.getElementById("idcheckpalchaveE").checked) {
            query.palchave = {
                value: document.getElementById("idpalchave2").value,
                operation: (document.getElementById("idcheckpalchaveOU").checked == true ? "or" : "and")
            };
        }
        if (document.getElementById("idcheckdatapublicacaoOU").checked || document.getElementById("idcheckdatapublicacaoE").checked) {
            query.dataInicial = {
                value: document.getElementById("iddatapublicacao21").value,
                operation: (document.getElementById("idcheckdatapublicacaoOU").checked == true ? "or" : "and")
            };
            query.dataFinal = {
                value: document.getElementById("iddatapublicacao22").value,
                operation: (document.getElementById("idcheckdatapublicacaoOU").checked == true ? "or" : "and")
            };
        }
    }
    return query;
};

var setNumberOfResults = function(n) {
    document.getElementById("idNroRows").style.display = "initial";
    document.getElementById("idNroRows").textContent = n;
};

var setCatalogMessage = function (m) {
  document.getElementById("idMsgDialogo3").textContent = m;  
};

var setSearchMessage = function (m) {
  document.getElementById("idMsgDialogo2").textContent = m;  
};

var clearResultList = function() {
    resultadosBusca = [];
    var tabelaResultados = document.getElementById("idTabelaResultados");
    while (tabelaResultados.firstChild) {
        tabelaResultados.removeChild(tabelaResultados.firstChild);
    }
};

var showResultsForPage = function (index) {
    console.log("1");
    var aux = resultadosBusca;
    clearResultList();
    resultadosBusca = aux;
    
    currentPage = index;
    document.getElementById("idPaginaDestino").value = index + 1;
    console.log("2");
    tabelaResultados = document.getElementById("idTabelaResultados");
    tabelaResultados.style.display = resultadosBusca.length > 0 ? "table" :  "none";
    
    console.log("Resultados busca length: " + resultadosBusca.length);
    var initialIndex = currentPage * resultsPerPage;
    var finalIndex = (currentPage+1) * resultsPerPage;
    if (finalIndex > resultadosBusca.length) finalIndex = resultadosBusca.length;
    console.log("4");
    console.log("initialIndex: " + initialIndex);
    console.log("finalIndex: " + finalIndex);
    for (i = initialIndex; i < finalIndex; i++) {
        var ref = resultadosBusca[i];
        console.log("Adicionando link para a entrada: "+ref.titulo + " - " + ref.autoria);
        var link = document.createElement("a");
        link.setAttribute("class","result");
        link.setAttribute("href","#");
        link.setAttribute("onclick","loadCatalogForReference(" + i + ");");
        
        var node = document.createTextNode(ref.titulo + " - " + ref.autoria);
        link.appendChild(node);
        
        link.appendChild(document.createElement("br"));
        
        tabelaResultados.appendChild(link);
    }
    console.log("5");
};

var showPreviousPage = function () {
    if (currentPage > 0)
        showResultsForPage(currentPage -1);
};

var showNextPage = function () {
    var lastPage = Math.ceil(resultadosBusca.length / resultsPerPage) - 1;
    console.log("lastPage: " + lastPage);
    if (currentPage < lastPage)
        showResultsForPage(currentPage + 1);  
};

var onSearchResult = function(result) {
    console.log("onSearchResult called");
    console.log(result.resultList.length);
    
    resultadosBusca = Array.from(result.resultList);
    
    showResultsForPage(0);
    
    setNumberOfResults(resultadosBusca.length);
    setSearchMessage(result.message);
};

var clearSearch = function () {
    
    document.getElementById("idpatrimonio2").value = "";
    document.getElementById("idtitulo2").value = "";
    document.getElementById("idautoria2").value = "";
    document.getElementById("idveiculo2").value = "";
    document.getElementById("iddatapublicacao21").value = "";
    document.getElementById("iddatapublicacao22").value = "";
    document.getElementById("idpalchave2").value = "";
    
    setNumberOfResults(0);
    clearResultList();
    
    document.getElementById("idTabelaResultados").style.display = "none";
    document.getElementById("idNroRows").style.display = "none";
};

var clearCatalog = function () {
    document.getElementById("idpatrimonio3").value = "";
    document.getElementById("idtitulo3").value = "";
    document.getElementById("idautoria3").value = "";
    document.getElementById("idveiculo3").value = "";
    document.getElementById("iddatapublicacao3").value = "";
    document.getElementById("idpalchave3").value = "";
};

var search = function () {
    console.log("search called!");
    console.log("creating requestObj");
    var requestObj = SearchQuery();
    console.log("calling doAjaxRequest");
    doAjaxRequest("search",requestObj,onSearchResult);
};

var edit = function () {
    document.getElementById("idtitulo3").readOnly = false;
    document.getElementById("idautoria3").readOnly = false;
    document.getElementById("idveiculo3").readOnly = false;
    document.getElementById("iddatapublicacao3").readOnly = false;
    document.getElementById("idpalchave3").readOnly = false;
};

var onCreateResult = function (result) {
    document.getElementById("idpatrimonio3").value = result.patrimonio;
    setCatalogMessage(result.message);
};

var create = function () {
    var requestObj = new ReferenciaBibliografica();
    doAjaxRequest("create",requestObj,onCreateResult);
};

var onRemoveResult = function (result) {
    setCatalogMessage(result.message);
    clearCatalog();
};

var remove = function () {
    var requestObj = new ReferenciaBibliografica();
    doAjaxRequest("remove",requestObj,onRemoveResult);
};

var onUpdateResult = function(result) {
  setCatalogMessage(result.message);
};

var update = function () {
  var requestObj = new ReferenciaBibliografica();
  doAjaxRequest("update",requestObj,onUpdateResult);
};

var loadCatalogForReference = function (index) {
    var ref = resultadosBusca[index];
    resultadosBuscaIndex = index;
    
    document.getElementById("idpatrimonio3").value = ref.serialno;
    document.getElementById("idtitulo3").value = ref.titulo;
    document.getElementById("idautoria3").value = ref.autoria;
    document.getElementById("idveiculo3").value = ref.veiculo;
    document.getElementById("iddatapublicacao3").value = ref.dataPublicacao;
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
    
    if (resultadosBusca.length > 0) {
        document.getElementById("idTabelaResultados").style.display = "table";
        document.getElementById("idNroRows").style.display = "initial";   
    } else {
        document.getElementById("idTabelaResultados").style.display = "none";
        document.getElementById("idNroRows").style.display = "none";
    }
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

var showNextSearchResult = function () {
    if (resultadosBuscaIndex < (resultadosBusca.length-1))  
      loadCatalogForReference(resultadosBuscaIndex + 1);
};

var showPreviousSearchResult = function () {
    if (resultadosBuscaIndex > 0)
        loadCatalogForReference(resultadosBuscaIndex-1);
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
    document.getElementById("idLimparBusca").onclick = clearSearch;
    document.getElementById("idSalvarNovo").onclick = create;
    document.getElementById("idExcluir").onclick = remove;
    document.getElementById("idSalvarAtual").onclick = update;
    document.getElementById("idLimparCat").onclick = clearCatalog;
    document.getElementById("idItemAnterior").onclick = showPreviousSearchResult;
    document.getElementById("idItemProximo").onclick = showNextSearchResult;
    document.getElementById("idPagAnterior").onclick = showPreviousPage;
    document.getElementById("idPagProxima").onclick = showNextPage;
    
    var ControleDeChecks = function () {
        this.patrimonio = document.getElementById('idcheckpatrimonio');
        this.tituloOU = document.getElementById('idchecktituloOU');
        this.tituloE = document.getElementById('idchecktituloE');
        this.autoriaOU = document.getElementById('idcheckautoriaOU');
        this.autoriaE = document.getElementById('idcheckautoriaE');
        this.veiculoOU = document.getElementById('idcheckveiculoOU');
        this.veiculoE = document.getElementById('idcheckveiculoE');
        this.palchaveE = document.getElementById('idcheckpalchaveE');
        this.palchaveOU = document.getElementById('idcheckpalchaveOU');
        this.dataPublicacaoE = document.getElementById('idcheckdatapublicacaoE');
        this.dataPublicacaoOU = document.getElementById('idcheckdatapublicacaoOU');
    };
    
    var controle = new ControleDeChecks();
    controle.patrimonio.onclick = function () {
        controle.tituloE.checked = false;
        controle.tituloOU.checked = false;
        controle.autoriaE.checked = false;
        controle.autoriaOU.checked = false;
        controle.veiculoE.checked = false;
        controle.veiculoOU.checked = false;
        controle.palchaveE.checked = false;
        controle.palchaveOU.checked = false;
        controle.dataPublicacaoE.checked = false;
        controle.dataPublicacaoOU.checked = false;
    };
    
    controle.tituloOU.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.tituloOU.checked) controle.tituloE.checked = false;
    };
    
    controle.tituloE.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.tituloE.checked) controle.tituloOU.checked = false;
    };
    
    controle.autoriaOU.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.autoriaOU.checked) controle.autoriaE.checked = false;
    };
    
    controle.autoriaE.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.autoriaE.checked) controle.autoriaOU.checked = false;
    };
    
    controle.veiculoOU.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.veiculoOU.checked) controle.veiculoE.checked = false;
    };
    
    controle.veiculoE.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.veiculoE.checked) controle.veiculoOU.checked = false;
    };
    
    controle.dataPublicacaoOU.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.dataPublicacaoOU.checked) controle.dataPublicacaoE.checked = false;
    };
    
    controle.dataPublicacaoE.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.dataPublicacaoE.checked) controle.dataPublicacaoOU.checked = false;
    };
    
    controle.palchaveE.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.palchaveE.checked) controle.palchaveOU.checked = false;
    };
    
    controle.palchaveOU.onclick = function () {
        controle.patrimonio.checked = false;
        if (controle.palchaveOU.checked) controle.palchaveE.checked = false;
    };
    
    setNumberOfResults(0);
};

window.onload(onload);
