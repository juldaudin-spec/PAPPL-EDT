<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Récapitulatif ${groupe.nomGroupe}</title>
        <%@include file="imports.jspf" %>
        <link href="css/groupes.css" type="text/css" rel="stylesheet" />
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1>Récapitulatif de ${groupe.nomGroupe}</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">                       
                        <table class="table table-striped">
                            <thead>
                            <th class="col-6">
                                Enseignement
                            </th>
                            <th class="col-2">
                                Type de cours
                            </th>
                            <th class="col-2">
                                durée à faire
                            </th>
                            <th class="col-2">
                                durée programmée
                            </th>
                            </thead>
                            <tbody>
                                <c:forEach var="enseignement" items="${enseignements}" varStatus="status">
                                    <tr>
                                        <td  class="col-6">
                                            ${enseignement.acronyme}
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
                                                                        <c:choose>
                                                                            <c:when test="${(seance.acronyme.acronyme==enseignement.acronyme)&&(seance.intitule.intitule==type.intitule)}">
                                                                                <c:set var="dureeTot" value="${dureeTot+seance.duree}"/>
                                                                            </c:when>
                                                                        </c:choose>
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
                        <form action="groupes.do" method="POST">
                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                            <button formaction="groupes.do">Retour à la liste des Groupes</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>