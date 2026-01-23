/* -----------------------------------------
 * Projet CasProject
 *
 * Ecole Centrale Nantes
 * Jean-Yves MARTIN
 * ----------------------------------------- */
package tp.projetpappl.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import tp.projetpappl.items.Connection;
import tp.projetpappl.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import tp.projetpappl.repositories.ConnectionRepository;

/**
 * Login controller manages user login/logout
 *
 * @author kwyhr
 */
@Controller
public class LoginController {

    @Autowired
    private ConnectionRepository connectionRepository;

    @RequestMapping(value = "index.do", method = RequestMethod.GET)
    public ModelAndView handleIndexGET(HttpServletRequest request) {
        ModelAndView returned = new ModelAndView("index");
        return returned;
    }

    private String getCASCallbackURL(CASClient casClient) {
        return casClient.getServerURL() + "/caslogin.do";
    }

    private String getCASCallbackURLLogout(CASClient casClient) {
        return casClient.getServerURL() + "/index.do";
    }

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> handleLoginGET(HttpServletRequest request) {
        CASClient casClient = new CASClient();
        String callbackAdress = getCASCallbackURL(casClient);
        return casClient.redirectTo(CASClient.CASSERVERURL + "/login?service=" + CASClient.encodeCallbackURL(callbackAdress));
    }

    @RequestMapping(value = "caslogin.do", method = RequestMethod.GET)
    public ModelAndView handleCasLogin(HttpServletRequest request) {
        CASClient casClient = new CASClient();

        String token = request.getParameter("ticket");
        System.out.println("========================================");
        System.out.println("=== CAS LOGIN START ===");
        System.out.println("Ticket: " + token);

        String response = casClient.getCASUser(token, getCASCallbackURL(casClient));
        System.out.println("");
        System.out.println("=== FULL CAS RESPONSE ===");
        System.out.println(response);
        System.out.println("=== END CAS RESPONSE ===");
        System.out.println("");

        String uid = casClient.getXMLData(response, "cas:uid");
        System.out.println("Extracted UID with 'cas:uid': [" + uid + "]");

        // TRY ALTERNATIVE TAG NAMES IF EMPTY
        if (uid == null || uid.isEmpty()) {
            System.out.println("Trying alternative: 'uid' (without namespace)");
            uid = casClient.getXMLData(response, "uid");
            System.out.println("Result with 'uid': [" + uid + "]");
        }

        if (uid == null || uid.isEmpty()) {
            System.out.println("Trying alternative: 'user'");
            uid = casClient.getXMLData(response, "user");
            System.out.println("Result with 'user': [" + uid + "]");
        }

        System.out.println("");
        System.out.println("Final UID to use: [" + uid + "]");

        Connection connectUser = connectionRepository.create(uid);

        System.out.println("");
        System.out.println("Created user object: " + connectUser);
        if (connectUser != null) {
            System.out.println("User code: " + connectUser.getConnectionCode());
            System.out.println("User login: " + connectUser.getConnectionLogin());
        } else {
            System.out.println("ERROR: connectUser is NULL!");
        }

        ModelAndView returned = new ModelAndView("index");
        returned.addObject("user", connectUser);
        System.out.println("=== CAS LOGIN END ===");
        System.out.println("========================================");
        return returned;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> handlePOSTLogout(HttpServletRequest request) {
        CASClient casClient = new CASClient();

        String connectCode = request.getParameter("connexion");
        if ((connectCode != null) && (!connectCode.isEmpty())) {
            Connection user = connectionRepository.getByConnectionCode(connectCode);
            if (user != null) {
                connectionRepository.remove(user);
                String callbackAdress = getCASCallbackURLLogout(casClient);
                return casClient.redirectTo(CASClient.CASSERVERURL + "/logout?service=" + CASClient.encodeCallbackURL(callbackAdress));
            }
        }
        return casClient.redirectTo("index.do");
    }
}