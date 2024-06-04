package es.udc.redes.tutorial.tcp.server;

import java.awt.datatransfer.Clipboard;
import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) throws IOException {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }
        ServerSocket socket = null;
        Socket ClienteSocket = null;
        int port = Integer.parseInt(argv[0]);
        try {
            // Create a server socket
            socket = new ServerSocket(port);
            // Set a timeout of 300 secs
            socket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                ClienteSocket = socket.accept();
                // Set the input channel
                BufferedReader recivido = new BufferedReader(new InputStreamReader(ClienteSocket.getInputStream()));
                // Set the output channel
                PrintWriter envio = new PrintWriter(ClienteSocket.getOutputStream(), true);
                // Receive the client message
                String mensaje = recivido.readLine();
                System.out.println("SERVER: Received " + mensaje
                        + " from " + ClienteSocket.getInetAddress().toString()
                        + ":" + ClienteSocket.getPort());
                // Send response to the client
                envio.println(mensaje);
                System.out.println("SERVER: Sending " + mensaje +
                        " to " + ClienteSocket.getInetAddress().toString() +
                        ":" + ClienteSocket.getPort());
                // Close the streams
                envio.close();
                recivido.close();
            }
        // Uncomment next catch clause after implementing the logic            
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            assert ClienteSocket != null;
            ClienteSocket.close();
        }
    }
}
