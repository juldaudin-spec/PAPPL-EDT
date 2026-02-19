/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import static tp.projetpappl.controllers.Tools.getIntFromString;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Seance;
import tp.projetpappl.items.TypeLecon;
import tp.projetpappl.repositories.ContientRepository;
import tp.projetpappl.repositories.EnseignementRepository;
import tp.projetpappl.repositories.GroupeRepository;
import tp.projetpappl.repositories.SeanceRepository;

/**
 *
 * @author nathan
 */
@Controller
public class ContientController {

    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private ContientRepository contientRepository;
    @Autowired
    @Lazy
    private SeanceRepository seanceRepository;
    @Autowired
    @Lazy
    private EnseignementRepository enseignementRepository;

    @RequestMapping(value = "compteRenduGroupe.do", method = RequestMethod.POST)
    public ModelAndView handleSummaryGroupeGet(HttpServletRequest request) {
        ModelAndView returned = new ModelAndView("compteRenduGroupe");
        String idStr = request.getParameter("idGroupe");
        Optional<Groupe> groupeOpt = null;
        if (idStr != null) {
            groupeOpt = groupeRepository.findById(idStr);
        }
        Groupe groupe;
        if (groupeOpt != null) {
            groupe = groupeOpt.get();

            List<Enseignement> enseignements = contientRepository.findEnseignementByGroupe(groupe);
            List<Seance> listSeance = seanceRepository.findSeanceByGroupe(groupe);

            Collections.sort(listSeance, Seance.getComparatorByEnseignement());
            checkEnseignement(enseignements, listSeance);
            Collections.sort(enseignements, Enseignement.getComparator());

            List<List<TypeLecon>> intituleByEnseignement = getIntituleByEnseignement(groupe, enseignements);
            checkTypeLecon(intituleByEnseignement, listSeance);

            List<List<List<Contient>>> contients = getListContient(groupe, enseignements, intituleByEnseignement);

            returned.addObject("groupe", groupe);
            returned.addObject("enseignements", enseignements);
            returned.addObject("intitules", intituleByEnseignement);
            returned.addObject("contients", contients);
            returned.addObject("seances", listSeance);
        }
        return returned;
    }

    @RequestMapping(value = "compteRenduEnseignement.do", method = RequestMethod.POST)
    public ModelAndView handleSummaryEnseignementGet(HttpServletRequest request) {
        ModelAndView returned = new ModelAndView("compteRenduEnseignement");
        String idStr = request.getParameter("acronyme");
        Optional<Enseignement> enseignementOpt = null;
        if (idStr != null) {
            enseignementOpt = enseignementRepository.findById(idStr);
        }
        Enseignement enseignement;
        if (enseignementOpt != null) {
            enseignement = enseignementOpt.get();

            List<Groupe> groupes = contientRepository.findGroupeByEnseignement(enseignement);
            List<Seance> listSeance = seanceRepository.findSeanceByEnseignement(enseignement);

            Collections.sort(listSeance, Seance.getComparatorByGroupe());
            checkGroupes(groupes, listSeance);
            Collections.sort(groupes, Groupe.getComparator());

            List<List<TypeLecon>> intituleByEnseignement = getIntituleByGroupe(enseignement, groupes);
            checkTypeLecon(intituleByEnseignement, listSeance, groupes);

            List<List<List<Contient>>> contients = getListContient(enseignement, groupes, intituleByEnseignement);

            returned.addObject("groupes", groupes);
            returned.addObject("enseignement", enseignement);
            returned.addObject("intitules", intituleByEnseignement);
            returned.addObject("contients", contients);
            returned.addObject("seances", listSeance);
        }
        return returned;
    }

    public List<List<TypeLecon>> getIntituleByEnseignement(Groupe groupe, List<Enseignement> enseignements) {
        List<List<TypeLecon>> intituleByEnseignement = new ArrayList<>();
        if (!enseignements.isEmpty()) {
            for (Enseignement enseignement : enseignements) {
                List<TypeLecon> intitules = contientRepository.findIntituleByEnseignementByGroupe(enseignement, groupe);
                Collections.sort(intitules, TypeLecon.getComparator());
                intituleByEnseignement.add(intitules);
            }
        }
        return intituleByEnseignement;
    }

    public void checkEnseignement(List<Enseignement> enseignements, List<Seance> seances) {
        List<String> acronymes = new ArrayList<>(enseignements.size());
        for (Enseignement enseignement : enseignements) {
            acronymes.add(enseignement.getAcronyme());
        }
        for (Seance seance : seances) {
            if (!acronymes.contains(seance.getAcronyme().getAcronyme())) {
                acronymes.add(seance.getAcronyme().getAcronyme());
                enseignements.add(seance.getAcronyme());
            }
        }
    }

    public List<List<List<Contient>>> getListContient(Groupe groupe, List<Enseignement> enseignements, List<List<TypeLecon>> intituleByEnseignement) {
        List<List<List<Contient>>> contients = new ArrayList<>();
        int i = 0;
        if (!intituleByEnseignement.isEmpty()) {
            for (Enseignement enseignement : enseignements) {
                List<List<Contient>> contientByEnseignement = new ArrayList<>();
                for (TypeLecon typeLecon : intituleByEnseignement.get(i)) {
                    List<Contient> contientByType = contientRepository.findContientByIntituleByEnseignementByGroupe(typeLecon, enseignement, groupe);
                    contientByEnseignement.add(contientByType);
                }
                i++;
                contients.add(contientByEnseignement);
            }
        }
        return contients;

    }

    /**
     *
     * @param intituleByEnseignement une liste triée par Enseignement de (liste
     * de TypeLecon triée)
     * @param enseignements une liste triée d'Enseignement
     * @param seances une liste triée par eneignement de Seance
     */
    private void checkTypeLecon(List<List<TypeLecon>> intituleByEnseignement, List<Seance> seances) {
        List<String> types = new ArrayList<>(5);
        int j = 0;
        List<TypeLecon> listTemp;
        String previous = null;
        if (!seances.isEmpty() && (!intituleByEnseignement.isEmpty())) {
            previous = seances.get(j).getAcronyme().getAcronyme();
            types = new ArrayList<>(5);
            for (TypeLecon type : intituleByEnseignement.get(j)) {
                types.add(type.getIntitule());
            }
        }
        Seance seance;
        for (int i = 0; i < seances.size(); i++) {
            seance = seances.get(i);
            if (!seance.getAcronyme().getAcronyme().equals(previous)) {
                j += 1;
                previous = seance.getAcronyme().getAcronyme();
                types = new ArrayList<>(5);
                for (TypeLecon type : intituleByEnseignement.get(j)) {
                    types.add(type.getIntitule());
                }
            }
            if (!types.contains(seance.getIntitule().getIntitule())) {
                types.add(seance.getIntitule().getIntitule());
                listTemp = intituleByEnseignement.get(j);
                listTemp.add(seance.getIntitule());
                intituleByEnseignement.set(j, listTemp);
            }

        }

    }

    /**
     *
     * @param intituleByEnseignement une liste triée par Enseignement de (liste
     * de TypeLecon triée)
     * @param enseignements une liste triée d'Enseignement
     * @param seances une liste triée par eneignement de Seance
     */
    private void checkTypeLecon(List<List<TypeLecon>> intituleByEnseignement, List<Seance> seances, List<Groupe> groupes) {
        //initialisation
        List<String> types = new ArrayList<>(5);
        int j = 0;
        List<TypeLecon> listTemp = new ArrayList<>();
        List<String> listGroupe = new ArrayList<>(groupes.size());
        for (Groupe groupe : groupes) {
            listGroupe.add(groupe.getNomGroupe());
        }
        if (!intituleByEnseignement.isEmpty()) {
            types = new ArrayList<>(5);
            for (TypeLecon type : intituleByEnseignement.get(j)) {
                types.add(type.getIntitule());
            }
        }
        Seance seance;
        //end of initialisation
        for (String groupe : listGroupe) {
            listTemp = intituleByEnseignement.get(j);
            types = new ArrayList<>(5);
            for (TypeLecon type : intituleByEnseignement.get(j)) {
                types.add(type.getIntitule());
            }
            for (int i = 0; i < seances.size(); i++) {
                seance = seances.get(i);
                for (Groupe groupeTest : seance.getGroupeList()) {
                    if (groupe.equals(groupeTest.getNomGroupe())) {
                        if ((!types.contains(seance.getIntitule().getIntitule()))) {
                            types.add(seance.getIntitule().getIntitule());
                            listTemp.add(seance.getIntitule());
                        }
                    }
                }
            }
            intituleByEnseignement.set(j, listTemp);
            j += 1;
        }
    }

    private void checkGroupes(List<Groupe> groupes, List<Seance> seances) {
        List<String> groupesStr = new ArrayList<>(groupes.size());
        for (Groupe groupe : groupes) {
            groupesStr.add(groupe.getNomGroupe());
        }
        for (Seance seance : seances) {
            for (Groupe groupe : seance.getGroupeList()) {
                if (!groupesStr.contains(groupe.getNomGroupe())) {
                    groupesStr.add(groupe.getNomGroupe());
                    groupes.add(groupe);
                }
            }
        }
    }

    public List<List<TypeLecon>> getIntituleByGroupe(Enseignement enseignement, List<Groupe> groupes) {
        List<List<TypeLecon>> intituleByGroupe = new ArrayList<>();
        if (!groupes.isEmpty()) {
            for (Groupe groupe : groupes) {
                List<TypeLecon> intitules = contientRepository.findIntituleByEnseignementByGroupe(enseignement, groupe);
                Collections.sort(intitules, TypeLecon.getComparator());
                intituleByGroupe.add(intitules);
            }
        }
        return intituleByGroupe;
    }

    public List<List<List<Contient>>> getListContient(Enseignement enseignement, List<Groupe> groupes, List<List<TypeLecon>> intituleByEnseignement) {
        List<List<List<Contient>>> contients = new ArrayList<>();
        int i = 0;
        if (!intituleByEnseignement.isEmpty()) {
            for (Groupe groupe : groupes) {
                List<List<Contient>> contientByEnseignement = new ArrayList<>();
                for (TypeLecon typeLecon : intituleByEnseignement.get(i)) {
                    List<Contient> contientByType = contientRepository.findContientByIntituleByEnseignementByGroupe(typeLecon, enseignement, groupe);
                    contientByEnseignement.add(contientByType);
                }
                i++;
                contients.add(contientByEnseignement);
            }
        }
        return contients;
    }
}
