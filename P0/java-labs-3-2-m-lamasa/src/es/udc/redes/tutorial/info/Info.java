package es.udc.redes.tutorial.info;

import javax.naming.directory.BasicAttribute;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;

public class Info {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.info.Info <path relativo>");
            System.exit(-1);
        }
        Path ruta_relativa = Path.of(args[0]);
        File f = ruta_relativa.getFileName().toFile();
        PosixFileAttributes atributos = Files.readAttributes(ruta_relativa, PosixFileAttributes.class);
        String type = Files.probeContentType(ruta_relativa);

        System.out.println("Tamaño: " + atributos.size());
        System.out.println("Fecha última modificación: " + atributos.lastModifiedTime());
        System.out.println("Nombre: " + ruta_relativa.getFileName());
        System.out.println("Extension: " + ruta_relativa);
        System.out.println("Ruta absoluta: " + f.getAbsolutePath());

        if(type != null){
            System.out.println("Es un: " + type);
        }else{
            if (atributos.isRegularFile()) {                  //creo q esto é overkill q flipas
                System.out.println("Es un fichero de texto"); //pero asi asegurome
            } else if (atributos.isDirectory()) {
                System.out.println("Es un Directorio");
            }else{
                System.out.println("Unknown");
            }
        }


    }
}
