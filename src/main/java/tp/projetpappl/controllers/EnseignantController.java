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
import java.util.Date;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.repositories.EnseignantRepository;

/**
 *
 * @author julda
 */
@Controller
public class EnseignantController {

    @Autowired
    private EnseignantRepository enseignantRepository;

    @RequestMapping(value = "enseignants.do", method = RequestMethod.POST)
    public ModelAndView handlePostUsers(HttpServletRequest request) {
        List<Enseignant> myList = new ArrayList<>(enseignantRepository.findAll());
        Collections.sort(myList, Enseignant.getComparator());

        ModelAndView returned = new ModelAndView("enseignants");
        returned.addObject("enseignantsList", myList);

        return returned;
    }

    @RequestMapping(value = "editenseignants.do", method = RequestMethod.POST)
    public ModelAndView handleEditUserPost(HttpServletRequest request) {
        ModelAndView returned;

        String initiales = request.getParameter("initiales");

        if (initiales != null) {
            //ID may exist
            Enseignant enseignant = enseignantRepository.getByInitiales(initiales);
            returned = new ModelAndView("enseignants");
            returned.addObject("enseignants", enseignant);
        } else {
            returned = new ModelAndView("enseignants");
            Collection<Enseignant> myList = enseignantRepository.findAll();
            returned.addObject("enseignantsList", myList);

        }
        return returned;
    }

    @RequestMapping(value = "saveenseignant.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveUser(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update enseignants

        String initiales = request.getParameter("initiales");
        String firstName = request.getParameter("FirstName");
        String lastName = request.getParameter("LastName");
        String birthdateStr = request.getParameter("Birthdate");

        Date birthday = Tools.getDateFromString(birthdateStr, "yyyy-MM-dd");

        if (!(initiales.isEmpty()) && initiales !=""){
            enseignantRepository.update(initiales, firstName, lastName);
        }
        else{
            enseignantRepository.create(initiales, firstName, lastName);
        }
        // return to list
        returned = new ModelAndView("enseignants");
        Collection<Enseignant> myList = enseignantRepository.findAll();
        returned.addObject("enseignantsList", myList);
        return returned;
    }
    @RequestMapping(value = "deletenseignant.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteUser(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update enseignants

        String initiales = request.getParameter("initiales");

        Enseignant enseignant = enseignantRepository.getByInitiales(initiales);
        enseignantRepository.delete(enseignant);

        // return to list
        returned = new ModelAndView("enseignants");
        Collection<Enseignant> myList = enseignantRepository.findAll();
        returned.addObject("enseignantsList", myList);

        return returned;
    }
    
    @RequestMapping(value= "creerenseignant.do" , method = RequestMethod.POST)
    public ModelAndView handlePostCreateUser(){
        ModelAndView returned;
        
        Enseignant newEnseignant = new Enseignant();
        returned = new ModelAndView("enseignants");
        returned.addObject("enseignant", newEnseignant);
        
        return returned;
    }
}
