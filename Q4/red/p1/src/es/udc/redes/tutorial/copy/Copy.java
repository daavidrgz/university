package es.udc.redes.tutorial.copy;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Copy {

    public static void main(String[] args) {
        if ( args.length != 2 ) {
            System.err.println("Format: es.udc.redes.tutorial.copy.Copy <fichero origen> <fichero destino>");
            System.exit(-1);
        }

        int c;
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null; 

        try {
            inputStream = new FileInputStream(args[0]);
            outputStream = new FileOutputStream(args[1]);
            while ( (c = inputStream.read()) != -1 ) {
                outputStream.write(c);
            }
        } catch (IOException e) {
            System.err.println("IO Error");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if ( inputStream != null )
                    inputStream.close();
                if ( outputStream != null )
                    outputStream.close();
            }
            catch (IOException e) {
                System.err.println("IO Error");
            }
        }
    }
    
}
