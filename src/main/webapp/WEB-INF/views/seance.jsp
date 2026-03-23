<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <%@include file="imports.jspf" %>
        <title><fmt:message key="addSeance"/></title>
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5" id="main-content">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <h1><fmt:message key="addSeance"/></h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="saveseance.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <c:choose>
                                        <c:when test="${(empty seance) || (empty seance.idSeance)}">
                                        <input name="IdSeance" value="-1" type="hidden"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" class="form-control" name="IdSeance" value="${seance.idSeance}"/>
                                    </c:otherwise>
                                </c:choose>
                                <tr>
                                    <th scope="col"><fmt:message key="start"/></th>
                                    <td>
                                        <label class="hidden-label" for="HDebut">
                                            <fmt:message key="start"/></label>
                                            <input type="datetime-local" class="form-control" name="HDebut" id="HDebut" value="<fmt:formatDate value="${seance.HDebut}" pattern="yyyy-MM-dd'T'HH:mm" />"/></td>
                                </tr>
                                <tr>
                                    <th scope="col"><fmt:message key="duration"/><fmt:message key="minutes"/></th>
                                    <td>
                                        <label class="hidden-label" for="Duree">
                                            <fmt:message key="duration"/><fmt:message key="minutes"/></label>
                                        <input type="int" class="form-control" name="Duree" id="Duree" value="${seance.duree}"/></td>
                                </tr>
                                <tr>
                                    <th scope="col"><fmt:message key="enseignement"/></th>
                                    <td colspan="2">
                                        <label class="hidden-label" for="IdSeance">
                                        <fmt:message key="enseignement"/></label>
                                        <input type="hidden" name="IdSeance" id="IdSeance" value="${seance.idSeance}" />
                                        <select name="Enseignement" id="IdSeance" required
                                                class="form-control form-select form-select-lg mb-3">
                                            <c:choose>
                                                <c:when test="${(empty seance) || (empty seance.acronyme)}"><option value="" disabled selected><fmt:message key="chooseEnseignement"/></option></c:when>
                                                <c:otherwise><option value="${seance.acronyme.acronyme}">${seance.acronyme.nomEnseignement}</option></c:otherwise>
                                            </c:choose>

                                            <c:forEach var="enseignement" items="${enseignementsList}">
                                                <option value="${enseignement.acronyme}">${enseignement.nomEnseignement}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="col"><fmt:message key="courseType"/></th>
                                    <td colspan="2">
                                        <label class="hidden-label" for="IdSeance">
                                        <fmt:message key="courseType"/></label>
                                        <input type="hidden" name="IdSeance" id="IdSeance" value="${seance.idSeance}" />
                                        <select name="TypeLecon" id="IdSeance" required
                                                class="form-control form-select form-select-lg mb-3">
                                            <c:choose>
                                                <c:when test="${(empty seance) || (empty seance.intitule)}"><option value="" disabled selected><fmt:message key="chooseCourseType"/></option></c:when>
                                                <c:otherwise><option value="${seance.intitule.intitule}">${seance.intitule.intitule}</option></c:otherwise>
                                            </c:choose>

                                            <c:forEach var="typeLecon" items="${typeLeconsList}">
                                                <option value="${typeLecon.intitule}">${typeLecon.intitule}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="col"><fmt:message key="group_s"/></th>
                                    <td>
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-md-5">
                                                    <div id="listgroupes" 
                                                         class="list-group overflow-auto border border-primary" 
                                                         style="height:350px;overflow-y: scroll;" 
                                                         tabindex="0" 
                                                         ondrop="dropAndRename(event, 'ml', 'm')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="itemIter" items="${seance.groupeList}">
                                                            <div class="list-group-item list-group-item-action" id="m_${itemIter.nomGroupe}" draggable="true" ondragstart="drag(event)">
                                                                <input type="hidden" name="m[${itemIter.nomGroupe}]" value="${itemIter.nomGroupe}"/>
                                                                ${itemIter.nomGroupe}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <div class="col-md-5">
                                                    <div id="listAllgroupes" 
                                                         class="list-group overflow-auto border border-primary" 
                                                         style="height:350px;overflow-y: scroll;" 
                                                         tabindex="0" 
                                                         ondrop="dropAndRename(event, 'm', 'ml')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="groupe" items="${groupesList}">
                                                            <div class="list-group-item list-group-item-action" id="ml_${groupe.nomGroupe}" draggable="true" ondragstart="drag(event)" style="display:block">
                                                                <input type="hidden" name="ml[${groupe.nomGroupe}]" value="${groupe.nomGroupe}"/>
                                                                ${groupe.nomGroupe}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div onkeyup="filterList('listAllgroupes', 'listAllgroupesFilter')" >
                                                        <input type="text" id="listAllgroupesFilter" placeholder="Rechercher.." title="Type in a name" style="width:100%">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="col"><fmt:message key="enseignant_s"/></th>
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
                                                                <input type="hidden" name="e[${itemIter.initiales}]" value="${itemIter.initiales}"/>
                                                                ${itemIter.nomEnseignant} ${itemIter.prenom}
                                                            </div> 
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <div class="col-md-5">
                                                    <div id="listAllenseignants" 
                                                         class="list-group overflow-auto border border-primary" 
                                                         style="height:350px;overflow-y: scroll;" 
                                                         tabindex="0" 
                                                         ondrop="dropAndRename(event, 'e','el')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="enseignant" items="${enseignantsList}">
                                                            <div class="list-group-item list-group-item-action" id="el_${enseignant.initiales}" draggable="true" ondragstart="drag(event)" style="display:block">
                                                                <input type="hidden" name="el[${enseignant.initiales}]" value="${enseignant.initiales}"/>
                                                                ${enseignant.nomEnseignant} ${enseignant.prenom}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div onkeyup="filterList('listAllenseignants', 'listAllenseignantsFilter')" >
                                                        <input type="text" id="listAllenseignantsFilter" placeholder="<fmt:message key="search"/>" title="Type in a name" style="width:100%">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="col"><fmt:message key="room_s"/></th>
                                    <td>
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-md-5">
                                                    <div id="listsalles" 
                                                         class="list-group overflow-auto border border-primary" 
                                                         style="height:350px;overflow-y: scroll;" 
                                                         tabindex="0" 
                                                         ondrop="dropAndRename(event, 'sl', 's')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="itemIter" items="${seance.salleList}">
                                                            <div class="list-group-item list-group-item-action" id="s_${itemIter.numeroSalle}" draggable="true" ondragstart="drag(event)">
                                                                <input type="hidden" name="s[${itemIter.numeroSalle}]" value="${itemIter.numeroSalle}"/>
                                                                ${itemIter.numeroSalle}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <div class="col-md-5">
                                                    <div id="listAllsalles" 
                                                         class="list-group overflow-auto border border-primary" 
                                                         style="height:350px;overflow-y: scroll;" 
                                                         tabindex="0" 
                                                         ondrop="dropAndRename(event,'s', 'sl')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="salle" items="${sallesList}">
                                                            <div class="list-group-item list-group-item-action" id="sl_${salle.numeroSalle}" draggable="true" ondragstart="drag(event)" style="display:block">
                                                                <input type="hidden" name="sl[${salle.numeroSalle}]" value="${salle.numeroSalle}"/>
                                                                ${salle.numeroSalle}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div onkeyup="filterList('listAllsalles', 'listAllsallesFilter')" >
                                                        <input type="text" id="listAllsallesFilter" placeholder="<fmt:message key="search"/>" title="Type in a name" style="width:100%">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td scope="col" colspan="2" class="text-center">
                                            
                                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                                            <button type="submit" class="btn btn-block btn-primary"><fmt:message key="save"/></button>
                                        
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">

                        <%-- Succès --%>
                        <c:if test='${newseance}'>
                            <div class="alert alert-success"><fmt:message key="seanceSaved"/></div>
                        </c:if>

                        <%-- Erreur bloquante (conflit enseignant ou groupe) --%>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger">${error}</div>
                        </c:if>

                        <%-- Warnings salles (non bloquant) --%>
                        <c:if test="${not empty warningsSalles}">
                            <c:forEach var="w" items="${warningsSalles}">
                                <div class="alert alert-warning">${w}</div>
                            </c:forEach>
                        </c:if>

                        <%-- Warnings maquette (non bloquant) --%>
                        <c:if test="${not empty warningsMaquette}">
                            <c:forEach var="w" items="${warningsMaquette}">
                                <div class="alert alert-info">${w}</div>
                            </c:forEach>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
