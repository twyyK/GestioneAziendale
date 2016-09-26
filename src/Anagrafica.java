import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.concurrent.SynchronousQueue;

public class Anagrafica {
    public static char[] PK_anag;
    public static char[] Descrizione_anag;
    public static char[] Tipo_anag;
    public static String addTo;
    public static File fileName = new File("resources/anagrafica");
    public static Path filePath = Paths.get(fileName.toString());
    public static String alphanumericPattern = "^[a-zA-Z0-9\\-\\s]+$";
    public static boolean codeModify;
    public static boolean descModify;
    public static boolean typeModify;

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

            if("3".equals(input)){
                modify();
                break;
            }

            if("4".equals(input)){
                stampa_Tipo();
                break;
            }

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

            if("2".equals(input)){
                delete();
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
            byte[] fileBytes = Files.readAllBytes(filePath);
            String printBytes = new String(fileBytes, "UTF-8");

            if (PK_anag == null) {
                // CODICE CAUSALE
                Main.log("");
                Main.log("Immettere codice causale (MAX 5):");
                System.out.print("> ");
                input1 = scanner.nextLine();
                if(input1.toString().length() <= 5 && input1.matches(alphanumericPattern) && !printBytes.contains(input1)){
                    PK_anag = input1.toCharArray();
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

            if (Descrizione_anag == null) {
                // DESCRIZIONE CAUSALE
                Main.log("");
                Main.log("Descrizione causale (MAX 30):");
                System.out.print("> ");
                input2 = scanner.nextLine();
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
                    Tipo_anag = input3.toUpperCase().toCharArray();
                } else {
                    add();
                    break;
                }
            }

            if(PK_anag != null || Descrizione_anag != null || Tipo_anag != null){
                String PK_anagString = String.valueOf(PK_anag);
                String Descrizione_anagString = String.valueOf(Descrizione_anag);
                String Tipo_anagString = String.valueOf(Tipo_anag);
                Main.log("Aggiunta causale con:");
                Main.log("Codice: "+PK_anagString);
                Main.log("Descrizione: "+Descrizione_anagString);
                Main.log("Tipo: "+Tipo_anagString.toUpperCase());
                Main.keyContinue();
                addTo = PK_anagString+" | "+Descrizione_anagString+" | "+Tipo_anagString.toUpperCase()+"\n";
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
                    File tempFileName = new File("resources/tempAnagrafica");
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName));
                    int n = 0;

                    String currentLine;

                    while((currentLine = reader.readLine()) != null){
                        if (currentLine.contains(input1+" |")){
                            n++;
                            continue;
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
                    Main.log("La causale non esiste!");
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

    public static void stampa_Tipo() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        byte[] fileBytes = Files.readAllBytes(filePath);
        String printBytes = new String(fileBytes, "UTF-8");
        while (true) {

            Main.log("");
            Main.log("Inserire tipo da filtrare (E,U):");
            System.out.print("> ");
            String input1 = scanner.nextLine();

            if(printBytes.isEmpty()){
                Main.log("Non ci sono dati da mostrare!");
                Main.keyContinue();
                menu();
                break;
            } else if(!printBytes.contains("| "+input1.toUpperCase())){
                Main.log("Non ci sono dati da mostrare con \""+input1.toUpperCase()+"\"!");
                Main.keyContinue();
                menu();
                break;
            } else {
                if(input1.toUpperCase().equals("E") || input1.toUpperCase().equals("U")){
                    BufferedReader reader = new BufferedReader(new FileReader(fileName));
                    String currentLine;
                    Main.log("Filtrando tutte la causali per tipo \"" + input1.toUpperCase() + "\":");
                    while((currentLine = reader.readLine()) != null){
                        if (!currentLine.contains("| "+input1.toUpperCase())) continue;
                        Main.log(currentLine);
                    }
                    Main.keyContinue();
                    reader.close();
                    menu();
                    break;
                } else {
                    Main.log("Devi inserire un tipo valido (\"E\",\"U\")!");
                    Main.keyContinue();
                    stampa_Tipo();
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
            Main.log("Inserire codice della causale:");
            String input1 = scanner.nextLine();

            File tempFileName = new File("resources/tempAnagrafica");
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName));

            String currentLine;

            while((currentLine = reader.readLine()) != null){
                if (currentLine.contains(input1+" |")){
                    String[] lineModify = currentLine.split(" \\| ");
                    Main.log("Cosa vuoi modificare (Codice,Descrizione,Tipo)?");
                    String input2 = scanner.nextLine();
                    Main.log("Sostituirlo con?");
                    String input3 = scanner.nextLine();
                    if(input2.equalsIgnoreCase("codice") || input2.equalsIgnoreCase("c")){
                        if(input3.length() <= 5 && input3.matches(alphanumericPattern) && !printBytes.contains(input3)){
                            lineModify[0] = input3;
                            codeModify = true;
                        }
                    } else if(input2.equalsIgnoreCase("descrizione") || input2.equalsIgnoreCase("d")){
                        if(input3.length() <= 30 && input3.matches(alphanumericPattern)){
                            lineModify[1] = input3;
                            descModify = true;
                        }
                    } else if(input2.equalsIgnoreCase("tipo") || input2.equalsIgnoreCase("t")){
                        if(input3.equalsIgnoreCase("E") && input3.matches(alphanumericPattern) || input3.equalsIgnoreCase("U") && input3.matches(alphanumericPattern)){
                            lineModify[2] = input3;
                            typeModify = true;
                        }
                    }
                    writer.write(lineModify[0] + " | " + lineModify[1] + " | " + lineModify[2].toUpperCase() + System.getProperty("line.separator"));
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
