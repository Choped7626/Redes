package es.udc.redes.tutorial.udp.server;

import java.net.*;
import java.util.Arrays;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }
        DatagramSocket sDatagram = null;
        try {
            // Create a server socket
            int puerto = Integer.parseInt(argv[0]);
            sDatagram = new DatagramSocket(puerto);
            // Set maximum timeout to 300 secs
            sDatagram.setSoTimeout(300000);
            while (true) {
                // Prepare datagram for reception
                byte array[] = new byte[1024];
                DatagramPacket datagramRecivir = new DatagramPacket(array , array.length);
                // Receive the message
                sDatagram.receive(datagramRecivir);
                System.out.println("SERVER: Received "
                        + new String(datagramRecivir.getData(), 0, datagramRecivir.getLength())
                        + " from " + datagramRecivir.getAddress().toString() + ":"
                        + datagramRecivir.getPort());
                // Prepare datagram to send response
                String responder = new String(datagramRecivir.getData() , 0 , datagramRecivir.getLength());
                DatagramPacket datagramEnviar = new DatagramPacket(responder.getBytes() , responder.getBytes().length , datagramRecivir.getAddress() , datagramRecivir.getPort());
                // Send response
                sDatagram.send(datagramEnviar);
                System.out.println("SERVER: Sending "
                        + new String(datagramEnviar.getData()) + " to "
                        + datagramEnviar.getAddress().toString() + ":"
                        + datagramEnviar.getPort());
            }
          
        // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            sDatagram.close();
        }
    }
}
