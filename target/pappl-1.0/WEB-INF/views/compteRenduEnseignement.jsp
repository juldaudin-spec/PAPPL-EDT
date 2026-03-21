<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
<<<<<<< HEAD
        <%@include file="imports.jspf" %>
        <title><fmt:message key="summary"/> ${enseignement.acronyme}</title>
        
=======
        <title>Récapitulatif ${enseignement.acronyme}</title>
        <%@include file="imports.jspf" %>
>>>>>>> 33d973b6f47ba0a48550595588d742b3dc5f17f9
        <link href="css/groupes.css" type="text/css" rel="stylesheet" />
        
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
<<<<<<< HEAD
                        <h1><fmt:message key="summary"/> ${enseignement.acronyme}</h1>
=======
                        <h1>Récapitulatif de ${enseignement.acronyme}</h1>
>>>>>>> 33d973b6f47ba0a48550595588d742b3dc5f17f9
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">                       
                        <table class="table table-striped">
                            <thead>
                            <th class="col-6">
<<<<<<< HEAD
                                <fmt:message key="group"/>
                            </th>
                            <th class="col-2">
                                <fmt:message key="courseType"/>
                            </th>
                            <th class="col-2">
                                <fmt:message key="duration"/>
                            </th>
                            <th class="col-2">
                                <fmt:message key="scheduled"/>
=======
                                Groupe
                            </th>
                            <th class="col-2">
                                Type de cours
                            </th>
                            <th class="col-2">
                                Durée à faire
                            </th>
                            <th class="col-2">
                                Durée programmée
>>>>>>> 33d973b6f47ba0a48550595588d742b3dc5f17f9
                            </th>
                            </thead>
                            <tbody>
                                <c:forEach var="groupe" items="${groupes}" varStatus="status">
                                    <tr>
                                        <td  class="col-6">
                                            ${groupe.nomGroupe}
                                        </td>
                                        <td colspan="3">
                                            <div class="col-md-12">   
                                                <table class="table table-striped">
                                                    <tbody>
                                                        <c:forEach var="type" items="${intitules.get(status.index)}" varStatus="status2">
                                                            <tr class="col-6">
                                                                <td class="col-4">
                                                                    ${type.intitule}
                                                                </td>
                                                                <td class="col-4">
                                                                    <c:forEach var="contient" items="${contients.get(status.index).get(status2.index)}">
                                                                        <c:set var="heures" value="${contient.volumetrie div 60}"  />
                                                                        <c:set var="minutes" value="${contient.volumetrie mod 60}" />
                                                                        <fmt:formatNumber var="heures" value="${heures - (heures mod 1)}" pattern="0" />
                                                                        <fmt:formatNumber var="minutes" value="${minutes}" pattern="" />
                                                                        <p>${heures}h${minutes}</p>

                                                                    </c:forEach>
                                                                </td>
                                                                <td class="col-4">
                                                                    <c:set var="dureeTot" value="0" scope="page"/>
                                                                    <c:forEach var="seance" items="${seances}">
                                                                        <c:forEach var="groupeParticipant" items="${seance.groupeList}">
                                                                        <c:choose>
                                                                            <c:when test="${(groupe.nomGroupe==groupeParticipant.nomGroupe)&&(seance.intitule.intitule==type.intitule)}">
                                                                                <c:set var="dureeTot" value="${dureeTot+seance.duree}"/>
                                                                            </c:when>
                                                                        </c:choose>
                                                                        </c:forEach>
                                                                    </c:forEach>
                                                                    <c:set var="heures" value="${dureeTot div 60}" />
                                                                    <c:set var="minutes" value="${dureeTot mod 60}" />
                                                                        <fmt:formatNumber var="heures" value="${heures - (heures mod 1)}" pattern="0" />
                                                                        <fmt:formatNumber var="minutes" value="${minutes}" pattern="" />
                                                                    <p>${heures}h${minutes}</p>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        <td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="enseignements.do" method="POST">
                            <input type="hidden" name="connexion" value="${user.connectionCode}">
<<<<<<< HEAD
                            <button formaction="enseignements.do"><fmt:message key="backToEnseignements"/></button>
=======
                            <button formaction="enseignements.do">Retour à la liste des Enseignements</button>
>>>>>>> 33d973b6f47ba0a48550595588d742b3dc5f17f9
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>