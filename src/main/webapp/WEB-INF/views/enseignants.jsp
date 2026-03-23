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
        <%@include file="imports.jspf" %>
        <title><fmt:message key="listEnseignants"/></title>
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5" id="main-content">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1><fmt:message key="listEnseignants"/></h1>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">

                        <table class="table table-striped table-md sortable">
                            <thead>
                                <tr>
                                    <th scope="col" class="col-md-2"><fmt:message key="initials"/></th>
                                    <th scope="col" class="col-md-3"><fmt:message key="name"/></th>
                                    <th scope="col" class="col-md-3"><fmt:message key="firstname"/></th>
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

