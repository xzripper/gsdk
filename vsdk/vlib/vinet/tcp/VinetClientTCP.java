package vsdk.vlib.vinet.tcp;

import java.net.Socket;

import java.net.InetSocketAddress;

import java.net.SocketTimeoutException;

import java.net.BindException;

import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.io.IOException;

/**
 * VinetClientTCP -
 *
 * A thread-friendly, no-bloat, event-driven, and asynchronous TCP client
 * for Violent Immediate mode Networking (Vinet) that is designed to be
 * easy and simple to use. The client establishes a connection to a server,
 * manages communication, and handles various events such as connection,
 * disconnection, data reception, and exceptions.<br>
 *
 * Key Features:<br>
 * - Thread-friendly: Utilizes a separate thread for handling the connection and data reception.<br>
 * - No-bloat: Designed with minimal and essential features for efficient communication.<br>
 * - Event-driven: Uses an event handler to manage different events such as connection, disconnection, data reception, and exceptions.<br>
 * - Asynchronous: The client connects and communicates with the server asynchronously.<br>
 * - Easy and simple: Provides straightforward methods for connecting, sending data, and emitting events.<br><br>
 *
 * The VinetClientTCP class allows you to:<br>
 * - Initialize the client with server host, port, and connection timeout.<br>
 * - Connect to the server and manage the connection state.<br>
 * - Send data to the server and emit custom events.<br>
 * - Handle different events through the VinetClientEventsTCP interface.<br>
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
public class VinetClientTCP {
    private Socket socket;

    private final String host;

    private final int port;

    private final int timeout;

    private final InetSocketAddress inetAddress;

    private final VinetClientEventsTCP events;

    private VinetClientServer serverRepr;

    private boolean active;

    /**
     * Initialize Violent Immediate mode Networking client (TCP).
     *
     * @param events_ Events handler.
     * @param host_ Server host.
     * @param port_ Server port.
     * @param connectionTime Time client has to connect to the server.
     */
    public VinetClientTCP(VinetClientEventsTCP events_, String host_, int port_, int connectionTime) {
        host = host_;
        port = port_;

        timeout = connectionTime;

        inetAddress = new InetSocketAddress(host, port);

        events = events_;

        active = true;

        connect();
    }

    private void connect() {
        new Thread(() -> {
            try {
                socket = new Socket();

                try {
                    socket.connect(inetAddress, timeout);
                } catch(BindException bExc) {
                    events.exception(bExc, this);
                } catch(SocketTimeoutException timeoutExc) {
                    events.timeout(this);
                }

                if(socket.isConnected()) {
                    serverRepr = new VinetClientServer(socket);

                    events.connection(serverRepr, this);

                    try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        String data;

                        try {
                            while(active && (data = in.readLine()) != null) {
                                if(data.startsWith("EV-BGN ")) {
                                    String[] parts = data.substring(7, data.indexOf(" EV-END")).split("&&&");

                                    events.event(parts[0].replace("EVENT-NAME", "").trim(), parts[1].replace("EVENT-DATA", "").trim(), serverRepr, this);
                                } else {
                                    events.receive(data, serverRepr, this);
                                }
                            }
                        } catch(IOException ioExc) {
                            if(ioExc.getMessage().equals("Connection reset")) {
                                events.lost(this);
                            } else {
                                if(!ioExc.getMessage().equals("Connection closed") && active) {
                                    events.exception(ioExc, this);
                                }
                            }
                        }
                    } catch(IOException ioExc) {
                        events.exception(ioExc, this);
                    } finally {
                        disconnect();
                    }
                }
            } catch(IOException ioExc) {
                events.exception(ioExc, this);
            }
        }).start();
    }

    /**
     * Send data to server.
     *
     * @param data Data.
     */
    public void send(String data) {
        if(socket != null && !socket.isClosed() && serverRepr != null && serverRepr.validOut()) {
            serverRepr.answer(data);
        }
    }

    /**
     * Emit server event.
     *
     * @param eventName Event name.
     * @param data Event data.
     */
    public void emit(String eventName, String data) {
        send("EV-BGN EVENT-NAME%s&&&EVENT-DATA%s EV-END".formatted(eventName, data));
    }

    /**
     * Disconnect from the server.
     */
    public void disconnect() {
        active = false;

        if(socket != null) {
            events.disconnection(serverRepr, this);

            try {
                socket.close();
            } catch(IOException ioExc) {
                events.exception(ioExc, this);
            }

            if(serverRepr != null) serverRepr.close();
        }
    }

    /**
     * Is successfully connected to the server.
     */
    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    /**
     * Get connection host.
     */
    public String getHost() {
        return host;
    }

    /**
     * Get connection port.
     */
    public int getPort() {
        return port;
    }

    /**
     * Get time client has to connect.
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Get inet socket address.
     */
    public InetSocketAddress getSocketAddress() {
        return inetAddress;
    }

    /**
     * Get client socket.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Get server representation.
     */
    public VinetClientServer getServerRepr() {
        return serverRepr;
    }

    /**
     * Change client active state.
     *
     * @param active_ Active?
     */
    public void setActive(boolean active_) {
        active = active_;
    }

    /**
     * Is client active.
     */
    public boolean isActive() {
        return active;
    }
}
