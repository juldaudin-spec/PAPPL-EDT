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
import tp.projetpappl.repositories.GroupeRepository;

/**
 *
 * @author julda
 */
@Controller
public class GroupeController {

    @Autowired
    private GroupeRepository groupeRepository;

    @RequestMapping(value = "groupe.do", method=RequestMethod.POST)
    public ModelAndView handlePostGroupes(HttpServletRequest request) {

        ModelAndView returned = new ModelAndView("groupe");
        returned.addObject("groupe",new Groupe());

        return returned;
    }
    @RequestMapping(value = "groupes.do", method=RequestMethod.POST)
    public ModelAndView handlePostGroupe(HttpServletRequest request) {
        List<Groupe> myList = new ArrayList<>(groupeRepository.findAll());
        Collections.sort(myList, Groupe.getComparator());

        ModelAndView returned = new ModelAndView("groupes");
        returned.addObject("groupesList", myList);

        return returned;
}

    @RequestMapping(value = "editgroupe.do", method = RequestMethod.POST)
    public ModelAndView handleEditGroupePost(HttpServletRequest request) {
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
        return returned;
    }

    @RequestMapping(value = "savegroupe.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveGroupe(HttpServletRequest request) {

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
        return returned;
    }

    @RequestMapping(value = "deletegroupe.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteGroupe(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update groupes
        String nomGroupe = request.getParameter("NomGroupe");

        groupeRepository.remove(nomGroupe);

        // return to list
        returned = new ModelAndView("groupes");
        Collection<Groupe> myList = groupeRepository.findAll();
        returned.addObject("groupesList", myList);

        return returned;
    }
    
    @RequestMapping(value = "csvgroupe.do", method = RequestMethod.POST)
    public ModelAndView handlePostCSVGroupe(HttpServletRequest request) {

        ModelAndView returned;

        // Create or update groupes
        String fichier = request.getParameter("csvFile");
        ArrayList<Class> Format = new ArrayList<Class>(2);
        Format.add(String.class);
        Format.add(int.class);
        ArrayList<ArrayList<Object>> donnees= new ArrayList<ArrayList<Object>>();
        try{
            donnees = Tools.haveValues(fichier, Format);
        } catch(Exception e){
            e.printStackTrace();
        }
        String nbEleveStr = request.getParameter("NbEleve");
        int nbEleve = Tools.getIntFromString(nbEleveStr);
        boolean success = true;
        for (List<Object> valeur : donnees){
            if(valeur.get(0).getClass()==String.class && valeur.get(1).getClass()==int.class){
                groupeRepository.create((String) valeur.get(0),(int) valeur.get(1));
            }
            else{
                success = false;
            }
        }
        returned = new ModelAndView("groupe");
        returned.addObject("newgroupe", success);
        return returned;
    }
}

