/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nathan
 */
@Controller
public class AffichageController {
    
    @RequestMapping(value="affichageEDT.do")
    public ModelAndView handleIndexGet(){
        return new ModelAndView("affichageEDT");
        
    }
    
}
