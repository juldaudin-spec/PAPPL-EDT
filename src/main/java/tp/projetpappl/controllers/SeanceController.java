/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

/**
 *
 * @author julda
 */
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import static tp.projetpappl.controllers.Tools.getIntFromString;
import tp.projetpappl.items.Connection;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Salle;
import tp.projetpappl.items.Seance;
import tp.projetpappl.items.TypeLecon;
import tp.projetpappl.repositories.ContientRepository;
import tp.projetpappl.repositories.EnseignantRepository;
import tp.projetpappl.repositories.EnseignementRepository;
import tp.projetpappl.repositories.GroupeRepository;
import tp.projetpappl.repositories.SalleRepository;
import tp.projetpappl.repositories.SeanceRepository;
import tp.projetpappl.repositories.TypeLeconRepository;

/**
 *
 * @author julda
 */
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
    @Autowired
    private AuthHelper authHelper;
    @Autowired
    private ContientRepository contientRepository;

    @RequestMapping(value = "seance.do", method = RequestMethod.POST)
    public ModelAndView handlePostSeance(HttpServletRequest request) {
        String seanceStr = "seance";
        Connection user = authHelper.getAuthenticatedUser(request);
        Seance seanceChoisie;
        if (user == null) {
            return new ModelAndView("redirect:login.do");
        }

        ModelAndView returned = new ModelAndView(seanceStr);
        String idSeanceStr = request.getParameter("idSeance");
        if ((idSeanceStr != null) && (!idSeanceStr.isEmpty())) {
            Optional<Seance> seance = seanceRepository.findById(Tools.getIntFromString(idSeanceStr));
            if (seance.isPresent()) {
                seanceChoisie = seance.get();
            } else {
                seanceChoisie = new Seance();
            }
        } else {
            seanceChoisie = new Seance();
        }//TODO à changer pour n'afficher que ce qui est disponible, et pour les enseignants: ceux qui enseignent dans la matière, et pareil pojur le reste
        returned.addObject(seanceStr, seanceChoisie);
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("groupesList", groupeRepository.findAll());
        returned.addObject("enseignementsList", enseignementRepository.findAll());
        returned.addObject("typeLeconsList", typeLeconRepository.findAll());
        returned.addObject("sallesList", salleRepository.findAll());
        returned.addObject("user", user);
        return returned;
    }

    @RequestMapping(value = "saveseance.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveSeance(HttpServletRequest request) {
        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:login.do");
        }

        String acronymeEnseignement = request.getParameter("Enseignement");
        Enseignement enseignement = enseignementRepository.getByAcronyme(acronymeEnseignement);
        if (!authHelper.canModifyFiliere(request, enseignement.getFiliere())) {
            ModelAndView forbidden = new ModelAndView("403");
            forbidden.addObject("user", user);
            return forbidden;
        }


        ModelAndView returned = new ModelAndView("seance");
        boolean succes = false;

        try {
            String idSeanceStr = request.getParameter("IdSeance");
            int idSeance = Tools.getIntFromString(idSeanceStr);

            String dureeStr = request.getParameter("Duree");
            int duree = Tools.getIntFromString(dureeStr);
            if (duree <= 0) {
                throw new IllegalArgumentException("Durée invalide : merci de saisir une durée strictement supérieure à 0.");
            }

            //String acronymeEnseignement = request.getParameter("Enseignement");
            //Enseignement enseignement = enseignementRepository.getByAcronyme(acronymeEnseignement);

            String intituleLecon = request.getParameter("TypeLecon");
            TypeLecon typeLecon = typeLeconRepository.getByIntitule(intituleLecon);

            HashMap<String, String> nomGroupes = Tools.getArrayFromRequest(request, "m");
            ArrayList<Groupe> groupes = new ArrayList<>();
            for (String nomGroupe : nomGroupes.values()) {
                Groupe g = groupeRepository.getByNomGroupe(nomGroupe);
                if (g != null) {
                    groupes.add(g);
                }
            }

            HashMap<String, String> numeroSalles = Tools.getArrayFromRequest(request, "s");
            ArrayList<Salle> salles = new ArrayList<>();
            for (String numeroSalle : numeroSalles.values()) {
                Salle s = salleRepository.getByNumeroSalle(numeroSalle);
                if (s != null) {
                    salles.add(s);
                }
            }

            String hDebutStr = request.getParameter("HDebut");
            Date hDebut = Tools.getDateFromString(hDebutStr, "yyyy-MM-dd'T'HH:mm");

            HashMap<String, String> initialesEnseignants = Tools.getArrayFromRequest(request, "e");
            ArrayList<Enseignant> enseignants = new ArrayList<>();
            for (String initialesEnseignant : initialesEnseignants.values()) {
                Enseignant e = enseignantRepository.getByInitiales(initialesEnseignant);
                if (e != null) {
                    enseignants.add(e);
                }
            }

            // 1) Conflits enseignants / groupes (BLOQUANT)
            verifierConflitsSeance(idSeance, hDebut, duree, groupes, enseignants);

            // 2) Warnings salles AVANT le save (non bloquant)
            List<String> warningsSalles = verifierConflitsSalles(idSeance, hDebut, duree, salles);
            returned.addObject("warningsSalles", warningsSalles);

            // 3) Warnings maquette AVANT le save (non bloquant)
            if (enseignement != null && typeLecon != null) {
                List<String> warningsMaquette = verifierMaquette(groupes, enseignement, typeLecon);
                returned.addObject("warningsMaquette", warningsMaquette);
            }

            // 4) Save de la séance
            Seance retour;
            if (idSeance > 0) {
                retour = seanceRepository.update(idSeance, enseignement, enseignants, typeLecon, groupes, salles, hDebut, duree);
            } else {
                retour = seanceRepository.create(enseignement, enseignants, typeLecon, groupes, salles, hDebut, duree);
            }

            if (retour != null) {
                succes = true;
            }

        } catch (IllegalArgumentException e) {
            returned.addObject("error", e.getMessage());
        } catch (Exception e) {
            returned.addObject("error", "Erreur lors de l'enregistrement de la séance : " + e.getMessage());
        }

        returned.addObject("newseance", succes);
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("groupesList", groupeRepository.findAll());
        returned.addObject("enseignementsList", enseignementRepository.findAll());
        returned.addObject("typeLeconsList", typeLeconRepository.findAll());
        returned.addObject("sallesList", salleRepository.findAll());
        returned.addObject("user", user);
        return returned;
    }

    // -------------------------------------------------------------------------
    // Vérification conflits enseignants / groupes (BLOQUANT)
    // -------------------------------------------------------------------------
    private void verifierConflitsSeance(
            int idSeanceCourante,
            Date nouveauDebut,
            int nouvelleDuree,
            List<Groupe> nouveauxGroupes,
            List<Enseignant> nouveauxEnseignants) {

        List<Seance> seancesExistantes = new ArrayList<>(seanceRepository.findAll());

        for (Seance seanceExistante : seancesExistantes) {

            if (idSeanceCourante > 0
                    && seanceExistante.getIdSeance() != null
                    && seanceExistante.getIdSeance().equals((Integer) idSeanceCourante)) {
                continue;
            }

            if (!seChevauchent(
                    nouveauDebut,
                    nouvelleDuree,
                    seanceExistante.getHDebut(),
                    seanceExistante.getDuree())) {
                continue;
            }

            if (partageEnseignant(nouveauxEnseignants, seanceExistante.getEnseignantList())) {
                throw new IllegalArgumentException(
                        "Conflit enseignant : un enseignant ne peut pas donner 2 cours en même temps.");
            }

            if (conflitGroupes(nouveauxGroupes, seanceExistante.getGroupeList())) {
                throw new IllegalArgumentException(
                        "Conflit groupe : un groupe ne peut pas avoir 2 cours en même temps.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Vérification conflits salles (NON BLOQUANT)
    // -------------------------------------------------------------------------
    private List<String> verifierConflitsSalles(
            int idSeanceCourante, Date hDebut, int duree, List<Salle> salles) {

        List<String> warnings = new ArrayList<>();
        List<Seance> seancesExistantes = new ArrayList<>(seanceRepository.findAll());

        for (Seance s : seancesExistantes) {
            if (idSeanceCourante > 0
                    && s.getIdSeance() != null
                    && s.getIdSeance().equals((Integer) idSeanceCourante)) {
                continue;
            }
            if (!seChevauchent(hDebut, duree, s.getHDebut(), s.getDuree())) {
                continue;
            }
            if (s.getSalleList() == null) {
                continue;
            }
            for (Salle salleDemandee : salles) {
                for (Salle salleExistante : s.getSalleList()) {
                    if (salleDemandee.getNumeroSalle() != null
                            && salleDemandee.getNumeroSalle().equals(salleExistante.getNumeroSalle())) {
                        warnings.add("⚠️ La salle " + salleDemandee.getNumeroSalle()
                                + " est déjà occupée à ce créneau.");
                    }
                }
            }
        }
        return warnings;
    }

    // -------------------------------------------------------------------------
    // Vérification maquette (NON BLOQUANT)
    // -------------------------------------------------------------------------
    private List<String> verifierMaquette(
            List<Groupe> groupes, Enseignement enseignement, TypeLecon typeLecon) {

        List<String> warnings = new ArrayList<>();

        for (Groupe groupe : groupes) {
            List<Contient> contients = contientRepository
                    .findContientByIntituleByEnseignementByGroupe(typeLecon, enseignement, groupe);

            if (contients == null || contients.isEmpty()) {
                warnings.add("⚠️ Aucune volumétrie définie pour le groupe "
                        + groupe.getNomGroupe()
                        + " / " + enseignement.getAcronyme()
                        + " / " + typeLecon.getIntitule());
                continue;
            }

            BigInteger volumetriePrevue = contients.get(0).getVolumetrie();

            List<Seance> seancesGroupe = seanceRepository.findSeanceByGroupe(groupe);
            int minutesProgrammees = 0;
            for (Seance s : seancesGroupe) {
                if (s.getAcronyme() != null
                        && s.getAcronyme().getAcronyme().equals(enseignement.getAcronyme())
                        && s.getIntitule() != null
                        && s.getIntitule().getIntitule().equals(typeLecon.getIntitule())
                        && s.getDuree() != null) {
                    minutesProgrammees += s.getDuree();
                }
            }

            if (volumetriePrevue != null) {
                int prevues = volumetriePrevue.intValue();
                if (minutesProgrammees > prevues) {
                    warnings.add("⚠️ Groupe " + groupe.getNomGroupe()
                            + " : heures programmées (" + minutesProgrammees
                            + " min) dépassent la maquette (" + prevues + " min) pour "
                            + enseignement.getAcronyme() + " / " + typeLecon.getIntitule());
                } else if (minutesProgrammees == prevues) {
                    warnings.add("✅ Groupe " + groupe.getNomGroupe()
                            + " : maquette complète pour "
                            + enseignement.getAcronyme() + " / " + typeLecon.getIntitule());
                } else {
                    warnings.add("ℹ️ Groupe " + groupe.getNomGroupe()
                            + " : " + minutesProgrammees + "/" + prevues
                            + " min programmées pour "
                            + enseignement.getAcronyme() + " / " + typeLecon.getIntitule());
                }
            }
        }
        return warnings;
    }

    // -------------------------------------------------------------------------
    // Méthodes utilitaires
    // -------------------------------------------------------------------------
    private boolean seChevauchent(Date debut1, int duree1, Date debut2, Integer duree2) {
        if (debut1 == null || debut2 == null || duree2 == null) {
            return false;
        }

        long d1 = debut1.getTime();
        long f1 = d1 + (long) duree1 * 60L * 1000L;

        long d2 = debut2.getTime();
        long f2 = d2 + (long) duree2 * 60L * 1000L;

        return d1 < f2 && d2 < f1;
    }

    private boolean partageEnseignant(List<Enseignant> liste1, List<Enseignant> liste2) {
        if (liste1 == null || liste2 == null) {
            return false;
        }

        for (Enseignant e1 : liste1) {
            if (e1 == null || e1.getInitiales() == null) {
                continue;
            }
            for (Enseignant e2 : liste2) {
                if (e2 == null || e2.getInitiales() == null) {
                    continue;
                }
                if (e1.getInitiales().equals(e2.getInitiales())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean conflitGroupes(List<Groupe> liste1, List<Groupe> liste2) {
        if (liste1 == null || liste2 == null) {
            return false;
        }

        for (Groupe g1 : liste1) {
            for (Groupe g2 : liste2) {

                if (memeGroupe(g1, g2)) {
                    return true;
                }

                if (estLieParDependance(g1, g2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean memeGroupe(Groupe g1, Groupe g2) {
        return g1 != null
                && g2 != null
                && g1.getNomGroupe() != null
                && g1.getNomGroupe().equals(g2.getNomGroupe());
    }

    private boolean estLieParDependance(Groupe g1, Groupe g2) {
        return estParentDe(g1, g2) || estParentDe(g2, g1);
    }

    private boolean estParentDe(Groupe parent, Groupe enfant) {
        if (parent == null || enfant == null) {
            return false;
        }

        if (enfant.getGroupeList() != null) {
            for (Groupe pere : enfant.getGroupeList()) {
                if (memeGroupe(parent, pere)) {
                    return true;
                }
            }
        }

        if (parent.getGroupeList1() != null) {
            for (Groupe fils : parent.getGroupeList1()) {
                if (memeGroupe(enfant, fils)) {
                    return true;
                }
            }
        }

        return false;
    }
    
    @RequestMapping(value = "deleteseance.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteSeance(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:login.do");
        }
        
        ModelAndView returned;

        String idSeanceStr = request.getParameter("idSeance");
        int idSeance=-1;
        if (idSeanceStr != null){
            idSeance=getIntFromString(idSeanceStr);
        }
      
        seanceRepository.remove(idSeance);

        returned = new ModelAndView("index");
        returned.addObject("user", user);

        return returned;
    }
    
}
