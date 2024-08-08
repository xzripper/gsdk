package gsdk.glib.ginet.tcp;

import java.net.Socket;

import java.net.InetAddress;

import java.io.PrintWriter;

import java.io.IOException;

import static java.util.UUID.randomUUID;;

/**
 * Server representation of a client.
 */
public class GinetServerClient {
    private final Socket client;

    private final InetAddress clientAddress;

    private final PrintWriter out;

    private final String clientIdentifier;

    protected GinetServerClient(Socket client_, String identifier) throws IOException {
        client = client_;

        clientAddress = client.getInetAddress();

        out = new PrintWriter(client.getOutputStream(), true);

        clientIdentifier = identifier == null ? randomUUID().toString() : identifier;
    }

    /**
     * Send data to client.
     *
     * @param data Data to send.
     */
    public void send(String data) {
        out.println(data);
    }

    /**
     * Disconnect from the server.
     */
    public void disconnect() throws IOException {
        client.close();
    }

    /**
     * Get client IP.
     */
    public String getIP() {
        return clientAddress.getHostAddress();
    }

    /**
     * Get client port.
     */
    public int getPort() {
        return client.getPort();
    }

    /**
     * Get client (Socket).
     */
    public Socket getClient() {
        return client;
    }

    /**
     * Get client out (PrintWriter).
     */
    public PrintWriter getClientOut() {
        return out;
    }

    /**
     * Get client identifier.
     */
    public String getClientIdentifier() {
        return clientIdentifier;
    }
}
