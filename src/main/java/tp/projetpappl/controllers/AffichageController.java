/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Seance;
import tp.projetpappl.repositories.GroupeRepository;
import tp.projetpappl.repositories.SeanceRepository;

/**
 *
 * @author nathan
 */
@Controller
public class AffichageController {
    
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private GroupeRepository groupeRepository;
    
    @RequestMapping(value="affichageEDT.do",method=RequestMethod.POST)
    public ModelAndView handleIndexGet(HttpServletRequest request){
        ModelAndView returned = new ModelAndView("affichageEDT");
        
        String[] idStr=request.getParameterValues("idGroupe");
        
        List<Groupe> groupes=groupeRepository.findAll();
        
        Optional<Groupe> result;
        Groupe groupe;
        List<String> listeGroupe=new ArrayList<>(idStr.length); 
        List<Seance> myList;
        List<List<Seance>> myList2=new ArrayList<>(idStr.length); 
        
        for (String elem :idStr){
            result = groupeRepository.findById(elem);
            if ((result.isPresent())){
                groupe= result.get();
                myList=groupe.getSeanceList();
                Collections.sort(myList,Seance.getComparator());  
                myList2.add(myList);
                listeGroupe.add(groupe.getNomGroupe());}
            }
        
        returned.addObject("groupes", groupes);
        returned.addObject("listeSeance", myList2);
        returned.addObject("listeGroupe", listeGroupe);
        return returned;
    }
    
}
