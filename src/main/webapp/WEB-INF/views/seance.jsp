<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <meta charset="UTF-8">
        <title>Ajouter Seance</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- jQuery (optionnel pour Bootstrap 5) -->
        <script src="https://code.jquery.com/jquery-3.7.1.js"
                integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
        crossorigin="anonymous"></script>
        <link href="css/seances.css" type="text/css" rel="stylesheet" />
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
                        <h1>Créer une nouvelle séance</h1>
                    </div>
                </div>
                
                <!-- ========== AJOUT DES MESSAGES D'ERREUR ET SUCCÈS ========== -->
                <div class="row">
                    <div class="col-md-12">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <strong>Erreur !</strong> ${error}
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                        
                        <c:if test="${newseance}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <strong>Succès !</strong> Séance créée/modifiée avec succès
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        </c:if>
                    </div>
                </div>
                <!-- =========================================================== -->
                
                <div class="row">
                    <div class="col-md-12">
                        <form action="saveseance.do" method="POST">
                            <table class="table table-striped">
                                <tbody>
                                    <c:choose>
                                        <c:when test="${(empty seance) || (empty seance.idSeance)}"><input name="IdSeance" value="-1" type="hidden"/></c:when>
                                    <c:otherwise><input type="hidden" class="form-control" name="IdSeance" value="${seance.idSeance}"/></c:otherwise>
                                </c:choose>
                                <tr>
                                    <th scope="col">Date et heure de début</th>
                                    <td><input type="datetime-local" class="form-control" name="HDebut" value="${seance.HDebut}"/></td>
                                </tr>
                                <tr>
                                    <th scope="col">Durée (minutes)</th>
                                    <td><input type="int" class="form-control" name="Duree" value="${seance.duree}"/></td>
                                </tr>
                                <tr>
                                    <th scope="col">Matière</th>
                                    <td colspan="2">
                                        <input type="hidden" name="IdSeance" value="${seance.idSeance}" />
                                        <select name="Enseignement" required
                                                class="form-control form-select form-select-lg mb-3">
                                            <c:choose>
                                                <c:when test="${(empty seance) || (empty seance.acronyme)}"><option value="" disabled selected>Choisissez la matière</option></c:when>
                                                <c:otherwise><option value="${seance.acronyme}" disabled selected>${seance.acronyme.nomEnseignement}</option></c:otherwise>
                                            </c:choose>

                                            <c:forEach var="enseignement" items="${enseignementsList}">
                                                <option value="${enseignement.acronyme}">${enseignement.nomEnseignement}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="col">Type de leçon</th>
                                    <td colspan="2">
                                        <input type="hidden" name="IdSeance" value="${seance.idSeance}" />
                                        <select name="TypeLecon" required
                                                class="form-control form-select form-select-lg mb-3">
                                            <c:choose>
                                                <c:when test="${(empty seance) || (empty seance.intitule)}"><option value="" disabled selected>Choisissez un type de leçon</option></c:when>
                                                <c:otherwise><option value="${seance.intitule}" disabled selected>${seance.intitule.intitule} ${seance.responsable.prenom}</option></c:otherwise>
                                            </c:choose>

                                            <c:forEach var="typeLecon" items="${typeLeconsList}">
                                                <option value="${typeLecon.intitule}">${typeLecon.intitule}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <th scope="col">Groupe(s)</th>
                                    <td>
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-md-5">
                                                    <div id="listgroupes" 
                                                         class="list-group overflow-auto border border-primary" 
                                                         style="height:350px;overflow-y: scroll;" 
                                                         tabindex="0" 
                                                         ondrop="dropAndRename(event, 'm')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="itemIter" items="${seance.groupeList}">
                                                            <div class="list-group-item list-group-item-action" id="m_${itemIter.nomGroupe}" draggable="true" ondragstart="drag(event)">
                                                                <input type="hidden" name="m[${itemIter.nomGroupe}]" value="${itemIter.nomGroupe}"/>
                                                                ${itemIter.nomGroupe}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <div class="col-md-5">
                                                    <div id="listAllgroupes" 
                                                         class="list-group overflow-auto border border-primary" 
                                                         style="height:350px;overflow-y: scroll;" 
                                                         tabindex="0" 
                                                         ondrop="dropAndRename(event, 'ml')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="groupe" items="${groupesList}">
                                                            <div class="list-group-item list-group-item-action" id="m_${groupe.nomGroupe}" draggable="true" ondragstart="drag(event)" style="display:block">
                                                                <input type="hidden" name="ml[${groupe.nomGroupe}]" value="${groupe.nomGroupe}"/>
                                                                ${groupe.nomGroupe}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div onkeyup="filterList('listAllgroupes', 'listAllgroupesFilter')" >
                                                        <input type="text" id="listAllgroupesFilter" placeholder="Rechercher.." title="Type in a name" style="width:100%">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
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
                                                         ondrop="dropAndRename(event, 'e')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="itemIter" items="${seance.enseignantList}">
                                                            <div class="list-group-item list-group-item-action" id="e_${itemIter.initiales}" draggable="true" ondragstart="drag(event)">
                                                                <input type="hidden" name="e[${itemIter.initiales}]" value="${itemIter.initiales}"/>
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
                                                         ondrop="dropAndRename(event, 'el')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="enseignant" items="${enseignantsList}">
                                                            <div class="list-group-item list-group-item-action" id="e_${enseignant.initiales}" draggable="true" ondragstart="drag(event)" style="display:block">
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
                                <tr>
                                    <th scope="col">Salle(s)</th>
                                    <td>
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-md-5">
                                                    <div id="listsalles" 
                                                         class="list-group overflow-auto border border-primary" 
                                                         style="height:350px;overflow-y: scroll;" 
                                                         tabindex="0" 
                                                         ondrop="dropAndRename(event, 's')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="itemIter" items="${seance.salleList}">
                                                            <div class="list-group-item list-group-item-action" id="s_${itemIter.numeroSalle}" draggable="true" ondragstart="drag(event)">
                                                                <input type="hidden" name="s[${itemIter.numeroSalle}]" value="${itemIter.numeroSalle}"/>
                                                                ${itemIter.numeroSalle}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <div class="col-md-5">
                                                    <div id="listAllsalles" 
                                                         class="list-group overflow-auto border border-primary" 
                                                         style="height:350px;overflow-y: scroll;" 
                                                         tabindex="0" 
                                                         ondrop="dropAndRename(event, 'sl')" 
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="salle" items="${sallesList}">
                                                            <div class="list-group-item list-group-item-action" id="s_${salle.numeroSalle}" draggable="true" ondragstart="drag(event)" style="display:block">
                                                                <input type="hidden" name="sl[${salle.numeroSalle}]" value="${salle.numeroSalle}"/>
                                                                ${salle.numeroSalle}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div onkeyup="filterList('listAllsalles', 'listAllsallesFilter')" >
                                                        <input type="text" id="listAllsallesFilter" placeholder="Rechercher.." title="Type in a name" style="width:100%">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
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
            </div>
        </div>
    </body>
</html>