/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
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
