package gsdk.glib.ginet.tcp;

import java.net.ServerSocket;

import java.net.Socket;

import java.io.InputStreamReader;

import java.io.BufferedReader;

import java.io.IOException;

import java.util.List;

import java.util.HashMap;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GinetServerTCP -
 *
 * A thread-friendly, no-bloat, event-driven, and asynchronous TCP server
 * for Game Immediate mode Networking (Vinet) that is designed to be
 * easy and simple to use. The server manages client connections,
 * communication, and various events such as client connection,
 * disconnection, data reception, and exceptions.<br>
 *
 * Key Features:<br>
 * - Thread-friendly: Utilizes a separate thread for handling incoming client connections.<br>
 * - No-bloat: Designed with minimal and essential features for efficient server management.<br>
 * - Event-driven: Uses an event handler to manage different events such as client connection, disconnection, data reception, and exceptions.<br>
 * - Asynchronous: The server accepts and communicates with clients asynchronously.<br>
 * - Easy and simple: Provides straightforward methods for sending data, broadcasting messages, and emitting events to clients.<br><br>
 *
 * The GinetServerTCP class allows you to:<br>
 * - Initialize the server with a specified port and events handler.<br>
 * - Start the server and accept client connections.<br>
 * - Send data to specific clients or broadcast to all clients.<br>
 * - Emit custom events to specific clients or broadcast to all clients.<br>
 * - Handle different events through the GinetServerEventsTCP interface.<br>
 * - Soft handling of critical situations.<br><br>
 *
 * The Transmission Control Protocol (TCP) is one of the main protocols of the Internet protocol suite.
 * It originated in the initial network implementation in which it complemented the Internet Protocol (IP).
 * Therefore, the entire suite is commonly referred to as TCP/IP.
 * TCP provides reliable, ordered, and error-checked delivery of a stream of octets (bytes) between applications running on hosts communicating via an IP network.
 * Major internet applications such as the World Wide Web, email, remote administration, and file transfer rely on TCP, which is part of the Transport layer of the TCP/IP suite.
 * SSL/TLS often runs on top of TCP.
 *
 * TCP is connection-oriented, meaning that sender and receiver firstly need to establish a connection based on agreed parameters;
 * they do this through three-way handshake procedure. The server must be listening (passive open) for connection requests from clients before a connection is established.
 * Three-way handshake (active open), retransmission, and error detection adds to reliability but lengthens latency.
 * Applications that do not require reliable data stream service may use the User Datagram Protocol (UDP) instead, which provides a connectionless datagram service that prioritizes time over reliability.
 * TCP employs network congestion avoidance. However, there are vulnerabilities in TCP, including denial of service, connection hijacking, TCP veto, and reset attack.
 * <br>
 * @see <a href="https://en.wikipedia.org/wiki/Transmission_Control_Protocol">Transmission Control Protocol (Wikipedia).</a>
 */
public class GinetServerTCP {
    private final ServerSocket socket;

    private final List<GinetServerClient> clients;

    private final HashMap<String, String> visitors;

    private final GinetServerEventsTCP events;

    private boolean active;

    /**
     * Initialize Game Immediate mode Networking server (TCP).
     *
     * @param events_ Events handler.
     * @param port Server port.
     */
    public GinetServerTCP(GinetServerEventsTCP events_, int port) throws IOException {
        socket = new ServerSocket(port);

        clients = new CopyOnWriteArrayList<>();

        visitors = new HashMap<>();

        events = events_;

        active = true;

        server();
    }

    private void server() {
        events.start(this);

        new Thread(() -> {
            while(active) {
                try {
                    Socket client = socket.accept();

                    GinetServerClient vinetClient = new GinetServerClient(client, getVisitorIdentifier(socket.getInetAddress().getHostAddress()));

                    clients.add(vinetClient);

                    events.connection(vinetClient, this);

                    client(vinetClient);
                } catch(IOException ioExc) {
                    events.exception(ioExc, this);
                }
            }

            events.end(this);
        }).start();
    }

    private void client(GinetServerClient client) {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(client.getClient().getInputStream()))) {
            String data;

            try {
                while(active && !socket.isClosed() && (data = in.readLine()) != null) {
                    if(data.startsWith("EV-BGN ")) {
                        String[] parts = data.substring(7, data.indexOf(" EV-END")).split("&&&");

                        events.event(parts[0].replace("EVENT-NAME", "").trim(), parts[1].replace("EVENT-DATA", "").trim(), client, this);
                    } else {
                        events.receive(data, client, this);
                    }
                }
            } catch(IOException ioExc) {
                if(ioExc.getMessage().equals("Connection reset")) {
                    events.lost(client, this);
                } else {
                    if(!ioExc.getMessage().equals("Socket closed") && active) {
                        events.exception(ioExc, this);
                    }
                }
            }
        } catch(IOException ioExc) {
            events.exception(ioExc, this);
        } finally {
            events.disconnection(client, this);

            clients.remove(client);

            try {
                client.getClient().close();
            } catch(IOException ioExc) {
                events.exception(ioExc, this);
            }
        }
    }

    /**
     * Send data to specific client.
     *
     * @param data Data to send.
     * @param client Client.
     */
    public void send(String data, GinetServerClient client) {
        if(!hasClient(client)) return;

        client.send(data);
    }

    /**
     * Emit client event.
     *
     * @param eventName Event name.
     * @param data Event data.
     * @param client Client.
     */
    public void emit(String eventName, String data, GinetServerClient client) {
        send("EV-BGN EVENT-NAME%s&&&EVENT-DATA%s EV-END".formatted(eventName, data), client);
    }

    /**
     * Broadcast data to all clients.
     *
     * @param data Data to broadcast.
     */
    public void broadcast(String data) {
        for(GinetServerClient client : clients) send(data, client);
    }

    /**
     * Emit event for all clients.
     *
     * @param eventName Event name.
     * @param data Event data.
     */
    public void broadcastEvent(String eventName, String data) {
        for(GinetServerClient client : clients) emit(eventName, data, client);
    }

    /**
     * Disconnect client.
     *
     * @param client Client to disconnect.
     */
    public void disconnect(GinetServerClient client) {
        if(!hasClient(client)) return;

        try {
            client.getClient().close();
        } catch(IOException ioExc) {
            events.exception(ioExc, this);
        }
    }

    /**
     * Get client from array.
     *
     * @param index Client index.
     */
    public GinetServerClient getClient(int index) {
        return clients.get(index);
    }

    /**
     * Is client in list.
     *
     * @param client Client.
     */
    public boolean hasClient(GinetServerClient client) {
        return clients.contains(client);
    }

    /**
     * Stop server and disconnect all clients.
     */
    public void stop() {
        active = false;

        try {
            socket.close();

            for(GinetServerClient client : clients) {
                client.getClient().close();

                client.getClientOut().close();
            }
        } catch(IOException ioExc) {
            events.exception(ioExc, this);
        }
    }

    /**
     * Register visitor.
     *
     * @param client Client.
     */
    public void registerVisitor(GinetServerClient client) {
        visitors.put(client.getIP(), client.getClientIdentifier());
    }

    /**
     * Get client by visitor IP.
     *
     * @param ip Visitor IP.
     */
    public GinetServerClient getVisitor(String ip) {
        for(GinetServerClient client : clients) {
            if(client.getClientIdentifier().equals(getVisitorIdentifier(ip))) {
                return client;
            }
        }

        return null;
    }

    /**
     * Has the visitor connected to the server before?
     *
     * @param ip Visitor IP.
     */
    public boolean hadVisitor(String ip) {
        return visitors.containsKey(ip);
    }

    /**
     * Get visitor identifier or null if there was no visitor like this.
     *
     * @param ip Visitor IP.
     */
    public String getVisitorIdentifier(String ip) {
        return visitors.getOrDefault(ip, null);
    }

    /**
     * Get server socket.
     */
    public ServerSocket getServerSocket() {
        return socket;
    }

    /**
     * Get clients list.
     */
    public List<GinetServerClient> getClients() {
        return clients;
    }

    /**
     * Get visitors map (IP:Identifier).
     */
    public HashMap<String, String> getVisitors() {
        return visitors;
    }

    /**
     * Change server active state.
     *
     * @param active_ Active?
     */
    public void setActive(boolean active_) {
        active = active_;
    }

    /**
     * Is server active?
     */
    public boolean isActive() {
        return active;
    }
}
