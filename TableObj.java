package y2.coursework;

/**
 *
 * @author Christopher
 */
public class TableObj {
     
    //defines all variables for TableObj class (all columns in a football league table - https://sports.stackexchange.com/questions/14957/abbreviations-in-a-football-league-table-what-do-they-mean)
    private String teamName;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesDrawn;
    private int gamesLost;
    private int goalsScored ;
    private int goalsAgainst;
    private int goalsDifference;
    private int points;
    
    //defines constructor method to create an instance of TableObj
    public TableObj(String name, int played, int won, int drawn, int lost, int scored, int against, int point) {
        //declares value of class variables based upon input into constructor method
    	teamName = name;
        gamesPlayed = played;
        gamesWon = won;
        gamesDrawn = drawn;
        gamesLost = lost;
        goalsScored = scored;
        goalsAgainst = against;
        goalsDifference = goalsScored - goalsAgainst;
        points = point;
    } 
    
    //defines getTeamName method
    public String getTeamName() {
    	//returns teamName value when method called
        return teamName;
    }
    
    //defines getPlayed method
    public int getPlayed() {
    	//returns gamesPlayed value when method called
        return gamesPlayed;
    }
    
    //defines getWon method
    public int getWon() {
    	//returns gamesWon value when method called
        return gamesWon;
    }
    
    //defines getDrawn method
    public int getDrawn() {
    	//returns gamesDrawn value when method called
        return gamesDrawn;
    }
    
    //defines getLost method
    public int getLost() {
    	//returns gamesLost value when method called
        return gamesLost;
    }
    
    //defines getScored method
    public int getScored() {
    	//returns goalsScored value when method called
        return goalsScored;
    }
    
    //defines getAgainst method
    public int getAgainst() {
    	//returns goalsAgainst value when method called
        return goalsAgainst;
    }
    
    //defines getDiff method
    public int getDiff() {
    	//returns goalsDifference value when method called
        return goalsDifference;
    }
    
    //defines getPoints method
    public int getPoints() {
    	//returns points value when method called
        return points;
    }
    
    //defines overriding toString method
    @Override
    public String toString() {
    	//returns teamName value when method called
        return teamName;
    }
    
}
