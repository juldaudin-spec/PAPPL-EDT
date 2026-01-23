<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Emploi du temps</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- jQuery (optionnel pour Bootstrap 5) -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
                integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
        crossorigin="anonymous"></script>
        <link href="${pageContext.request.contextPath}/css/mainPage.css" type="text/css" rel="stylesheet" />
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
        <script type="text/javascript" src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">

                        <h5>Veuillez choisir les groupes à visualiser</h5>
                    </div>
                    <form action="affichageEDT.do" method="POST">
                        <select name="idGroupe" multiple>
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
                            <table class="table table-striped">
                                <thead>
                                    <tr>
                                        <th scope="col" class="text-center">Horaire</th>
                                            <c:forEach var="groupeSelect" items="${listeGroupe}">
                                            <th scope="col" class="text-center">${groupeSelect}</th>
                                            </c:forEach>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="jour" items="${HDebut}" varStatus="status">
                                        <tr>
                                            <td>
                                                <div class="Jour">
                                                    ${jour}
                                                </div>
                                            </td>
                                            <c:forEach var="seanceByDay" items="${listeSeance[status.index]}">

                                                <td class="texte-center">
                                                    <div class="Jour">
                                                        <c:forEach var="seance" items="${seanceByDay}">
                                                            <c:choose>
                                                                <c:when test="${not empty seance.acronyme}">
                                                                    <div class="Seance">
                                                                    ${seance.acronyme.acronyme}<!-- <button actionform="seance.do" value="${seance.idSeance}" method="POST">${seance.acronyme.acronyme}</button> -->
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
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </body>
    </html>
