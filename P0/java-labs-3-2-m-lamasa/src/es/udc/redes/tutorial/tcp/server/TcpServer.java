package es.udc.redes.tutorial.tcp.server;
import java.io.IOException;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

  public static void main(String argv[]) throws IOException {
    if (argv.length != 1) {
      System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
      System.exit(-1);
    }
    ServerSocket socket = null;
    Socket socketCliente = null;
    int port = Integer.parseInt(argv[0]);
    try {
      // Create a server socket
      socket = new ServerSocket(port);
      // Set a timeout of 300 secs
      socket.setSoTimeout(300000);
      while (true) {
        // Wait for connections
        socketCliente = socket.accept();
        // Create a ServerThread object, with the new connection as parameter
        ServerThread conexionParelela = new ServerThread(socketCliente);
        // Initiate thread using the start() method
        conexionParelela.start();
      }
    // Uncomment next catch clause after implementing the logic
    } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
     } finally{
        assert socket != null;
        socket.close();
    }
  }
}
