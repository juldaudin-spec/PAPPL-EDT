/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.web.servlet.ModelAndView;
import tp.projetpappl.items.Seance;

/**
 *
 * @author nathan
 */
public class AffichageControllerTest {
    
    public AffichageControllerTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }
    
    

    /**
     * Test of handleIndexGet method, of class AffichageController.
     */
    @org.junit.jupiter.api.Test
    public void testHandleIndexGet() {
        System.out.println("handleIndexGet");
        HttpServletRequest request = null;
        AffichageController instance = new AffichageController();
        ModelAndView expResult = null;
        ModelAndView result = instance.handleIndexGet(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of listHDebut method, of class AffichageController.
     */
    @org.junit.jupiter.api.Test
    void testListHDebut() {
        System.out.println("listHDebut");
        List<List<Seance>> myList2 = null;
        AffichageController instance = new AffichageController();
        List<Date> expResult = null;
        List<Date> result = instance.listHDebut(myList2);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of transform method, of class AffichageController.
     */
    @org.junit.jupiter.api.Test
    void testTransform() {
        System.out.println("transform");
        List<List<Seance>> myList2 = null;
        List<Date> listHDebut = null;
        AffichageController instance = new AffichageController();
        List<List<Seance>> expResult = null;
        List<List<Seance>> result = instance.transform(myList2, listHDebut);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of addSeanceLigne method, of class AffichageController.
     */
    @Test
    void testAddSeanceLigne() {
        System.out.println("addSeanceLigne");
        List<Seance> seanceGroupe = null;
        List<Seance> ligne = null;
        Date horaire = null;
        AffichageController instance = new AffichageController();
        instance.addSeanceLigne(seanceGroupe, ligne, horaire);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    
}
