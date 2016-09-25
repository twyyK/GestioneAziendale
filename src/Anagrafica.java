import sun.security.krb5.internal.crypto.Des;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class Anagrafica {
    public static char[] PK_anag;
    public static char[] Descrizione_anag;
    public static char[] Tipo_anag;
    public static String addTo;
    public static File fileName = new File("resources/anagrafica");
    public static Path filePath = Paths.get(fileName.toString());
    public static String alphanumericPattern = "^[a-zA-Z0-9]*$";

    public static boolean createFile() throws IOException {
        String bytes = "";
        byte[] write = bytes.getBytes();
        if(!fileName.exists()){
            Path filePath = Paths.get(fileName.toString());
            Files.write(filePath, write); // Creare il File
            Main.log("File \"anagrafica\" creato con successo in " + fileName.toString());
            return true;
        }
        return false;
    }

    public static void menu() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            Main.log("");
            Main.log("Menù Anagrafica:");
            Main.log("1: Aggiunge una nuova causale.");
            Main.log("2: Eliminare una causale specifica.");
            Main.log("3: Modifica una causale.");
            Main.log("4: Ricerca una determinata causale, tramite il tipo.");
            Main.log("5: Stampa tutte le causali.");
            Main.log("6: Ritorna indietro");
            Main.log("0: Esci dal programma");
            System.out.print("> ");
            String input = scanner.nextLine();

            if("0".equals(input)) {
                Main.log("Ciao Ciao!");
                break;
            }

            if("1".equals(input)){
                add();
                break;
            }

            if("6".equals(input)){
                Main.menu();
                break;
            }

            if("5".equals(input)){
                print();
                break;
            }
        }
        scanner.close();
    }

    public static void add() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            String input1,input2,input3,input4;
            input1 = input2 = input3 = input4 = new String();

            if (PK_anag == null) {
                // CODICE CAUSALE
                Main.log("");
                Main.log("Immettere codice causale (MAX 5):");
                System.out.print("> ");
                input1 = scanner.next();
                if(input1.toString().length() <= 5 && input1.matches(alphanumericPattern)){
                    PK_anag = input1.toCharArray();
                } else {
                    add();
                    break;
                }
            }

            if (Descrizione_anag == null) {
                // DESCRIZIONE CAUSALE
                Main.log("");
                Main.log("Descrizione causale (MAX 30):");
                System.out.print("> ");
                input2 = scanner.next();
                if(input2.toString().length() <= 30 && input2.matches(alphanumericPattern)){
                    Descrizione_anag = input2.toCharArray();
                } else {
                    add();
                    break;
                }
            }

            if (Tipo_anag == null) {
                // TIPO CAUSALE
                Main.log("");
                Main.log("Tipo causale (E,U):");
                System.out.print("> ");
                input3 = scanner.next();
                if(input3.toString().equalsIgnoreCase("E") && input3.matches(alphanumericPattern) || input3.toString().equalsIgnoreCase("U") && input3.matches(alphanumericPattern)){
                    Tipo_anag = input3.toCharArray();
                } else {
                    add();
                    break;
                }
            }

            if(PK_anag != null || Descrizione_anag != null || Tipo_anag != null){
                Main.log("Aggiunta causale con:");
                Main.log("Codice: "+input1);
                Main.log("Descrizione: "+input2);
                Main.log("Tipo: "+input3);
                Thread.sleep(3000);
                addTo = input1+" | "+input2+" | "+input3+"\n";
                Files.write(filePath, addTo.getBytes(), StandardOpenOption.APPEND);
                resetVar();
                menu();
                break;
            }
        }

        scanner.close();
    }

    public static void resetVar(){
        PK_anag = null;
        Descrizione_anag = null;
        Tipo_anag = null;
    }

    public static void print() throws IOException, InterruptedException {
        byte[] fileBytes = Files.readAllBytes(filePath);
        String printBytes = new String(fileBytes, "UTF-8");
        Main.log(printBytes);
        Thread.sleep(3000);
        menu();
    }
}
