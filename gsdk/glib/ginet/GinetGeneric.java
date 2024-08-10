package gsdk.glib.ginet;

import java.net.URL;
import java.net.URI;

import java.net.HttpURLConnection;

import java.net.http.HttpClient;

import java.net.http.HttpRequest;

import java.net.http.HttpResponse;

import java.nio.file.Path;

import java.nio.file.Paths;

/**
 * Utility class for checking internet connection, downloading files, sending GET requests, ETC.
 */
public class GinetGeneric {
    /**
     * Checks internet connection by trying to connect to specified address.
     * 
     * @param address Address to connect.
     * @param timeout Timeout (MS).
     */
    public static boolean internetConnection(String address, int timeout) {
        try{

            HttpURLConnection connection = (HttpURLConnection) new URL(address).openConnection();

            connection.setConnectTimeout(timeout);

            connection.connect();

            return connection.getResponseCode() == 200;
        } catch(Exception exc) {
            return false;
        }
    }

    /**
     * Checks internet connection by trying to connect to Google with one second timeout.
     */
    public static boolean internetConnection() {
        return internetConnection("http://google.com", 1000);
    }

    /**
     * Send GET request to URI and get response (method is supported only for Java 11 and more).
     * 
     * @param uri URI.
     */
    public static HttpResponse<String> get(String uri) {
        HttpClient client = HttpClient.newHttpClient();

        try {
            return client.send(HttpRequest.newBuilder().uri(URI.create(uri)).GET().build(), HttpResponse.BodyHandlers.ofString());
        } catch(Exception exception) {
            return null;
        }
    }

    /**
     * Get URI response code (method is supported only for Java 11 and more).
     * 
     * @param uri URI.
     */
    public static int getStatus(String uri) {
        return get(uri).statusCode();
    }

    /**
     * Get URI body response (method is supported only for Java 11 and more).
     * 
     * @param uri URI.
     */
    public static String getBody(String uri) {
        return get(uri).body();
    }

    /**
     * Save file to <code>out</code> path (method is supported only for Java 11 and more).
     * 
     * @param uri URI.
     * @param out Save path.
     */
    public static boolean download(String uri, String out) {
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<Path> response = client.send(HttpRequest.newBuilder().uri(URI.create(uri)).GET().build(), HttpResponse.BodyHandlers.ofFile(Paths.get(out)));

            return response.statusCode() == 200;
        } catch(Exception exception) {
            return false;
        }
    }
}
