package es.udc.redes.webserver;


import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;


public class ServerThread extends Thread {

    private final Socket socket;

    private static File findFile(File dir, String name) {
        File result = null;
        File[] dirlist  = dir.listFiles();
        for(int i = 0; i < Objects.requireNonNull(dirlist).length; i++) {
            if(dirlist[i].isDirectory()) {
                result = findFile(dirlist[i], name);
                if (result!=null) break;
            } else if(dirlist[i].getName().matches(name)) {
                return dirlist[i];
              }
        }
        return result;
    }

    private void httpResponse(File file_enesimo, Date date, PrintWriter respuesta ,SimpleDateFormat simpleDateFormat, int codigo) throws IOException {
        String type = Files.probeContentType(file_enesimo.toPath());
        String mensaje;
        switch (codigo) {
            case 200:
                mensaje = "HTTP/1.0 200 OK" + "\r\n";
                break;
            case 304:
                mensaje = "HTTP/1.0 304 Not Modified" + "\r\n";
                break;
            case 400:
                mensaje = "HTTP/1.0 400 Bad Request" + "\r\n";
                break;
            default:
                mensaje = "HTTP/1.0 404 Not Found" + "\r\n";
                break;
        };
        mensaje += "Date: " + simpleDateFormat.format(date) + "\r\n";
        mensaje += "Server: Choped Unix Server" + "\r\n";
        mensaje += "Last-Modified: " + simpleDateFormat.format(new Date(file_enesimo.lastModified())) + "\r\n";
        mensaje += "Content-Length: " + file_enesimo.length() + "\r\n";
        mensaje += "Content-Type: " + ((type == null) ? "application/octet-stream" : type) + "\r\n";
        respuesta.println(mensaje);
    }

    private void entityBody(File file_enesimo, OutputStream outputStream) throws IOException {
        byte[] passDataFich = Files.readAllBytes(file_enesimo.toPath());
        outputStream.write(passDataFich);
        outputStream.flush();
        outputStream.close();
        return;
    }

    private int Modified(BufferedReader request, File buscado, Date date, SimpleDateFormat simpleDateFormat, PrintWriter respuesta ) throws IOException, ParseException {
        String s = null;
        String[] fecha = new String[0];
        Date fechaLocal;
        Date fechaRecivida;
        while (!((s = request.readLine()).equals(""))){
            if(s.contains("If-Modified-Since")){
                fecha = s.split(" " , 2);
                fechaLocal = new Date(buscado.lastModified());
                fechaRecivida = simpleDateFormat.parse(fecha[1]);
                if(fechaLocal.after(fechaRecivida)){
                    httpResponse(buscado, date, respuesta, simpleDateFormat, 304);
                    return 0;
                }else{
                    httpResponse(buscado, date, respuesta, simpleDateFormat, 200);
                    return 1;
                }
            }
        }
        httpResponse(buscado, date, respuesta, simpleDateFormat, 200);
        return 1;
    }

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    public void run() {
        try {
            BufferedReader request = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter respuesta = new PrintWriter(socket.getOutputStream(), true);
            OutputStream outputStream = socket.getOutputStream();

            String fstHTTPreq = request.readLine();
            if(fstHTTPreq == null)
                return;
            String[] datosHTTP = fstHTTPreq.split(" /| ");

            String workingDir = System.getProperty("user.dir");
            File mainFolder = new File(workingDir);
            File buscado = findFile(mainFolder , datosHTTP[1]);

            Date date = new Date();
            String patron = "EEE, dd MMM YYYY HH:mm:ss z";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patron);

            if(!(Objects.equals(datosHTTP[0], "GET") || datosHTTP[0].equals("HEAD"))){

                File f400 = new File("p1-files/error400.html");
                httpResponse(f400, date, respuesta, simpleDateFormat, 400);
                entityBody(f400, outputStream);

            }

            if(buscado != null){

                int i = Modified(request , buscado , date , simpleDateFormat , respuesta);
                if(!datosHTTP[0].equals("HEAD"))
                    if(i == 1)
                        entityBody(buscado, outputStream);

            }else{

                File f404 = new File("p1-files/error404.html");
                httpResponse(f404, date, respuesta, simpleDateFormat, 404);
                if(!datosHTTP[0].equals("HEAD"))
                    entityBody(f404, outputStream);

            }
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}