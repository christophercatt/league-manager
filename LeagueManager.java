/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package y2.coursework;

//imports
import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



/**
 *
 * @author Christopher
 */
public class LeagueManager extends Application {
    
    //creates arraylist of strings to store team names
    private static ArrayList<String> teamNames = new ArrayList<String>();
    
    //creates arraylist of FixtureObj to store fixture objects
    private static ArrayList<FixtureObj> fixtures = new ArrayList<FixtureObj>();
    
    //declares date variable for later initilisation
    private static LocalDate date;
    
    //creates table, only accepting objects of type TableObj to be displayed
    private static TableView<TableObj> table = new TableView<TableObj>();
    
    //creates titled panes to display upcoming and recent fixtures
    private static TitledPane recentPane = new TitledPane();
    private static TitledPane fixturePane = new TitledPane();
    
    //creates table columns which require modification from multiple methods
    private static TableColumn pointsCol = new TableColumn("Pts");
    private static TableColumn diffCol = new TableColumn("+/-");
    
    //defines main application start method to run main method code
    @Override
    public void start(Stage primaryStage) {
    
        //runs loadFromFile method to load data from saved files into arraylists
        loadFromFile();
        
        // If fixtures array is empty but there ARE team names, then generate fixtures 
        if(fixtures.isEmpty() && !teamNames.isEmpty()) {
            /* CODE TO GENERATE FIXTURES USING ROUND ROBIN METHOD
            ** Referenced from Nakul Padalkar: https://www.quora.com/What-is-the-algorithm-of-fixture-for-football-league-matches
            ** if(!(i == j)) to eliminate team playing themself (e.g. 1vs1)
            ** !fixtures.contains(j + "vs" + i) to eliminate teams playing each other twice (e.g. 1vs2 and 2vs1 are the same)
            ** Math.random() with if else statement to randomise if fixture is home or away
            ** !fixtures.contains(i + "vs" + j) to eliminate duplicate matches appearing after random generator
            ** vsTxt is placeholder text for display readability
            ** Collections.shuffle() randomises order of the fixtures so that a teams matches are spread out
            ** To work out amount of matches overall: (teamNames.size()*(teamNames.size()-1)) / 2
            ** To work out amount of games a team will play: teamNames.size()-1 */            
            
            //creates double value to be used later to store random number for radnomisation
            double random;
            //for loop to iterate through team names array to select team one
            for(int i = 0; i < teamNames.size(); i++){
                //for loop to iterate through team names array to select team two
                for(int j = 0; j < teamNames.size(); j++) {
                    //if(!(i == j)) to eliminate team playing themself (e.g. 1vs1)
                    if(i != j) {
                        //declares counter variable to break do loop
                        int counter = 0;
                        //declares boolean variable to break do loop
                        boolean check = true;
                        //declares do/while loop
                        do {
                            //for loop to check if fixture already exists within fixtures array
                            for(FixtureObj fObj : fixtures) {
                                //if fixture exists as home match, break loop as fixture already exists
                                if(teamNames.get(i).equals(fObj.getTeamOne()) && teamNames.get(j).equals(fObj.getTeamTwo())) {
                                    check = false;
                                //if fixture exists as away match, break loop as fixture already exists
                                }else if(teamNames.get(j).equals(fObj.getTeamOne()) && teamNames.get(i).equals(fObj.getTeamTwo())) {
                                    check = false;
                                }
                            }
                            //increment counter
                            counter++;  
                        //do while counter does not exceep teamNames array pointer and check = true
                        }while(check == true && counter < teamNames.size());

                        //if do/while loop completes and check = true, then current fixture does not already exist in fixtures array
                        if(check == true) {
                            //generate random number between ~0 and ~0.99
                            random = Math.random();
                            //if random number is above 0.5, then create fixture as home match
                            if(random > 0.5) {
                                fixtures.add(new FixtureObj(teamNames.get(i), teamNames.get(j)));
                            //if random number is 0.5 and below, then create fixture as away match
                            } else {
                                fixtures.add(new FixtureObj(teamNames.get(j), teamNames.get(i)));
                            }
                        }
                    }
                }
            }
            
            //once all fixtures created, randomise fixture order
            Collections.shuffle(fixtures);

            //creates size of team names array as variable for easier use in following calculation
            int n = teamNames.size();
            //works out total number of matches in season and stores as variable
            int m = (n * (n - 1)) / 2;
            //declares count variable
            int counter = 1;
            //for loop to iterate through all matches in season
            for(int i = 0; i < m; i++) {
                //declares switch statement to replace multiple if/else statements
                switch (counter) {
                    //if counter = 1
                    case 1:
                        //increments date to date of the next monday after current date value
                        date = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
                        //sets fixture date to new date value of future monday
                        fixtures.get(i).setDate(date);
                        //increments counter
                        counter++;
                        //breaks current loop through switch
                        break;
                    case 2:
                        //increments date to date of the next wednesday after current date value
                        date = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.WEDNESDAY));
                        //sets fixture date to new date value of future wednesday
                        fixtures.get(i).setDate(date);
                        //increments counter
                        counter++;
                        //breaks current loop through switch
                        break;
                    case 3:
                        //increments date to date of the next friday after current date value
                        date = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY));
                        //sets fixture date to new date value of future friday
                        fixtures.get(i).setDate(date);
                        //resets counter
                        counter = 1;
                        //breaks current loop through switch
                        break;
                    default:
                        //if error, breaks current loop through switch
                        break;
                }
            }
            //runs saveToFile method to save changes to arraylists to file
            saveToFile();
            /* CODE TO GENERATE FIXTURES USING ROUND ROBIN METHOD END */
        }
        
        //set size of table
        table.setMaxSize(445, 400);
        
        //creates table columns and specifies their options
        TableColumn nameCol = new TableColumn("Team");
        nameCol.setCellValueFactory(new PropertyValueFactory<TableObj, String>("teamName"));
        nameCol.setMinWidth(100);
        
        TableColumn playedCol = new TableColumn("P");
        playedCol.setCellValueFactory(new PropertyValueFactory<TableObj, Integer>("played"));
        playedCol.setMinWidth(40);
        
        TableColumn wonCol = new TableColumn("W");
        wonCol.setCellValueFactory(new PropertyValueFactory<TableObj, Integer>("won"));
        wonCol.setMinWidth(40);
        
        TableColumn drawnCol = new TableColumn("D");
        drawnCol.setCellValueFactory(new PropertyValueFactory<TableObj, Integer>("drawn"));
        drawnCol.setMinWidth(40);
        
        TableColumn lostCol = new TableColumn("L");
        lostCol.setCellValueFactory(new PropertyValueFactory<TableObj, Integer>("lost"));
        lostCol.setMinWidth(40);
        
        TableColumn scoredCol = new TableColumn("GS");
        scoredCol.setCellValueFactory(new PropertyValueFactory<TableObj, Integer>("scored"));
        scoredCol.setMinWidth(40);
        
        TableColumn againstCol = new TableColumn("GA");
        againstCol.setCellValueFactory(new PropertyValueFactory<TableObj, Integer>("against"));
        againstCol.setMinWidth(40);
        
        
        diffCol.setCellValueFactory(new PropertyValueFactory<TableObj, Integer>("diff"));
        diffCol.setMinWidth(40);
        
        
        pointsCol.setCellValueFactory(new PropertyValueFactory<TableObj, Integer>("points"));
        pointsCol.setMinWidth(50);
        //END! of creating table columns and specifying their options

        //populate table with data generated within tablePopulator method
        table.getItems().addAll(tablePopulator());
        //adds all columns to the table
        table.getColumns().addAll(nameCol, playedCol, wonCol, drawnCol, lostCol, scoredCol, againstCol, diffCol, pointsCol);
        //runs sortTable method to sort table on specified columns within method 
        sortTable(true);
        
        //runs saveToFile method to save current state of arrays to file
        saveToFile();
        
        
        //code to disable reordering of columns when column is dragged - http://anxiousweb.blogspot.com/2012/07/how-to-disable-column-reordering-in.html
        table.getColumns().addListener(new ListChangeListener() {
            public boolean check;
 
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !check) {
                    this.check = true;
                    table.getColumns().setAll(nameCol, playedCol, wonCol, drawnCol, lostCol, scoredCol, againstCol, diffCol, pointsCol);
                    this.check = false;
                }
            }
        });
        
        
        //defines options for upcoming pane and populates with data generated in listToLabels method
        fixturePane.setText("Upcoming Fixtures");
        fixturePane.setContent(listToLabels(fixtures, 0));
        fixturePane.setMaxWidth(300);
        fixturePane.setCollapsible(false);
        
        //defines options for recent pane and populates with data generated in listToLabels method
        recentPane.setText("Recent Fixtures");
        recentPane.setContent(listToLabels(fixtures, 1));
        recentPane.setMaxWidth(300);
        recentPane.setCollapsible(false);
        
        //creates button and defines button on-click
        Button addScoreBtn = new Button("Enter Score");
        addScoreBtn.setOnAction(e -> {
            //opens score popup in ScorePopup class to allow users to enter scores for fixtures
            ScorePopup.display(fixtures);
        });
        
        //creates button and defines button on-click
        Button viewBtn = new Button("View All Fixtures");
        viewBtn.setOnAction(e -> {
            //opens fixtures popup in FixturesPopup class to allow users to view all fixtures within the season
            FixturesPopup.display(fixtures);
        });
        
        //creates button and defines button on-click
        Button reportBtn = new Button("Generate Team Report");
        reportBtn.setOnAction(e -> {
            //opens reports popup window in ReportsPopup class to allow user to generate team reports
            ReportsPopup.display(tablePopulator());
        });
        
        //defines layout to be added for button placement and its options
        FlowPane btnFlowPane = new FlowPane();
        btnFlowPane.setHgap(20);
        btnFlowPane.setVgap(20);
        btnFlowPane.getChildren().addAll(addScoreBtn, viewBtn, reportBtn);
        btnFlowPane.setAlignment(Pos.CENTER);
        
        //defines layout to be added for button placement and its options
        HBox btnBox = new HBox(20);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.setMaxWidth(300);
        btnBox.getChildren().add(btnFlowPane);
        
        //defines layout to be added for fixture views and its options
        VBox box = new VBox(40);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(fixturePane, recentPane, btnBox);
        
        //defines menu item
        MenuItem resetMenu = new MenuItem("End Season");
        //defines menu
        Menu menu = new Menu("File");
        //adds menu item to menu
        menu.getItems().add(resetMenu);
        //defines menu bar
        MenuBar menuBar = new MenuBar();
        //adds menu to menu bar
        menuBar.getMenus().add(menu);
        
        //if menu item is clicked
        resetMenu.setOnAction(e -> {
            //run confirmReset method to confirm if user wishes to reset application to defaults
            confirmReset(primaryStage);
        });
        
        //defines main layout to be added for table and fixture view placement
        HBox main = new HBox(80);
        main.getChildren().addAll(box, table);
        main.setAlignment(Pos.CENTER);
        
        //defines root layout and its options
        VBox root = new VBox(40);
        root.setAlignment(Pos.TOP_CENTER);
        root.getChildren().addAll(menuBar, main);
        
        //defines scene and its options
        Scene scene = new Scene(root, 1040, 540);        
        primaryStage.setTitle("League Manager");
        primaryStage.setScene(scene);
        //add 'main' css stylesheets to scene
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        primaryStage.setResizable(false);
        //set icon of scene to image at given url
        primaryStage.getIcons().add(new Image("http://icons.iconarchive.com/icons/icons-land/metro-raster-sport/16/Soccer-Ball-icon.png"));
        primaryStage.show();   
    }
    
    //defines setTeamNames method to set team names from name values entered in seperate class
    public static void setTeamNames(List<TextField> tFields) { 
        //for loop to iterate through tFields array
        for(TextField temp : tFields) {
                //converts textfield to functional String to make use of isEmpty method
                String tempTf = temp.getText();
                //checks if team name is not empty
                if(!tempTf.isEmpty()) {
                    //add team name to teamNames array 
                    teamNames.add(temp.getText());
                }
        }
    }
    
    //defines getDate method to get date created in seperate class
    public static void getDate(LocalDate startDate) {
        //local variable date is set to date value passed from seperate class
        date = startDate;
    }
    
    //defines listToLabels method to create upcoming and recent fixture views
    public static VBox listToLabels(List<FixtureObj> list, int var) {
        //creates vertical box layout and defines options
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        
        //spacer text initialised for later use
        String sp = " : ";
        //count variable initialised
        int j = 0;
        
        //if var value = 0, create list of upcoming fixtures
        if(var == 0) {
            //for loop to iterate through fixtures array
            for(int i = 0; i < list.size(); i++) {
                //if j variable is greater than 5, break loop so that a max of 6 upcoming fixtures can be displayed at a time
                if(j > 5) {
                    break;
                //if j is less then or equal to 5, check if fixture has had a score entered for it or not
                } else if(list.get(i).getTeamOneScore() == -1 && list.get(i).getTeamTwoScore() == -1) {
                    //if no score has been entered for current fixture, add fixture as label to vbox 
                    content.getChildren().add(new Label(list.get(i).getFixture() + sp + list.get(i).getDate()));
                    //increment j
                    j++;
                }
            }
        //if var value = 1, create list of recent fixtures
        } else if(var == 1) {
            //for loop to iterate through fixtures array
            for(int i = list.size() - 1; i >= 0; i--) {
                //if j variable is greater than 5, break loop so that a max of 6 recent fixtures can be displayed at a time
                if(j > 5) {
                    break;
                //if j is less then or equal to 5, check if fixture has had a score entered for it or not
                } else if(list.get(i).getTeamOneScore() >= 0 && list.get(i).getTeamTwoScore() >= 0) {
                    //if a score has been entered for current fixture, add fixture as label to vbox
                    content.getChildren().add(new Label(list.get(i).getCompletedFixture())); 
                    //increment j
                    j++;
                }
            }
        }
        //return vbox
        return content;
    }
    
    //defines saveToFile method to save arraylists to file
    public static void saveToFile() {
        //declares try/catch error block
        try {
            //declares new file output stream function to save to file "fixtures.txt"
            FileOutputStream ffos = new FileOutputStream("fixtures.txt");
            //declares new object output stream function to save objects to "fixtures.txt"
            ObjectOutputStream foos = new ObjectOutputStream(ffos);
            //specifies to save any objects within fixtures array to 'fixtures.txt' file
            foos.writeObject(fixtures);
            //closes file streams
            foos.close();
            ffos.close();
            //declares new file output stream function to save to file "teamnames.txt"
            FileOutputStream tfos = new FileOutputStream("teamnames.txt");
            //declares new object output stream function to save objects to "teamnames.txt"
            ObjectOutputStream toos = new ObjectOutputStream(tfos);
            //specifies to save any objects within teamNames array to 'teamnames.txt' file
            toos.writeObject(teamNames);
            //closes file streams
            toos.close();
            tfos.close();
        }catch(Exception e) {
            //displays error information in console
            e.printStackTrace();
        }
    }
    
    //defines loadFromFile method to load arraylists from saved file
    public void loadFromFile() {
        //declares try/catch error block
        try {
            //declares new file input stream function to read file "fixtures.txt"
            FileInputStream ffis = new FileInputStream("fixtures.txt");
            //declares new object input stream function to read objects within "fixtures.txt"
            ObjectInputStream fois = new ObjectInputStream(ffis);
            //specifies that any object that is of type FixtureObj is inserted into fixtures arraylist
            fixtures = (ArrayList<FixtureObj>) fois.readObject();
            //closes file streams
            fois.close();
            ffis.close(); 
            //declares new file input stream function to read file "teamNames.txt"
            FileInputStream tfis = new FileInputStream("teamNames.txt");
            //declares new object input stream function to read objects within "teamNames.txt"
            ObjectInputStream tois = new ObjectInputStream(tfis);
            //specifies that any non-FixtureObj object type is inserted into teamNames arraylist
            teamNames = (ArrayList) tois.readObject();
            //closes file streams
            tois.close();
            tfis.close();  
        }catch(Exception e) {
            //if error occured then file does not exist, run teamNameChooser method to generate team names
            teamNameChooser();
        }
    }

    //defines teamNameChooser method to gather team names for application setup
    public void teamNameChooser() {
        //creates confirmation alert and specifies options
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Team Chooser");
        alert.setHeaderText("Do you wish to use custom or default team names?");
        
        //creates buttons
        ButtonType customNames = new ButtonType("Custom Names");
        ButtonType defaultNames = new ButtonType("Default Names");
        ButtonType cancelNames = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        
        //add buttons to alert window
        alert.getButtonTypes().setAll(customNames, defaultNames, cancelNames);
        
        //display alert and wait for user input
        Optional<ButtonType> result = alert.showAndWait();
        //if custom names button is clicked within alert
        if(result.get() == customNames) {
            //run display method within TeamsPopup class to allow user to enter custom team names
            TeamsPopup.display();
        //if default names button is clicked within alert
        } else if(result.get() == defaultNames) {
            //create date object as current date
            date = LocalDate.now();
            //add default sample team names to teamNames array
            teamNames.add("Chelsea");
            teamNames.add("Man Utd");
            teamNames.add("Man City");
            teamNames.add("Arsenal");
            teamNames.add("Tottenham Hotspurs");
            teamNames.add("Liverpool FC");
        //if cancel button is clicked within alert
        } else if(result.get() == cancelNames) {
            //exit out of the current java runtime
            Platform.exit();
            System.exit(0);
        }
    }
    
    //defines confirmReset method to reset application to defaults
    public void confirmReset(Stage stage) {
        //creates info alert and specifies options
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("End of Season Alert");
        alert.setHeaderText("Do you wish to end the current season? Doing so will generate \nan 'End of Season' report and will reset the application to defaults:");
        
        //creates buttons
        ButtonType resetBtn = new ButtonType("End Season");
        ButtonType cancelBtn = new ButtonType("Cancel");
        
        //add buttons to alert window
        alert.getButtonTypes().setAll(resetBtn, cancelBtn);
        
        //display alert and wait for user input
        Optional<ButtonType> result = alert.showAndWait();
        //if reset button is clicken within alert
        if(result.get() == resetBtn) {
            //declares try/catch error block
            try {
                    
                //creates temp array populated via 'tablePopulator()' method
                ArrayList<TableObj> tmp = new ArrayList<TableObj>(tablePopulator());
                
                //spacer text initialised for later use
                String s = "    ";
                //creates new date object
                Date currentDate = new Date();
                //creates temp date instant value set from date variable value
                Date d = Date.from(currentDate.toInstant());
                //creates dateformat to specify output display format of date value
                DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
                //binds temp date into specified dateformat, then assigns calue to temp string date variable
                String strDate = dF.format(d);
                
                //declares try/catch error block
                try {
                    //declares new file writer function to create file "End of Season Report" with creation date in file name
                    FileWriter file = new FileWriter("End of Season Report " + strDate + ".txt", true);
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
                //if 'teamnames.txt' and 'fixtures.txt' exist, delete both files to reset program to defaults
                Files.deleteIfExists(Paths.get("teamnames.txt"));
                Files.deleteIfExists(Paths.get("fixtures.txt"));
                //close current stage
                stage.close();
                //if running from .jar file, then first if statement will be true
                if(new File("LeagueManager.jar").exists()) {
                    //re-open .jar file
                    Desktop.getDesktop().open(new File("LeagueManager.jar"));
                //if running from .java files, second if statement will be true
                } else if(new File("dist/LeagueManager.jar").exists()) {
                    //open .jar file compiled in /dist folder
                    Desktop.getDesktop().open(new File("dist/LeagueManager.jar"));
                }
                //exit out of the current java runtime
                Platform.exit();
            }catch(Exception ex) {
                //displays error information in console
                ex.printStackTrace();
            }
        //if cancel button is clicked within alert
        } else if(result.get() == cancelBtn) {
            //close out of alert and return to main program
            alert.close();
        }
    }
    
    //defines tablePopulator method 
    public static ObservableList tablePopulator() {
        
        //creates temp arraylist to populate table with
        ArrayList<TableObj> arrayTable = new ArrayList<TableObj>();
        
        //for loop to iterate through teamNames array
        for(String team : teamNames) {
            //creates and initialises local variables
            int played = 0;
            int won = 0;
            int drawn = 0;
            int lost = 0;
            int scored = 0;
            int against = 0;
            int points = 0;
            
            //for loop to iterate through fixtures array to generate table values for each team
            for(FixtureObj fix : fixtures) {
                //if team one and team two score is zero or greater, then score has been entered for fixture
                if(fix.getTeamOneScore() >= 0 && fix.getTeamTwoScore() >= 0) {
                    //if team one in fixture matches current team in teamNames
                    if(team.equals(fix.getTeamOne())) {
                        //add 1 to played count
                        played++;
                        //if team one score is greater than team two score, then team one won match
                        if(fix.getTeamOneScore() > fix.getTeamTwoScore()) {
                            //add 1 to won count
                            won++;
                            //add team one score to current scored value
                            scored = scored + fix.getTeamOneScore();
                            //add team two score to current against value
                            against = against + fix.getTeamTwoScore();
                            //add 3 to current points value
                            points = points + 3;
                        //if team one score is the same as team two score, then match is draw
                        } else if (fix.getTeamOneScore() == fix.getTeamTwoScore()) {
                            //add 1 to drawn count
                            drawn++;
                            //add team one score to current scored value
                            scored = scored + fix.getTeamOneScore();
                            //add team two score to current against value
                            against = against + fix.getTeamTwoScore();
                            //add 1 to current points value
                            points = points + 1;
                        //if team one score is less than team two score, then team one lost match
                        } else if(fix.getTeamOneScore() < fix.getTeamTwoScore()) {
                            //add 1 to lost count
                            lost++;
                            //add team one score to current scored value
                            scored = scored + fix.getTeamOneScore();
                            //add team two score to current against value
                            against = against + fix.getTeamTwoScore();
                        }
                    //if team two in fixture matches current team in teamNames
                    } else if(team.equals(fix.getTeamTwo())) {
                        //add 1 to played count
                        played++;
                        //if team two score is greater than team one score, then team two won match
                        if(fix.getTeamTwoScore() > fix.getTeamOneScore()) {
                            //add 1 to won count
                            won++;
                            //add team two score to current scored value
                            scored = scored + fix.getTeamTwoScore();
                            //add team one score to current against value
                            against = against + fix.getTeamOneScore();
                            //add 3 to current points value
                            points = points + 3;
                        //if team one score is the same as team two score, then match is draw
                        } else if (fix.getTeamTwoScore() == fix.getTeamOneScore()) {
                            //add 1 to drawn count
                            drawn++;
                            //add team two score to current scored value
                            scored = scored + fix.getTeamTwoScore();
                            //add team one score to current against value
                            against = against + fix.getTeamOneScore();
                            //add 1 to current points value
                            points = points + 1;
                        //if team two score is less than team one score, then team two lost match
                        } else if(fix.getTeamTwoScore() < fix.getTeamOneScore()) {
                            //add 1 to lost count
                            lost++;
                            //add team two score to current scored value
                            scored = scored + fix.getTeamTwoScore();
                            //add team one score to current against value
                            against = against + fix.getTeamOneScore();
                        }
                    }
                }
            }
            //add new object to arrayTable
            arrayTable.add(new TableObj(team, played, won, drawn, lost, scored, against, points)); 
        }
        //converts arrayTable arraylist to observable list
        ObservableList data = FXCollections.observableList(arrayTable);
        //returns observable list
        return data;
    }
    
    //defines addScore method
    public static void addScore(String fixture, String scoreOne, String scoreTwo) {
        //for loop to interate through fixtures arraylist
        for(int i = 0; i < fixtures.size(); i++) {
            //finds index value of fixture in fixtures array that matches the fixture which was selected
            if(fixtures.get(i).getFixture().equals(fixture)) {
                //sets score for the correct fixture
                fixtures.get(i).setScore(Integer.parseInt(scoreOne), Integer.parseInt(scoreTwo));
            }
        }
        //clears all items from table
        table.getItems().clear();
        //populates table using tablePopulator method
        table.getItems().addAll(tablePopulator());
        //set content of fixturePane via listToLabels method
        fixturePane.setContent(listToLabels(fixtures, 0));
        //set content of recentPane via listToLabels method
        recentPane.setContent(listToLabels(fixtures, 1));
        //runs sortTable method to sort all entries within the table
        sortTable(false);
        //runs saveFile method to save changes made to file
        saveToFile();
    }
    
    //defines sortTable method
    public static void sortTable(boolean var) {
        //checks if var value is true or false
        if(var == true) {
            //if var = true, sort table by points and difference columns in reversed order
            pointsCol.setComparator(pointsCol.getComparator().reversed());
            pointsCol.setSortNode(new Group());
            diffCol.setComparator(diffCol.getComparator().reversed());
            diffCol.setSortNode(new Group());
            table.getSortOrder().addAll(pointsCol, diffCol);
        } else {
            //if var = false, sort table by points and difference columns
            pointsCol.setComparator(pointsCol.getComparator());
            pointsCol.setSortNode(new Group());
            diffCol.setComparator(diffCol.getComparator());
            diffCol.setSortNode(new Group());
            table.getSortOrder().addAll(pointsCol, diffCol);
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
