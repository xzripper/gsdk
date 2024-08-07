package vsdk.vlib.vinet.tcp;

/**
 * Networking server events.
 */
public interface VinetServerEventsTCP {
    /**
     * Triggered when server is booted.
     *
     * @param server Server reference.
     */
    void start(VinetServerTCP server);

    /**
     * Triggered when server is stopped.
     *
     * @param server Server reference.
     */
    void end(VinetServerTCP server);

    /**
     * Triggered when new client connected.
     *
     * @param client Client.
     * @param server Server reference.
     */
    void connection(VinetServerClient client, VinetServerTCP server);

    /**
     * Triggered when client disconnected.
     *
     * @param client Client.
     * @param server Server reference.
     */
    void disconnection(VinetServerClient client, VinetServerTCP server);

    /**
     * Triggered when client send data.
     *
     * @param data Received data.
     * @param client Client that sent data.
     * @param server Server reference.
     */
    void receive(String data, VinetServerClient client, VinetServerTCP server);

    /**
     * Triggered when event is toggled by client.
     *
     * @param name Event name.
     * @param data Event data.
     * @param client Client that toggled event.
     * @param server Server reference.
     */
    void event(String name, String data, VinetServerClient client, VinetServerTCP server);

    /**
     * Triggered when exception raised on server side in `process` method.
     *
     * @param exception Raised exception.
     * @param server Server reference.
     */
    void exception(Exception exception, VinetServerTCP server);

    /**
     * Triggered when client left the server without disconnecting.
     *
     * @param client Client that left.
     * @param server Server reference.
     */
    void lost(VinetServerClient client, VinetServerTCP server);
}
