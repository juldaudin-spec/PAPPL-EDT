<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <%@include file="imports.jspf" %>
        <title><fmt:message key="addRoom"/></title>
        <link href="css/salles.css" type="text/css" rel="stylesheet" />
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1><fmt:message key="addRoom"/></h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="savesalle.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col"><fmt:message key="wichRoom"/></th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${(empty salle) || (empty salle.numeroSalle)}"><input name="NumeroSalle" value=""/></c:when>
                                                <c:otherwise><input type="text" class="form-control" name="NumeroSalle" value="${salle.numeroSalle}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                    
                                    <tr>
                                        <th scope="col"><fmt:message key="nbStudent"/></th>
                                        <td><input type="int" class="form-control" name="Capacite" value="${salle.capacite}"/></td>
                                    </tr>
                                    <tr>
                                        <th scope="col"><fmt:message key="roomInfo"/></th>
                                        <td><input type="text" class="form-control" name="Typologie" value="${salle.typologie}"/></td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td scope="col" colspan="2" class="text-center"><button type="submit" class="btn btn-block btn-primary"><fmt:message key="save"/></button></td>
                                    </tr>
                                </tfoot>
                            </table>
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <c:if test='${newsalle}'>
                            <p><fmt:message key="roomSaved"/></p>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="salles.do" method="POST">
                            <button formaction="salles.do"><fmt:message key="backToRooms"/></button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>