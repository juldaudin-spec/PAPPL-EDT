/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tp.projetpappl.controllers;

/**
 *
 * @author julda
 */
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import tp.projetpappl.items.Enseignant;
import tp.projetpappl.repositories.EnseignantRepository;
import tp.projetpappl.items.Connection;
import tp.projetpappl.tools.ReadableFile;

/**
 *
 * @author julda
 */

@MultipartConfig
@Controller
public class EnseignantController {

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @RequestMapping(value = "enseignant.do", method = RequestMethod.POST)
    public ModelAndView handlePostEnseignant(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:login.do");
        }

        ModelAndView returned = new ModelAndView("enseignant");
        returned.addObject("enseignant", new Enseignant());

        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "enseignants.do", method = RequestMethod.POST)
    public ModelAndView handlePostEnseignants(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:login.do");
        }

        List<Enseignant> myList = new ArrayList<>(enseignantRepository.findAll());
        Collections.sort(myList, Enseignant.getComparator());

        ModelAndView returned = new ModelAndView("enseignants");
        returned.addObject("enseignantsList", myList);

        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "editenseignant.do", method = RequestMethod.POST)
    public ModelAndView handleEditEnseignantPost(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:login.do");
        }

        if (!authHelper.canModifyGlobal(request)) {
            ModelAndView forbidden = new ModelAndView("403");
            forbidden.addObject("user", user);
            return forbidden;
        }

        ModelAndView returned;

        String initiales = request.getParameter("Initiales");
        if (initiales != null) {
            //ID may exist
            Enseignant enseignant = enseignantRepository.getByInitiales(initiales);
            returned = new ModelAndView("enseignant");
            returned.addObject("enseignant", enseignant);
        } else {
            returned = new ModelAndView("enseignants");
            Collection<Enseignant> myList = enseignantRepository.findAll();
            returned.addObject("enseignantsList", myList);

        }

        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "saveenseignant.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveEnseignant(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:login.do");
        }

        if (!authHelper.canModifyGlobal(request)) {
            ModelAndView forbidden = new ModelAndView("403");
            forbidden.addObject("user", user);
            return forbidden;
        }

        ModelAndView returned;

        // Create or update enseignants
        String initiales = request.getParameter("Initiales");
        String firstName = request.getParameter("Prenom");
        String lastName = request.getParameter("Nom");
        boolean succes = false;

        if (!(initiales.isEmpty()) && initiales != "") {
            enseignantRepository.create(initiales, firstName, lastName);
            succes = true;

            // return to list
            returned = new ModelAndView("enseignant");
        } else {
            returned = new ModelAndView("enseignant");
        }
        returned.addObject("newenseignant", succes);

        returned.addObject("user", user);

        return returned;
    }

    @RequestMapping(value = "deleteenseignant.do", method = RequestMethod.POST)
    public ModelAndView handlePostDeleteEnseignant(HttpServletRequest request) {

        Connection user = authHelper.getAuthenticatedUser(request);
        if (user == null) {
            return new ModelAndView("redirect:login.do");
        }

        if (!authHelper.canModifyGlobal(request)) {
            ModelAndView forbidden = new ModelAndView("403");
            forbidden.addObject("user", user);
            return forbidden;
        }

        ModelAndView returned;

        // Create or update enseignants
        String initiales = request.getParameter("Initiales");

        enseignantRepository.remove(initiales);

        // return to list
        returned = new ModelAndView("enseignants");
        Collection<Enseignant> myList = enseignantRepository.findAll();
        returned.addObject("enseignantsList", myList);

        returned.addObject("user", user);

        return returned;
    }
    @RequestMapping(value = "createimportenseignants.do", method = RequestMethod.POST)
    public ModelAndView handlePostSaveImportEnseignant(
            @RequestParam("fichier") MultipartFile file,
            HttpServletRequest request) throws IOException {

        Connection user = authHelper.getAuthenticatedUser(request);

        ModelAndView returned;

        boolean succes = false;

        if (file != null && !file.isEmpty()) {

            // conversion MultipartFile → File
            File tempFile = File.createTempFile("upload_", file.getOriginalFilename());
            file.transferTo(tempFile);

            ReadableFile input = new ReadableFile(tempFile);
            List<List<String>> enseignantsliststr = input.readFile();

            if (!enseignantsliststr.isEmpty()) {
                enseignantRepository.createByListStr(enseignantsliststr);
                succes = true;
            }

            tempFile.delete(); // nettoyage
        }

        returned = new ModelAndView("enseignant");
        returned.addObject("newenseignant", succes);
        returned.addObject("user", user);

        return returned;
    }
}
