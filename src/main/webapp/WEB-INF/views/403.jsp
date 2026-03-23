<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Accès refusé</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
    <div class="container text-center mt-5">
        <h1 class="display-1">403</h1>
        <h2>Accès refusé</h2>
        <p>Vous n'avez pas les droits nécessaires pour effectuer cette action.</p>
        <a href="${pageContext.request.contextPath}/index.do" class="btn btn-dark mt-3">Retour à l'accueil</a>
    </div>
</body>
</html>