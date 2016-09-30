import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String args[]) throws IOException, InterruptedException {
        log("Programma di Gestione Aziendale by Riccardo Belletti | Classe 5° INB");
        createResourcesDir();
        Anagrafica.createFile();
        Parametri.createFile();
        Movimenti.createFile();
        menu();
    }

    public static void log(String logMsg){
        System.out.println(logMsg);
    }

    public static boolean createResourcesDir(){
        File resourcesDir = new File("./resources");
        if(!resourcesDir.exists()){
            resourcesDir.mkdir();
            Main.log("Cartella \"resources\" creata con successo in " + resourcesDir.toString());
            return true;
        }
        return false;
    }

    public static void menu() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            log("");
            log("Menù:");
            log("1: Anagrafica");
            log("2: Movimenti");
            log("3: Parametri");
            log("0: Esci dal programma");
            System.out.print("> ");
            String input = scanner.nextLine();

            if("0".equals(input)) {
                log("Arrivederci!");
                break;
            }

            if("1".equals(input)){
                Anagrafica.menu();
                break;
            }

            if("2".equals(input)){
                Movimenti.menu();
                break;
            }

            if("3".equals(input)){
                Parametri.menu();
                break;
            }

            if(input.equals("test")){ // Menù nascosto per i test
                // Non c'è nulla per adesso
            }
        }

        scanner.close();
    }

    public static void keyContinue(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            Main.log("Premere invio per continuare...");
            String input = scanner.nextLine();
            if(input.matches(".") || input.isEmpty()){
                break;
            }
        }
    }
}