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
import java.math.BigInteger;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Salle;
import tp.projetpappl.items.TypeLecon;
import tp.projetpappl.repositories.ContientRepository;
import tp.projetpappl.repositories.EnseignantRepository;
import tp.projetpappl.repositories.EnseignementRepository;
import tp.projetpappl.repositories.SalleRepository;
import tp.projetpappl.repositories.TypeLeconRepository;

/**
 *
 * @author julda
 */
@Controller
public class EnseignementController {

    @Autowired
    private EnseignementRepository enseignementRepository;
    @Autowired
    private EnseignantRepository enseignantRepository;
    @Autowired
    private TypeLeconRepository typeLeconRepository;
    @Autowired
    private ContientRepository contientRepository;
    @Autowired
    private SalleRepository salleRepository;

    @RequestMapping(value = "enseignement.do", method = RequestMethod.POST)
    public ModelAndView handlePostEnseignements(HttpServletRequest request) {

        ModelAndView returned = new ModelAndView("enseignement");
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("enseignement", new Enseignement());
        List<TypeLecon> leconList = typeLeconRepository.findAll();
        returned.addObject("typeLeconsList", leconList);
        List<Salle> salleList = salleRepository.findAll();
        returned.addObject("sallesList", salleList);

        return returned;
    }

    @RequestMapping(value = "enseignements.do", method = RequestMethod.POST)
    public ModelAndView handlePostEnseignement(HttpServletRequest request) {
        List<Enseignement> myList = new ArrayList<>(enseignementRepository.findAll());
        Collections.sort(myList, Enseignement.getComparator());
        List<Enseignant> Enseignants = new ArrayList<>(enseignantRepository.findAll());
        Collections.sort(myList, Enseignement.getComparator());

        ModelAndView returned = new ModelAndView("enseignements");
        returned.addObject("enseignementsList", myList);
        returned.addObject("enseignantsList", Enseignants);

        return returned;
    }

    @RequestMapping(value = "editenseignement.do", method = RequestMethod.POST)
    public ModelAndView handleEditEnseignementPost(HttpServletRequest request) {
        ModelAndView returned;

        String acronyme = request.getParameter("Acronyme");
        if (acronyme != null) {
            //ID may exist
            Enseignement enseignement = enseignementRepository.getByAcronyme(acronyme);
            returned = new ModelAndView("enseignement");
            returned.addObject("enseignement", enseignement);
            returned.addObject("enseignantsList", enseignantRepository.findAll());
        } else {
            returned = new ModelAndView("enseignements");
            Collection<Enseignement> myList = enseignementRepository.findAll();
            returned.addObject("enseignementsList", myList);

        }

        returned.addObject("typeLeconsList", typeLeconRepository.findAll());
        returned.addObject("sallesList", salleRepository.findAll());
        return returned;
    }

    @RequestMapping(value = "saveenseignement.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveEnseignement(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update enseignements
        String acronyme = request.getParameter("Acronyme");
        String nomEnseignement = request.getParameter("NomEnseignement");
        String filiere = request.getParameter("Filiere");
        String responsableStr = request.getParameter("InitialesEnseignant");
        ArrayList<Contient> contientList = new ArrayList<Contient>();
        HashMap<String, String> nomEnseignants
                = Tools.getArrayFromRequest(request, "e");
        ArrayList<Enseignant> enseignants = new ArrayList<>();

        for (String iniEnseignant : nomEnseignants.values()) {
            Enseignant e = enseignantRepository.getByInitiales(iniEnseignant);
            if (e != null) {
                enseignants.add(e);
            }
        }
        Enseignant responsable = enseignantRepository.getByInitiales(responsableStr);
        boolean succes = false;
        if (!(acronyme.isEmpty()) && acronyme != "") {
            Enseignement retour = enseignementRepository.create(acronyme, nomEnseignement, filiere, responsable, enseignants);
            if (retour != null) {
                succes = true;
            }
        }
        for (TypeLecon typeLecon : typeLeconRepository.findAll()) {
            String heuresStr = request.getParameter(typeLecon.getIntitule());
            int heures = Tools.getIntFromString(heuresStr);
            String demande = "salle[" + typeLecon.getIntitule() + "]";
            String salle = request.getParameter(demande);
            BigInteger h = BigInteger.valueOf(heures);
            Contient contient = contientRepository.create(acronyme, typeLecon.getIntitule(), h, salle);
        }
        
        returned = new ModelAndView("enseignement");
        returned.addObject("newenseignement", succes);
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("typeLeconsList", typeLeconRepository.findAll());
        returned.addObject("sallesList", salleRepository.findAll());
        returned.addObject("enseignement", enseignementRepository.getByAcronyme(acronyme)); // si besoin

        return returned;
    }

    @RequestMapping(value = "deleteenseignement.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteEnseignement(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update enseignements
        String acronyme = request.getParameter("Acronyme");

        enseignementRepository.remove(acronyme);

        // return to list
        returned = new ModelAndView("enseignements");
        Collection<Enseignement> myList = enseignementRepository.findAll();
        returned.addObject("enseignementsList", myList);
        Collection<Enseignant> Enseignants = enseignantRepository.findAll();
        returned.addObject("enseignantsList", Enseignants);

        return returned;
    }
}
