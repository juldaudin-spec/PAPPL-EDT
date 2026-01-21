<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Accueil</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- jQuery -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
                integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
        crossorigin="anonymous"></script>
        
        <!-- Bootstrap CSS depuis Internet -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        
        <!-- Ton CSS personnalisé -->
        <link href="${pageContext.request.contextPath}/css/mainPage.css" type="text/css" rel="stylesheet" />
        
        <!-- Ton JS personnalisé -->
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <h2>Logiciel de création d'emplois du temps</h2>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">
                        <h5>Veuillez choisir dans le menu ci-dessus l'action à réaliser</h5>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Bootstrap JS à la fin -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>