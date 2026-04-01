package tp.projetpappl.repositories;

import tp.projetpappl.items.*;

/**
 *
 * @author Oussama
 */
public interface ConnectionRepositoryCustom {


    public Connection create(String login);


    public void remove(Connection item);


    public Connection getByConnectionCode(String connectionCode);
}
