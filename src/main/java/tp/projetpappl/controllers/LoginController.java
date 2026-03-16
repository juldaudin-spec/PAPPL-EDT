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
 * Login controller manages user login/logout via CAS
 *
 * @author Assistant
 */
@Controller
public class LoginController {

    @Autowired
    private ConnectionRepository connectionRepository;

    /**
     * CAS callback URL
     */
    private String getCASCallbackURL(CASClient casClient) {
        return casClient.getServerURL() + "/caslogin.do";
    }

    private String getCASCallbackURLLogout(CASClient casClient) {
        return casClient.getServerURL() + "/index.do";
    }

    /**
     * GET mode Redirect to CAS
     */
    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> handleLoginGET(HttpServletRequest request) {
        CASClient casClient = new CASClient();
        String callbackAdress = getCASCallbackURL(casClient);
        return casClient.redirectTo(CASClient.CASSERVERURL + "/login?service=" + CASClient.encodeCallbackURL(callbackAdress));
    }

    /**
     * CAS login callback
     */
    @RequestMapping(value = "caslogin.do", method = RequestMethod.GET)
    public ModelAndView handleCasLogin(HttpServletRequest request) {
        CASClient casClient = new CASClient();

        String token = request.getParameter("ticket");
        String response = casClient.getCASUser(token, getCASCallbackURL(casClient));

        String uid = casClient.getXMLData(response, "cas:uid");
        Connection connectUser = connectionRepository.create(uid);

        ModelAndView returned = new ModelAndView("index");
        returned.addObject("user", connectUser);
        return returned;
    }

    /**
     * Logout from the Application and from CAS
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> handlePOSTLogout(HttpServletRequest request) {
        CASClient casClient = new CASClient();

        String connectCode = request.getParameter("connexion");
        if ((connectCode != null) && (!connectCode.isEmpty())) {
            Connection user = connectionRepository.getByConnectionCode(connectCode);
            if (user != null) {
                // Remove connection
                connectionRepository.remove(user);

                // Application disconnection
                String callbackAdress = getCASCallbackURLLogout(casClient);
                return casClient.redirectTo(CASClient.CASSERVERURL + "/logout?service=" + CASClient.encodeCallbackURL(callbackAdress));
            }
        }
        return casClient.redirectTo("index.do");
    }
}
