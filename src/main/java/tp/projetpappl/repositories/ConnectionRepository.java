/* -----------------------------------------
 * Projet Kepler
 *
 * Ecole Centrale Nantes
 * Jean-Yves MARTIN
 * ----------------------------------------- */
 
package tp.projetpappl.repositories;

import tp.projetpappl.items.Connection;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author kwyhr
 */
@Repository
public interface ConnectionRepository extends JpaRepository<Connection, String>, ConnectionRepositoryCustom {

    /**
     * Find all entries with the code
     * @param connectionCode
     * @return
     */
    public Collection<Connection> findByConnectionCode(@Param("connectionCode")String connectionCode);

    /**
     * Find all entries with the login
     * @param connectionLogin
     * @return
     */
    public Collection<Connection> findByConnectionLogin(@Param("connectionLogin")String connectionLogin);

}
