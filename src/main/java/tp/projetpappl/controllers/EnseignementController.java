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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.repositories.EnseignantRepository;
import tp.projetpappl.repositories.EnseignementRepository;

/**
 *
 * @author julda
 */
@Controller
public class EnseignementController {

    @Autowired
    private EnseignementRepository enseignementRepository;
    @Autowired EnseignantRepository enseignantRepository;

    @RequestMapping(value = "enseignement.do", method=RequestMethod.POST)
    public ModelAndView handlePostEnseignements(HttpServletRequest request) {

        ModelAndView returned = new ModelAndView("enseignement");
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("enseignement",new Enseignement());

        return returned;
    }
    @RequestMapping(value = "enseignements.do", method=RequestMethod.POST)
    public ModelAndView handlePostEnseignement(HttpServletRequest request) {
        List<Enseignement> myList = new ArrayList<>(enseignementRepository.findAll());
        Collections.sort(myList, Enseignement.getComparator());

        ModelAndView returned = new ModelAndView("enseignements");
        returned.addObject("enseignementsList", myList);

        return returned;
}

    @RequestMapping(value = "editenseignement.do", method = RequestMethod.POST)
    public ModelAndView handleEditUserPost(HttpServletRequest request) {
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
        return returned;
    }

    @RequestMapping(value = "saveenseignement.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveUser(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update enseignements
        String acronyme = request.getParameter("Acronyme");
        String nomEnseignement = request.getParameter("NomEnseignement");
        String filiere = request.getParameter("Filiere");
        String responsableStr = request.getParameter("Responsable");
        Enseignant responsable = enseignantRepository.getByInitiales(responsableStr);
        boolean succes = false;

        if (!(acronyme.isEmpty()) && acronyme != ""){
            Enseignement retour = enseignementRepository.create(acronyme, nomEnseignement, filiere, responsable);
            if(retour !=null){
                succes = true;
            }
        }
        returned = new ModelAndView("enseignement");
        returned.addObject("newenseignement", succes);
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        return returned;
    }

    @RequestMapping(value = "deleteenseignement.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteUser(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update enseignements
        String acronyme = request.getParameter("Acronyme");

        enseignementRepository.remove(acronyme);

        // return to list
        returned = new ModelAndView("enseignements");
        Collection<Enseignement> myList = enseignementRepository.findAll();
        returned.addObject("enseignementsList", myList);

        return returned;
    }
}
