import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class Parametri {
    public static char[] Codice_IVA; //Codice alfanumerico univoco della causale
    public static char[] Descrizione_iva;
    public static float iva = -1; //Iva
    public static String addTo; //Stringa utilizzata per aggiungere le causali al file 
    public static File fileName = new File("resources/parametri"); //Definisce il file Anagrafica
    public static Path filePath = Paths.get(fileName.toString()); //Definisce il percorso del file Anagrafica
    public static String alphanumericPattern = "^[a-zA-Z0-9\\-\\s]+$"; //Stringa Regex: utilizzata per filtrare i caratteri alfanumerici e spazi
    public static String alphanumericDescPattern = "^[a-zA-Z0-9\\-\\s\\/\\.]+$";
    public static boolean codeModify; //Stabilisce se la modifica del codice ha avuto successo
    public static boolean descModify; //Stabilisce se la modifica della descrizione ha avuto successo
    public static boolean IVAModify; //Stabilisce se la modifica della tipologia ha avuto successo

    public static boolean createFile() throws IOException {
        String bytes = "";
        byte[] write = bytes.getBytes(); //Salva i bytes della stringa nel byte array
        if(!fileName.exists()){
            Path filePath = Paths.get(fileName.toString()); //Converte il percorso in stringa
            Files.write(filePath, write); // Creare il File
            Main.log("File \"parametri\" creato con successo in " + fileName.toString());
            return true;
        }
        return false;
    }

    public static void menu() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        loop: while (true) {

            Main.log("Menù Parametri:");
            Main.log("1: Aggiungi un parametro.");
            Main.log("2: Eliminare un parametro.");
            Main.log("3: Modifica un parametro.");
            Main.log("4: Ricerca di tutti i parametri con una determinata IVA.");
            Main.log("5: Stampa tutti i parametri.");
            Main.log("9: Ritorna indietro");
            Main.log("0: Esci dal programma");
            System.out.print("> ");
            String input = scanner.nextLine();

            switch (input) {
                case "0":
                    Main.log("Arrivederci!");
                    System.exit(1);
                case "9":
                    Main.menu();
                    break loop;
                case "1":
                    add();
                    break loop;
                case "2":
                    delete();
                    break loop;
                case "3":
                    modify();
                    break loop;
                case "4":
                    stampa_iva();
                    break loop;
                case "5":
                    print();
                    break loop;
                default:
                    Main.log("");
                    Main.log("Funzione non disponibile!");
                    Main.keyContinue();
                    menu();
                    break loop;
            }
        }
        scanner.close();
    }

    public static void add() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {

            String input1,input2,input3,input4;
            input1 = input2 = input3 = input4 = new String();
            byte[] fileBytes = Files.readAllBytes(filePath);
            String printBytes = new String(fileBytes, "UTF-8");

            if (Codice_IVA == null) {
                // CODICE IVA
                Main.log("");
                Main.log("Immettere codice IVA (MAX 5):");
                System.out.print("> ");
                input1 = scanner.nextLine();
                //Se la stringa "Printbytes" che contiene il file Parametri, non contiene il codice causale emesso dall'utente crea una nuova causale
                if(input1.toString().length() <= 5 && input1.matches(alphanumericPattern) && !printBytes.contains(input1)){
                    Codice_IVA = input1.toCharArray();
                } else {
                    if(input1.toString().length() > 5){
                        Main.log("Il codice non può superare i 5 caratteri");
                        Main.keyContinue();
                    } else if(!input1.matches(alphanumericPattern)){
                        Main.log("Il codice deve essere alfanumerico");
                        Main.keyContinue();
                    } else if(printBytes.contains(input1)){
                        Main.log("Il codice deve essere univoco");
                        Main.keyContinue();
                    }
                    add();
                    break;
                }
            }

            if (Descrizione_iva == null) {
                // DESCRIZIONE IVA  
                Main.log("");
                Main.log("Descrizione causale (MAX 30):");
                System.out.print("> ");
                input2 = scanner.nextLine();
                if(input2.toString().length() <= 30 && input2.matches(alphanumericDescPattern)){
                    Descrizione_iva = input2.toCharArray();
                } else {
                    add();
                    break;
                }
            }

            if (iva == -1) {
                // Parametro
                Main.log("");
                Main.log("IVA (Compreso tra 0 e 100):");
                System.out.print("> ");
                input3 = scanner.nextLine();
                if(Float.parseFloat(input3) >= 0 && Float.parseFloat(input3) <= 100){
                    iva = Float.parseFloat(input3);
                } else {
                    add();
                    break;
                }
            }

            if(Codice_IVA != null && Descrizione_iva != null && iva >= 0 && iva <=100){
                String Codice_IVAString = String.valueOf(Codice_IVA); //Converte da char array a stringa
                String Descrizione_ivaString = String.valueOf(Descrizione_iva);
                String ivaString = String.valueOf(iva);
                Main.log("Aggiunta causale con:");
                Main.log("Codice: "+Codice_IVAString);
                Main.log("Descrizione: "+Descrizione_ivaString);
                Main.log("Percentuale IVA: "+ivaString);
                Main.keyContinue();
                addTo = Codice_IVAString+" | "+Descrizione_ivaString+" | "+ivaString+"\n";
                Files.write(filePath, addTo.getBytes(), StandardOpenOption.APPEND);
                resetVar();
                menu();
                break;
            }
        }

        scanner.close();
    }

    public static void resetVar(){
        Codice_IVA = null;
        Descrizione_iva = null;
        iva = -1;
    }

    public static boolean print() throws IOException, InterruptedException {
        byte[] fileBytes = Files.readAllBytes(filePath);
        String printBytes = new String(fileBytes, "UTF-8");
        if (printBytes.isEmpty()){ // Se il file è vuoto, restituisce un avviso.
            Main.log("Non ci sono dati da mostrare.");
            Main.keyContinue();
            menu();
            return false;
        } else {
            Main.log(printBytes);
            Main.keyContinue();
            menu();
            return true;
        }
    }

    public static void delete() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        byte[] fileBytes = Files.readAllBytes(filePath);
        String printBytes = new String(fileBytes, "UTF-8");
        while (true) {

            if(!printBytes.isEmpty()){
                Main.log("");
                Main.log("Premere invio senza scrivere nulla, cancella tutto!");
                Main.log("Inserire il codice causale da eliminare:");
                System.out.print("> ");
                String input1 = scanner.nextLine();

                if (printBytes.contains(input1)){
                    File tempFileName = new File("resources/tempParametri");
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName));
                    int n = 0;

                    String currentLine;

                    while((currentLine = reader.readLine()) != null){
                        if (currentLine.contains(input1+" |")){
                            n++;
                            continue; //Salta la parte sottostante e continua il giro
                        }
                        writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    writer.close();
                    reader.close();
                    fileName.delete();
                    tempFileName.renameTo(fileName);
                    if(n == 1){
                        Main.log("Eliminato 1 elemento!");
                    } else if(n == 0){
                    } else {
                        Main.log("Eliminati " + n + " elementi!");
                    }
                    Main.keyContinue();
                    menu();
                    break;
                } else {
                    Main.log("Il parametro non esiste!");
                    Main.keyContinue();
                    menu();
                    break;
                }
            } else {
                Main.log("Inserire dei dati.");
                menu();
                break;
            }

        }
        scanner.close();
    }

    public static void stampa_iva() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        byte[] fileBytes = Files.readAllBytes(filePath);
        String printBytes = new String(fileBytes, "UTF-8");
        while (true) {

            Main.log("");
            Main.log("Inserire IVA da filtrare:");
            System.out.print("> ");
            String input1 = scanner.nextLine();

            if(printBytes.isEmpty()){
                Main.log("Non ci sono dati da mostrare!");
                Main.keyContinue();
                menu();
                break;
            } else if(!printBytes.contains("| "+input1)){
                Main.log("Non ci sono dati da mostrare con \""+input1+"\"!");
                Main.keyContinue();
                menu();
                break;
            } else {
                if(Float.parseFloat(input1) >= 0 || Float.parseFloat(input1) <= 100){
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    String currentLine;
                    Main.log("Filtrando tutte le aliquote per percentuale \"" + input1+ "\":");
                    while((currentLine = reader.readLine()) != null){
                        if (!currentLine.contains("| "+input1)) continue;
                        Main.log(currentLine);
                    }
                    Main.keyContinue();
                    reader.close();
                    menu();
                    break;
                } else {
                    Main.log("Devi inserire un dato valido (Compreso tra 0 e 100)!");
                    Main.keyContinue();
                    stampa_iva();
                    break;
                }
            }
        }
        scanner.close();
    }

    public static void modify() throws IOException, InterruptedException {
        byte[] fileBytes = Files.readAllBytes(filePath);
        String printBytes = new String(fileBytes, "UTF-8");
        Scanner scanner = new Scanner(System.in);
        while(true){
            Main.log("Inserire codice del parametro: ");
            String input1 = scanner.nextLine();

            File tempFileName = new File("resources/tempParametri");
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName));

            String currentLine;

            while((currentLine = reader.readLine()) != null){
                if (currentLine.contains(input1+" |")){
                    String[] lineModify = currentLine.split(" \\| "); //Regex: Divide la stringa in array
                    Main.log("Cosa vuoi modificare (Codice,Descrizione,IVA)?");
                    String input2 = scanner.nextLine();
                    Main.log("Sostituirlo con?");
                    String input3 = scanner.nextLine();
                    if(input2.equalsIgnoreCase("codice") || input2.equalsIgnoreCase("c")){
                        if(input3.length() <= 5 && input3.matches(alphanumericPattern) && !printBytes.contains(input3)){
                            lineModify[0] = input3;
                            codeModify = true;
                        } else {
                            Main.log("Qualcosa non va :/");
                        }
                    } else if(input2.equalsIgnoreCase("descrizione") || input2.equalsIgnoreCase("d")){
                        if(input3.length() <= 30 && input3.matches(alphanumericDescPattern)){
                            lineModify[1] = input3;
                            descModify = true;
                        } else {
                            Main.log("Qualcosa non va :/");
                        }
                    } else if(input2.equalsIgnoreCase("iva") || input2.equalsIgnoreCase("i")){
                        if(Float.parseFloat(input3) >= 0 && Float.parseFloat(input3) <= 100){
                            float parsingFloat = Float.parseFloat(input3);
                            lineModify[2] = Float.toString(parsingFloat);
                            IVAModify = true;
                        } else {
                            Main.log("Qualcosa non va :/");
                        }
                    }
                    writer.write(lineModify[0] + " | " + lineModify[1] + " | " + lineModify[2]+ System.getProperty("line.separator"));
                } else {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
            writer.close();
            reader.close();
            fileName.delete();
            tempFileName.renameTo(fileName);
            Main.keyContinue();
            menu();
            break;
        }
        scanner.close();
    }
}