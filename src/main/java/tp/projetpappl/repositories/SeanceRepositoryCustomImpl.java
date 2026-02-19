/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import tp.projetpappl.controllers.Tools;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Salle;
import tp.projetpappl.items.Seance;
import tp.projetpappl.items.TypeLecon;

/**
 *
 * @author nathan
 */
@Repository
public class SeanceRepositoryCustomImpl implements SeanceRepositoryCustom {

    @Autowired
    @Lazy
    EnseignementRepository enseignementRepository;
    @Autowired
    @Lazy
    EnseignantRepository enseignantRepository;
    @Autowired
    @Lazy
    TypeLeconRepository typeLeconRepository;
    @Autowired
    @Lazy
    GroupeRepository groupeRepository;
    @Autowired
    @Lazy
    SalleRepository salleRepository;

    @Autowired
    @Lazy
    SeanceRepository seanceRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Seance create(Enseignement enseignement, List<Enseignant> Enseignants, TypeLecon typeLecon, List<Groupe> Groupes, List<Salle> Salles, Date hDebut, int duree) {

        // Ensure we have full data
        if (enseignement != null) {
            enseignement = enseignementRepository.getByAcronyme(enseignement.getAcronyme());
        }
        for (int i = 0; i < Groupes.size(); i++) {
            if (Groupes.get(i) != null) {
                Groupes.set(i, groupeRepository.getByNomGroupe(Groupes.get(i).getNomGroupe()));
            }
        }
        for (int i = 0; i < Salles.size(); i++) {
            if (Salles.get(i) != null) {
                Salles.set(i, salleRepository.getByNumeroSalle(Salles.get(i).getNumeroSalle()));
            }
        }
        for (int i = 0; i < Enseignants.size(); i++) {
            if (Enseignants.get(i) != null) {
                Enseignants.set(i, enseignantRepository.getByInitiales(Enseignants.get(i).getInitiales()));
            }
        }

        // Build new seance
        if ((enseignement != null) && (typeLecon != null) && (hDebut != null)) {
            Seance item = new Seance();
            item.setHDebut(hDebut);
            item.setSalleList(Salles);
            item.setEnseignantList(Enseignants);
            item.setGroupeList(Groupes);
            item.setAcronyme(enseignement);
            item.setIntitule(typeLecon);
            item.setDuree(duree);
            seanceRepository.saveAndFlush(item);

            Optional<Seance> result = seanceRepository.findById(item.getIdSeance());
            if (result.isPresent()) {
                item = result.get();

                // Set reverse fields
                enseignement.getSeanceList().add(item);
                enseignementRepository.saveAndFlush(enseignement);
                for (Enseignant enseignant : Enseignants) {
                    enseignant.getSeanceList().add(item);
                    enseignantRepository.saveAndFlush(enseignant);
                }
                for (Groupe groupe : Groupes) {
                    groupe.getSeanceList().add(item);
                    groupeRepository.saveAndFlush(groupe);
                }
                for (Salle salle : Salles) {
                    salle.getSeanceList().add(item);
                    salleRepository.saveAndFlush(salle);
                }
                // return item
                return item;
            }
        }
        return null;
    }

    @Override
    public List<Seance> findSeanceByGroupe(Groupe groupe) {
        List<Seance> listSeance = null;
        if (groupe != null) {
            String requete = "SELECT s FROM Seance s JOIN s.groupeList g WHERE g.nomGroupe= :nomGroupe";
            TypedQuery<Seance> query = entityManager.createQuery(requete, Seance.class);
            query.setParameter("nomGroupe", groupe.getNomGroupe());
            listSeance = query.getResultList();
        }
        return listSeance;
    }

    @Override
    public void sortByEnseignementByIntitule(List<Seance> listSeance, List<Enseignement> listEnseignement, List<List<TypeLecon>> listIntitule) {
        List<Seance> listSeanceTemp = new ArrayList<>(listSeance.size());
        for (int i = 0; i < listEnseignement.size(); i++) {
            Enseignement enseignement = listEnseignement.get(i);
            for (TypeLecon typeLecon : listIntitule.get(i)) {
                for (Seance seance : listSeance) {
                    if ((seance.getAcronyme().getAcronyme() == null ? enseignement.getAcronyme() == null : seance.getAcronyme().getAcronyme().equals(enseignement.getAcronyme())) && (seance.getIntitule().getIntitule() == null ? typeLecon.getIntitule() == null : seance.getIntitule().getIntitule().equals(typeLecon.getIntitule()))) {
                        listSeanceTemp.add(seance);
                    } else {
                        if (!listEnseignement.contains(seance.getAcronyme())) {
                            listEnseignement.add(seance.getAcronyme());
                        } else {
                            if (!listIntitule.get(i).contains(seance.getIntitule())) {
                                listIntitule.get(i).add(seance.getIntitule());
                            }
                        }
                    }
                }
            }
        }
        listSeance = listSeanceTemp;
    }

    @Override
    public List<Seance> findSeanceByEnseignement(Enseignement enseignement) {
        List<Seance> listSeance = null;
        if (enseignement != null) {
            String requete = "SELECT s FROM Seance s WHERE s.acronyme= :acronyme";
            TypedQuery<Seance> query = entityManager.createQuery(requete, Seance.class);
            query.setParameter("acronyme", enseignement);
            listSeance = query.getResultList();
        }
        return listSeance;
    }
    

    @Override
    public Seance update(int IdSeance, Enseignement enseignement, List<Enseignant> Enseignants, TypeLecon typeLecon, List<Groupe> Groupes, List<Salle> Salles, Date hDebut, int duree) {
        if (IdSeance > 0) {
            IdSeance = seanceRepository.getReferenceById(IdSeance).getIdSeance();
        }
        // Ensure we have full data
        if (enseignement != null) {
            enseignement = enseignementRepository.getReferenceById(enseignement.getAcronyme());
        }
        for (int i = 0; i < Enseignants.size(); i++) {
            Enseignants.set(i, enseignantRepository.getByInitiales(Enseignants.get(i).getInitiales()));
        }
        if (typeLecon != null) {
            typeLecon = typeLeconRepository.getReferenceById(typeLecon.getIntitule());
        }
        for (int i = 0; i < Groupes.size(); i++) {
            Groupes.set(i, groupeRepository.getByNomGroupe(Groupes.get(i).getNomGroupe()));
        }
        for (int i = 0; i < Salles.size(); i++) {
            Salles.set(i, salleRepository.getByNumeroSalle(Salles.get(i).getNumeroSalle()));
        }

        // Build new seance
        if ((IdSeance > 0) && (enseignement != null) && (typeLecon != null) && (hDebut != null)) {
            Seance item = seanceRepository.getReferenceById(IdSeance);
            item.setHDebut(hDebut);
            item.setSalleList(Salles);
            item.setEnseignantList(Enseignants);
            item.setGroupeList(Groupes);
            item.setAcronyme(enseignement);
            item.setIntitule(typeLecon);
            item.setDuree(duree);
            seanceRepository.saveAndFlush(item);

            Optional<Seance> result = seanceRepository.findById(item.getIdSeance());
            if (result.isPresent()) {
                item = result.get();

                // Set reverse fields
                enseignement.getSeanceList().add(item);
                enseignementRepository.saveAndFlush(enseignement);
                for (Enseignant enseignant : Enseignants) {
                    enseignant.getSeanceList().add(item);
                    enseignantRepository.saveAndFlush(enseignant);
                }
                for (Groupe groupe : Groupes) {
                    groupe.getSeanceList().add(item);
                    groupeRepository.saveAndFlush(groupe);
                }
                for (Salle salle : Salles) {
                    salle.getSeanceList().add(item);
                    salleRepository.saveAndFlush(salle);
                }

                // return item
                return item;
            }
        }
        return null;
    }
}
