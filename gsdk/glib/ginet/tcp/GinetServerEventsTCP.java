package gsdk.glib.ginet.tcp;

/**
 * Networking server events.
 */
public interface GinetServerEventsTCP {
    /**
     * Triggered when server is booted.
     *
     * @param server Server reference.
     */
    void start(GinetServerTCP server);

    /**
     * Triggered when server is stopped.
     *
     * @param server Server reference.
     */
    void end(GinetServerTCP server);

    /**
     * Triggered when new client connected.
     *
     * @param client Client.
     * @param server Server reference.
     */
    void connection(GinetServerClient client, GinetServerTCP server);

    /**
     * Triggered when client disconnected.
     *
     * @param client Client.
     * @param server Server reference.
     */
    void disconnection(GinetServerClient client, GinetServerTCP server);

    /**
     * Triggered when client send data.
     *
     * @param data Received data.
     * @param client Client that sent data.
     * @param server Server reference.
     */
    void receive(String data, GinetServerClient client, GinetServerTCP server);

    /**
     * Triggered when event is toggled by client.
     *
     * @param name Event name.
     * @param data Event data.
     * @param client Client that toggled event.
     * @param server Server reference.
     */
    void event(String name, String data, GinetServerClient client, GinetServerTCP server);

    /**
     * Triggered when exception raised on server side in `process` method.
     *
     * @param exception Raised exception.
     * @param server Server reference.
     */
    void exception(Exception exception, GinetServerTCP server);

    /**
     * Triggered when client left the server without disconnecting.
     *
     * @param client Client that left.
     * @param server Server reference.
     */
    void lost(GinetServerClient client, GinetServerTCP server);
}
