package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.projetpappl.items.Admin;
import tp.projetpappl.items.Connection;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.repositories.AdminRepository;
import tp.projetpappl.repositories.ConnectionRepository;
import tp.projetpappl.repositories.EnseignantRepository;
import tp.projetpappl.repositories.EnseignementRepository;

/**
 * Composant Spring centralisé pour la gestion de l'authentification
 * et des droits d'accès dans l'application.
 *
 * Toutes les vérifications d'accès passent par cette classe,
 * ce qui évite la duplication de code dans chaque controller.
 *
 * @author Oussama
 */
@Component
public class AuthHelper {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private EnseignementRepository enseignementRepository;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Récupère l'utilisateur actuellement connecté à partir du code de session
     * transmis en paramètre de la requête HTTP.
     *
     * Le code de session est un identifiant unique généré à la connexion CAS
     * et transmis dans chaque formulaire via un champ caché "connexion".
     *
     * @param request la requête HTTP entrante
     * @return l'objet Connection correspondant à la session, ou null si
     *         le code est absent ou invalide
     */
    public Connection getAuthenticatedUser(HttpServletRequest request) {
        String connectionCode = request.getParameter("connexion");
        if (connectionCode == null || connectionCode.isEmpty()) {
            return null;
        }
        return connectionRepository.getByConnectionCode(connectionCode);
    }

    /**
     * Vérifie si la requête est associée à une session valide.
     *
     * @param request la requête HTTP entrante
     * @return true si l'utilisateur est authentifié, false sinon
     */
    public boolean isAuthenticated(HttpServletRequest request) {
        return getAuthenticatedUser(request) != null;
    }

    /**
     * Récupère l'enseignant correspondant à la session courante,
     * en faisant le lien entre le login CAS (stocké dans la session)
     * et le champ login de la table enseignant.
     *
     * Ce champ doit être renseigné avec l'uid CAS exact de l'enseignant
     * pour que la correspondance fonctionne.
     *
     * @param request la requête HTTP entrante
     * @return l'objet Enseignant correspondant, ou null si la session est
     *         invalide ou si aucun enseignant ne correspond au login CAS
     */
    public Enseignant getEnseignantFromConnection(HttpServletRequest request) {
        Connection connection = getAuthenticatedUser(request);
        if (connection == null) {
            return null;
        }
        return enseignantRepository.getByLogin(connection.getConnectionLogin());
    }

    /**
     * Vérifie si l'utilisateur connecté est un administrateur global,
     * c'est-à-dire si son login CAS est présent dans la table admin.
     *
     * @param request la requête HTTP entrante
     * @return true si l'utilisateur est admin global, false sinon
     */
    public boolean isAdmin(HttpServletRequest request) {
        Connection connection = getAuthenticatedUser(request);
        if (connection == null) {
            return false;
        }
        return adminRepository.getByLogin(connection.getConnectionLogin()) != null;
    }

    /**
     * Vérifie si l'utilisateur connecté a le droit de modifier les données
     * d'une filière donnée.
     *
     * La règle est la suivante :
     * - Un administrateur global peut modifier toutes les filières.
     * - Un responsable peut modifier uniquement les filières pour lesquelles
     *   il est désigné responsable d'au moins un enseignement.
     * - Tout autre utilisateur se voit refuser l'accès.
     *
     * @param request la requête HTTP entrante
     * @param filiere le nom de la filière dont on veut vérifier l'accès
     * @return true si l'utilisateur peut modifier cette filière, false sinon
     */
    public boolean canModifyFiliere(HttpServletRequest request, String filiere) {
        // Un admin global bypasse toutes les vérifications de filière
        if (isAdmin(request)) {
            return true;
        }
        // Une filière nulle ou vide ne peut pas être vérifiée
        if (filiere == null || filiere.isEmpty()) {
            return false;
        }
        // On cherche l'enseignant correspondant à la session courante
        Enseignant enseignant = getEnseignantFromConnection(request);
        if (enseignant == null) {
            return false;
        }
        // On vérifie qu'il est responsable d'au moins un enseignement dans cette filière
        return enseignementRepository.existsByResponsableAndFiliere(enseignant, filiere);
    }

    /**
     * Vérifie si l'utilisateur connecté a le droit de modifier les données
     * globales de l'application (enseignants, salles, groupes).
     *
     * Seuls les administrateurs globaux ont ce droit.
     *
     * @param request la requête HTTP entrante
     * @return true si l'utilisateur est admin global, false sinon
     */
    public boolean canModifyGlobal(HttpServletRequest request) {
        return isAdmin(request);
    }
}