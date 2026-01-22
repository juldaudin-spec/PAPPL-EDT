/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

/**
 * Ceci est le controlleur pour tout ce qui concerne l'affichage de l'emplois du temps (EDT)
 * @author nathan
 */
@Controller
public class AffichageController {
    
    @Autowired
    private GroupeRepository groupeRepository;
    
    /**
     * permet d'ouvrir la page d'affichage des EDT
     * @param request
     * @return 
     */
    @RequestMapping(value="affichageEDT.do",method=RequestMethod.POST)
    public ModelAndView handleIndexGet(HttpServletRequest request){
        ModelAndView returned = new ModelAndView("affichageEDT");
        
        if (request!=null){
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
        returned.addObject("listeGroupe", listeGroupe);}
        return returned;
    }
    
    /**
     * Permet de récupérer la liste des heures où les cours commencent
     * @param myList2
     * @return 
     */
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
            listHDebut.sort(Comparator.naturalOrder());
        }
        return listHDebut;
    }
    /**
     * permet de transformer la liste qui affiche les cours à l'horizontal vers une liste qui permet d'afficher les cours à la verticale
     * @param myList2
     * @param listHDebut
     * @return 
     */
    public List<List<Seance>> transform(List<List<Seance>> myList2, List<Date> listHDebut){
        List<List<Seance>> listeSeance=null;
        List<Seance> ligne;
        if (myList2!=null&&listHDebut!=null){
            listeSeance=new LinkedList<>();
            for (Date horaire : listHDebut){
                ligne = new ArrayList<>(myList2.size());
                for (List<Seance> seanceGroupe : myList2){
                    addSeanceLigne(seanceGroupe, ligne, horaire);
                }
                listeSeance.add(ligne);
            } 
        }
        return listeSeance;
    }
    
    /**
     * fonction qui permet d'ajouter une séance à la ligne si l'horaire de la ligne correspond à celle de la séance et que tout va bien
     * @param seanceGroupe
     * @param ligne
     * @param horaire 
     */
    public void addSeanceLigne(List<Seance> seanceGroupe, List<Seance> ligne, Date horaire){
        if (seanceGroupe!=null&&ligne!=null){
        if ((!seanceGroupe.isEmpty())&&horaire!=null){
                    if (horaire.compareTo(seanceGroupe.get(0).getHDebut())==0){
                        ligne.add(seanceGroupe.remove(0));
                    }
                    else{
                        ligne.add(null);
                    }}
                    else{
                        ligne.add(null);
                    }
                
    }}
}
