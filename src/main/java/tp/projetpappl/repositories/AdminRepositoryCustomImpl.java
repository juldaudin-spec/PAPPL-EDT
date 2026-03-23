package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Admin;

@Repository
public class AdminRepositoryCustomImpl implements AdminRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    @Lazy
    private AdminRepository adminRepository;

    @Override
    public Admin getByLogin(String login) {
        try {
            return entityManager
                    .createNamedQuery("Admin.findByLogin", Admin.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    public Admin create(String login, String nom, String prenom) {
        if (login != null && !login.isEmpty() && nom != null && !nom.isEmpty()) {
            Admin item = new Admin(login);
            item.setNom(nom);
            item.setPrenom(prenom);
            adminRepository.saveAndFlush(item);
            return getByLogin(login);
        }
        return null;
    }

    @Override
    public void remove(String login) {
        if (login != null) {
            Admin admin = getByLogin(login);
            if (admin != null) {
                adminRepository.delete(admin);
            }
        }
    }
}