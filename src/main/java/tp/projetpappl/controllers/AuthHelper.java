package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.projetpappl.items.Connection;
import tp.projetpappl.repositories.ConnectionRepository;

/**
 * Helper class for authentication checks
 *
 * @author Assistant
 */
@Component
public class AuthHelper {

    @Autowired
    private ConnectionRepository connectionRepository;

    /**
     * Check if user is authenticated and return user object
     *
     * @param request HTTP request
     * @return Connection object if authenticated, null otherwise
     */
    public Connection getAuthenticatedUser(HttpServletRequest request) {
        String connectionCode = request.getParameter("connexion");

        if (connectionCode == null || connectionCode.isEmpty()) {
            return null;
        }

        return connectionRepository.getByConnectionCode(connectionCode);
    }

    /**
     * Check if user is authenticated
     *
     * @param request HTTP request
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated(HttpServletRequest request) {
        return getAuthenticatedUser(request) != null;
    }
}