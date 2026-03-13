package tp.projetpappl.repositories;

import tp.projetpappl.items.*;

/**
 * Custom repository methods for Connection
 * 
 * @author Assistant
 */
public interface ConnectionRepositoryCustom {

    /**
     * Create new Connection
     * @param login
     * @return Connection
     */
    public Connection create(String login);

    /**
     * Remove Connection
     * @param item
     */
    public void remove(Connection item);

    /**
     * Get a Connection
     * @param connectionCode
     * @return Connection
     */
    public Connection getByConnectionCode(String connectionCode);
}
