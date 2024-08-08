Example of creating simple TCP chat.

Client:
```java
import gsdk.glib.ginet.tcp.GinetClientTCP;
import gsdk.glib.ginet.tcp.GinetClientEventsTCP;
import gsdk.glib.ginet.tcp.GinetClientServer;

import gsdk.glib.ginet.GinetTransferableObjectUtility;

import java.util.Scanner;

import java.io.IOException;

public class ClientTest {
    public static void main(String[] args) {
        GinetClientTCP client = new GinetClientTCP(new GinetClientEventsTCP() {
            @Override
            public void connection(GinetClientServer server, GinetClientTCP client) {
                System.out.println("Connected to the server.");

                server.answer(GinetTransferableObjectUtility.assemble("I'm connected!"));
            }

            @Override
            public void disconnection(GinetClientServer server, GinetClientTCP client) {
                if(client.isConnected()) {
                    server.answer(GinetTransferableObjectUtility.assemble("Bye bye!"));
                }

                System.out.println("Disconnected from the server.");
            }

            @Override
            public void receive(String data, GinetClientServer server, GinetClientTCP client) {
                String message = GinetTransferableObjectUtility.disassemble(data)[0];

                if(message.equals("disconnect")) client.disconnect();

                System.out.printf("Server says %s\n", message);
                System.out.printf("Server says %s (no disassembling)\n", data);
            }

            @Override
            public void event(String name, String data, GinetClientServer server, GinetClientTCP client) {
                System.out.printf("Event from server! %s : %s!\n", name, GinetTransferableObjectUtility.disassemble(data)[0]);
            }

            @Override
            public void exception(Exception exception, GinetClientTCP client) {
                exception.printStackTrace();
            }

            @Override
            public void lost(GinetClientTCP client) {
                System.out.println("Lost server connection!");
            }

            @Override
            public void timeout(GinetClientTCP client) {
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
                client.emit("message", GinetTransferableObjectUtility.assemble(messageToServer));
            } else {
                client.send(GinetTransferableObjectUtility.assemble(messageToServer));
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
import gsdk.glib.ginet.GinetServerTCP;
import gsdk.glib.ginet.GinetServerClient;
import gsdk.glib.ginet.GinetServerEventsTCP;

import gsdk.glib.ginet.GinetTransferableObjectUtility;

import java.util.Scanner;

import java.io.IOException;

public class ServerTest {
    public static void main(String[] args) {
        GinetServerTCP server = new GinetServerTCP(new GinetServerEventsTCP() {
            @Override
            public void start(GinetServerTCP server) {
                System.out.println("Server is running!");
            }

            @Override
            public void end(GinetServerTCP server) {
                System.out.println("Server is closed.");
            }

            @Override
            public void connection(GinetServerClient client, GinetServerTCP server) {
                System.out.printf("Client %s joined.\n", client.getClientIdentifier());

                if(server.hadVisitor(client.getIP())) {
                    System.out.printf("Hello again client %s!\n", client.getClientIdentifier());
                } else {
                    server.registerVisitor(client);

                    server.getVisitor(client.getIP()).send(GinetTransferableObjectUtility.assemble("Hello new client (%s)! Registered you as a new visitor!".formatted(client.getClientIdentifier())));
                }
            }

            @Override
            public void disconnection(GinetServerClient client, GinetServerTCP server) {
                System.out.printf("Client %s left from the server.\n", client.getClientIdentifier());
            }

            @Override
            public void receive(String data, GinetServerClient client, GinetServerTCP server) {
                System.out.printf("Client %s says %s\n", client.getClientIdentifier(), GinetTransferableObjectUtility.disassemble(data)[0]);
                System.out.printf("Client %s says %s (no disassembling)\n", client.getClientIdentifier(), data);
            }

            @Override
            public void event(String name, String data, GinetServerClient client, GinetServerTCP server) {
                System.out.printf("Got event from client!: %s: %s\n", name, GinetTransferableObjectUtility.disassemble(data)[0]);
            }

            @Override
            public void exception(Exception exception, GinetServerTCP server) {
                 System.out.printf("Exception!: %s: %s\n", exception.getClass().getName(), exception.getMessage());
            }

            @Override
            public void lost(GinetServerClient client, GinetServerTCP server) {
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
                server.broadcastEvent("message", GinetTransferableObjectUtility.assemble(messageToClients));
            } else {
                server.broadcast(GinetTransferableObjectUtility.assemble(messageToClients));
            }
        }
    }

    public static String input(Scanner scanner, String placeholder) {
        System.out.print(placeholder);

        return scanner.nextLine();
    }
}
```
