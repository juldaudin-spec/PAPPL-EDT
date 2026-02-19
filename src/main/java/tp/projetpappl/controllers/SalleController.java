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
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import tp.projetpappl.items.Salle;
import tp.projetpappl.repositories.SalleRepository;

/**
 *
 * @author julda
 */
@Controller
public class SalleController {

    @Autowired
    private SalleRepository salleRepository;

    @RequestMapping(value = "salle.do", method=RequestMethod.POST)
    public ModelAndView handlePostSalles(HttpServletRequest request) {

        ModelAndView returned = new ModelAndView("salle");
        returned.addObject("salle",new Salle());

        return returned;
    }
    @RequestMapping(value = "salles.do", method=RequestMethod.POST)
    public ModelAndView handlePostSalle(HttpServletRequest request) {
        List<Salle> myList = new ArrayList<Salle>(salleRepository.findAll());

        ModelAndView returned = new ModelAndView("salles");
        returned.addObject("sallesList", myList);

        return returned;
}

    @RequestMapping(value = "editsalle.do", method = RequestMethod.POST)
    public ModelAndView handleEditSallePost(HttpServletRequest request) {
        ModelAndView returned;

        String numeroSalle = request.getParameter("NumeroSalle");
        if (numeroSalle != null) {
            //ID may exist
            Salle salle = salleRepository.getByNumeroSalle(numeroSalle);
            returned = new ModelAndView("salle");
            returned.addObject("salle", salle);
        } else {
            returned = new ModelAndView("salles");
            Collection<Salle> myList = salleRepository.findAll();
            returned.addObject("sallesList", myList);

        }
        return returned;
    }

    @RequestMapping(value = "savesalle.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveSalle(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update salles
        String numeroSalle = request.getParameter("NumeroSalle");
        String capaciteStr = request.getParameter("Capacite");
        String typologie = request.getParameter("Typologie");
        int capacite = Tools.getIntFromString(capaciteStr);
        boolean succes = false;

        if (!(numeroSalle.isEmpty()) && numeroSalle != "") {
            salleRepository.create(numeroSalle, capacite, typologie);
            succes = true;

            // return to list
            returned = new ModelAndView("salle");
        }
        else{
            returned = new ModelAndView("salle");
        }
        returned.addObject("newsalle", succes);
        return returned;
    }

    @RequestMapping(value = "deletesalle.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteSalle(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update salles
        String numeroSalle = request.getParameter("NumeroSalle");

        salleRepository.remove(numeroSalle);

        // return to list
        returned = new ModelAndView("salles");
        Collection<Salle> myList = salleRepository.findAll();
        returned.addObject("sallesList", myList);

        return returned;
    }
}

