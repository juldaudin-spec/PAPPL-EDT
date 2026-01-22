/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.repositories;

import java.util.ArrayList;
import java.util.Date;
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
    public Seance create(Enseignement enseignement, Enseignant enseignant, TypeLecon typeLecon, Groupe groupe, Salle salle, Date hDebut, int duree) {

        // Ensure we have full data
        if (enseignement != null) {
            enseignement = enseignementRepository.getReferenceById(enseignement.getAcronyme());
        }
        if (enseignant != null) {
            enseignant = enseignantRepository.getReferenceById(enseignant.getInitiales());
        }
        if (typeLecon != null) {
            typeLecon = typeLeconRepository.getReferenceById(typeLecon.getIntitule());
        }
        if (groupe != null) {
            groupe = groupeRepository.getReferenceById(groupe.getNomGroupe());
        }

        // Build new seance
        if ((enseignement != null) && (typeLecon != null) && (hDebut != null)) {
            Seance item = new Seance();
            item.setHDebut(hDebut);
            ArrayList<Enseignant> ListEnseignant = new ArrayList<Enseignant>();
            ListEnseignant.add(enseignant);
            ArrayList<Groupe> ListGroupe = new ArrayList<Groupe>();
            ListGroupe.add(groupe);
            ArrayList<Salle> ListSalle = new ArrayList<Salle>();
            ListSalle.add(salle);
            item.setSalleList(ListSalle);
            item.setEnseignantList(ListEnseignant);
            item.setGroupeList(ListGroupe);
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
                enseignant.getSeanceList().add(item);
                enseignantRepository.saveAndFlush(enseignant);
                groupe.getSeanceList().add(item);
                groupeRepository.saveAndFlush(groupe);
                salle.getSeanceList().add(item);
                salleRepository.saveAndFlush(salle);

                // return item
                return item;
            }
        }
        return null;
    }
}
