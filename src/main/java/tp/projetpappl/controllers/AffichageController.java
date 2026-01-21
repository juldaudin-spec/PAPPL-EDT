/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
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
        List<String> listeGroupe= null;
        List<Seance> myList;
        List<List<Seance>> myList2=null;
        
        if (idStr!=null){
        listeGroupe=new ArrayList<>(idStr.length); 
        myList2=new ArrayList<>(idStr.length); 
        
        for (String elem :idStr){
            result = groupeRepository.findById(elem);
            if ((result.isPresent())){
                groupe= result.get();
                myList=groupe.getSeanceList();
                Collections.sort(myList,Seance.getComparator());  
                myList2.add(myList);
                listeGroupe.add(groupe.getNomGroupe());}
            }
        }
        
        List<Date> listHDebut = listHDebut(myList2);
        List<List<Seance>> listSeance = transform(myList2, listHDebut);
        
        
        returned.addObject("groupes", groupes);
        returned.addObject("HDebut", listHDebut);
        returned.addObject("listeSeance", listSeance);
        returned.addObject("listeGroupe", listeGroupe);
        return returned;
    }
    
    public List<Date> listHDebut(List<List<Seance>> myList2){
        List<Date> listHDebut=null;
        if (myList2!=null){
            listHDebut=new ArrayList<>();
            for (List<Seance> seanceGroupe : myList2){
                for (Seance seance : seanceGroupe){
                    if (!listHDebut.contains(seance.getHDebut())){
                        listHDebut.add(seance.getHDebut());
                    }
                }
            }
        }
        return listHDebut;
    }
    
    public List<List<Seance>> transform(List<List<Seance>> myList2, List<Date> listHDebut){
        List<List<Seance>> listeSeance=null;
        List<Seance> ligne;
        if (myList2!=null&&listHDebut!=null){
            listeSeance=new LinkedList<>();
            for (Date horaire : listHDebut){
                ligne = new ArrayList<>(myList2.size());
                for (List<Seance> SeanceGroupe : myList2){
                    if (!SeanceGroupe.isEmpty()){
                    if (horaire==SeanceGroupe.get(0).getHDebut()){
                        ligne.add(SeanceGroupe.remove(0));
                    }
                    else{
                        ligne.add(null);
                    }}
                    else{
                        ligne.add(null);
                    }
                }
                listeSeance.add(ligne);
            } 
        }
        return listeSeance;
    }
}
