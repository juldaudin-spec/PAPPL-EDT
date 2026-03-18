<%-- 
    Document   : salles
    Created on : 19 janv. 2026, 16:31:33
    Author     : julda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr-fr">
    <head>
        <%@include file="imports.jspf" %>
        <title><fmt:message key="listRoom"/></title>
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1><fmt:message key="listRoom"/></h1>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12">

                        <table class="table table-striped table-md sortable">
                            <thead>
                                <tr>
                                    <th scope="col" class="col-md-2"><fmt:message key="wichRoom"/></th>
                                    <th scope="col" class="col-md-3"><fmt:message key="roomCapacity"/></th>
                                    <th scope="col" class="col-md-3"><fmt:message key="roomInfo"/><fmt:message key="listRoomInfo"/></th>
                                    <th scope="col" class="col-md-3"><fmt:message key="editRoom"/></th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="item" items="${sallesList}">
                                    <tr>
                                        <td  scope="col">${item.numeroSalle}</td>
                                        <td>${item.capacite}</td>
                                        <td>${item.typologie}</td>
                                        <td class="text-center">
                                            <form action="editUser" method="POST">
                                                <input type="hidden" name="NumeroSalle" value="${item.numeroSalle}" />
                                                <button class="btn" formaction="editsalle.do" name="edit" class="icon">
                                                    <img src="img/edit.png" alt="edit" class="icon">
                                                </button>
                                                <button class="btn" name="delete" formaction="deletesalle.do" class="icon">
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
                                        <form action="salle.do" method="POST">
                                            <button class="btn">
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

