package tp.projetpappl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Admin;

/**
 *
 * @author Oussama
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, String>, AdminRepositoryCustom {
}