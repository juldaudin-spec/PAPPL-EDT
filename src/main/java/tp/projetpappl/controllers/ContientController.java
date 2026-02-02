/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import static tp.projetpappl.controllers.Tools.getIntFromString;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.Seance;
import tp.projetpappl.items.TypeLecon;
import tp.projetpappl.repositories.ContientRepository;
import tp.projetpappl.repositories.EnseignementRepository;
import tp.projetpappl.repositories.GroupeRepository;
import tp.projetpappl.repositories.SeanceRepository;

/**
 *
 * @author nathan
 */
@Controller
public class ContientController {
    @Autowired
    private GroupeRepository groupeRepository;
    @Autowired
    private ContientRepository contientRepository;
    @Autowired
    @Lazy
    private SeanceRepository seanceRepository;
    @Autowired
    @Lazy
    private EnseignementRepository enseignementRepository;

    @RequestMapping(value = "compteRenduGroupe.do", method = RequestMethod.POST)
    public ModelAndView handleIndexGet(HttpServletRequest request) {
        ModelAndView returned = new ModelAndView("compteRenduGroupe");
        String idStr = request.getParameter("idGroupe");
        Optional<Groupe> groupeOpt =null;
        if (idStr!=null){
            groupeOpt=groupeRepository.findById(idStr);
        }
        Groupe groupe;
        if (groupeOpt!=null){
            groupe=groupeOpt.get();
        
            List<Enseignement> enseignements = contientRepository.findEnseignementByGroupe(groupe);
            
            List<List<TypeLecon>> intituleByEnseignement=getIntituleByEnseignement(groupe, enseignements);
            
            List<List<List<Contient>>> contients = getListContient(groupe, enseignements, intituleByEnseignement);
            
            List<Seance> listSeance = seanceRepository.findSeanceByGroupe(groupe);
            seanceRepository.sortByEnseignementByIntitule(listSeance, enseignements, intituleByEnseignement);
            
            returned.addObject("groupe",groupe);
            returned.addObject("enseignements",enseignements);
            returned.addObject("intitules",intituleByEnseignement);
            returned.addObject("contients",contients);
            returned.addObject("seances",listSeance);
            }
        return returned;
        }
        
        public List<List<TypeLecon>> getIntituleByEnseignement(Groupe groupe, List<Enseignement> enseignements){
            List<List<TypeLecon>> intituleByEnseignement=new ArrayList<>();
            if (!enseignements.isEmpty()){
                for (Enseignement enseignement : enseignements){
            List<TypeLecon> intitules = contientRepository.findIntituleByEnseignementByGroupe(enseignement, groupe);
            intituleByEnseignement.add(intitules);
                    }}
        return intituleByEnseignement;
    }
    public List<List<List<Contient>>> getListContient (Groupe groupe, List<Enseignement> enseignements, List<List<TypeLecon>> intituleByEnseignement){
        List<List<List<Contient>>> contients = new ArrayList<>();
            int i=0;
            if (!intituleByEnseignement.isEmpty()){
                for (Enseignement enseignement : enseignements){
                    List<List<Contient>> contientByEnseignement = new ArrayList<>();
                    for (TypeLecon typeLecon : intituleByEnseignement.get(i)){
            List<Contient> contientByType = contientRepository.findContientByIntituleByEnseignementByGroupe(typeLecon ,enseignement, groupe);
            contientByEnseignement.add(contientByType);
                    }
                    i++;
                    contients.add(contientByEnseignement);
                    }
            }
        return contients;
        
    }
    
      
}
