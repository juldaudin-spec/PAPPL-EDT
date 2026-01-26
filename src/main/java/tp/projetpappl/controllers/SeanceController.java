package tp.projetpappl.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Salle;
import tp.projetpappl.items.Seance;
import tp.projetpappl.items.TypeLecon;
import tp.projetpappl.repositories.EnseignantRepository;
import tp.projetpappl.repositories.EnseignementRepository;
import tp.projetpappl.repositories.GroupeRepository;
import tp.projetpappl.repositories.SalleRepository;
import tp.projetpappl.repositories.SeanceRepository;
import tp.projetpappl.repositories.TypeLeconRepository;

@Controller
public class SeanceController {

    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private TypeLeconRepository typeLeconRepository;
    @Autowired
    private EnseignementRepository enseignementRepository;
    @Autowired
    private SalleRepository salleRepository;

    @RequestMapping(value = "seance.do", method = RequestMethod.POST)
    public ModelAndView handlePostSeance(HttpServletRequest request) {
        ModelAndView returned = new ModelAndView("seance");
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("seance", new Seance());
        returned.addObject("groupesList", groupeRepository.findAll());
        returned.addObject("enseignementsList", enseignementRepository.findAll());
        returned.addObject("typeLeconsList", typeLeconRepository.findAll());
        returned.addObject("sallesList", salleRepository.findAll());
        return returned;
    }

    @RequestMapping(value = "saveseance.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveSeance(HttpServletRequest request) {

        ModelAndView returned;

        String idSeanceStr = request.getParameter("IdSeance");
        int idSeance = Tools.getIntFromString(idSeanceStr);
        String dureeStr = request.getParameter("Duree");
        int duree = Tools.getIntFromString(dureeStr);
        if (duree <= 0) {
            throw new IllegalArgumentException("Duree invalide, merci de remplir une durée strictement supérieur à 0");
        }

        String acronymeEnseignement = request.getParameter("Enseignement");
        Enseignement enseignement = enseignementRepository.getByAcronyme(acronymeEnseignement);
        String intituleLecon = request.getParameter("TypeLecon");
        TypeLecon typeLecon = typeLeconRepository.getByIntitule(intituleLecon);
        HashMap<String, String> nomGroupes = Tools.getArrayFromRequest(request, "ml");
        ArrayList<Groupe> groupes = new ArrayList<>();

        for (String nomGroupe : nomGroupes.values()) {
            Groupe g = groupeRepository.getByNomGroupe(nomGroupe);
            if (g != null) {
                groupes.add(g);
            }
        }

        HashMap<String, String> numeroSalles = Tools.getArrayFromRequest(request, "sl");
        ArrayList<Salle> salles = new ArrayList<>();

        for (String numeroSalle : numeroSalles.values()) {
            Salle s = salleRepository.getByNumeroSalle(numeroSalle);
            if (s != null) {
                salles.add(s);
            }
        }

        String hDebutStr = request.getParameter("HDebut");
        Date hDebut = Tools.getDateFromString(hDebutStr, "yyyy-MM-dd'T'HH:mm");

        HashMap<String, String> initialesEnseignants = Tools.getArrayFromRequest(request, "el");
        ArrayList<Enseignant> enseignants = new ArrayList<>();

        for (String initialesEnseignant : initialesEnseignants.values()) {
            Enseignant e = enseignantRepository.getByInitiales(initialesEnseignant);
            if (e != null) {
                enseignants.add(e);
            }
        }

        String conflitMessage = verifierConflits(idSeance, groupes, enseignants, salles, hDebut, duree, enseignement, typeLecon);
        
        if (conflitMessage != null) {
            returned = new ModelAndView("seance");
            returned.addObject("error", conflitMessage);
            returned.addObject("newseance", false);
            returned.addObject("enseignantsList", enseignantRepository.findAll());
            returned.addObject("groupesList", groupeRepository.findAll());
            returned.addObject("enseignementsList", enseignementRepository.findAll());
            returned.addObject("typeLeconsList", typeLeconRepository.findAll());
            returned.addObject("sallesList", salleRepository.findAll());
            return returned;
        }

        boolean succes = false;
        Seance retour = null;
        if (idSeance > 0) {
            retour = seanceRepository.update(idSeance, enseignement, enseignants, typeLecon, groupes, salles, hDebut, duree);
        } else {
            retour = seanceRepository.create(enseignement, enseignants, typeLecon, groupes, salles, hDebut, duree);
        }
        if (retour != null) {
            succes = true;
        }
        returned = new ModelAndView("seance");
        returned.addObject("newseance", succes);
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("groupesList", groupeRepository.findAll());
        returned.addObject("enseignementsList", enseignementRepository.findAll());
        returned.addObject("typeLeconsList", typeLeconRepository.findAll());
        returned.addObject("sallesList", salleRepository.findAll());
        return returned;
    }

    private String verifierConflits(int idSeance, List<Groupe> groupes, 
                                    List<Enseignant> enseignants, 
                                    List<Salle> salles, 
                                    Date hDebut, int duree,
                                    Enseignement enseignement, TypeLecon typeLecon) {
        if (hDebut == null || duree <= 0) {
            return "Date et durée invalides";
        }
        
        Date fin = new Date(hDebut.getTime() + duree * 60000);
        Collection<Seance> toutesSeances = seanceRepository.findAll();
        
        String conflitGroupe = verifierConflitsGroupes(idSeance, groupes, hDebut, fin, toutesSeances);
        if (conflitGroupe != null) {
            return conflitGroupe;
        }
        
        String conflitEnseignant = verifierConflitsEnseignants(idSeance, enseignants, hDebut, fin, toutesSeances);
        if (conflitEnseignant != null) {
            return conflitEnseignant;
        }
        
        String conflitSalle = verifierConflitsSalles(idSeance, salles, hDebut, fin, toutesSeances);
        if (conflitSalle != null) {
            return conflitSalle;
        }
        
        String erreurVolumetrie = verifierVolumetrieHoraire(idSeance, enseignement, typeLecon, duree, toutesSeances);
        if (erreurVolumetrie != null) {
            return erreurVolumetrie;
        }
        
        return null;
    }
    
    private String verifierConflitsGroupes(int idSeance, List<Groupe> groupes, 
                                          Date debut, Date fin, 
                                          Collection<Seance> toutesSeances) {
        for (Groupe groupe : groupes) {
            List<Groupe> groupesLies = getGroupesLies(groupe);
            groupesLies.add(groupe);
            
            for (Groupe g : groupesLies) {
                for (Seance seanceExist : toutesSeances) {
                    if (idSeance > 0 && seanceExist.getIdSeance() != null && 
                        seanceExist.getIdSeance() == idSeance) {
                        continue;
                    }
                    
                    if (seanceExist.getGroupeList() != null) {
                        for (Groupe groupeSeance : seanceExist.getGroupeList()) {
                            if (groupeSeance.getNomGroupe().equals(g.getNomGroupe())) {
                                if (seanceExist.getHDebut() != null && seanceExist.getDuree() != null) {
                                    Date debutExist = seanceExist.getHDebut();
                                    Date finExist = new Date(debutExist.getTime() + 
                                        seanceExist.getDuree() * 60000);
                                    
                                    if (debut.before(finExist) && fin.after(debutExist)) {
                                        return "Conflit détecté : Le groupe '" + g.getNomGroupe() + 
                                               "' a déjà une séance à cet horaire";
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private List<Groupe> getGroupesLies(Groupe groupe) {
        List<Groupe> groupesLies = new ArrayList<>();
        Groupe groupeComplet = groupeRepository.getByNomGroupe(groupe.getNomGroupe());
        
        if (groupeComplet != null) {
            if (groupeComplet.getGroupeList() != null) {
                groupesLies.addAll(groupeComplet.getGroupeList());
            }
            if (groupeComplet.getGroupeList1() != null) {
                groupesLies.addAll(groupeComplet.getGroupeList1());
            }
        }
        
        return groupesLies;
    }
    
    private String verifierConflitsEnseignants(int idSeance, List<Enseignant> enseignants, 
                                               Date debut, Date fin,
                                               Collection<Seance> toutesSeances) {
        for (Enseignant enseignant : enseignants) {
            for (Seance seanceExist : toutesSeances) {
                if (idSeance > 0 && seanceExist.getIdSeance() != null && 
                    seanceExist.getIdSeance() == idSeance) {
                    continue;
                }
                
                if (seanceExist.getEnseignantList() != null) {
                    for (Enseignant enseignantSeance : seanceExist.getEnseignantList()) {
                        if (enseignantSeance.getInitiales().equals(enseignant.getInitiales())) {
                            if (seanceExist.getHDebut() != null && seanceExist.getDuree() != null) {
                                Date debutExist = seanceExist.getHDebut();
                                Date finExist = new Date(debutExist.getTime() + 
                                    seanceExist.getDuree() * 60000);
                                
                                if (debut.before(finExist) && fin.after(debutExist)) {
                                    return "Conflit détecté : L'enseignant '" + 
                                           enseignant.getNomEnseignant() + 
                                           "' a déjà une séance à cet horaire";
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private String verifierConflitsSalles(int idSeance, List<Salle> salles, 
                                         Date debut, Date fin,
                                         Collection<Seance> toutesSeances) {
        for (Salle salle : salles) {
            for (Seance seanceExist : toutesSeances) {
                if (idSeance > 0 && seanceExist.getIdSeance() != null && 
                    seanceExist.getIdSeance() == idSeance) {
                    continue;
                }
                
                if (seanceExist.getSalleList() != null) {
                    for (Salle salleSeance : seanceExist.getSalleList()) {
                        if (salleSeance.getNumeroSalle().equals(salle.getNumeroSalle())) {
                            if (seanceExist.getHDebut() != null && seanceExist.getDuree() != null) {
                                Date debutExist = seanceExist.getHDebut();
                                Date finExist = new Date(debutExist.getTime() + 
                                    seanceExist.getDuree() * 60000);
                                
                                if (debut.before(finExist) && fin.after(debutExist)) {
                                    return "Conflit détecté : La salle '" + salle.getNumeroSalle() + 
                                           "' est déjà réservée à cet horaire";
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    
    private String verifierVolumetrieHoraire(int idSeance, Enseignement enseignement, 
                                             TypeLecon typeLecon, int dureeNouvelle,
                                             Collection<Seance> toutesSeances) {
        
        int minutesUtilisees = 0;
        
        for (Seance seanceExist : toutesSeances) {
            if (idSeance > 0 && seanceExist.getIdSeance() != null && 
                seanceExist.getIdSeance() == idSeance) {
                continue;
            }
            
            if (seanceExist.getAcronyme() != null && 
                seanceExist.getAcronyme().getAcronyme().equals(enseignement.getAcronyme()) &&
                seanceExist.getIntitule() != null &&
                seanceExist.getIntitule().getIntitule().equals(typeLecon.getIntitule()) &&
                seanceExist.getDuree() != null) {
                minutesUtilisees += seanceExist.getDuree();
            }
        }
        
        minutesUtilisees += dureeNouvelle;
        int heuresUtilisees = minutesUtilisees / 60;
        
        int volumetrieMax = getVolumetrieFromEnseignement(enseignement, typeLecon);
        
        if (volumetrieMax > 0 && heuresUtilisees > volumetrieMax) {
            return "Warning : Volume horaire dépassé pour " + enseignement.getNomEnseignement() + 
                   " (" + typeLecon.getIntitule() + "). " +
                   "Utilisé : " + heuresUtilisees + "h / Prévu : " + volumetrieMax + "h";
        }
        
        return null;
    }
    
    private int getVolumetrieFromEnseignement(Enseignement enseignement, TypeLecon typeLecon) {
        if (enseignement != null && enseignement.getContientList() != null) {
            for (Object obj : enseignement.getContientList()) {
                tp.projetpappl.items.Contient contient = (tp.projetpappl.items.Contient) obj;
                if (contient.getIntitule() != null && 
                    contient.getIntitule().equals(typeLecon.getIntitule()) &&
                    contient.getVolumetrie() != null) {
                    return contient.getVolumetrie().intValue();
                }
            }
        }
        return 0;
    }
}