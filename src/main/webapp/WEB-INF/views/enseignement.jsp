<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <%@include file="imports.jspf" %>
        <title><fmt:message key="addEnseignement"/></title>
        <link href="css/enseignements.css" type="text/css" rel="stylesheet" />
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <h1><fmt:message key="addEnseignement"/></h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="saveenseignement.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col"><fmt:message key="enseignementAcronyme"/></th>
                                        <td>
                                            <label class="hidden-label" for="Acronyme">
                                                <fmt:message key="enseignementAcronyme"/>
                                            </label>
                                            <c:choose>
                                                <c:when test="${(empty enseignement) || (empty enseignement.acronyme)}"><input id="Acronyme" name="Acronyme" value=""/></c:when>
                                                <c:otherwise><input id="Acronyme" type="text" class="form-control" name="Acronyme" value="${enseignement.acronyme}"/></c:otherwise>
                                            </c:choose>
                                        </td>

                                    <tr>
                                        <th scope="col"><fmt:message key="enseignementName"/></th>
                                        <td>
                                            <label class="hidden-label" for="NomEnseignement">
                                                <fmt:message key="enseignementName"/></label>
                                            <input type="text" class="form-control" name="NomEnseignement" id="NomEnseignement" value="${enseignement.nomEnseignement}"/>

                                        </td>
                                    </tr>
                                    <tr>
                                        <th scope="col"><fmt:message key="programme"/></th>
                                        <td>
                                            <label class="hidden-label" for="Filiere">
                                                <fmt:message key="programme"/>
                                            </label>
                                            <input type="text" class="form-control" name="Filiere" id="Filiere" value="${enseignement.filiere}"/>

                                        </td>
                                    </tr>
                                    <tr>
                                        <th scope="col"><fmt:message key="manager"/></th>
                                        <td colspan="2">
                                            <label class="hidden-label" for="AcronymeEnseignement">
                                                <fmt:message key="manager"/></label>
                                            <input type="hidden" name="AcronymeEnseignement" id="AcronymeEnseignement" value="${enseignement.acronyme}" />
                                            <select name="InitialesEnseignant" required
                                                    class="form-control form-select form-select-lg mb-3" id="AcronymeEnseignement">
                                                <c:choose>
                                                    <c:when test="${(empty enseignement) || (empty enseignement.responsable)}"><option value="" disabled selected><fmt:message key="chooseEnseignant"/></option></c:when>
                                                    <c:otherwise><option value="${enseignement.responsable.initiales}" disabled selected>${enseignement.responsable.nomEnseignant} ${enseignement.responsable.prenom}</option></c:otherwise>
                                                </c:choose>


                                                <c:forEach var="enseignant" items="${enseignantsList}">
                                                    <option value="${enseignant.initiales}">${enseignant.nomEnseignant} ${enseignant.prenom}</option>
                                                </c:forEach>
                                            </select>

                                        </td>
                                    </tr>
                                    <tr>
                                        <th scope="col">Enseignant(s)</th>
                                        <td>
                                            <div class="container">
                                                <div class="row">
                                                    <div class="col-md-5">
                                                        <div id="listenseignants" 
                                                             class="list-group overflow-auto border border-primary" 
                                                             style="height:350px;overflow-y: scroll;" 
                                                             tabindex="0" 
                                                             ondrop="dropAndRename(event, 'el', 'e')" 
                                                             ondragover="allowDrop(event)">
                                                            <c:forEach var="itemIter" items="${seance.enseignantList}">
                                                                <div class="list-group-item list-group-item-action" id="e_${itemIter.initiales}" draggable="true" ondragstart="drag(event)">
                                                                    <input type="hidden" name="e[${itemIter.initiales.initiales}]" value="${itemIter.initiales.initiales}"/>
                                                                    ${itemIter.initiales.nomEnseignant} ${itemIter.initiales.prenom}
                                                                </div>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-5">
                                                        <div id="listAllenseignants" 
                                                             class="list-group overflow-auto border border-primary" 
                                                             style="height:350px;overflow-y: scroll;" 
                                                             tabindex="0" 
                                                             ondrop="dropAndRename(event, 'e', 'el')" 
                                                             ondragover="allowDrop(event)">
                                                            <c:forEach var="enseignant" items="${enseignantsList}">
                                                                <div class="list-group-item list-group-item-action" id="el_${enseignant.initiales}" draggable="true" ondragstart="drag(event)" style="display:block">
                                                                    <input type="hidden" name="el[${enseignant.initiales}]" value="${enseignant.initiales}"/>
                                                                    ${enseignant.nomEnseignant} ${enseignant.prenom}
                                                                </div>
                                                            </c:forEach>
                                                        </div>
                                                        <div onkeyup="filterList('listAllenseignants', 'listAllenseignantsFilter')" >
                                                            <input type="text" id="listAllenseignantsFilter" placeholder="Rechercher.." title="Type in a name" style="width:100%">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    

                                <c:forEach var="typeLecon" items="${typeLeconsList}">
                                    <tr>
                                        <th scope="col">Nombre de minutes de ${typeLecon.intitule}</th>
                                        <td><input type="int" class="form-control" name="${typeLecon.intitule}"/></td>
                                    </tr>
                                    <tr>
                                        <th scope="col">Salle demandée pour les ${typeLecon.intitule}</th>
                                        <td>
                                            <select name="salle[${typeLecon.intitule}]" required
                                                    class="form-control form-select form-select-lg mb-3">
                                                <option value="" disabled selected>Choisissez une salle</option>

                                                <c:forEach var="salle" items="${sallesList}">
                                                    <option value="${salle.getNumeroSalle()}">${salle.getNumeroSalle()}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td scope="col" colspan="2" class="text-center">
                                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                                            <button type="submit" class="btn btn-block btn-primary">Save</button>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <c:if test='${newenseignement}'>
                            <p><fmt:message key="enseignementSaved"/></p>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="enseignements.do" method="POST">
                            <button formaction="enseignements.do"><fmt:message key="backToEnseignements"/></button>
                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="enseignements.do" method="POST">
                            <button formaction="listenseignements.do">Importer une liste d'enseignements</button>
                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>