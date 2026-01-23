/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;
/**
 *
 * @author nathan
 */
@Repository
public class EnseignementRepositoryCustomImpl implements EnseignementRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Lazy
    EnseignementRepository enseignementRepository;
    @Autowired
    @Lazy
    EnseignantRepository enseignantRepository;
    
    @Override
    public List<String> findAllAcronyme() {
        String requete = "SELECT Acronyme FROM Enseignement";
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }

    @Override
    public List<String> findAcronymeParEnseignant(String acronyme) {// Il y a un problème ici entre le Custom et le CustomImpl
        String requete = "SELECT acronyme FROM Enseigne WHERE acronyme="+acronyme;
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }
    
    @Override
    public List<String> findAcronymeParGroupe(String nomGroupe) {
        String requete = "SELECT acronyme FROM Contient JOIN Etudie ON Contient.contient_id=Etudie.contient_id WHERE nom_groupe="+nomGroupe;
        TypedQuery<String> query = entityManager.createQuery(requete, String.class);
        return query.getResultList();
    }
    @Override
    public Enseignement getByAcronyme(String acronyme) {
        try {
            return entityManager
                    .createNamedQuery("Enseignement.findByAcronyme", Enseignement.class)
                    .setParameter("acronyme", acronyme)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
    @Override
    public Enseignement update(String acronyme, String nom, String filiere, Enseignant responsable){
        Enseignement enseignementAcronyme = null;
        if(acronyme != null){
            // Ensure validity from database
            enseignementAcronyme = getByAcronyme(acronyme);
            acronyme = enseignementAcronyme.getAcronyme();
        }
        if ((acronyme != null)
                && ((nom != null) && (! nom.isEmpty())
                && (filiere != null) && (! filiere.isEmpty())
                && (responsable != null))){
            // Update data
            enseignementAcronyme = getByAcronyme(acronyme);
            enseignementAcronyme.setFiliere(filiere);
            enseignementAcronyme.setNomEnseignement(nom);
            // Save to database
            responsable.getEnseignementList1().add(enseignementAcronyme);
            enseignantRepository.saveAndFlush(responsable);
            enseignementRepository.saveAndFlush(enseignementAcronyme);
            //Ensure we have the last version
            enseignementAcronyme = getByAcronyme(enseignementAcronyme.getAcronyme());
        }
        return enseignementAcronyme;
    }
    @Override
    public void remove(String acronyme){
        if (acronyme !=null){
            //Ensure validity from database
            Enseignement Enseignement = getByAcronyme(acronyme);
            acronyme = Enseignement.getAcronyme();
        }
        if (acronyme != null){
            enseignementRepository.delete(getByAcronyme(acronyme));
        }
    }
    @Override
    public Enseignement create(String acronyme, String nom, String filiere, Enseignant responsable){
        if ((nom != null) && (! nom.isEmpty())
                && (filiere != null) && (! filiere.isEmpty())
                && (responsable != null)
                && (acronyme != null && (!acronyme.isEmpty()))){
            Enseignement item = new Enseignement(acronyme);
            // Update data
            item.setNomEnseignement(nom);
            item.setFiliere(filiere);
            item.setResponsable(responsable);
            // Save to database
            enseignementRepository.saveAndFlush(item);
            responsable.getEnseignementList1().add(item);
            enseignantRepository.saveAndFlush(responsable);
            //Ensure we have the last version
            return getByAcronyme(acronyme);
        }
        return null;
    }
}
