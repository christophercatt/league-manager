package y2.coursework;

//imports
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Christopher
 */
public class FixturesPopup {
    
    //defines method to call to run popup
    public static void display(List<FixtureObj> fixtures) {
        
        //defines stage and stage options
        Stage popup = new Stage();
        popup.setOnCloseRequest(e -> {
            popup.close();
        });
        popup.initModality(Modality.APPLICATION_MODAL);
        
        //creates labels
        Label titleLabel = new Label("Season Fixture List");
        Label txtLabel = new Label("Below is a list of all fixtures this season, both\n completed and upcoming.");
        
        //gives labels individual ID's to target with CSS
        titleLabel.setId("titleLabel");
        txtLabel.setId("txtLabel");
        
        //creates vertical box layout and defines options
        VBox stringBox = new VBox(10);
        stringBox.setAlignment(Pos.CENTER);
        
        //for loop to iterate through fixtures arraylist
        for(FixtureObj item : fixtures) {
            //creates and adds new labels to box layout for each object in fixtures array
            stringBox.getChildren().add(new Label(item.getFixtureList()));
        }
        
        //creates scrollpane and defines scrollpane options
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefSize(300, 300);
        scrollPane.setContent(stringBox);
        
        //creates button and defines button on-click
        Button doneBtn = new Button("Done");
        doneBtn.setOnAction(e -> {
           popup.close(); 
        });
        
        //defines root layout and its options
        VBox root = new VBox(30);
        root.setPrefSize(200, 300);
        root.getChildren().addAll(titleLabel, txtLabel, scrollPane, doneBtn);
        root.setAlignment(Pos.CENTER);
        
        //defines scene and its options
        Scene scene = new Scene(root, 500, 500);
        popup.setResizable(false);
        popup.setTitle("View Fixtures");
        popup.setScene(scene);
        //add 'popup' and 'main' css stylesheet to scene
        scene.getStylesheets().add(FixturesPopup.class.getResource("main.css").toExternalForm());
        scene.getStylesheets().add(FixturesPopup.class.getResource("popup.css").toExternalForm());
        //set icon of scene to image at given url
        popup.getIcons().add(new Image("http://icons.iconarchive.com/icons/icons-land/metro-raster-sport/16/Soccer-Ball-icon.png"));
        popup.showAndWait();
    }
    
}
