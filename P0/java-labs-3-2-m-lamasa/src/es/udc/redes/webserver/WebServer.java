package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket;

public class WebServer {
    
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Format: es.udc.redes.webserver.WebServer <port>");
            System.exit(-1);
        }
        Socket cliente = null;
        ServerSocket socket = null;
        int port = Integer.parseInt(args[0]);
        try{
            socket = new ServerSocket(port);
            socket.setSoTimeout(300000);
            while (true){
                cliente = socket.accept();
                es.udc.redes.webserver.ServerThread conexParalela = new ServerThread(cliente);
                conexParalela.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            assert socket != null;
            socket.close();
        }
    }
}
