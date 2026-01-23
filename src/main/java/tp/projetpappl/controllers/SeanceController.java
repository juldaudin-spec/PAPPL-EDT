/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

/**
 *
 * @author julda
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
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
    

    @RequestMapping(value = "seance.do", method=RequestMethod.POST)
    public ModelAndView handlePostSeance(HttpServletRequest request) {

        ModelAndView returned = new ModelAndView("seance");
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("seance",new Seance());
        returned.addObject("groupesList",groupeRepository.findAll());
        returned.addObject("enseignementsList",enseignementRepository.findAll());
        returned.addObject("typeLeconsList",typeLeconRepository.findAll());
        returned.addObject("sallesList",salleRepository.findAll());
        return returned;
    }
    /*
    @RequestMapping(value = "seances.do", method=RequestMethod.POST)
    public ModelAndView handlePostSeance(HttpServletRequest request) {
        List<Seance> myList = new ArrayList<>(seanceRepository.findAll());
        Collections.sort(myList, Seance.getComparator());
        List<Enseignant> Enseignants = new ArrayList<>(enseignantRepository.findAll());
        Collections.sort(myList, Seance.getComparator());

        ModelAndView returned = new ModelAndView("seances");
        returned.addObject("seancesList", myList);
        returned.addObject("enseignantsList", Enseignants);

        return returned;
}

    @RequestMapping(value = "editseance.do", method = RequestMethod.POST)
    public ModelAndView handleEditSeancePost(HttpServletRequest request) {
        ModelAndView returned;

        String idSeanceStr = request.getParameter("idSeance");
        int idSeance = Tools.getIntFromString(idSeanceStr);
        if (idSeance != null) {
            //ID may exist
            Seance seance = seanceRepository.getById(idSeance);
            returned = new ModelAndView("seance");
            returned.addObject("seance", seance);
            returned.addObject("enseignantsList", enseignantRepository.findAll());
        } else {
            returned = new ModelAndView("seances");
            Collection<Seance> myList = seanceRepository.findAll();
            returned.addObject("seancesList", myList);

        }
        return returned;
    }
    */
    @RequestMapping(value = "saveseance.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveSeance(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update seances
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
        String nomGroupe = request.getParameter("nomGroupe");
        Groupe groupe = groupeRepository.getByNomGroupe(nomGroupe);
        
        String salleStr = request.getParameter("numeroSalle");
        Salle salle = salleRepository.getByNumeroSalle(salleStr);
        
        String hDebutStr = request.getParameter("HDebut");
        Date hDebut = Tools.getDateFromString(hDebutStr, "yyyy-MM-dd'T'HH:mm");

        
        String enseignantStr = request.getParameter("InitialesEnseignant");
        Enseignant enseignant = enseignantRepository.getByInitiales(enseignantStr);
        boolean succes = false;
        
        Seance retour = null;
        if (idSeance > 0){//if id exist
            retour = seanceRepository.update(idSeance, enseignement, enseignant, typeLecon, groupe, salle, hDebut, duree);
        }
        else{
            retour = seanceRepository.create(enseignement, enseignant, typeLecon, groupe, salle, hDebut, duree);
        }
        if(retour !=null){
                succes = true;
            }
        returned = new ModelAndView("seance");
        returned.addObject("newseance", succes);
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("groupesList",groupeRepository.findAll());
        returned.addObject("enseignementsList",enseignementRepository.findAll());
        returned.addObject("typeLeconsList",typeLeconRepository.findAll());
        returned.addObject("sallesList",salleRepository.findAll());
        return returned;
    }
    /*
    @RequestMapping(value = "deleteseance.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteSeance(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update seances
        String acronyme = request.getParameter("Acronyme");

        seanceRepository.remove(acronyme);

        // return to list
        returned = new ModelAndView("seances");
        Collection<Seance> myList = seanceRepository.findAll();
        returned.addObject("seancesList", myList);
        Collection<Enseignant> Enseignants = enseignantRepository.findAll();
        returned.addObject("enseignantsList", Enseignants);

        return returned;
    }
    */
}
