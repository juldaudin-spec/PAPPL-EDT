/* -----------------------------------------
 * Projet CasProject
 *
 * Ecole Centrale Nantes
 * Jean-Yves MARTIN
 * ----------------------------------------- */
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

/**
 * Manage CAS.
 *
 * @author kwyhr
 */
public class CASClient {

    public static final String CASSERVERURL = "https://ssov6-test.ec-nantes.fr/cas";

    /**
     * default constructor
     */
    public CASClient() {
    }

    /**
     * Encode callBack URL to be used as a parameter
     *
     * @param url
     * @return
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
     * Browser is redirected to another route
     *
     * @param thelRoute
     * @return
     */
    public ResponseEntity<String> redirectTo(String thelRoute) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", thelRoute);
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }

    /**
     * Get server ip adress
     *
     * @return
     */
    public static String getLocalIPAdress() {
        InetAddress ip;

        // Try to find current IP adress through NetworkInterface
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("ERROR getting network interface: " + ex.getMessage());
        }

        // Doest worK Maybe through InetAddress ?
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException ex) {
            System.out.println("ERROR getting localhost: " + ex.getMessage());
        }

        // Doest work !!!
        return null;
    }

    /**
     * Get current server address
     *
     * @return
     */
    public String getServerURL() {
        String returnedAdress = "http://" + CASClient.getLocalIPAdress() + ":8080/pappl-1.0";
        return returnedAdress;
    }

    /**
     * Get XML infos from CAS ticket
     *
     * @param token
     * @param callbackURL
     * @return
     */
    public String getCASUser(String token, String callbackURL) {
        System.out.println("=== getCASUser() START ===");
        System.out.println("Token: " + token);
        System.out.println("Callback URL: " + callbackURL);

        if ((token != null) && (!token.isEmpty())) {
            try {
                // Generate URI
                String url = CASSERVERURL + "/serviceValidate?ticket=" + token + "&service=" + CASClient.encodeCallbackURL(callbackURL);
                System.out.println("Full validation URL: " + url);

                URI serviceToCall = new URI(url);

                // Build request
                HttpRequest request = HttpRequest
                        .newBuilder()
                        .uri(serviceToCall)
                        .GET()
                        .build();

                System.out.println("Sending HTTP request to CAS...");

                // Launch request and get result
                HttpClient client = HttpClient.newHttpClient();
                HttpResponse<byte[]> responseOfByteArray = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

                System.out.println("HTTP Response Status: " + responseOfByteArray.statusCode());
                String responseBody = new String(responseOfByteArray.body());
                System.out.println("Response body length: " + responseBody.length() + " characters");
                System.out.println("=== getCASUser() SUCCESS ===");

                return responseBody;

            } catch (URISyntaxException ex) {
                System.out.println("ERROR: Invalid URI syntax");
                System.out.println("Message: " + ex.getMessage());
                ex.printStackTrace();
            } catch (IOException ex) {
                System.out.println("ERROR: IOException during HTTP request");
                System.out.println("Message: " + ex.getMessage());
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                System.out.println("ERROR: Request interrupted");
                System.out.println("Message: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            System.out.println("ERROR: Token is null or empty!");
        }

        System.out.println("=== getCASUser() FAILED - Returning null ===");
        return null;
    }

    /**
     * Get tag value in a XML document
     *
     * @param xmlStr
     * @param field
     * @return
     */
    public String getXMLData(String xmlStr, String field) {
        if ((xmlStr != null) && (!xmlStr.isEmpty())) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(xmlStr)));

                // get tags
                NodeList elements = document.getElementsByTagName(field);
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