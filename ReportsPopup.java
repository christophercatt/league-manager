package y2.coursework;

//imports
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Christopher
 */
public class ReportsPopup {
    
    //creates arraylist of TableObj for team selection
    private static ArrayList<TableObj> objects = new ArrayList<TableObj>();
    
    //defines method to call to run popup
    public static void display(ObservableList<TableObj> fixtures) {
        
        //defines stage and stage options
        Stage popup = new Stage();
        popup.setOnCloseRequest(e -> {
            popup.close();
        });
        popup.initModality(Modality.APPLICATION_MODAL);
        
        //creates labels
        Label titleLabel = new Label("Report Generator");
        Label txtLabel = new Label("Below is a list of all teams this season.\nPlease select the team(s) you wish to generate report(s) for.\n(Hold down CTRL and click to select multiple teams)");
        
        //gives labels individual ID's to target with CSS
        titleLabel.setId("titleLabel");
        txtLabel.setId("txtLabel");
        
        //creates vertical box layout and defines options
        VBox stringBox = new VBox(10);
        stringBox.setAlignment(Pos.CENTER);
        
        //creates list for user view of teams and defines options
        ListView list = new ListView(fixtures);
        list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list.setMaxHeight(300);
        
        //allows for detection of multiple list selection and sends output to castSelected method
        //https://stackoverflow.com/questions/24158394/javafx-listview-multiple-selection
        list.setOnMouseClicked(e -> {
            ObservableList<TableObj> selectedItems = list.getSelectionModel().getSelectedItems();
            castSelected(selectedItems);
        });
        
        //creates new date object
        Date currentDate = new Date();
        //creates temp date instant value set from date variable value
        Date d = Date.from(currentDate.toInstant());
        //creates dateformat to specify output display format of date value
        DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
        //binds temp date into specified dateformat, then assigns calue to temp string date variable
        String strDate = dF.format(d);
        
        //spacer text initialised for later use
        String s = "    ";
        
        //creates button and defines button on-click
        Button generateSeasonReport = new Button("Generate Season Report");
        generateSeasonReport.setOnAction(e -> {
            
            //creates temp array populated via 'tablePopulator()' method in LeagueManager class
            ArrayList<TableObj> tmp = new ArrayList<TableObj>(LeagueManager.tablePopulator());
            
            //declares try/catch error block
            try {
                //declares new file writer function to create file "Season Report.txt"
                FileWriter file = new FileWriter("Season Report.txt", true);
                //declares new print writer function to write data to file created in file writer
                PrintWriter fileWriter = new PrintWriter(file);
                
                //prints heading in file with the date of file creation
                fileWriter.println("#################################################");
                fileWriter.println("##   Season Report : Generated at " + strDate + "   ##");
                fileWriter.println("#################################################");
                fileWriter.println();
                
                
                //for loop to iterate through tmp array and prints all info for every team to the file
                for(TableObj item : tmp) {
                    fileWriter.println(item.getTeamName());
                    fileWriter.println(s + "Games Played : " + item.getPlayed());
                    fileWriter.println(s + "Games Won : " + item.getWon());
                    fileWriter.println(s + "Games Drawn : " + item.getDrawn());
                    fileWriter.println(s + "Games Lost : " + item.getLost());
                    fileWriter.println(s + "Goals Scored : " + item.getScored());
                    fileWriter.println(s + "Goals Conceded : " + item.getAgainst());
                    fileWriter.println(s + "Goals Difference : " + item.getDiff());
                    fileWriter.println(s + "Points Scored : " + item.getPoints());
                    fileWriter.println();
                    fileWriter.println("#################################################");
                    fileWriter.println();
                }
                
                //closes file streams
                fileWriter.close();
                file.close();
                
            } catch(Exception ex) {
                //displays error information in console
                System.out.println(ex);
            }
        });
        
        //creates button and defines button on-click
        Button generateTeamReport = new Button("Generate Team Report(s)");
        generateTeamReport.setOnAction(e -> {
            
            //checks if objects array is not empty
            if(objects.size() != 0) {
                //declares string for report file name
                String fileName;
                //checks if objects contains only 1 entry, or more than one selected team
                if(objects.size() <=1) {
                    //specifies if only one team selected, file name will be the team name
                    fileName = objects.get(0).getTeamName();
                } else {
                    //specifies if more than one team selected, file name will be "Teams Report"
                    fileName = "Teams Report";
                }
                
                //declares try/catch error block
                try {
                    //declares new file writer function to create file with file name previously generated
                    FileWriter file = new FileWriter(fileName + ".txt", false);
                    //declares new print writer function to write data to file created in file writer
                    PrintWriter fileWriter = new PrintWriter(file);
                    
                    //prints heading in file with the date of file creation
                    fileWriter.println("#################################################");
                    fileWriter.println("##   Teams Report : Generated at " + strDate + "    ##");
                    fileWriter.println("#################################################");
                    fileWriter.println();
                    
                    //for loop to iterate through objects array and prints all info for each team selected to file
                    for(TableObj item : objects) {
                        fileWriter.println(item.getTeamName());
                        fileWriter.println(s + "Games Played : " + item.getPlayed());
                        fileWriter.println(s + "Games Won : " + item.getWon());
                        fileWriter.println(s + "Games Drawn : " + item.getDrawn());
                        fileWriter.println(s + "Games Lost : " + item.getLost());
                        fileWriter.println(s + "Goals Scored : " + item.getScored());
                        fileWriter.println(s + "Goals Conceded : " + item.getAgainst());
                        fileWriter.println(s + "Goals Difference : " + item.getDiff());
                        fileWriter.println(s + "Points Scored : " + item.getPoints());
                        fileWriter.println();
                        fileWriter.println("#################################################");

                    } 

                    //closes file streams
                    fileWriter.close();
                    file.close();
                }catch(Exception ex) {
                    //displays error information in console
                    System.out.println(ex);
                }     
            }
        });
        
        //creates button and defines button on-click
        Button doneBtn = new Button("Done");
        doneBtn.setOnAction(e -> {
           popup.close(); 
        });
        
        //defines layout to be added for button placement and its options
        HBox btnBox = new HBox(20);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(generateTeamReport, generateSeasonReport, doneBtn);
        
        //defines root layout and its options
        VBox root = new VBox(30);
        root.setPrefSize(200, 300);
        root.getChildren().addAll(titleLabel, txtLabel, list, btnBox);
        root.setAlignment(Pos.CENTER);
        
        //defines scene and its options
        Scene scene = new Scene(root, 500, 500);
        popup.setResizable(false);
        popup.setTitle("Generate Reports");
        popup.setScene(scene);
        //add 'popup' and 'main' css stylesheets to scene
        scene.getStylesheets().add(ReportsPopup.class.getResource("main.css").toExternalForm());
        scene.getStylesheets().add(ReportsPopup.class.getResource("popup.css").toExternalForm());
        //set icon of scene to image at given url
        popup.getIcons().add(new Image("http://icons.iconarchive.com/icons/icons-land/metro-raster-sport/16/Soccer-Ball-icon.png"));
        popup.showAndWait();
    }
    
    //defines castSelected method to cast selected list items (in observable list type) through to objects array
    public static void castSelected(ObservableList<TableObj> item) {
        ArrayList<TableObj> tmp = new ArrayList<TableObj>(item);
        objects = tmp;
    }
    
}
