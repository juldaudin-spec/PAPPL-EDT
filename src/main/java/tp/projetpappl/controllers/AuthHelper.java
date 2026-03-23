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

    public Connection getAuthenticatedUser(HttpServletRequest request) {
        String connectionCode = request.getParameter("connexion");
        if (connectionCode == null || connectionCode.isEmpty()) {
            return null;
        }
        return connectionRepository.getByConnectionCode(connectionCode);
    }

    public boolean isAuthenticated(HttpServletRequest request) {
        return getAuthenticatedUser(request) != null;
    }

    public Enseignant getEnseignantFromConnection(HttpServletRequest request) {
        Connection connection = getAuthenticatedUser(request);
        if (connection == null) {
            return null;
        }
        return enseignantRepository.getByLogin(connection.getConnectionLogin());
    }

    public boolean isAdmin(HttpServletRequest request) {
        Connection connection = getAuthenticatedUser(request);
        if (connection == null) {
            return false;
        }
        return adminRepository.getByLogin(connection.getConnectionLogin()) != null;
    }

    public boolean canModifyFiliere(HttpServletRequest request, String filiere) {
        if (isAdmin(request)) {
            return true;
        }
        if (filiere == null || filiere.isEmpty()) {
            return false;
        }
        Enseignant enseignant = getEnseignantFromConnection(request);
        if (enseignant == null) {
            return false;
        }
        return enseignementRepository.existsByResponsableAndFiliere(enseignant, filiere);
    }

    public boolean canModifyGlobal(HttpServletRequest request) {
        return isAdmin(request);
    }
}