import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Movimenti {
    public static int idMov = -1;
    public static float valoreMov = -1;
    public static String dataMov;
    public static String causaleMov;
    public static String descrizioneMov;
    public static int ivaMov = -1;
    public static File fileName = new File("resources/movimenti"); //Definisce il file Anagrafica
    public static Path filePath = Paths.get(fileName.toString()); //Definisce il percorso del file Anagrafica
    public static String alphanumericPattern = "^[a-zA-Z0-9\\-\\s]+$"; //Stringa Regex: utilizzata per filtrare i caratteri alfanumerici e spazi
    public static String datePattern = "(0[1-9]|1[0-9]|2[0-9]|3[0-1]|[1-9])\\/([1-9]|0[1-9]|1[0-2])\\/[0-9]{4}"; //Stringa Regex: utilizzata per controllare se la data va bene

    public static boolean createFile() throws IOException {
        if(!fileName.isFile()) {
            if (fileName.createNewFile()) { // Creare il File
                Main.log("File \"movimenti\" creato con successo in " + fileName.toString());
                return true;
            }
        }
        return false;
    }

    public static void menu() throws InterruptedException, IOException {
        Scanner scanner = new Scanner(System.in);

        loop: while (true) {

            Main.log("Menù Movimenti:");
            Main.log("1: Aggiunge un nuovo movimento.");
            Main.log("2: Eliminare un movimento.");
            Main.log("3: Modifica un movimento.");
            Main.log("4: Stampa tutti i movimenti relativi ad una causale.");
            Main.log("5: Stampa tutti i movimenti relativi ad una data.");
            Main.log("6: Stampa tutti i movimenti con un valore compreso tra due valori inseriti.");
            Main.log("7: Stampa tutti i movimenti.");
            Main.log("9: Ritorna indietro");
            Main.log("0: Esci dal programma");
            System.out.print("> ");
            String input = scanner.nextLine();

            switch (input){
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
                    printCausale();
                    break loop;
                case "5":
                    printDate();
                    break loop;
                case "6":
                    printTwoValue();
                    break loop;
                case "7":
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

    public static void add() throws IOException, InterruptedException {
        String input1,input2,input3,input4,input5,input6;
        String fileContent = Files.readAllLines(filePath).toString(); // Contenuto File Movimenti
        String fileAnagrafica = Files.readAllLines(Paths.get("resources/anagrafica")).toString();
        String fileParametri = Files.readAllLines(Paths.get("resources/parametri")).toString();

        Scanner scanner = new Scanner(System.in);
            while(true){

                // Impostare l'ID del movimento
                if(fileContent == "" || fileContent == null || fileContent.isEmpty() || fileContent == "[]"){
                    idMov = 1;
                } else {
                    // Prendere la ultima linea del File
                    BufferedReader fileRead = new BufferedReader(new FileReader(fileName));
                    String lastLine = new String(),readLine;
                    while ((readLine = fileRead.readLine()) != null) {
                        lastLine = readLine;
                    }
                    String[] getLastMovId = lastLine.split(" \\| ");
                    idMov = Integer.parseInt(getLastMovId[0]) + 1;
                }

                // Impostare la data del movimento
                if(dataMov == null){
                    Main.log("Impostare la data di oggi o manuale? ([o]ggi/[m]anuale):");
                    input1 = scanner.nextLine();
                    if(input1.equals("oggi") || input1.equals("o")){
                        Date date = Calendar.getInstance().getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        dataMov = sdf.format(date);
                    } else if(input1.equals("manuale") || input1.equals("m")){
                        Main.log("Inserire la data (dd/MM/yyyy):");
                        input2 = scanner.nextLine();
                        if(input2.matches(datePattern) && input2.length() <= 10){
                            dataMov = input2;
                        } else {
                            Main.log("Il formato della data non è corretto!");
                            Main.keyContinue();
                            add();
                            break;
                        }
                    } else {
                        Main.log("Inserire \"oggi\" oppure \"manuale\"!");
                        Main.keyContinue();
                        add();
                        break;
                    }

                if(causaleMov == null){
                    Main.log("Inserire codice causale:");
                    input3 = scanner.nextLine();
                    if(input3.length() <= 5 && input3.matches(alphanumericPattern) && fileAnagrafica.contains(input3)){
                        causaleMov = input3;
                    } else {
                        Main.log("Codice causale Invalido");
                        Main.keyContinue();
                        add();
                        break;
                    }
                }

                if(valoreMov == -1){
                    Main.log("Inserire importo in euro:");
                    input4 = scanner.nextLine();
                    if(isInteger(input4)){
                        valoreMov = Float.parseFloat(input4);
                    } else {
                        Main.log("Non è un numero valido!");
                        Main.keyContinue();
                        add();
                        break;
                    }
                }

                if(descrizioneMov == null){
                    Main.log("Inserire una descrizione:");
                    input5 = scanner.nextLine();
                    if(input5.length() <= 30 && input5.matches(alphanumericPattern)){
                        descrizioneMov = input5;
                    } else {
                        Main.log("La descrizione non è valida.");
                        Main.keyContinue();
                        add();
                        break;
                    }
                }

                if(ivaMov == -1){
                    Main.log("Inserire codice IVA:");
                    input6 =  scanner.nextLine();
                    if(isInteger(input6) && fileParametri.contains(input6+" |")){
                        ivaMov = Integer.parseInt(input6);
                    } else {
                        Main.log("Il codice IVA non è valido.");
                        Main.keyContinue();
                        add();
                        break;
                    }
                }

                if(idMov != -1 && dataMov != null && causaleMov != null && valoreMov != -1 && descrizioneMov != null && ivaMov != -1){
                    Main.log("Aggiunta causale con:");
                    Main.log("ID Movimento: "+idMov);
                    Main.log("Data: "+dataMov);
                    Main.log("Causale: "+causaleMov);
                    Main.log("Valore: "+valoreMov);
                    Main.log("Descrizione: "+descrizioneMov);
                    Main.log("Codice IVA: "+ivaMov);
                    Main.keyContinue();
                    String addTo = idMov+" | "+dataMov+" | "+causaleMov+" | "+valoreMov+" | "+descrizioneMov+" | "+ivaMov;
                    FileWriter fw = new FileWriter(fileName,true);
                    fw.write(addTo+"\n");
                    fw.close();
                    resetVar();
                    menu();
                    break;
                }

            }
        scanner.close();
    }
    }

    public static boolean isInteger(String stringa) {
        try {
            Integer.parseInt(stringa);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    public static void resetVar(){
        idMov = -1;
        dataMov = null;
        causaleMov = null;
        valoreMov = -1;
        descrizioneMov = null;
        ivaMov = -1;
    }

    public static void delete() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String fileContent = Files.readAllLines(filePath).toString();
            if(fileContent != "" || fileContent != null || !fileContent.isEmpty() || fileContent != "[]"){
                Main.log("");
                Main.log("Premere invio senza scrivere nulla, cancella tutto!");
                Main.log("Inserire l'ID del movimento da eliminare:");
                System.out.print("> ");
                String input1 = scanner.nextLine();

                if(fileContent.contains(input1+" |")){
                    File tempFileName = new File("resources/tempMovimenti");
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
                    Main.log("L'ID inserito non esiste");
                    Main.keyContinue();
                    delete();
                    break;
                }
            }
        }
        scanner.close();
    }

    public static void modify() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while(true){

            Main.log("Inserire ID del movimento da modificare:");
            String input1 = scanner.nextLine();

            File tempFileName = new File("resources/tempMovimenti");
            String fileAnagrafica = Files.readAllLines(Paths.get("resources/anagrafica")).toString();
            String fileParametri = Files.readAllLines(Paths.get("resources/parametri")).toString();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName));

            String currentLine;

            while((currentLine = reader.readLine()) != null){
                if (currentLine.contains(input1+" |")){
                    String[] lineModify = currentLine.split(" \\| "); //Regex: Divide la stringa in array
                    Main.log("Cosa vuoi modificare (Data,Causale,Valore,Descrizione,CodiceIVA)?");
                    String input2 = scanner.nextLine();
                    Main.log("Sostituirlo con?");
                    String input3 = scanner.nextLine();
                    if(input2.equalsIgnoreCase("data")){
                        if(input3.matches(datePattern) && input3.length() <= 10){
                            lineModify[1] = input3;
                        } else {
                            Main.log("Qualcosa non va :/");
                        }
                    } else if(input2.equalsIgnoreCase("causale")){
                        if(input3.length() <= 5 && input3.matches(alphanumericPattern) && fileAnagrafica.contains(input3)){
                            lineModify[2] = input3;
                        } else {
                            Main.log("Qualcosa non va :/");
                        }
                    } else if(input2.equalsIgnoreCase("valore")){
                        if(isInteger(input3)){
                            lineModify[3] = input3;
                        } else {
                            Main.log("Qualcosa non va :/");
                        }
                    } else if(input2.equalsIgnoreCase("descrizione")){
                        if(input3.length() <= 30 && input3.matches(alphanumericPattern)){
                            lineModify[4] = input3;
                        } else {
                            Main.log("Qualcosa non va :/");
                        }
                    } else if(input2.equalsIgnoreCase("codiceiva")){
                        if(isInteger(input3) && fileParametri.contains(input3+" |")){
                            lineModify[5] = input3;
                        } else {
                            Main.log("Qualcosa non va :/");
                        }
                    }
                    writer.write(lineModify[0] + " | " + lineModify[1] + " | " + lineModify[2] + " | " + lineModify[3] + " | " + lineModify[4] + " | " + lineModify[5] + System.getProperty("line.separator"));
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
    }

    public static void print() throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String currentLine;
        while((currentLine = reader.readLine()) != null){
            Main.log(currentLine);
        }
        reader.close();
        Main.keyContinue();
        menu();
    }

    public static void printCausale() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            String fileContent = Files.readAllLines(filePath).toString();
            Main.log("Inserire codice causale, da filtrare:");
            System.out.print(">");
            String input = scanner.nextLine();

            if(fileContent.contains(input) && input.length() <= 5){
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                String currentLine;
                while((currentLine = reader.readLine()) != null){
                    if(!currentLine.contains("| "+input+" |")) continue;
                    Main.log(currentLine);
                }
                reader.close();
                Main.keyContinue();
                menu();
                break;
            } else {
                Main.log("La causale inserita non esiste.");
                Main.keyContinue();
                menu();
                break;
            }
        }
        scanner.close();
    }

    public static void printDate() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            String fileContent = Files.readAllLines(filePath).toString();
            Main.log("Inserire una Data, da filtrare:");
            System.out.print(">");
            String input = scanner.nextLine();

            if(fileContent.contains(input) && input.matches(datePattern)){
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                String currentLine;
                while((currentLine = reader.readLine()) != null){
                    if(!currentLine.contains("| "+input+" |")) continue;
                    Main.log(currentLine);
                }
                reader.close();
                Main.keyContinue();
                menu();
                break;
            } else {
                Main.log("La causale inserita non esiste.");
                Main.keyContinue();
                menu();
                break;
            }
        }
        scanner.close();
    }

    public static void printTwoValue() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        while (true){
            String fileContent = Files.readAllLines(filePath).toString();
            Main.log("Inserire due valori, separati da virgola, per filtrare i movimenti:");
            System.out.print(">");
            String input = scanner.nextLine();
            String[] dInput = input.split(",");
            float[] fInput = new float[2];
            fInput[0] = Float.parseFloat(dInput[0]);
            fInput[1] = Float.parseFloat(dInput[1]);
            int n = 0;

                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                String currentLine;
                while((currentLine = reader.readLine()) != null){
                    String[] lineModify = currentLine.split(" \\| ");
                    if(fInput[0] > Float.parseFloat(lineModify[3]) || fInput[1] < Float.parseFloat(lineModify[3])) continue;
                    n++;
                    Main.log(currentLine);
                }
                reader.close();
            if(n == 0) {
                Main.log("Non esiste nessun movimento con questo range di valori.");
                Main.keyContinue();
                menu();
                break;
            } else {
                Main.keyContinue();
                menu();
                break;
            }
        }
        scanner.close();
    }

    // Fine Class
}
