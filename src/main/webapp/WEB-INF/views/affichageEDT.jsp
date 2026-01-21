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
                        <h5>Logiciel de création d'emplois du temps</h5>
                    </div>
                </div>

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
            </div>
        </div>
    </body>
</html>
