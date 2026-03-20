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
 * Utility class for CAS authentication
 * 
 * @author Assistant
 */
public class CASClient {

    public static final String CASSERVERURL = "https://ssov6-test.ec-nantes.fr/cas";
    
    /**
     * CONFIGURATION: SSL verification disabled for test CAS server
     * For production, set to false and install proper certificates
     */
    private static final boolean DISABLE_SSL_VERIFICATION_FOR_TEST = true;

    public CASClient() {
    }

    /**
     * Encode callback URL to be used as a parameter
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
     * Browser redirect to another route
     */
    public ResponseEntity<String> redirectTo(String thelRoute) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", thelRoute);
        return new ResponseEntity<String>(headers, HttpStatus.FOUND);
    }

    /**
     * Get server IP address
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
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("ERROR getting network interface: " + ex.getMessage());
        }

        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException ex) {
            System.out.println("ERROR getting localhost: " + ex.getMessage());
        }

        return null;
    }

    /**
     * Get current server address
     */
    public String getServerURL() {
        String returnedAdress = "http://" + CASClient.getLocalIPAdress() + ":8080/pappl";
        return returnedAdress;
    }

    /**
     * Create an HttpClient that trusts all SSL certificates
     * WARNING: Only use for test/development environments!
     */
    private static HttpClient createTrustingHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
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
            return HttpClient.newHttpClient();
        }
    }

    /**
     * Get XML infos from CAS ticket
     */
    public String getCASUser(String token, String callbackURL) {
        if ((token != null) && (!token.isEmpty())) {
            try {
                String url = CASSERVERURL + "/serviceValidate?ticket=" + token + "&service=" + CASClient.encodeCallbackURL(callbackURL);
                URI serviceToCall = new URI(url);

                HttpRequest request = HttpRequest
                        .newBuilder()
                        .uri(serviceToCall)
                        .GET()
                        .build();

                HttpClient client;
                if (DISABLE_SSL_VERIFICATION_FOR_TEST) {
                    client = createTrustingHttpClient();
                } else {
                    client = HttpClient.newHttpClient();
                }
                
                HttpResponse<byte[]> responseOfByteArray = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                return new String(responseOfByteArray.body());
                
            } catch (URISyntaxException | IOException | InterruptedException ex) {
                System.out.println("ERROR in getCASUser: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Get tag value in a XML document
     */
    public String getXMLData(String xmlStr, String field) {
        if ((xmlStr != null) && (!xmlStr.isEmpty())) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(xmlStr)));

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
