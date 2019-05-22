package y2.coursework;

//imports
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Christopher
 */
public class TeamsPopup {
    
    //creates arraylist of textfields used later to extract data from user
    private static ArrayList<TextField> tFields = new ArrayList<TextField>();
    
    //defines method to call to run popup
    public static void display() {
        
        //defines stage and stage options
        Stage popup = new Stage();
        popup.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        popup.initModality(Modality.APPLICATION_MODAL);
        
        //creates labels
        Label titleLabel = new Label("Add Custom Team Names");
        Label txtLabel = new Label("Please enter all team names in the boxes below. \nBy default, a minimum of 3 different teams are required. \nIf more teams are required, please click the 'Add New Team' button.");
        
        //gives labels individual ID's to target with CSS
        titleLabel.setId("titleLabel");
        txtLabel.setId("txtLabel");
        
        //creates scrollpane and defines scrollpane options
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefSize(300, 200);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        //creates vertical box layout and defines options
        VBox tFieldsBox = new VBox(10);
        tFieldsBox.setPadding(new Insets(20));

        //predefines multiple use variable to save memory usage
        String txt = "Enter Team Name:      ";
        
        //for loop to quickly add mutiple input layouts to the screen, cutting down lines of code needed
        for(int i = 0; i < 3; i++) {
            
            //defines layout to be added for user input and its options
            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER);
            tFields.add(new TextField());
            box.getChildren().addAll(new Label(txt), tFields.get(i));
            
            //adds box to vertical layout and sets focus for user input
            tFieldsBox.getChildren().add(box);
            box.getChildren().get(1).requestFocus();
            
        }
        
        //creates label to specify how many teams have been entered by calling the size of the arraylist of user input
        Label teamCount = new Label("Amount of Teams: " + tFields.size());

        //sets content of the scrollpane to 
        scrollPane.setContent(tFieldsBox);    
        
        //creates button and defines button on-click
        Button addBtn = new Button("Add New Team");
        addBtn.setOnAction(e -> {
        	//defines layout to be added for user input and its options
            HBox box = new HBox(10);
            box.setAlignment(Pos.CENTER);
            tFields.add(new TextField());
            
            //adds box to vertical layout and sets focus for user input
            box.getChildren().addAll(new Label(txt), tFields.get(tFields.size() - 1));
            tFieldsBox.getChildren().add(box);
            box.getChildren().get(1).requestFocus();
            
            // scrolls to the absolute bottom of scrollPane (accounts for padding + spacing) and sets team count text
            tFieldsBox.heightProperty().addListener(observable -> scrollPane.setVvalue(1D));
            teamCount.setText("Amount of Teams: " + tFields.size());
        });
        
        //creates button and defines button on-click
        Button removeBtn = new Button("Remove Last Entry");
        removeBtn.setOnAction(e -> {
        	//removes input layer from 
            tFieldsBox.getChildren().remove(tFields.size() - 1);
            tFields.remove(tFields.size() - 1);
            teamCount.setText("Amount of Teams: " + tFields.size());
        });
        
        //creates button and defines button options and on-click
        Button doneBtn = new Button("Done");
        doneBtn.setDisable(true);
        doneBtn.setOnAction(e -> {
        	//creates temporary arraylist
            ArrayList<String> temp = new ArrayList<>();
            //for loop to iterate through temp array
            for(TextField tempTF : tFields) {
            	//creates temporary string for input checking
                String tempSTR = tempTF.getText();
                //checks to see if input is empty
                if(!tempSTR.isEmpty()) {
                	//if input is not empty, add to the temp array 
                    temp.add(tempSTR);
                }
            }
            
            //checks if temp array is empty
            if(!temp.isEmpty()) {
            	//if temp array is not empty, pass tFields array through to setTeamNames method in LeagueManager class
                LeagueManager.setTeamNames(tFields);
                //closes the popup
                popup.close();
            } else {
            	//if temp array is empty, set teamCount text as error message
                teamCount.setText("ERROR: NO TEAM NAMES POPULATED");
            }            
        });

        //defines layout to be added for button placement and its options
        HBox btnBox = new HBox(30);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(addBtn, removeBtn, doneBtn);
        
        //creates date label
        Label dateLabel = new Label("Tournament Start Date :");
        
        //creates datepicker and defines datepicker options and on-click
        DatePicker datePicker = new DatePicker();
        datePicker.setShowWeekNumbers(false);
        datePicker.setOnAction(e -> {
        	//pass datepicker value through to getDate method in LeagueManager class
            LeagueManager.getDate(datePicker.getValue());
            //enable done button
            doneBtn.setDisable(false);
        });
        
        //defines layout to be added for datepicker placement and its options
        HBox dateBox = new HBox(30);
        dateBox.setAlignment(Pos.CENTER);
        dateBox.getChildren().addAll(dateLabel, datePicker);
        
        //defines root layout and its options
        VBox box = new VBox(30);
        box.setPrefSize(200, 300);
        box.getChildren().addAll(titleLabel, txtLabel, dateBox, scrollPane, teamCount, btnBox);
        box.setAlignment(Pos.CENTER);
        
        //defines scene and its options
        Scene scene = new Scene(box, 500, 520);
        popup.setResizable(false);
        popup.setTitle("Add Teams");
        popup.setScene(scene);
        //add 'popup' css stylesheet to scene
        scene.getStylesheets().add(TeamsPopup.class.getResource("popup.css").toExternalForm());
        //set icon of scene to image at given url
        popup.getIcons().add(new Image("http://icons.iconarchive.com/icons/icons-land/metro-raster-sport/16/Soccer-Ball-icon.png"));
        popup.showAndWait();
    } 
}