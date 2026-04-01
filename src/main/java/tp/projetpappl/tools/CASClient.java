package tp.projetpappl.tools;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.StringReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;

/**
 * Classe utilitaire gérant toute la communication avec le serveur CAS
 * (Central Authentication Service) de Centrale Nantes.
 *
 * Cette classe est responsable de :
 * - La construction des URLs d'appel au serveur CAS
 * - La redirection HTTP du navigateur vers le CAS
 * - La validation des tickets CAS via serviceValidate
 * - L'extraction des données utilisateur depuis la réponse XML du CAS
 *
 * @author Oussama
 */
public class CASClient {

    /**
     * URL de base du serveur CAS de test de Centrale Nantes.
     */
    public static final String CASSERVERURL = "https://ssov6-test.ec-nantes.fr/cas";

    /**
     * Indicateur de désactivation de la vérification SSL.
     *
     * Le serveur CAS de test utilise un certificat auto-signé non reconnu
     * par Java. Ce flag permet de contourner cette vérification.
     *
     * ATTENTION : cette option ne doit jamais être activée en production.
     * En production, il faut importer le certificat du serveur CAS dans
     * le keystore Java et passer ce flag à false.
     */
    private static final boolean DISABLE_SSL_VERIFICATION_FOR_TEST = true;

    public CASClient() {
    }

    /**
     * Encode une URL pour qu'elle puisse être transmise en tant que paramètre
     * dans une autre URL (encodage ISO-8859-1).
     *
     * Utilisé pour encoder l'URL de callback avant de la passer au serveur CAS
     * en paramètre "service".
     *
     * @param url l'URL à encoder
     * @return l'URL encodée, ou l'URL originale en cas d'erreur d'encodage
     */
    public static String encodeCallbackURL(String url) {
        String callBackURL = url;
        try {
            callBackURL = URLEncoder.encode(url, "ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            System.out.println("ERROR encoding URL: " + ex.getMessage());
        }
        return callBackURL;
    }

    /**
     * Crée une réponse HTTP 302 pour rediriger le navigateur vers une autre URL.
     *
     * Utilisé pour rediriger l'utilisateur vers le serveur CAS lors de la
     * connexion, et vers la page de logout CAS lors de la déconnexion.
     *
     * @param thelRoute l'URL cible de la redirection
     * @return une ResponseEntity avec le statut 302 et l'en-tête Location
     */
    public ResponseEntity<String> redirectTo(String thelRoute) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", thelRoute);
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }

    /**
     * Récupère l'adresse IP locale du serveur en parcourant les interfaces
     * réseau disponibles. Exclut les adresses de loopback (127.0.0.1) et
     * retourne la première adresse IPv4 trouvée.
     *
     * Cette méthode est utilisée pour construire l'URL de callback que le
     * serveur CAS utilisera pour rappeler notre application. On utilise l'IP
     * directe plutôt qu'un nom de domaine car l'application tourne en local.
     *
     * @return l'adresse IP locale sous forme de chaîne, ou null si introuvable
     */
    public static String getLocalIPAdress() {
        InetAddress ip;

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    // On ignore le loopback et on ne garde que les adresses IPv4
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("ERROR getting network interface: " + ex.getMessage());
        }

        // Fallback sur getLocalHost si aucune interface n'a été trouvée
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException ex) {
            System.out.println("ERROR getting localhost: " + ex.getMessage());
        }

        return null;
    }

    /**
     * Construit l'URL de base de notre application à partir de l'IP locale.
     * Format : http://[IP]:8080/pappl
     *
     * Cette URL est utilisée comme préfixe pour construire les URLs de callback
     * transmises au serveur CAS.
     *
     * @return l'URL de base de l'application
     */
    public String getServerURL() {
        String returnedAdress = "http://" + CASClient.getLocalIPAdress() + ":8080/pappl";
        return returnedAdress;
    }

    /**
     * Crée un HttpClient configuré pour accepter tous les certificats SSL
     * sans vérification, y compris les certificats auto-signés.
     *
     * ATTENTION : cette méthode est uniquement destinée aux environnements
     * de test. Elle désactive complètement la vérification de la chaîne de
     * confiance SSL, ce qui expose à des attaques de type man-in-the-middle.
     * Ne jamais utiliser en production.
     *
     * @return un HttpClient avec vérification SSL désactivée, ou un client
     *         standard en cas d'erreur de configuration SSL
     */
    private static HttpClient createTrustingHttpClient() {
        try {
            // TrustManager qui accepte tous les certificats sans vérification
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            // Aucune vérification du certificat client
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            // Aucune vérification du certificat serveur
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            System.out.println("ERROR creating SSL context: " + ex.getMessage());
            // En cas d'erreur, on retourne un client standard
            return HttpClient.newHttpClient();
        }
    }

    /**
     * Valide un ticket CAS auprès du serveur CAS et récupère les informations
     * de l'utilisateur sous forme de document XML.
     *
     * Le flux est le suivant :
     * 1. On appelle l'endpoint serviceValidate du serveur CAS
     * 2. On lui fournit le ticket reçu et l'URL de callback
     * 3. Le serveur CAS retourne un XML contenant les attributs de l'utilisateur
     *    (notamment cas:uid)
     *
     * @param token       le ticket CAS reçu en paramètre de la requête callback
     * @param callbackURL l'URL de callback utilisée lors de la demande du ticket
     * @return le contenu XML de la réponse du serveur CAS, ou null en cas d'erreur
     */
    public String getCASUser(String token, String callbackURL) {
        if ((token != null) && (!token.isEmpty())) {
            try {
                // Construction de l'URL de validation du ticket
                String url = CASSERVERURL + "/serviceValidate?ticket=" + token
                        + "&service=" + CASClient.encodeCallbackURL(callbackURL);
                URI serviceToCall = new URI(url);

                HttpRequest request = HttpRequest
                        .newBuilder()
                        .uri(serviceToCall)
                        .GET()
                        .build();

                // Choix du client HTTP selon la configuration SSL
                HttpClient client;
                if (DISABLE_SSL_VERIFICATION_FOR_TEST) {
                    client = createTrustingHttpClient();
                } else {
                    client = HttpClient.newHttpClient();
                }

                HttpResponse<byte[]> responseOfByteArray = client.send(
                        request, HttpResponse.BodyHandlers.ofByteArray()
                );
                return new String(responseOfByteArray.body());

            } catch (URISyntaxException | IOException | InterruptedException ex) {
                System.out.println("ERROR in getCASUser: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Extrait la valeur d'un champ donné depuis un document XML.
     *
     * Utilisé pour extraire cas:uid depuis la réponse XML du serveur CAS.
     * Le champ doit apparaître exactement une fois dans le document.
     *
     * Exemple d'appel : getXMLData(response, "cas:uid")
     * retourne l'uid de l'utilisateur authentifié.
     *
     * @param xmlStr la chaîne XML à parser
     * @param field  le nom du tag XML dont on veut extraire la valeur
     * @return la valeur du tag demandé, ou une chaîne vide si introuvable
     */
    public String getXMLData(String xmlStr, String field) {
        if ((xmlStr != null) && (!xmlStr.isEmpty())) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(xmlStr)));

                NodeList elements = document.getElementsByTagName(field);
                // On s'assure que le tag est présent exactement une fois
                if (elements.getLength() == 1) {
                    Node n = elements.item(0);
                    String value = n.getFirstChild().getNodeValue();
                    return value;
                }
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                System.out.println("ERROR parsing XML for field '" + field + "': " + ex.getMessage());
            }
        }
        return "";
    }
}