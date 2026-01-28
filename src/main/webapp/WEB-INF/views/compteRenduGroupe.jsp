<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Récapitulatif ${groupe.nomGroupe}</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- jQuery (optionnel pour Bootstrap 5) -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
                integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
        crossorigin="anonymous"></script>
        <link href="css/groupes.css" type="text/css" rel="stylesheet" />
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
        <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>
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
                                <th>
                                    Enseignement
                                </th>
                                <th>
                                    Type de cours
                                </th>
                                <th>
                                    durée à faire
                                </th>
                                <th>
                                    durée programmée
                                </th>
                                </thead>
                                <tbody>
                                    <c:forEach var="enseignement" items="${enseignements}">
                                    <tr>
                                        <td>
                                            ${enseignement.acronyme}
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                        <table class="table table-striped">
                                            <tbody>
                                                <tr>
                                                    test
                                                </tr>
                                            </tbody>
                                        </table>
                                        <td>
                                    </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="groupes.do" method="POST">
                            <button formaction="groupes.do">Retour à la liste des Groupes</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>