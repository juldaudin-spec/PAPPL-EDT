<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <%@include file="imports.jspf" %>
        <meta charset="UTF-8">
        <title>
            <fmt:message key="EDT"/>
        </title>
        <link rel="stylesheet" href="css/EDT.css">
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <%@ page import="java.util.Locale"%>
        <%@ page import="java.time.format.TextStyle"%>
        <div class="py-5" id="main-content">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">

                        <h5><fmt:message key="chooseGroupe"/></h5>
                    </div>
                    <form action="affichageEDT.do" method="POST">
                        <label for="groupes"><fmt:message key="selectGroupe"/> </label>
                        <select id="groupes" name="idGroupe" multiple>
                            <c:forEach var="groupe" items="${groupes}">
                                <option value="${groupe.nomGroupe}">${groupe.nomGroupe}</option>
                            </c:forEach>
                        </select>
                        <input type="hidden" name="connexion" value="${user.connectionCode}">
                        <input type="hidden" name="connexion" value="${user.connectionCode}">
                        <button>
                            <fmt:message key="submit"/>
                        </button>
                    </form>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="table-responsive">
                                <table class="table table-striped">
                                    <thead>
                                        <tr>
                                            <th scope="col" class="col-2 text-center"><fmt:message key="date"/></th>
                                                <c:forEach var="groupeSelect" items="${listeGroupe}">
                                                <th scope="col-auto" class="text-center">${groupeSelect}
                                                <form action="exporterICS.do">
                                                    <input type="hidden" name="connexion" value="${user.connectionCode}">
                                                    <input type="hidden" name="groupeSelect" value=${groupeSelect}>
                                                    <button>Exporter au format ICS</button>
                                                </form>
                                                <form action="exporterExcel.do">
                                                    <input type="hidden" name="connexion" value="${user.connectionCode}">
                                                    <input type="hidden" name="groupeSelect" value=${groupeSelect}>
                                                    <button>Exporter au format Excel</button>
                                                </form>
                                            </th>
                                            </c:forEach>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="jour" items="${HDebut}" varStatus="status">
                                            <tr>
                                                <td>
                                                    <div class="position-relative" id="Jour">
                                                        <div id="nomJour">
                                                            ${jour.getDayOfWeek().getDisplayName(TextStyle.FULL, pageContext.request.locale)}
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
                                                                    <form action="seance.do" method="POST">
                                                                        <div id="Seance" style="
                                                                             --position-element: ${((seance.HDebut.getHours()-8)*60+seance.HDebut.getMinutes())/3}px;
                                                                             --taille-element:   ${seance.duree/3}px;
                                                                             ">
                                                                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                                                                            <button class="nav-link" id="boutonSeance" style="text-align:center; color:black" name="idSeance" formaction="seance.do" value="${seance.idSeance}" method="POST">${seance.acronyme.acronyme}</button>
                                                                        </div>
                                                                    </form>
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
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
