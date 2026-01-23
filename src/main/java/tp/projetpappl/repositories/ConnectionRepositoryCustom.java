/* -----------------------------------------
 * Projet Kepler
 *
 * Ecole Centrale Nantes
 * Jean-Yves MARTIN
 * ----------------------------------------- */
 
package tp.projetpappl.repositories;

import tp.projetpappl.items.*;

/**
 *
 * @author kwyhr
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
     * @return connectCode
     */
    public Connection getByConnectionCode(String connectionCode);

}
