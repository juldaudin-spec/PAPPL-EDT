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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import static tp.projetpappl.controllers.Tools.getIntFromString;
import tp.projetpappl.items.Contient;
import tp.projetpappl.items.Enseignement;
import tp.projetpappl.items.Groupe;
import tp.projetpappl.items.TypeLecon;
import tp.projetpappl.repositories.ContientRepository;
import tp.projetpappl.repositories.GroupeRepository;

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
            returned.addObject("groupe",groupe);
        
            List<Enseignement> enseignements = contientRepository.findEnseignementByGroupe(groupe.getNomGroupe());
            returned.addObject("enseignements",enseignements);
            
            List<List<TypeLecon>> intituleByEnseignement=getIntituleByEnseignement(groupe, enseignements);
            returned.addObject("intitules",intituleByEnseignement);
            
            List<List<List<Contient>>> contients = getListContient(groupe, enseignements, intituleByEnseignement);
            returned.addObject("contients",contients);
            }
        return returned;
        }
        
        public List<List<TypeLecon>> getIntituleByEnseignement(Groupe groupe, List<Enseignement> enseignements){
            List<List<TypeLecon>> intituleByEnseignement=new ArrayList<>();
            if (!enseignements.isEmpty()){
                for (Enseignement enseignement : enseignements){
            List<TypeLecon> intitules = contientRepository.findIntituleByEnseignementByGroupe(enseignement.getAcronyme(), groupe.getNomGroupe());
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
                    i++;
                    for (TypeLecon typeLecon : intituleByEnseignement.get(i)){
            List<Contient> contientByType = contientRepository.findContientByIntituleByEnseignementByGroupe(typeLecon.getIntitule() ,enseignement.getAcronyme(), groupe.getNomGroupe());
            contientByEnseignement.add(contientByType);
                    }
                    contients.add(contientByEnseignement);
                    }
            }
        return contients;
        
    }
    
      
}
