<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <%@include file="imports.jspf" %>
        <title><fmt:message key="addGroup"/></title>
        <link href="css/groupes.css" type="text/css" rel="stylesheet" />
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5" id="main-content">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1><fmt:message key="addGroup"/></h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="savegroupe.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col"><fmt:message key="groupName"/></th>
                                        <td>
                                            <label class="hidden-label" for="NomGroupe">
                                                <fmt:message key="groupName"/>
                                            </label>
                                            <c:choose>
                                                <c:when test="${(empty groupe) || (empty groupe.nomGroupe)}"><input name="NomGroupe" id="NomGroupe" value=""/></c:when>
                                                <c:otherwise><input type="text" class="form-control" name="NomGroupe" id="NomGroupe" value="${groupe.nomGroupe}"/></c:otherwise>
                                            </c:choose>
                                        </td>
<tr>
                                        
                                        <th scope="col"><fmt:message key="nbStudent"/></th>
                                        <td>
                                            <label class="hidden-label" for="NbEleve">
                                            <fmt:message key="nbStudent"/></label>
                                            <input type="text" class="form-control" name="NbEleve" id="NbEleve" value="${groupe.nbEleve}"/></td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td scope="col" colspan="2" class="text-center">
                                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                                            <button type="submit" class="btn btn-block btn-primary"><fmt:message key="save"/></button>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <c:if test='${newgroupe}'>
                            <p><fmt:message key="groupSaved"/></p>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="groupes.do" method="POST">
                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                            <button formaction="groupes.do"><fmt:message key="backToGroups"/></button>
                        </form>
                    </div>
                </div>
               <div class="row">
                    <div class="col-md-12">
                        <form action="groupes.do" method="POST">
                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                            <button formaction="groupes.do">Importer une liste de groupes</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>