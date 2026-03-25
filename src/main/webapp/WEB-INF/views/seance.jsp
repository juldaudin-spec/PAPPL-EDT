<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">
    <head>
        <%@include file="imports.jspf" %>
        <%-- MODIF 1 : Titre dynamique création vs modification --%>
        <title>
            <c:choose>
                <c:when test="${(empty seance) || (empty seance.idSeance)}"><fmt:message key="addSeance"/></c:when>
                <c:otherwise><fmt:message key="editSeance"/></c:otherwise>
            </c:choose>
        </title>
    </head>
    <body>
        <%-- MODIF 2 : Zone d'annonces pour lecteur d'écran (cachée visuellement) --%>
        <%-- Permet d'annoncer les actions clavier : "groupe X ajouté", "salle Y retirée"... --%>
        <div id="sr-annonce"
             aria-live="polite"
             aria-atomic="true"
             class="visually-hidden">
        </div>

        <%@include file="navbar.jspf" %>

        <div class="py-5" id="main-content">
            <div class="container">
                <c:choose>
                    <c:when test="${(empty seance) || (empty seance.idSeance)}">
                        <div class="row">
                            <div class="col-md-12">
                                <%-- MODIF 3 : H1 dynamique --%>
                                <h1>
                                    <fmt:message key="addSeance"/>
                                </h1>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="row">
                            <div class="col-md-12">
                                <%-- MODIF 3 : H1 dynamique --%>
                                <h1>
                                    <fmt:message key="editSeance"/>
                                </h1>
                            </div>
                        </div>

                        <%-- Formulaire de suppression --%>
                        <div>
                            <form action="saveseance.do" method="POST">
                                <input type="hidden" name="connexion" value="${user.connectionCode}">
                                <input type="hidden" name="idSeance" value="${seance.idSeance}"/>
                                <%-- MODIF 4 : aria-label sur le bouton delete + alt="" sur l'image (décorative) --%>
                                <button class="btn"
                                        name="delete"
                                        formaction="deleteseance.do"
                                        aria-label="Supprimer cette séance">
                                    <img src="img/delete.png" alt="" class="icon" aria-hidden="true">
                                </button>
                            </form>
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="row">
                    <div class="col-md-12">
                        <form action="saveseance.do" method="POST" aria-label="Formulaire de création ou modification d'une séance">
                            <table class="table table-striped" role="presentation">
                                <tbody>
                                    <c:choose>
                                        <c:when test="${(empty seance) || (empty seance.idSeance)}">
                                        <input name="IdSeance" value="-1" type="hidden"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" class="form-control" name="IdSeance" value="${seance.idSeance}"/>
                                    </c:otherwise>
                                </c:choose>

                                <tr>
                                    <th scope="row">
                                        <label for="HDebut"><fmt:message key="start"/></label>
                                    </th>
                                    <td>
                                        <input type="datetime-local"
                                               class="form-control"
                                               name="HDebut"
                                               id="HDebut"
                                               required
                                               aria-required="true"
                                               value="<fmt:formatDate value="${seance.HDebut}" pattern="yyyy-MM-dd'T'HH:mm" />"/>
                                    </td>
                                </tr>

                                <tr>
                                    <th scope="row">
                                        <label for="Duree"><fmt:message key="duration"/><fmt:message key="minutes"/></label>
                                    </th>
                                    <td>
                                        <%-- MODIF 5 : type="number" au lieu de type="int" (invalide en HTML) --%>
                                        <input type="number"
                                               class="form-control"
                                               name="Duree"
                                               id="Duree"
                                               required
                                               aria-required="true"
                                               min="1"
                                               value="${seance.duree}"/>
                                    </td>
                                </tr>

                                <tr>
                                    <th scope="row">
                                        <label for="Enseignement"><fmt:message key="enseignement"/></label>
                                    </th>
                                    <td colspan="2">
                                        <%-- MODIF 6 : id unique "Enseignement" au lieu du dupliqué "IdSeance" --%>
                                        <select name="Enseignement"
                                                id="Enseignement"
                                                required
                                                aria-required="true"
                                                class="form-control form-select form-select-lg mb-3">
                                            <c:choose>
                                                <c:when test="${(empty seance) || (empty seance.acronyme)}">
                                                    <option value="" disabled selected><fmt:message key="chooseEnseignement"/></option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${seance.acronyme.acronyme}">${seance.acronyme.nomEnseignement}</option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:forEach var="enseignement" items="${enseignementsList}">
                                                <option value="${enseignement.acronyme}">${enseignement.nomEnseignement}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>

                                <tr>
                                    <th scope="row">
                                        <label for="TypeLecon"><fmt:message key="courseType"/></label>
                                    </th>
                                    <td colspan="2">
                                        <%-- MODIF 7 : id unique "TypeLecon" --%>
                                        <select name="TypeLecon"
                                                id="TypeLecon"
                                                required
                                                aria-required="true"
                                                class="form-control form-select form-select-lg mb-3">
                                            <c:choose>
                                                <c:when test="${(empty seance) || (empty seance.intitule)}">
                                                    <option value="" disabled selected><fmt:message key="chooseCourseType"/></option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${seance.intitule.intitule}">${seance.intitule.intitule}</option>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:forEach var="typeLecon" items="${typeLeconsList}">
                                                <option value="${typeLecon.intitule}">${typeLecon.intitule}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>

                                <%-- GROUPES --%>
                                <tr>
                                    <th scope="row"><fmt:message key="group_s"/></th>
                                    <td>
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-md-5">
                                                    <p class="mb-1" id="titre-groupes-sel"><strong>Groupes sélectionnés</strong></p>
                                                    <%-- MODIF 8 : Instructions clavier pour lecteur d'écran --%>
                                                    <p id="aide-groupes-sel" class="visually-hidden">Appuyez sur Entrée ou Espace pour retirer un groupe.</p>
                                                    <div id="listgroupes"
                                                         class="list-group overflow-auto border border-primary"
                                                         style="height:350px;overflow-y: scroll;"
                                                         tabindex="0"
                                                         role="listbox"
                                                         aria-labelledby="titre-groupes-sel"
                                                         aria-describedby="aide-groupes-sel"
                                                         aria-multiselectable="true"
                                                         ondrop="dropAndRename(event, 'ml', 'm')"
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="itemIter" items="${seance.groupeList}">
                                                            <div class="list-group-item list-group-item-action"
                                                                 id="m_${itemIter.nomGroupe}"
                                                                 draggable="true"
                                                                 ondragstart="drag(event)"
                                                                 tabindex="0"
                                                                 role="option"
                                                                 aria-selected="true"
                                                                 aria-label="Groupe sélectionné : ${itemIter.nomGroupe}. Appuyez sur Entrée pour retirer."
                                                                 onkeydown="handleItemKeydown(event, 'listAllgroupes', 'ml', 'm', 'groupe', this)">
                                                                <input type="hidden" name="m[${itemIter.nomGroupe}]" value="${itemIter.nomGroupe}"/>
                                                                ${itemIter.nomGroupe}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <div class="col-md-5">
                                                    <p class="mb-1" id="titre-groupes-dispo"><strong>Tous les groupes</strong></p>
                                                    <p id="aide-groupes-dispo" class="visually-hidden">Appuyez sur Entrée ou Espace pour ajouter un groupe.</p>
                                                    <div id="listAllgroupes"
                                                         class="list-group overflow-auto border border-primary"
                                                         style="height:350px;overflow-y: scroll;"
                                                         tabindex="0"
                                                         role="listbox"
                                                         aria-labelledby="titre-groupes-dispo"
                                                         aria-describedby="aide-groupes-dispo"
                                                         aria-multiselectable="true"
                                                         ondrop="dropAndRename(event, 'm', 'ml')"
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="groupe" items="${groupesList}">
                                                            <div class="list-group-item list-group-item-action"
                                                                 id="ml_${groupe.nomGroupe}"
                                                                 draggable="true"
                                                                 ondragstart="drag(event)"
                                                                 style="display:block"
                                                                 tabindex="0"
                                                                 role="option"
                                                                 aria-selected="false"
                                                                 aria-label="Groupe disponible : ${groupe.nomGroupe}. Appuyez sur Entrée pour ajouter."
                                                                 onkeydown="handleItemKeydown(event, 'listgroupes', 'm', 'ml', 'groupe', this)">
                                                                <input type="hidden" name="ml[${groupe.nomGroupe}]" value="${groupe.nomGroupe}"/>
                                                                ${groupe.nomGroupe}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div>
                                                        <label for="listAllgroupesFilter" class="visually-hidden">Rechercher un groupe</label>
                                                        <input type="text"
                                                               id="listAllgroupesFilter"
                                                               placeholder="Rechercher..."
                                                               style="width:100%"
                                                               onkeyup="filterList('listAllgroupes', 'listAllgroupesFilter')">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>

                                <%-- ENSEIGNANTS --%>
                                <tr>
                                    <th scope="row"><fmt:message key="enseignant_s"/></th>
                                    <td>
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-md-5">
                                                    <p class="mb-1" id="titre-ens-sel"><strong>Enseignants sélectionnés</strong></p>
                                                    <p id="aide-ens-sel" class="visually-hidden">Appuyez sur Entrée ou Espace pour retirer un enseignant.</p>
                                                    <div id="listenseignants"
                                                         class="list-group overflow-auto border border-primary"
                                                         style="height:350px;overflow-y: scroll;"
                                                         tabindex="0"
                                                         role="listbox"
                                                         aria-labelledby="titre-ens-sel"
                                                         aria-describedby="aide-ens-sel"
                                                         aria-multiselectable="true"
                                                         ondrop="dropAndRename(event, 'el', 'e')"
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="itemIter" items="${seance.enseignantList}">
                                                            <div class="list-group-item list-group-item-action"
                                                                 id="e_${itemIter.initiales}"
                                                                 draggable="true"
                                                                 ondragstart="drag(event)"
                                                                 tabindex="0"
                                                                 role="option"
                                                                 aria-selected="true"
                                                                 aria-label="Enseignant sélectionné : ${itemIter.nomEnseignant} ${itemIter.prenom}. Appuyez sur Entrée pour retirer."
                                                                 onkeydown="handleItemKeydown(event, 'listAllenseignants', 'el', 'e', 'enseignant', this)">
                                                                <input type="hidden" name="e[${itemIter.initiales}]" value="${itemIter.initiales}"/>
                                                                ${itemIter.nomEnseignant} ${itemIter.prenom}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <div class="col-md-5">
                                                    <p class="mb-1" id="titre-ens-dispo"><strong>Tous les enseignants</strong></p>
                                                    <p id="aide-ens-dispo" class="visually-hidden">Appuyez sur Entrée ou Espace pour ajouter un enseignant.</p>
                                                    <div id="listAllenseignants"
                                                         class="list-group overflow-auto border border-primary"
                                                         style="height:350px;overflow-y: scroll;"
                                                         tabindex="0"
                                                         role="listbox"
                                                         aria-labelledby="titre-ens-dispo"
                                                         aria-describedby="aide-ens-dispo"
                                                         aria-multiselectable="true"
                                                         ondrop="dropAndRename(event, 'e', 'el')"
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="enseignant" items="${enseignantsList}">
                                                            <div class="list-group-item list-group-item-action"
                                                                 id="el_${enseignant.initiales}"
                                                                 draggable="true"
                                                                 ondragstart="drag(event)"
                                                                 style="display:block"
                                                                 tabindex="0"
                                                                 role="option"
                                                                 aria-selected="false"
                                                                 aria-label="Enseignant disponible : ${enseignant.nomEnseignant} ${enseignant.prenom}. Appuyez sur Entrée pour ajouter."
                                                                 onkeydown="handleItemKeydown(event, 'listenseignants', 'e', 'el', 'enseignant', this)">
                                                                <input type="hidden" name="el[${enseignant.initiales}]" value="${enseignant.initiales}"/>
                                                                ${enseignant.nomEnseignant} ${enseignant.prenom}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div>
                                                        <label for="listAllenseignantsFilter" class="visually-hidden">Rechercher un enseignant</label>
                                                        <input type="text"
                                                               id="listAllenseignantsFilter"
                                                               placeholder="<fmt:message key="search"/>"
                                                               style="width:100%"
                                                               onkeyup="filterList('listAllenseignants', 'listAllenseignantsFilter')">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>

                                <%-- SALLES --%>
                                <tr>
                                    <th scope="row"><fmt:message key="room_s"/></th>
                                    <td>
                                        <div class="container">
                                            <div class="row">
                                                <div class="col-md-5">
                                                    <p class="mb-1" id="titre-salles-sel"><strong>Salles sélectionnées</strong></p>
                                                    <p id="aide-salles-sel" class="visually-hidden">Appuyez sur Entrée ou Espace pour retirer une salle.</p>
                                                    <div id="listsalles"
                                                         class="list-group overflow-auto border border-primary"
                                                         style="height:350px;overflow-y: scroll;"
                                                         tabindex="0"
                                                         role="listbox"
                                                         aria-labelledby="titre-salles-sel"
                                                         aria-describedby="aide-salles-sel"
                                                         aria-multiselectable="true"
                                                         ondrop="dropAndRename(event, 'sl', 's')"
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="itemIter" items="${seance.salleList}">
                                                            <div class="list-group-item list-group-item-action"
                                                                 id="s_${itemIter.numeroSalle}"
                                                                 draggable="true"
                                                                 ondragstart="drag(event)"
                                                                 tabindex="0"
                                                                 role="option"
                                                                 aria-selected="true"
                                                                 aria-label="Salle sélectionnée : ${itemIter.numeroSalle}. Appuyez sur Entrée pour retirer."
                                                                 onkeydown="handleItemKeydown(event, 'listAllsalles', 'sl', 's', 'salle', this)">
                                                                <input type="hidden" name="s[${itemIter.numeroSalle}]" value="${itemIter.numeroSalle}"/>
                                                                ${itemIter.numeroSalle}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                </div>
                                                <div class="col-md-5">
                                                    <p class="mb-1" id="titre-salles-dispo"><strong>Toutes les salles</strong></p>
                                                    <p id="aide-salles-dispo" class="visually-hidden">Appuyez sur Entrée ou Espace pour ajouter une salle.</p>
                                                    <div id="listAllsalles"
                                                         class="list-group overflow-auto border border-primary"
                                                         style="height:350px;overflow-y: scroll;"
                                                         tabindex="0"
                                                         role="listbox"
                                                         aria-labelledby="titre-salles-dispo"
                                                         aria-describedby="aide-salles-dispo"
                                                         aria-multiselectable="true"
                                                         ondrop="dropAndRename(event, 's', 'sl')"
                                                         ondragover="allowDrop(event)">
                                                        <c:forEach var="salle" items="${sallesList}">
                                                            <div class="list-group-item list-group-item-action"
                                                                 id="sl_${salle.numeroSalle}"
                                                                 draggable="true"
                                                                 ondragstart="drag(event)"
                                                                 style="display:block"
                                                                 tabindex="0"
                                                                 role="option"
                                                                 aria-selected="false"
                                                                 aria-label="Salle disponible : ${salle.numeroSalle}. Appuyez sur Entrée pour ajouter."
                                                                 onkeydown="handleItemKeydown(event, 'listsalles', 's', 'sl', 'salle', this)">
                                                                <input type="hidden" name="sl[${salle.numeroSalle}]" value="${salle.numeroSalle}"/>
                                                                ${salle.numeroSalle}
                                                            </div>
                                                        </c:forEach>
                                                    </div>
                                                    <div>
                                                        <label for="listAllsallesFilter" class="visually-hidden">Rechercher une salle</label>
                                                        <input type="text"
                                                               id="listAllsallesFilter"
                                                               placeholder="<fmt:message key="search"/>"
                                                               style="width:100%"
                                                               onkeyup="filterList('listAllsalles', 'listAllsallesFilter')">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="2" class="text-center">
                                            <input type="hidden" name="connexion" value="${user.connectionCode}">
                                            <%-- MODIF 9 : aria-label explicite sur le bouton submit --%>
                                            <button type="submit"
                                                    class="btn btn-block btn-primary"
                                                    aria-label="Enregistrer la séance dans l'emploi du temps">
                                                <fmt:message key="save"/>
                                            </button>
                                        </td>
                                    </tr>
                                </tfoot>
                            </table>
                        </form>
                    </div>
                </div>

                <%-- MODIF 10 : aria-live sur chaque alerte, pas sur le conteneur parent --%>
                <div class="row">
                    <div class="col-md-12">
                        <c:if test='${newseance}'>
                            <div class="alert alert-success" role="status" aria-live="polite" aria-atomic="true">
                                <fmt:message key="seanceSaved"/>
                            </div>
                        </c:if>
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger" role="alert" aria-live="assertive" aria-atomic="true">
                                ${error}
                            </div>
                        </c:if>
                        <c:if test="${not empty warningsSalles}">
                            <c:forEach var="w" items="${warningsSalles}">
                                <div class="alert alert-warning" role="alert" aria-live="polite" aria-atomic="true">${w}</div>
                            </c:forEach>
                        </c:if>
                        <c:if test="${not empty warningsMaquette}">
                            <c:forEach var="w" items="${warningsMaquette}">
                                <div class="alert alert-info" role="status" aria-live="polite" aria-atomic="true">${w}</div>
                            </c:forEach>
                        </c:if>
                    </div>
                </div>

            </div>
        </div>

        <%-- MODIF 11 : Gestionnaire clavier — appelle renameDraggedItem() de main.js --%>
        <script>
            function handleItemKeydown(event, targetListId, newPrefix, oldPrefix, typeElement, item) {
                if (event.key !== 'Enter' && event.key !== ' ')
                    return;
                event.preventDefault();

                var targetList = document.getElementById(targetListId);
                if (!targetList)
                    return;

                var isAdding = item.getAttribute('aria-selected') === 'false';
                var nom = item.textContent.trim();

                // Réutilise renameDraggedItem() déjà dans main.js pour cohérence avec le drag & drop
                renameDraggedItem(item, oldPrefix, newPrefix);

                targetList.appendChild(item);

                if (isAdding) {
                    item.setAttribute('aria-selected', 'true');
                    item.setAttribute('aria-label',
                            capitalizeFirst(typeElement) + ' sélectionné : ' + nom + '. Appuyez sur Entrée pour retirer.');
                    item.setAttribute('onkeydown',
                            "handleItemKeydown(event, '" + item.closest('[role=\"listbox\"]').id + "', '" + oldPrefix + "', '" + newPrefix + "', '" + typeElement + "', this)");
                } else {
                    item.setAttribute('aria-selected', 'false');
                    item.setAttribute('aria-label',
                            capitalizeFirst(typeElement) + ' disponible : ' + nom + '. Appuyez sur Entrée pour ajouter.');
                    item.setAttribute('onkeydown',
                            "handleItemKeydown(event, '" + targetListId + "', '" + newPrefix + "', '" + oldPrefix + "', '" + typeElement + "', this)");
                }

                item.focus();
                annoncerSR(isAdding ? typeElement + ' ' + nom + ' ajouté.' : typeElement + ' ' + nom + ' retiré.');
            }

            function annoncerSR(message) {
                var zone = document.getElementById('sr-annonce');
                if (!zone)
                    return;
                zone.textContent = '';
                setTimeout(function () {
                    zone.textContent = message;
                }, 50);
            }

            function capitalizeFirst(str) {
                return str.charAt(0).toUpperCase() + str.slice(1);
            }
        </script>

    </body>
</html>
