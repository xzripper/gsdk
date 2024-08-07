Example of creating simple TCP chat.

Client:
```java
import vsdk.vlib.vinet.tcp.VinetClientTCP;
import vsdk.vlib.vinet.tcp.VinetClientEventsTCP;
import vsdk.vlib.vinet.tcp.VinetClientServer;

import vsdk.vlib.vinet.VinetTransferableObjectUtility;

import java.util.Scanner;

import java.io.IOException;

public class ClientTest {
    public static void main(String[] args) {
        VinetClientTCP client = new VinetClientTCP(new VinetClientEventsTCP() {
            @Override
            public void connection(VinetClientServer server, VinetClientTCP client) {
                System.out.println("Connected to the server.");

                server.answer(VinetTransferableObjectUtility.assemble("I'm connected!"));
            }

            @Override
            public void disconnection(VinetClientServer server, VinetClientTCP client) {
                if(client.isConnected()) {
                    server.answer(VinetTransferableObjectUtility.assemble("Bye bye!"));
                }

                System.out.println("Disconnected from the server.");
            }

            @Override
            public void receive(String data, VinetClientServer server, VinetClientTCP client) {
                String message = VinetTransferableObjectUtility.disassemble(data)[0];

                if(message.equals("disconnect")) client.disconnect();

                System.out.printf("Server says %s\n", message);
                System.out.printf("Server says %s (no disassembling)\n", data);
            }

            @Override
            public void event(String name, String data, VinetClientServer server, VinetClientTCP client) {
                System.out.printf("Event from server! %s : %s!\n", name, VinetTransferableObjectUtility.disassemble(data)[0]);
            }

            @Override
            public void exception(Exception exception, VinetClientTCP client) {
                exception.printStackTrace();
            }

            @Override
            public void lost(VinetClientTCP client) {
                System.out.println("Lost server connection!");
            }

            @Override
            public void timeout(VinetClientTCP client) {
                System.out.printf("Tried connecting to the server for %d/ms but failed!\n", client.getTimeout());

                client.disconnect();
            }
        }, "IP TO SERVER PC", 8080, 5000);

        Scanner scanner = new Scanner(System.in);

        while(client.isActive()) {
            String messageToServer = input(scanner, "Message to server:\n");

            if(messageToServer.equals("disconnect")) {
                client.disconnect();

                break;
            }

            if(messageToServer.startsWith("ev ")) {
                client.emit("message", VinetTransferableObjectUtility.assemble(messageToServer));
            } else {
                client.send(VinetTransferableObjectUtility.assemble(messageToServer));
            }
        }
    }

    public static String input(Scanner scanner, String placeholder) {
        System.out.print(placeholder); return scanner.nextLine();
    }
}
```

Server:
```java
import vsdk.vlib.vinet.VinetServerTCP;
import vsdk.vlib.vinet.VinetServerClient;
import vsdk.vlib.vinet.VinetServerEventsTCP;

import vsdk.vlib.vinet.VinetTransferableObjectUtility;

import java.util.Scanner;

import java.io.IOException;

public class ServerTest {
    public static void main(String[] args) {
        VinetServerTCP server = new VinetServerTCP(new VinetServerEventsTCP() {
            @Override
            public void start(VinetServerTCP server) {
                System.out.println("Server is running!");
            }

            @Override
            public void end(VinetServerTCP server) {
                System.out.println("Server is closed.");
            }

            @Override
            public void connection(VinetServerClient client, VinetServerTCP server) {
                System.out.printf("Client %s joined.\n", client.getClientIdentifier());

                if(server.hadVisitor(client.getIP())) {
                    System.out.printf("Hello again client %s!\n", client.getClientIdentifier());
                } else {
                    server.registerVisitor(client);

                    server.getVisitor(client.getIP()).send(VinetTransferableObjectUtility.assemble("Hello new client (%s)! Registered you as a new visitor!".formatted(client.getClientIdentifier())));
                }
            }

            @Override
            public void disconnection(VinetServerClient client, VinetServerTCP server) {
                System.out.printf("Client %s left from the server.\n", client.getClientIdentifier());
            }

            @Override
            public void receive(String data, VinetServerClient client, VinetServerTCP server) {
                System.out.printf("Client %s says %s\n", client.getClientIdentifier(), VinetTransferableObjectUtility.disassemble(data)[0]);
                System.out.printf("Client %s says %s (no disassembling)\n", client.getClientIdentifier(), data);
            }

            @Override
            public void event(String name, String data, VinetServerClient client, VinetServerTCP server) {
                System.out.printf("Got event from client!: %s: %s\n", name, VinetTransferableObjectUtility.disassemble(data)[0]);
            }

            @Override
            public void exception(Exception exception, VinetServerTCP server) {
                 System.out.printf("Exception!: %s: %s\n", exception.getClass().getName(), exception.getMessage());
            }

            @Override
            public void lost(VinetServerClient client, VinetServerTCP server) {
                System.out.printf("Client %s left without saying goodbye :(\n", client.getClientIdentifier());
            }
        }, 8080);

        Scanner scanner = new Scanner(System.in);

        while(server.isActive()) {
            String messageToClients = input(scanner, "Message to clients:\n");

            if(messageToClients.equals("stop")) {
                server.stop();

                break;
            }

            if(messageToClients.startsWith("ev ")) {
                server.broadcastEvent("message", VinetTransferableObjectUtility.assemble(messageToClients));
            } else {
                server.broadcast(VinetTransferableObjectUtility.assemble(messageToClients));
            }
        }
    }

    public static String input(Scanner scanner, String placeholder) {
        System.out.print(placeholder);

        return scanner.nextLine();
    }
}
```
