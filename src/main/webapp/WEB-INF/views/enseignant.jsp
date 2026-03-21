<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Ajouter Enseignant</title>
        <%@include file="imports.jspf" %>
        <link href="css/enseignants.css" type="text/css" rel="stylesheet" />
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
                        <form action="saveenseignant.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col">Initiales</th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${(empty enseignant) || (empty enseignant.initiales)}"><input name="Initiales" value=""/></c:when>
                                                <c:otherwise><input type="text" class="form-control" name="Initiales" value="${enseignant.initiales}"/></c:otherwise>
                                            </c:choose>
                                        </td>

                                    <tr>
                                        <th scope="col">Nom</th>
                                        <td>
                                            <label> 
                                                Nom de l'enseignant
                                            <input type="text" class="form-control" name="Nom" value="${enseignant.nomEnseignant}"/></td>
                                            </label>
                                    </tr>
                                    <tr>
                                        <th scope="col">Prenom</th>
                                        <td>
                                            <label> 
                                                Prénom de l'enseignant
                                            <input type="text" class="form-control" name="Prenom" value="${enseignant.prenom}"/>
                                            </label>
                                        </td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td scope="col" colspan="2" class="text-center">
                                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                                            <button type="submit" class="btn btn-block btn-primary">Save</button>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <c:if test='${newenseignant}'>
                            <p>Enseignant créé/modifié avec succès</p>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="enseignants.do" method="POST">
                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                            <button formaction="enseignants.do">Afficher la liste des Enseignants</button>
                        </form>
                    </div>
                    <div class="col-md-12">
                        <form action="/upload" method="post" enctype="multipart/form-data">

                            <div class="mb-3">
                                <label for="fileInput" class="form-label">Choisir un fichier à importer</label>
                                <input class="form-control" type="file" id="fileInput" name="fichier">
                            </div>

                            <div class="d-grid">
                                <button type="submit" class="btn btn-block btn-primary">
                                    Envoyer
                                </button>
                            </div>

                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>