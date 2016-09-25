import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Anagrafica {
    public static char[] PK_anag;
    public static char[] Descrizione_anag;
    public static char[] Tipo_anag;

    public static boolean createFile() throws IOException {
        File fileName = new File("resources/anagrafica");
        String bytes = "";
        byte[] write = bytes.getBytes();
        if(!fileName.exists()){
            Path filePath = Paths.get(fileName.toString());
            Files.write(filePath, write);
            Main.log("File \"anagrafica\" creato con successo in " + fileName.toString());
            return true;
        }
        return false;
    }

    public static void menu() throws InterruptedException {
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


        }

        scanner.close();
    }

    public static void add() throws InterruptedException {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            Main.log("");
            Main.log("0: Esci dal programma");
            Main.log("6: Ritorna indietro");
            Main.log("Immettere codice causale:");
            System.out.print("> ");
            String input = scanner.nextLine();

            if("0".equals(input)) {
                Main.log("Ciao Ciao!");
                break;
            }

            if("6".equals(input)){
                menu();
                break;
            }

            if(PK_anag == null){
                Main.log("Immettere codice causale:");
                PK_anag = input.toCharArray();
            }
        }

        scanner.close();
    }
}
