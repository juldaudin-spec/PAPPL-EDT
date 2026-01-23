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
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Connection;
import tp.projetpappl.repositories.GroupeRepository;

/**
 *
 * @author julda
 */
@Controller
public class GroupeController {

    @Autowired
    private GroupeRepository groupeRepository;

    @Autowired
    private AuthHelper authHelper;

    @RequestMapping(value = "groupe.do", method=RequestMethod.POST)
    public ModelAndView handlePostGroupes(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned = new ModelAndView("groupe");
        returned.addObject("groupe",new Groupe());
        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "groupes.do", method=RequestMethod.POST)
    public ModelAndView handlePostGroupe(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        List<Groupe> myList = new ArrayList<>(groupeRepository.findAll());
        Collections.sort(myList, Groupe.getComparator());

        ModelAndView returned = new ModelAndView("groupes");
        returned.addObject("groupesList", myList);
        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "editgroupe.do", method = RequestMethod.POST)
    public ModelAndView handleEditUserPost(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned;

        String nomGroupe = request.getParameter("NomGroupe");
        if (nomGroupe != null) {
            //ID may exist
            Groupe groupe = groupeRepository.getByNomGroupe(nomGroupe);
            returned = new ModelAndView("groupe");
            returned.addObject("groupe", groupe);
        } else {
            returned = new ModelAndView("groupes");
            Collection<Groupe> myList = groupeRepository.findAll();
            returned.addObject("groupesList", myList);

        }
        returned.addObject("user", user);
        return returned;
    }

    @RequestMapping(value = "savegroupe.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveUser(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned;

        // Create or update groupes
        String nomGroupe = request.getParameter("NomGroupe");
        String nbEleveStr = request.getParameter("NbEleve");
        int nbEleve = Tools.getIntFromString(nbEleveStr);
        boolean succes = false;

        if (!(nomGroupe.isEmpty()) && nomGroupe != "") {
            groupeRepository.create(nomGroupe, nbEleve);
            succes = true;

            // return to list
            returned = new ModelAndView("groupe");
        }
        else{
            returned = new ModelAndView("groupe");
        }
        returned.addObject("newgroupe", succes);
        returned.addObject("user", user);
        return returned;
    }

    @RequestMapping(value = "deletegroupe.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteUser(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:index.do");
        }

        ModelAndView returned;

        // Create or update groupes
        String nomGroupe = request.getParameter("NomGroupe");

        groupeRepository.remove(nomGroupe);

        // return to list
        returned = new ModelAndView("groupes");
        Collection<Groupe> myList = groupeRepository.findAll();
        returned.addObject("groupesList", myList);
        returned.addObject("user", user);

        return returned;
    }
}