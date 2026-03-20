<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Créer Matière</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- jQuery (optionnel pour Bootstrap 5) -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
                integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
        crossorigin="anonymous"></script>
        <link href="css/enseignements.css" type="text/css" rel="stylesheet" />
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
        <script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>
    </head>
    <body>
        <%@include file="navbar.jspf" %>
        <div class="py-5">
            <div class="container">
                <div class="row">
                    <div class="col-md-12">
                        <h1>Créer une nouvelle matière</h1>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="saveenseignement.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <tr>
                                        <th scope="col">Acronyme</th>
                                        <td>
                                            <c:choose>
                                                <c:when test="${(empty enseignement) || (empty enseignement.acronyme)}"><input name="Acronyme" value=""/></c:when>
                                                <c:otherwise><input type="text" class="form-control" name="Acronyme" value="${enseignement.acronyme}"/></c:otherwise>
                                            </c:choose>
                                        </td>

                                    <tr>
                                        <th scope="col">Nom Enseignement</th>
                                        <td><input type="text" class="form-control" name="NomEnseignement" value="${enseignement.nomEnseignement}"/></td>
                                    </tr>
                                    <tr>
                                        <th scope="col">Filière</th>
                                        <td><input type="text" class="form-control" name="Filiere" value="${enseignement.filiere}"/></td>
                                    </tr>
                                    <tr>
                                        <th scope="col">Responsable</th>
                                        <td colspan="2">
                                            <input type="hidden" name="AcronymeEnseignement" value="${enseignement.acronyme}" />
                                            <select name="InitialesEnseignant" required
                                                    class="form-control form-select form-select-lg mb-3">
                                                <c:choose>
                                                    <c:when test="${(empty enseignement) || (empty enseignement.responsable)}"><option value="" disabled selected>Choisissez un enseignant</option></c:when>
                                                    <c:otherwise><option value="${enseignement.responsable.initiales}" disabled selected>${enseignement.responsable.nomEnseignant} ${enseignement.responsable.prenom}</option></c:otherwise>
                                                </c:choose>

                                                <c:forEach var="enseignant" items="${enseignantsList}">
                                                    <option value="${enseignant.initiales}">${enseignant.nomEnseignant} ${enseignant.prenom}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th scope="col">Enseignant(s)</th>
                                        <td>
                                            <div class="container">
                                                <div class="row">
                                                    <div class="col-md-5">
                                                        <div id="listenseignants" 
                                                             class="list-group overflow-auto border border-primary" 
                                                             style="height:350px;overflow-y: scroll;" 
                                                             tabindex="0" 
                                                             ondrop="dropAndRename(event, 'el', 'e')" 
                                                             ondragover="allowDrop(event)">
                                                            <c:forEach var="itemIter" items="${seance.enseignantList}">
                                                                <div class="list-group-item list-group-item-action" id="e_${itemIter.initiales}" draggable="true" ondragstart="drag(event)">
                                                                    <input type="hidden" name="e[${itemIter.initiales.initiales}]" value="${itemIter.initiales.initiales}"/>
                                                                    ${itemIter.initiales.nomEnseignant} ${itemIter.initiales.prenom}
                                                                </div>
                                                            </c:forEach>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-5">
                                                        <div id="listAllenseignants" 
                                                             class="list-group overflow-auto border border-primary" 
                                                             style="height:350px;overflow-y: scroll;" 
                                                             tabindex="0" 
                                                             ondrop="dropAndRename(event, 'e', 'el')" 
                                                             ondragover="allowDrop(event)">
                                                            <c:forEach var="enseignant" items="${enseignantsList}">
                                                                <div class="list-group-item list-group-item-action" id="el_${enseignant.initiales}" draggable="true" ondragstart="drag(event)" style="display:block">
                                                                    <input type="hidden" name="el[${enseignant.initiales}]" value="${enseignant.initiales}"/>
                                                                    ${enseignant.nomEnseignant} ${enseignant.prenom}
                                                                </div>
                                                            </c:forEach>
                                                        </div>
                                                        <div onkeyup="filterList('listAllenseignants', 'listAllenseignantsFilter')" >
                                                            <input type="text" id="listAllenseignantsFilter" placeholder="Rechercher.." title="Type in a name" style="width:100%">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    

                                <c:forEach var="typeLecon" items="${typeLeconsList}">
                                    <tr>
                                        <th scope="col">Nombre de minutes de ${typeLecon.intitule}</th>
                                        <td><input type="int" class="form-control" name="${typeLecon.intitule}"/></td>
                                    </tr>
                                    <tr>
                                        <th scope="col">Salle demandée pour les ${typeLecon.intitule}</th>
                                        <td>
                                            <select name="salle[${typeLecon.intitule}]" required
                                                    class="form-control form-select form-select-lg mb-3">
                                                <option value="" disabled selected>Choisissez une salle</option>

                                                <c:forEach var="salle" items="${sallesList}">
                                                    <option value="${salle.getNumeroSalle()}">${salle.getNumeroSalle()}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td scope="col" colspan="2" class="text-center">
                                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                                            <button type="submit" class="btn btn-block btn-primary">Save</button>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </form>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <c:if test='${newenseignement}'>
                            <p>Enseignement créé/modifié avec succès</p>
                        </c:if>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <form action="enseignements.do" method="POST">
                            <button formaction="enseignements.do">Afficher la liste des Enseignements</button>
                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>