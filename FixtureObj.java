package y2.coursework;

//imports
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author Christopher
 */
public class FixtureObj implements Serializable {
    
    //defines all variables for FixtureObj class
    private String teamOne;
    private String teamTwo;
    private LocalDate date;
    // set initial team scores to -1 to allow for null checking equivalent for int (as int is not an object and so cannot be null) - stultuske https://www.daniweb.com/programming/software-development/threads/370155/how-to-check-int-variable-is-null-in-java
    private int teamOneScore = -1;
    private int teamTwoScore = -1;
    
    //defines constructor method to create an instance of FixtureObj
    public FixtureObj(String t1, String t2) {
        //declares value of class variables based upon input into constructor method
        teamOne = t1;
        teamTwo = t2;        
    }
    
    //defines setDate method
    public void setDate(LocalDate d) {
    	//declares value of variable based upon input into method
        date = d;
    }
    
    //defines setScore method
    public void setScore(int t1, int t2) {
    	//declares value of variables based upon inputs into method
        teamOneScore = t1;
        teamTwoScore = t2;
    }
    
    //defines getFixture method
    public String getFixture() {
    	//returns string consisting of variables teamOne and teamTwo when method called
        return teamOne + " vs " + teamTwo;
    }
    
    //defines getCompletedFixture method
    public String getCompletedFixture() {
    	//returns string consisting of variables teamOne, teamOneScore, teamTwoScore and teamTwo when method called
        return teamOne + "   " + teamOneScore + " - " + teamTwoScore + "   " + teamTwo;
    }
    
    //defines getFixtureList method
    public String getFixtureList() {
    	//creates temp string
        String str = "";
        //creates temp date instant value set from date variable value
        Date d = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        //creates dateformat to specify output display format of date value 
        DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
        //binds temp date into specified dateformat, then assigns calue to temp string date variable
        String strDate = dF.format(d);
        
        //checks if scores have been entered for teamOne and teamTwo
        if(teamOneScore == -1 && teamTwoScore == -1) {
        	//if team scores have not been entered, then set temp string to contain: teamOne, teamTwo and temp string date
            str = teamOne + "   " + " - " + "   " + teamTwo + "  :  " + strDate;
        } else {
        	//if team scores have been entered, then set temp string to contain: teamOne, teamOneScore, teamtwoScore, teamTwo and temp string date
            str = teamOne + "   " + teamOneScore + " - " + teamTwoScore + "   " + teamTwo + "  :  " + strDate;
        }
    	//returns temp string value when method called
        return str;
    }
    
    //defines getDate method
    public String getDate() {
        //creates temp date instant value set from date variable value
        Date d = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        //creates dateformat to specify output display format of date value 
        DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
        //binds temp date into specified dateformat, then assigns calue to temp string date variable
        String strDate = dF.format(d);
        //returns temp string date value when method called
        return strDate;
    }
    
    //defines getScore method
    public String getScore() {
    	//returns string consisting of variables teamOne, teamOneScore, teamTwoScore and teamTwo when method called
        return teamOne + " " + teamOneScore + " : " + teamTwoScore + " " + teamTwo;
    }
    
    //defines getTeamOneScore method
    public int getTeamOneScore() {
    	//returns teamOneScore value when method called
        return teamOneScore;
    }
    
    //defines getTeamTwoScore method
    public int getTeamTwoScore() {
    	//returns teamTwoScore value when method called
        return teamTwoScore;
    }
    
    //defines getTeamOne method
    public String getTeamOne() {
    	//returns teamOne value when method called
        return teamOne;
    }
    
    //defines getTeamTwo method
    public String getTeamTwo() {
    	//returns teamTwo value when method called
        return teamTwo;
    }
}