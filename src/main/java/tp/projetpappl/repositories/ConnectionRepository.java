package tp.projetpappl.repositories;

import tp.projetpappl.items.Connection;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Oussama
 */

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, String>, ConnectionRepositoryCustom {

    public Collection<Connection> findByConnectionCode(@Param("connectionCode")String connectionCode);

    public Collection<Connection> findByConnectionLogin(@Param("connectionLogin")String connectionLogin);
}
