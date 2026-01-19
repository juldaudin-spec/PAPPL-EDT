<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Ajouter Enseignant</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- jQuery (optionnel pour Bootstrap 5) -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
                integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
        crossorigin="anonymous"></script>
        <link href="css/enseignants.css" type="text/css" rel="stylesheet" />
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
                        <h1>Créer un nouvel enseignant</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="creerenseignant.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col">Initiales</th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${(empty enseignant) || (empty enseignant.initiales)}"><input name="Initiales" value=""/></c:when>
                                                <c:otherwise><input name="Initiales" value="${user.initiales}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                    
                                    <tr>
                                        <th scope="col">Nom</th>
                                        <td><input type="text" class="form-control" name="Nom" value="${enseignant.nomEnseignant}"/></td>
                                    </tr>
                                    <tr>
                                        <th scope="col">Prenom</th>
                                        <td><input type="text" class="form-control" name="Prenom" value="${enseignant.prenom}"/></td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td scope="col" colspan="2" class="text-center"><button type="submit" class="btn btn-block btn-primary">Save</button></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <c:if test='${newenseignant}'>
                            <p>Nouvel enseignant créé avec succès</p>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="enseignants.do" method="POST">
                            <button formaction="enseignants.do">Afficher la liste des Enseignants</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>