<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <%@include file="imports.jspf" %>
        <meta charset="UTF-8">
        <title><fmt:message key="addEnseignant"/></title>
        
        <link href="css/enseignants.css" type="text/css" rel="stylesheet" />
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1><fmt:message key="addNewEnseignant"/></h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="saveenseignant.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col"><fmt:message key="initials"/></th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${(empty enseignant) || (empty enseignant.initiales)}"><input name="Initiales" value=""/></c:when>
                                                <c:otherwise><input type="text" class="form-control" name="Initiales" value="${enseignant.initiales}"/></c:otherwise>
                                            </c:choose>
                                        </td>
                                    
                                    <tr>
                                        <th scope="col"><fmt:message key="name"/></th>
                                        <td>
                                            <label> 
                                                <fmt:message key="name"/>
                                            <input type="text" class="form-control" name="Nom" value="${enseignant.nomEnseignant}"/></td>
                                            </label>
                                    </tr>
                                    <tr>
                                        <th scope="col"><fmt:message key="firstname"/></th>
                                        <td>
                                            <label> 
                                                <fmt:message key="firstname"/>
                                            <input type="text" class="form-control" name="Prenom" value="${enseignant.prenom}"/>
                                            </label>
                                        </td>
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
                        <c:if test='${newenseignant}'>
                            <p><fmt:message key="enseignantSaved"/></p>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="enseignants.do" method="POST">
                            <button formaction="enseignants.do"><fmt:message key="backToEnseignants"/></button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>