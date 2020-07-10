package steam.id.checker;
import java.io.*;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class SteamIDChecker {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Handles input
        Scanner scanner = new Scanner(System.in); String input; 
        
        // Menu title
        System.out.println( " ____  _ \n" +
                            "/ ___|| |_ ___  __ _ _ __ ___  \n" +
                            "\\___ \\| __/ _ \\/ _` | '_ ` _ \\ \n" +
                            " ___) | | |  __/ (_| | | | | | | \n" +
                            "|____/ \\__\\___|\\__,_|_| |_| |_| \n" +
                            "ID checker // made by Askerain\n");
        
        // If it doesn't exist, it will create a new configuration file.
        if (!new File("steamids").exists()) { 
            try {
                File newFile = new File("steamids");
                if (newFile.createNewFile()) {
                   System.out.println("\nThe \"steamids\" file has been created in the path the program is stored.");
                   System.out.println("Since this program is intended to be used with large lists, you will have to paste your list in the \"steamids\" file with your text editor.");
                }
            } catch (IOException ex) {};
        }
        
        // Menu
        System.out.println("You should now import your ID list into the \"steamids\" file. Type 'check' after you finished setting \"steamids\".");
        do {
            System.out.print("> ");
            input = scanner.nextLine();
            switch(input) {
                case "check":
                    String idfile = null;
                    if (new File("steamids").exists()) {
                        
                        // Checks how many lines the "steamids" file has.
                        BufferedReader readLines = new BufferedReader(new FileReader("steamids"));
                        String line = null;
                        int counter = 0; // Default
                        while ((line = readLines.readLine()) != null) {
                            counter +=1;
                        }
                        // Prints the number of lines for checking purposes.
                        System.out.print("The program will check "+counter+" Steam IDs. Continue? (y/N)\n> ");
                        input = scanner.nextLine();

                        if (input.equalsIgnoreCase("y") || input.equals("")) {
                        System.out.println("The program will save the unused ID names into a text file.");
                        System.out.print("Name of the text file: ");
                        String textfile = scanner.nextLine();
                        idfile = textfile;
                            try {
                                File newFile = new File(idfile);
                                if (newFile.createNewFile()) {
                                   System.out.println("File \""+idfile+"\" successfully created.");
                                }
                            } catch (IOException ex) {};
                        } else{break;}
                        
                        // ID checker
                        try (BufferedReader br = new BufferedReader(new FileReader("steamids"))) {
                            
                            int unusedID = 0;
                            int usedID = 0;
                            String id;
                            
                            while ((id = br.readLine()) != null) {
                                if (id.length() >= 3) {
                                    try { 
                                        FileWriter write = new FileWriter(idfile, true);
                                        System.out.print("Checking /id/"+id+"...");
                                        String url = "https://steamcommunity.com/id/"+id;
                                        Document document = Jsoup.connect(url).get();
                                        Element link = document.select(".error_ctn").first();
                                        if (link != null) {
                                            System.out.println(" This ID isn't taken.");
                                            write.append("\n"+id);
                                            write.close();
                                            unusedID+=1;
                                        }
                                        else {
                                            System.out.println(" This ID is taken.");
                                            usedID+=1;
                                        }
                                    } catch (IOException e) {
                                        System.out.println(" Error.");
                                        if (!new File("errorids").exists()) {
                                            try {
                                                File newFile = new File("errorids");
                                                if (newFile.createNewFile()) {
                                                    System.out.println("!! The file \"errorids\" has been created. It will write the IDs that couldn't be checked.");
                                                    System.out.println("!! You can move the IDs from \"errorids\" to \"steamids\" after the check is done.");
                                                    Thread.sleep(2000);
                                                }
                                            } catch (IOException ex) {};                                            
                                        }
                                        if (new File("errorids").exists()) {
                                            try {
                                                FileWriter write = new FileWriter("errorids", true);
                                                write.append("\n"+id);
                                                write.close();
                                            } catch (IOException ex) {};
                                        }
                                    }
                                }                                
                            }
                            System.out.println("... Check completed. \nUnused IDs: "+unusedID+" | Used IDs: "+usedID);
                        }
                    }
                    else {
                        System.out.println("The \"steamids\" file cannot be found. Please make it manually or reopen the program.");
                        }
                    break;
                case "exit":
                    System.exit(0); 
            }
        } while(true);
    }
}
