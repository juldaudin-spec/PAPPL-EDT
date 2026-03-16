<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Ajouter Groupe</title>
        <%@include file="imports.jspf" %>
        <link href="css/groupes.css" type="text/css" rel="stylesheet" />
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1>Créer un nouveau groupe</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="savegroupe.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col">Nom du Groupe</th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${(empty groupe) || (empty groupe.nomGroupe)}"><input name="NomGroupe" value=""/></c:when>
                                                <c:otherwise><input type="text" class="form-control" name="NomGroupe" value="${groupe.nomGroupe}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                    
                                    <tr>
                                        <th scope="col">Nombre d'élèves</th>
                                        <td><input type="text" class="form-control" name="NbEleve" value="${groupe.nbEleve}"/></td>
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
                        <c:if test='${newgroupe}'>
                            <p>Groupe créé/modifié avec succès</p>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="groupes.do" method="POST">
                            <button formaction="groupes.do">Afficher la liste des Groupes</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>