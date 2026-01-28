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
    public List<Contient> findContientByIntituleByEnseignementByGroupe(String intitule, String acronyme, String nomGroupe) {
        List<Contient> listContient =null;
        if (intitule!=null&&acronyme!=null&&nomGroupe!=null){
            String requete = "SELECT contient_id FROM contient c JOIN participe p ON p.contient_id=c.contient_id WHERE c.acronyme= :acronyme AND c.intitule= :intitule AND p.nom_groupe= :nomGroupe";
            TypedQuery<String> query = entityManager.createQuery(requete, String.class);
            query.setParameter("acronyme", acronyme);
            query.setParameter("intitule", intitule);
            query.setParameter("nomGroupe", nomGroupe);
            List<String> listID = query.getResultList();
            listContient=new ArrayList<>(listID.size());
            int id;
            for(String idStr : listID){
                id=Tools.getIntFromString(idStr);
                listContient.add(contientRepository.getReferenceById(id));
            }
        }
        return listContient;
    }

    @Override
    public List<TypeLecon> findIntituleByEnseignementByGroupe(String acronyme, String nomGroupe) {
        List<TypeLecon> listType = null;
        if (acronyme!=null&&nomGroupe!=null){
            String requete = "SELECT intitule FROM contient c JOIN participe p ON p.contient_id=c.contient_id WHERE c.acronyme= :acronyme AND p.nom_groupe= :nomGroupe";
            TypedQuery<String> query = entityManager.createQuery(requete, String.class);
            query.setParameter("acronyme", acronyme);
            query.setParameter("nomGroupe", nomGroupe);
            List<String> listID = query.getResultList();
            listType=new ArrayList<>(listID.size());
            for(String id : listID){                
                listType.add(typeLeconRepository.getReferenceById(id));
            }
        }
        return listType;
    }

    @Override
    public List<Enseignement> findEnseignementByGroupe(String nomGroupe) {
        List<Enseignement> listEnseignement = null;
        if (nomGroupe!=null){
            List<String> listID = enseignementRepository.findAcronymeParGroupe(nomGroupe);
            listEnseignement=new ArrayList<>();
            for(String id : listID){                
                listEnseignement.add(enseignementRepository.getReferenceById(id));
            }
        }
        return listEnseignement;
    }

    @Override
    public List<Groupe> findGroupeByEnseignement(String acronyme) {
        List<Groupe> listGroupe = null;
        if (acronyme!=null){
            List<String> listID = groupeRepository.findGroupeParEnseignement(acronyme);
            listGroupe=new ArrayList<>();
            for(String id : listID){                
                listGroupe.add(groupeRepository.getReferenceById(id));
            }
        }
        return listGroupe;
    }

    
    }
