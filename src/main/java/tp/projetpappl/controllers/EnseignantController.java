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
import tp.projetpappl.items.Connection;
import tp.projetpappl.repositories.EnseignantRepository;

/**
 *
 * @author julda
 */
@Controller
public class EnseignantController {

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Autowired
    private AuthHelper authHelper;

    @RequestMapping(value = "enseignant.do", method=RequestMethod.POST)
    public ModelAndView handlePostEnseignants(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned = new ModelAndView("enseignant");
        returned.addObject("enseignant",new Enseignant());
        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "enseignants.do", method=RequestMethod.POST)
    public ModelAndView handlePostEnseignant(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        List<Enseignant> myList = new ArrayList<>(enseignantRepository.findAll());
        Collections.sort(myList, Enseignant.getComparator());

        ModelAndView returned = new ModelAndView("enseignants");
        returned.addObject("enseignantsList", myList);
        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "editenseignant.do", method = RequestMethod.POST)
    public ModelAndView handleEditUserPost(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned;

        String initiales = request.getParameter("Initiales");
        if (initiales != null) {
            //ID may exist
            Enseignant enseignant = enseignantRepository.getByInitiales(initiales);
            returned = new ModelAndView("enseignant");
            returned.addObject("enseignant", enseignant);
        } else {
            returned = new ModelAndView("enseignants");
            Collection<Enseignant> myList = enseignantRepository.findAll();
            returned.addObject("enseignantsList", myList);

        }
        returned.addObject("user", user);
        return returned;
    }

    @RequestMapping(value = "saveenseignant.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveUser(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned;

        // Create or update enseignants
        String initiales = request.getParameter("Initiales");
        String firstName = request.getParameter("Prenom");
        String lastName = request.getParameter("Nom");
        boolean succes = false;

        if (!(initiales.isEmpty()) && initiales != "") {
            enseignantRepository.create(initiales, firstName, lastName);
            succes = true;

            // return to list
            returned = new ModelAndView("enseignant");
        }
        else{
            returned = new ModelAndView("enseignant");
        }
        returned.addObject("newenseignant", succes);
        returned.addObject("user", user);
        return returned;
    }

    @RequestMapping(value = "deleteenseignant.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteUser(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned;

        // Create or update enseignants
        String initiales = request.getParameter("Initiales");

        enseignantRepository.remove(initiales);

        // return to list
        returned = new ModelAndView("enseignants");
        Collection<Enseignant> myList = enseignantRepository.findAll();
        returned.addObject("enseignantsList", myList);
        returned.addObject("user", user);

        return returned;
    }
}