package gsdk.glib.ginet.tcp;

/**
 * Networking TCP client events.
 */
public interface GinetClientEventsTCP {
    /**
     * Triggered when connected to the server.
     *
     * @param server Server socket.
     */
    void connection(GinetClientServer server, GinetClientTCP client);

    /**
     * Triggered when disconnected from the server.
     *
     * @param server Server socket.
     */
    void disconnection(GinetClientServer server, GinetClientTCP client);

    /**
     * Triggered when received data from the server.
     *
     * @param data Received data.
     * @param server Server socket.
     */
    void receive(String data, GinetClientServer server, GinetClientTCP client);

    /**
     * Triggered when event is toggled by server.
     *
     * @param name Event name.
     * @param data Event data.
     * @param server Server reference.
     * @param client Client reference.
     */
    void event(String name, String data, GinetClientServer server, GinetClientTCP client);

    /**
     * Triggered when exception raised on client side in `connect` method.
     *
     * @param exception Exception raised.
     */
    void exception(Exception exception, GinetClientTCP client);

    /**
     * Triggered when lost connection to the server.
     */
    void lost(GinetClientTCP client);

    /**
     * Triggered when connection time is out.
     */
    void timeout(GinetClientTCP client);
}
