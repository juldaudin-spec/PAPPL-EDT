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
import tp.projetpappl.items.Connection;
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

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private AuthHelper authHelper;

    @RequestMapping(value = "enseignement.do", method=RequestMethod.POST)
    public ModelAndView handlePostEnseignements(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned = new ModelAndView("enseignement");
        returned.addObject("enseignantsList", enseignantRepository.findAll());
        returned.addObject("enseignement",new Enseignement());
        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "enseignements.do", method=RequestMethod.POST)
    public ModelAndView handlePostEnseignement(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        List<Enseignement> myList = new ArrayList<>(enseignementRepository.findAll());
        Collections.sort(myList, Enseignement.getComparator());
        List<Enseignant> Enseignants = new ArrayList<>(enseignantRepository.findAll());
        Collections.sort(myList, Enseignement.getComparator());

        ModelAndView returned = new ModelAndView("enseignements");
        returned.addObject("enseignementsList", myList);
        returned.addObject("enseignantsList", Enseignants);
        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "editenseignement.do", method = RequestMethod.POST)
    public ModelAndView handleEditUserPost(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

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
        returned.addObject("user", user);
        return returned;
    }

    @RequestMapping(value = "saveenseignement.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveUser(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned;

        // Create or update enseignements
        String acronyme = request.getParameter("Acronyme");
        String nomEnseignement = request.getParameter("NomEnseignement");
        String filiere = request.getParameter("Filiere");
        String responsableStr = request.getParameter("InitialesEnseignant");
        System.out.println(responsableStr);
        System.out.println(nomEnseignement);
        System.out.println(acronyme);
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
        returned.addObject("user", user);
        return returned;
    }

    @RequestMapping(value = "deleteenseignement.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteUser(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

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
        returned.addObject("user", user);

        return returned;
    }
}