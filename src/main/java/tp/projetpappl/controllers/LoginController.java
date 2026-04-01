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
 * Controller gérant l'authentification des utilisateurs via le protocole CAS
 * (Central Authentication Service) de Centrale Nantes.
 *
 * Le flux d'authentification se déroule en trois étapes :
 * 1. L'utilisateur est redirigé vers le serveur CAS (login.do)
 * 2. Le CAS rappelle notre application avec un ticket (caslogin.do)
 * 3. L'utilisateur peut se déconnecter de l'application et du CAS (logout.do)
 *
 * @author Oussama
 */
@Controller
public class LoginController {

    @Autowired
    private ConnectionRepository connectionRepository;

    /**
     * Construit l'URL de callback que le serveur CAS utilisera pour renvoyer
     * l'utilisateur vers notre application après authentification.
     * Cette URL pointe vers la route caslogin.do.
     *
     * @param casClient instance du client CAS
     * @return l'URL complète de callback pour la connexion
     */
    private String getCASCallbackURL(CASClient casClient) {
        return casClient.getServerURL() + "/caslogin.do";
    }

    /**
     * Construit l'URL de retour utilisée après la déconnexion CAS.
     * L'utilisateur est renvoyé vers la page d'accueil de l'application.
     *
     * @param casClient instance du client CAS
     * @return l'URL complète de retour après déconnexion
     */
    private String getCASCallbackURLLogout(CASClient casClient) {
        return casClient.getServerURL() + "/index.do";
    }

    /**
     * Point d'entrée de la connexion. Redirige l'utilisateur vers la page
     * d'authentification du serveur CAS de Centrale Nantes.
     *
     * L'URL de callback est encodée et passée en paramètre "service" pour
     * indiquer au CAS où renvoyer l'utilisateur après authentification.
     *
     * @param request la requête HTTP entrante
     * @return une réponse HTTP 302 redirigeant vers le serveur CAS
     */
    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> handleLoginGET(HttpServletRequest request) {
        CASClient casClient = new CASClient();
        String callbackAdress = getCASCallbackURL(casClient);
        return casClient.redirectTo(
                CASClient.CASSERVERURL + "/login?service="
                        + CASClient.encodeCallbackURL(callbackAdress)
        );
    }

    /**
     * Route de callback appelée par le serveur CAS après authentification.
     *
     * Le CAS envoie un ticket en paramètre "ticket". On valide ce ticket
     * en rappelant le serveur CAS via serviceValidate, qui retourne un XML
     * contenant les informations de l'utilisateur. On en extrait l'uid CAS,
     * puis on crée une session en base de données.
     *
     * @param request la requête HTTP entrante, contenant le paramètre "ticket"
     * @return la vue index avec l'objet "user" contenant la session créée
     */
    @RequestMapping(value = "caslogin.do", method = RequestMethod.GET)
    public ModelAndView handleCasLogin(HttpServletRequest request) {
        CASClient casClient = new CASClient();

        // Récupération du ticket envoyé par le serveur CAS
        String token = request.getParameter("ticket");

        // Validation du ticket auprès du serveur CAS — retourne un XML
        String response = casClient.getCASUser(token, getCASCallbackURL(casClient));

        // Extraction de l'uid de l'utilisateur depuis le XML de réponse
        String uid = casClient.getXMLData(response, "cas:uid");

        // Création de la session en base de données avec l'uid récupéré
        Connection connectUser = connectionRepository.create(uid);

        // Transmission de la session à la vue pour affichage dans la navbar
        ModelAndView returned = new ModelAndView("index");
        returned.addObject("user", connectUser);
        return returned;
    }

    /**
     * Gère la déconnexion de l'utilisateur.
     *
     * Deux actions sont effectuées :
     * 1. Suppression de la session en base de données (déconnexion applicative)
     * 2. Redirection vers le logout du serveur CAS pour détruire également
     *    la session SSO, ce qui déconnecte l'utilisateur de toutes les
     *    applications CASifiées simultanément.
     *
     * Si le code de connexion est absent ou invalide, on redirige simplement
     * vers la page d'accueil sans appeler le CAS.
     *
     * @param request la requête HTTP entrante, contenant le paramètre "connexion"
     * @return une réponse HTTP 302 vers le logout CAS ou vers la page d'accueil
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> handlePOSTLogout(HttpServletRequest request) {
        CASClient casClient = new CASClient();

        String connectCode = request.getParameter("connexion");
        if ((connectCode != null) && (!connectCode.isEmpty())) {
            Connection user = connectionRepository.getByConnectionCode(connectCode);
            if (user != null) {
                // Suppression de la session en base de données
                connectionRepository.remove(user);

                // Redirection vers le logout du serveur CAS
                String callbackAdress = getCASCallbackURLLogout(casClient);
                return casClient.redirectTo(
                        CASClient.CASSERVERURL + "/logout?service="
                                + CASClient.encodeCallbackURL(callbackAdress)
                );
            }
        }

        // Code absent ou invalide — retour à l'accueil sans appel au CAS
        return casClient.redirectTo("index.do");
    }
}