package vsdk.vlib.vinet.tcp;

/**
 * Networking TCP client events.
 */
public interface VinetClientEventsTCP {
    /**
     * Triggered when connected to the server.
     *
     * @param server Server socket.
     */
    void connection(VinetClientServer server, VinetClientTCP client);

    /**
     * Triggered when disconnected from the server.
     *
     * @param server Server socket.
     */
    void disconnection(VinetClientServer server, VinetClientTCP client);

    /**
     * Triggered when received data from the server.
     *
     * @param data Received data.
     * @param server Server socket.
     */
    void receive(String data, VinetClientServer server, VinetClientTCP client);

    /**
     * Triggered when event is toggled by server.
     *
     * @param name Event name.
     * @param data Event data.
     * @param server Server reference.
     * @param client Client reference.
     */
    void event(String name, String data, VinetClientServer server, VinetClientTCP client);

    /**
     * Triggered when exception raised on client side in `connect` method.
     *
     * @param exception Exception raised.
     */
    void exception(Exception exception, VinetClientTCP client);

    /**
     * Triggered when lost connection to the server.
     */
    void lost(VinetClientTCP client);

    /**
     * Triggered when connection time is out.
     */
    void timeout(VinetClientTCP client);
}
