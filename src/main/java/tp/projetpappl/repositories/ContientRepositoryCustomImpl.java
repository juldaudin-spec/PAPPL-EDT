/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.controllers.Tools;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.TypeLecon;
/**
 *
 * @author nathan
 */
@Repository
public class ContientRepositoryCustomImpl implements ContientRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    @Lazy
    ContientRepository contientRepository;
    @Autowired
    @Lazy
    TypeLeconRepository typeLeconRepository;
    @Autowired
    @Lazy
    EnseignementRepository enseignementRepository;
    @Autowired
    @Lazy
    GroupeRepository groupeRepository;
    
    @Override
    public List<Contient> findContientByIntituleByEnseignementByGroupe(TypeLecon intitule, Enseignement acronyme, Groupe groupe) {
        List<Contient> listContient =null;
        if (intitule!=null&&acronyme!=null&&groupe!=null){
            String requete = "SELECT c FROM Contient c JOIN c.groupeList g WHERE c.acronyme= :acronyme AND c.intitule= :intitule AND g.nomGroupe= :nomGroupe";
            TypedQuery<Contient> query = entityManager.createQuery(requete, Contient.class);
            query.setParameter("acronyme", acronyme);
            query.setParameter("intitule", intitule);
            query.setParameter("nomGroupe", groupe.getNomGroupe());
            listContient = query.getResultList();
        }
        return listContient;
    }

    @Override
    public List<TypeLecon> findIntituleByEnseignementByGroupe(Enseignement acronyme, Groupe groupe) {
        List<TypeLecon> listType = null;
        if (acronyme!=null&&groupe!=null){
            String requete = "SELECT c.intitule FROM Contient c JOIN c.groupeList g WHERE c.acronyme= :acronyme AND g.nomGroupe= :nomGroupe";
            TypedQuery<TypeLecon> query = entityManager.createQuery(requete, TypeLecon.class);
            query.setParameter("acronyme", acronyme);
            query.setParameter("nomGroupe", groupe.getNomGroupe());
            listType = query.getResultList();
        }
        return listType;
    }

    @Override
    public List<Enseignement> findEnseignementByGroupe(Groupe groupe) {
        String requete = "SELECT c.acronyme FROM Contient c JOIN c.groupeList g WHERE g.nomGroupe= :nomGroupe";
        TypedQuery<Enseignement> query = entityManager.createQuery(requete, Enseignement.class);
        query.setParameter("nomGroupe", groupe.getNomGroupe());
        return query.getResultList();
    }

    @Override
    public List<Groupe> findGroupeByEnseignement(Enseignement acronyme) {
        List<Groupe> listGroupe = null;
        if (acronyme!=null){
            List<String> listID = groupeRepository.findGroupeByEnseignement(acronyme.getAcronyme());
            listGroupe=new ArrayList<>();
            for(String id : listID){                
                listGroupe.add(groupeRepository.getReferenceById(id));
            }
        }
        return listGroupe;
    }

    
    }
