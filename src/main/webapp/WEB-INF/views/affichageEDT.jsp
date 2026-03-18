<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Emploi du temps</title>
        <%@include file="imports.jspf" %>
        <link rel="stylesheet" href="css/EDT.css">
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <%@ page import="java.util.Locale"%>
        <%@ page import="java.time.format.TextStyle"%>
        <div class="py-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">

                        <h5>Veuillez choisir les groupes à visualiser</h5>
                    </div>
                    <form action="affichageEDT.do" method="POST">
                        <label for="groupes">sélection groupes : </label>
                        <select id="groupes" name="idGroupe" multiple>
                            <c:forEach var="groupe" items="${groupes}">
                                <option value="${groupe.nomGroupe}">${groupe.nomGroupe}</option>
                            </c:forEach>
                        </select>
                        
                        <button>
                            valider
                        </button>
                    </form>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <form formaction="seance.do" method="POST">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th scope="col" class="col-2 text-center">Date</th>
                                                <c:forEach var="groupeSelect" items="${listeGroupe}">
                                                <th scope="col-auto" class="text-center">${groupeSelect}</th>
                                                </c:forEach>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="jour" items="${HDebut}" varStatus="status">
                                            <tr>
                                                <td>
                                                    <div class="position-relative" id="Jour">
                                                        <div id="nomJour">  
                                                            ${jour.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.FRENCH)}
                                                        </div>
                                                        <div id="date">
                                                            ${jour}</div>
                                                        <div id="Horaire" style="--position-element: 0px; --taille-element:1px;">
                                                            8h </div>
                                                        <div id="Horaire" style="--position-element: 40px; --taille-element:1px;">
                                                            10h</div>
                                                        <div id="Horaire" style="--position-element: 80px; --taille-element:1px;">
                                                            12h</div>
                                                        <div id="Horaire" style="--position-element: 120px; --taille-element:1px;">
                                                            14h</div>
                                                        <div id="Horaire" style="--position-element: 160px; --taille-element:1px;">
                                                            16h</div>
                                                        <div id="Horaire" style="--position-element: 200px; --taille-element:1px;">
                                                            18h</div>
                                                        
                                                    </div>

                                                </td>
                                                <c:forEach var="seanceByDay" items="${listeSeance[status.index]}">

                                                    <td class="texte-center">
                                                        <div class="position-relative" >
                                                            <c:forEach var="seance" items="${seanceByDay}">
                                                                <c:choose>
                                                                    <c:when test="${not empty seance.acronyme}">
                                                                        <div id="Seance" style="
                                                                             --position-element: ${((seance.HDebut.getHours()-8)*60+seance.HDebut.getMinutes())/3}px;
                                                                             --taille-element:   ${seance.duree/3}px;
                                                                             ">
                                                                            <button class="nav-link" id="boutonSeance" style="text-align:center" name="idSeance" formaction="seance.do" value="${seance.idSeance}" method="POST">${seance.acronyme.acronyme}</button> 
                                                                        </div>
                                                                    </c:when>
                                                                    <c:otherwise>

                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:forEach>
                                                        </div>
                                                    </td>
                                                </c:forEach>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
