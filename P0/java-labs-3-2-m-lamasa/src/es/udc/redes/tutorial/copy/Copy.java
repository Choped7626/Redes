package es.udc.redes.tutorial.copy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Copy {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Format: es.udc.redes.tutorial.copy.Copy <fichero origen> <fichero destino>");
            System.exit(-1);
        }
        String fichero_origen = args[0];        //son rutas
        String fichero_destino = args[1];       //supoño (teñen q ser, ou totales ou desde o current working direcotry)

        FileInputStream in = null;
        FileOutputStream out = null;

        try{
            in = new FileInputStream(fichero_origen);
            out = new FileOutputStream(fichero_destino);
            int c;

            while ((c = in.read()) != -1){
                out.write(c);
            }
        }finally {
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
        }

    }
    
}
