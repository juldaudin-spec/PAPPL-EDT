<%-- 
    Document   : enseignants
    Created on : 19 janv. 2026, 16:31:33
    Author     : julda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr-fr">
    <head>
        <meta charset="UTF-8">
        <title>Liste Enseignants</title>
        <%@include file="imports.jspf" %>
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1>Liste des enseignants</h1>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">

                        <table class="table table-striped table-md sortable">
                            <thead>
                                <tr>
                                    <th scope="col" class="col-md-2">Initiales</th>
                                    <th scope="col" class="col-md-3">Nom</th>
                                    <th scope="col" class="col-md-3">Prénom</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="item" items="${enseignantsList}">
                                    <tr>
                                        <td  scope="col">${item.initiales}</td>
                                        <td>${item.nomEnseignant}</td>
                                        <td>${item.prenom}</td>
                                        <td class="text-center">
                                            <form action="editUser" method="POST">
                                                <input type="hidden" name="connexion" value="${user.connectionCode}">
                                                <input type="hidden" name="Initiales" value="${item.initiales}" />
                                                <button class="btn" formaction="editenseignant.do" name="edit" class="icon">
                                                    <img src="img/edit.png" alt="edit" class="icon">
                                                </button>
                                                <button class="btn" name="delete" formaction="deleteenseignant.do" class="icon">
                                                    <img src="img/delete.png" alt="delete" class="icon">
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>

                            <tfoot>
                                <tr id="addNew">
                                    <td colspan="4" class="text-center">
                                        <form action="enseignant.do" method="POST">
                                            <button class="btn">
                                                <input type="hidden" name="connexion" value="${user.connectionCode}">
                                                <img src="img/add.png" alt="add" class="icon">
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </tfoot>

                        </table>

                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

