package gsdk.glib.ginet.tcp;

import java.net.Socket;

import java.io.PrintWriter;

import java.io.IOException;

/**
 * Client representation of a server.
 */
public class GinetClientServer {
    private final Socket server;

    private final PrintWriter out;

    protected GinetClientServer(Socket server_) throws IOException {
        server = server_;

        out = new PrintWriter(server.getOutputStream(), true);
    }

    /**
     * Answer to server.
     *
     * @param data Data.
     */
    public void answer(String data) {
        if(validOut()) out.println(data);
    }

    /**
     * Is PrintWriter valid (!= null).
     */
    public boolean validOut() {
        return out != null;
    }

    /**
     * Get server socket.
     */
    public Socket getServer() {
        return server;
    }

    /**
     * Get server out (PrintWriter).
     */
    public PrintWriter getServerOut() {
        return out;
    }

    /**
     * Close out channel (PrintWriter).
     */
    public void close() {
        if(validOut()) out.close();
    }
}
