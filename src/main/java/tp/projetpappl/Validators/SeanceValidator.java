package tp.projetpappl.validators;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.projetpappl.items.Seance;
import tp.projetpappl.items.Salle;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.repositories.SeanceRepository;

@Component
public class SeanceValidator {

    @Autowired
    private SeanceRepository seanceRepository;

    /**
     * Vérifie TOUS les conflits (salle + enseignant + groupe)
     * Retourne un message d'erreur si conflit, null sinon
     */
    public String verifierTousLesConflits(String numeroSalle, String initialesEnseignant, String nomGroupe, 
                                           Date hDebut, Date hFin, int idSeanceEnCours) {
        
        // Récupérer toutes les séances
        List<Seance> seances = seanceRepository.findAll();
        
        // Parcourir chaque séance
        for (Seance s : seances) {
            // Ignorer la séance en cours de modification
            if (s.getIdSeance() == idSeanceEnCours) {
                continue;
            }
            
            // Calculer l'heure de fin de cette séance
            Date seanceDebut = s.getHDebut();
            Date seanceFin = new Date(seanceDebut.getTime() + (s.getDuree() * 60 * 1000));
            
            // Vérifier s'il y a chevauchement de temps
            boolean chevauchement = hDebut.before(seanceFin) && hFin.after(seanceDebut);
            
            if (chevauchement) {
                // CONFLIT 1 : Même salle
                if (s.getSalleList() != null) {
                    for (Salle salle : s.getSalleList()) {
                        if (salle.getNumeroSalle().equals(numeroSalle)) {
                            return "⚠️ CONFLIT : La salle " + numeroSalle + " est déjà réservée à cette heure !";
                        }
                    }
                }
                
                // CONFLIT 2 : Même enseignant
                if (s.getEnseignantList() != null) {
                    for (Enseignant ens : s.getEnseignantList()) {
                        if (ens.getInitiales().equals(initialesEnseignant)) {
                            return "⚠️ CONFLIT : L'enseignant " + ens.getPrenom() + " " + ens.getNomEnseignant() + 
                                   " a déjà un cours à cette heure !";
                        }
                    }
                }
                
                // CONFLIT 3 : Même groupe
                if (s.getGroupeList() != null) {
                    for (Groupe groupe : s.getGroupeList()) {
                        if (groupe.getNomGroupe().equals(nomGroupe)) {
                            return "⚠️ CONFLIT : Le groupe " + nomGroupe + " a déjà un cours à cette heure !";
                        }
                    }
                }
            }
        }
        
        return null; // Aucun conflit
    }
}