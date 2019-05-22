package y2.coursework;

//imports
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
public class ScorePopup {
    
	//creates arraylist of strings to usefor combo box input
    private static ArrayList<String> comboStrings = new ArrayList<String>();
    //creates combo box for user input
    private static ComboBox combo = new ComboBox();
    
    //defines method to call to run popup
    public static void display(List<FixtureObj> fixtures) {
    	
    	//defines stage and stage options
    	Stage popup = new Stage();
        popup.setOnCloseRequest(e -> {
            popup.close();
        });
        popup.initModality(Modality.APPLICATION_MODAL);
        
        //creates labels
        Label titleLabel = new Label("Enter Fixture Results");
        Label txtLabel = new Label("Please select fixture to enter scores for, then\n enter scores in relevent textfields, check entered\ndetails are correct, then submit score.");
        Label comboLbl = new Label("Select fixture:");
        Label divide = new Label(" - ");
        
        //gives labels individual ID's to target with CSS
        titleLabel.setId("titleLabel");
        txtLabel.setId("txtLabel");
        
        //creates textfields for user input and its options
        TextField scoreOne = new TextField();
        TextField scoreTwo = new TextField();
        scoreOne.setMaxWidth(70);
        scoreTwo.setMaxWidth(70);
        
        //defines layout to be added for user input and its options
        HBox hbox = new HBox(40);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(scoreOne, divide, scoreTwo);
        
        //defines layout to be added for user input and its options
        VBox layoutBox = new VBox(30);
        layoutBox.setAlignment(Pos.CENTER);
        layoutBox.getChildren().addAll(comboLbl, combo, hbox);
        
        //creates buttons
        Button addBtn = new Button("Add Score");
        Button doneBtn = new Button("Done");
        
        //creates label
        Label checkLbl = new Label("Select to confirm entered score is correct: ");
        //creates checkbox for input validation
        CheckBox checkBox = new CheckBox();
        
        //adds listener to checkbox to auto detect state of checkbox
        checkBox.selectedProperty().addListener(e -> {
            if(checkBox.isSelected()) {
            	//if checkbox is selected, then enable add score button
                addBtn.setDisable(false);
            } else {
            	//if checkbox is not selected, then disable add score button
                addBtn.setDisable(true);
            }
        });
        
        //defines add score button on-click
        addBtn.setOnAction(e -> {
        	//validates user input to check that the following fields are not empty: combobox, scoreOne textfield and scoreTwo tetfield
            if(!combo.getSelectionModel().isEmpty() && !scoreOne.getText().trim().isEmpty() && !scoreTwo.getText().trim().isEmpty()) {
                //if all fields are not empty, parse inputs to string and pass through to addScore method in LeagueManager class
            	LeagueManager.addScore((String) combo.getValue(), scoreOne.getText(), scoreTwo.getText());
                //clear input fields
            	scoreOne.clear();
                scoreTwo.clear();
                combo.getSelectionModel().clearSelection();
                //disable add score button
                addBtn.setDisable(true);
                //de-select validation checkbox
                checkBox.setSelected(false);
                //pass through fixtures arraylist to listRefresh method
                listRefresh(fixtures);
            }
        });
        
        //defines done button on-click
        doneBtn.setOnAction(e -> {
        	//closes the popup
            popup.close();  
        });
        
        //defines layout to be added for checkbox validation placement and its options
        HBox check = new HBox(20);
        check.setAlignment(Pos.CENTER);
        check.getChildren().addAll(checkLbl, checkBox);
        
        //defines layout to be added for button placement and its options
        HBox btnBox = new HBox(20);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(addBtn, doneBtn);
        
        //checks if comboStrings array is empty
        if(comboStrings.isEmpty()) {
        	//if array is empty:
        	//pass through fixtures arraylist to listRefresh method
            listRefresh(fixtures);
            //disable add score button
            addBtn.setDisable(true);
            //de-select validation checkbox 
            checkBox.setSelected(false);
        }
        
        //defines root layout and its options
        VBox box = new VBox(30);
        box.setPrefSize(200, 300);
        box.getChildren().addAll(titleLabel, txtLabel, layoutBox, check, btnBox);
        box.setAlignment(Pos.CENTER);
        
        //defines scene and its options
        Scene scene = new Scene(box, 500, 500);
        popup.setResizable(false);
        popup.setTitle("Add Score");
        popup.setScene(scene);
        //add 'popup' and 'main' css stylesheets to scene
        scene.getStylesheets().add(ScorePopup.class.getResource("main.css").toExternalForm());
        scene.getStylesheets().add(ScorePopup.class.getResource("popup.css").toExternalForm());
        //set icon of scene to image at given url
        popup.getIcons().add(new Image("http://icons.iconarchive.com/icons/icons-land/metro-raster-sport/16/Soccer-Ball-icon.png"));
        popup.showAndWait();
    }
    
    //defines method to refresh displayed list within combobox
    public static void listRefresh(List<FixtureObj> fixtures) {
        //removes all items from comboStrings array
    	comboStrings.clear();
        
    	//for loop to iterate through fixtures array 
        for(FixtureObj item : fixtures) {
        	//check if team scores have been entered (team score equalling -1 means score has not been entered)
            if(item.getTeamOneScore() == -1 && item.getTeamTwoScore() == -1) {
            	//adds fixtures, whose scores have not been entered,  to comboStrings array 
            	comboStrings.add(new String(item.getFixture()));
            } 
        }
        
        //converts comboStrings array into observable list format
        ObservableList<String> observ = FXCollections.observableArrayList(comboStrings);
        //set contents of combobox to contents of observable list
        combo.setItems(observ);
    } 
}