<%-- 
    Document   : groupes
    Created on : 19 janv. 2026, 16:31:33
    Author     : julda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr-fr">
    <head>
<<<<<<< HEAD
        <%@include file="imports.jspf" %>
        <title><fmt:message key="listGroups"/></title>
=======
        <meta charset="UTF-8">
        <title>Liste Groupes</title>
        <%@include file="imports.jspf" %>
>>>>>>> 33d973b6f47ba0a48550595588d742b3dc5f17f9
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
<<<<<<< HEAD
                        <h1><fmt:message key="listGroups"/></h1>
=======
                        <h1>Liste des groupes</h1>
>>>>>>> 33d973b6f47ba0a48550595588d742b3dc5f17f9
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">

                        <table class="table table-striped table-md sortable">
                            <thead>
                                <tr>
<<<<<<< HEAD
                                    <th scope="col" class="col-md-2"><fmt:message key="groupName"/></th>
                                    <th scope="col" class="col-md-3"><fmt:message key="nbStudent"/></th>
=======
                                    <th scope="col" class="col-md-2">Nom Groupe</th>
                                    <th scope="col" class="col-md-3">Nombre d'élèves</th>
>>>>>>> 33d973b6f47ba0a48550595588d742b3dc5f17f9
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="item" items="${groupesList}">
                                    <tr>                                   
                                        <td  scope="col">
                                            <form method="POST">
                                                <button class="btn" formaction="compteRenduGroupe.do" name="idGroupe" value="${item.nomGroupe}">${item.nomGroupe}</button>
                                            </form>
                                        </td>

                                        <td>${item.nbEleve}</td>
                                        <td class="text-center">
                                            <form action="editUser" method="POST">
                                                <input type="hidden" name="connexion" value="${user.connectionCode}">
                                                <input type="hidden" name="NomGroupe" value="${item.nomGroupe}" />
                                                <button class="btn" formaction="editgroupe.do" name="edit" class="icon">
                                                    <img src="img/edit.png" alt="edit" class="icon">
                                                </button>
                                                <button class="btn" name="delete" formaction="deletegroupe.do" class="icon">
                                                    <img src="img/delete.png" alt="delete" class="icon">
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>

                            <tfoot>
                                <tr id="addNew">
                                    <td colspan="3">
                                        <form action="groupe.do" method="POST">
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

