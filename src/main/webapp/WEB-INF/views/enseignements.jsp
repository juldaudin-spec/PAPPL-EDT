<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Ajouter Enseignement</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- jQuery (optionnel pour Bootstrap 5) -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
                integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
        crossorigin="anonymous"></script>
        <link href="css/enseignements.css" type="text/css" rel="stylesheet" />
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
                        <h1>Créer un nouveau enseignement</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="saveenseignement.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col">Nom du Enseignement</th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${(empty enseignement) || (empty enseignement.nomEnseignement)}"><input name="NomEnseignement" value=""/></c:when>
                                                <c:otherwise><input type="text" class="form-control" name="NomEnseignement" value="${enseignement.nomEnseignement}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                    
                                    <tr>
                                        <th scope="col">Nombre d'élèves</th>
                                        <td><input type="text" class="form-control" name="NbEleve" value="${enseignement.nbEleve}"/></td>
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
                        <c:if test='${newenseignement}'>
                            <p>Enseignement créé/modifié avec succès</p>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="enseignements.do" method="POST">
                            <button formaction="enseignements.do">Afficher la liste des Enseignements</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>