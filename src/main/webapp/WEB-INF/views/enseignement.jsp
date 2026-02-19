<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Créer Matière</title>
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
                        <h1>Créer une nouvelle matière</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="saveenseignement.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col">Nom de l'Enseignement</th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${(empty enseignement) || (empty enseignement.acronyme)}"><input name="Acronyme" value=""/></c:when>
                                                <c:otherwise><input type="text" class="form-control" name="Acronyme" value="${enseignement.acronyme}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                    
                                    <tr>
                                        <th scope="col">Nom Enseignement</th>
                                        <td><input type="text" class="form-control" name="NomEnseignement" value="${enseignement.nomEnseignement}"/></td>
                                    </tr>
                                    <tr>
                                        <th scope="col">Filière</th>
                                        <td><input type="text" class="form-control" name="Filiere" value="${enseignement.filiere}"/></td>
                                    </tr>
                                    <tr>
                                        <th scope="col">Responsable</th>
                                        <td colspan="2">
                                            <input type="hidden" name="AcronymeEnseignement" value="${enseignement.acronyme}" />
                                            <select name="InitialesEnseignant" required
                                                    class="form-control form-select form-select-lg mb-3">
                                                <c:choose>
                                                <c:when test="${(empty enseignement) || (empty enseignement.responsable)}"><option value="" disabled selected>Choisissez un enseignant</option></c:when>
                                                <c:otherwise><option value="${enseignement.responsable.initiales}" disabled selected>${enseignement.responsable.nomEnseignant} ${enseignement.responsable.prenom}</option></c:otherwise>
                                            </c:choose>
                                                
                                                <c:forEach var="enseignant" items="${enseignantsList}">
                                                    <option value="${enseignant.initiales}">${enseignant.nomEnseignant} ${enseignant.prenom}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
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