<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <%@include file="imports.jspf" %>
        <title><fmt:message key="home"/></title>
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <h1><fmt:message key="whatIsIt"/></h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <p><fmt:message key="whatToDo"/></p>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
