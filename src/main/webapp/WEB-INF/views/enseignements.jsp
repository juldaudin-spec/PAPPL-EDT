<%-- 
    Document   : enseignements
    Created on : 19 janv. 2026, 16:31:33
    Author     : julda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr-fr">
    <head>
        <%@include file="imports.jspf" %>
        <title><fmt:message key="listEnseignement"/></title>
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5" id="main-content">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1><fmt:message key="listEnseignement"/></h1>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">

                        <table class="table table-striped table-md sortable">
                            <thead>
                                <tr>
                                    <th scope="col" class="col-md-2"><fmt:message key="enseignementAcronyme"/></th>
                                    <th scope="col" class="col-md-3"><fmt:message key="enseignementName"/></th>
                                    <th scope="col" class="col-md-3"><fmt:message key="programme"/></th>
                                    <th scope="col" class="col-md-3"><fmt:message key="manager"/></th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="item" items="${enseignementsList}">
                                    <tr>
                                        <td  scope="col">
                                            <form method="POST">
                                                <button class="btn" formaction="compteRenduEnseignement.do" name="acronyme" value="${item.acronyme}">${item.acronyme}</button>
                                            </form>
                                        </td>
                                        <td>${item.nomEnseignement}</td>
                                        <td>${item.filiere}</td>
                                        <td>${item.responsable.nomEnseignant} ${item.responsable.prenom}</td>
                                        <td class="text-center">
                                            <form action="editUser" method="POST">
                                                <input type="hidden" name="connexion" value="${user.connectionCode}">
                                                <input type="hidden" name="Acronyme" value="${item.acronyme}" />
                                                <button class="btn" formaction="editenseignement.do" name="edit" class="icon">
                                                    <img src="img/edit.png" alt="edit" class="icon">
                                                </button>
                                                <button class="btn" name="delete" formaction="deleteenseignement.do" class="icon">
                                                    <img src="img/delete.png" alt="delete" class="icon">
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>

                            <tfoot>
                                <tr id="addNew">
                                    <td colspan="5">
                                        <form action="enseignement.do" method="POST">
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