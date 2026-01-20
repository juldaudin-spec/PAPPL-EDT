<%-- 
    Document   : groupes
    Created on : 16 janv. 2026, 02:39:07
    Author     : julda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Ajouter Enseignant</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- jQuery (optionnel pour Bootstrap 5) -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
                integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
        crossorigin="anonymous"></script>
        <link href="css/enseignants.css" type="text/css" rel="stylesheet" />
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="bootstrap-5.3.8-dist/css/bootstrap.min.css">
        <script type="text/javascript" src="bootstrap-5.3.8-dist/js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>
    </head>
    
    <body>
        <header><%@include file="navbar.jspf" %></header>
        <h1>Créer un nouveau groupe</h1>

        <form action="infos" method="GET">
            <table class="text-center">
                <tr>
                    <th>Nom du groupe</th>
                    <td>
                        <input type="text"
                               name="nomGroupe"
                               maxlength="128"
                               required>
                    </td>
                </tr>

                <tr>
                    <th>Nombre d’élèves</th>
                    <td>
                        <input type="number"
                               name="nbEleve"
                               min="0">
                    </td>
                </tr>

                <tr>
                    <td colspan="2" class="text-center">
                        <button type="submit">Save</button>
                    </td>
                </tr>
            </table>
        </form>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">

                <div class="row">
                    <div class="col-md-12">
                        <h1>Créer un Groupe</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="creergroupe.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col">Nom du groupe</th>
                                        <td><input type="text" class="form-control" name="nomGroupe" maxlength="128" required/></td>

                                    <tr>
                                        <th scope="col">Nombre d'élèves</th>
                                        <td><input type="text" class="form-control" name="nbEleve" min="0" required/></td>

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
            </div>
        </div>
    </body>
</html>
