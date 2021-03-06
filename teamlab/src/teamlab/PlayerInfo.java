package teamlab;
import java.io.*;
import javax.swing.JOptionPane;
import java.util.*;


/**
 * @author Yiwen <br>
 *PlayerInfo class stores player information including name and score for score
 *display at the end of the game. 
 */
public class PlayerInfo{

	String name, playerName, scoreDisplay;
	int score = 0;
	String message ="Name" +"   " + "Score";
	HashMap<String, Integer> scoreMap = new HashMap<String, Integer>();
	
	/**
	 * Adds player entry
	 */
	public void addName() {
		
		playerName = Validator.validateName("What is your first name(Capital first letter)?");		
	}

	
	/**
	 * Returns player score
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	
	
	/**
	 * Add score to original score when player submits right answer
	 */
	public void updateScore() {
		score = score + 1;
		
	}
	
	/**
	 * Updates score when player achieves bingo
	 * @param time number of seconds used
	 * @param size size of the game
	 */
	public void bingoScore(int time, int size) {
		score += (size-2)*50 + 500/time;
	}
	

    /**Sorting function to sort hashmap by values
     * @param hm hashmap that needs to be sorted
     * @return temp sorted hashmap
     */
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) 
    { 
        // Create a list from elements of HashMap 
        List<Map.Entry<String, Integer> > list = 
               new LinkedList<Map.Entry<String, Integer> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() { 
            public int compare(Map.Entry<String, Integer> o1,  
                               Map.Entry<String, Integer> o2) 
            { 
                return (o2.getValue()).compareTo(o1.getValue()); 
            } 
        }); 
          
     // put data from sorted list to hashmap  
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>(); 
        for (Map.Entry<String, Integer> element : list) { 
            temp.put(element.getKey(), element.getValue()); 
        } 
        return temp; 
    } 
	
    
	/**Writes top 5 players of the game to new file 
	 * @throws IOException
	 */
	public void readDirectory() throws IOException{
		//Create a file object
		File playerData = new File ("TopFiveScore.txt");
		BufferedReader in = new BufferedReader(new FileReader(playerData));
		String line = in.readLine();
		while(line != null) {
			String[] entireLine = line.split(" ");
			name = entireLine[0]; 
			scoreDisplay = entireLine[1];
			int curScore = Integer.valueOf(scoreDisplay);
			scoreMap.put(name, curScore);
			line = in.readLine();
		} //End of while
		
	    in.close();
	    //Put current player's info into HashMap only when for a new player or previous player with higher score
	    if (!scoreMap.containsKey(playerName) || scoreMap.get(playerName) < score) scoreMap.put(playerName, score);
	}

	/**Checks player score and display scores
	 * @throws IOException
	 */
	public void checkScore() throws IOException{
		readDirectory();
		displaySortedMap(scoreMap);
	}//end of method
	
	
    
	/**This method passes the players' scores to sort by value, display results and write
	 * to TopFiveScore text file
	 * @param map
	 * @throws IOException
	 */
	public void displaySortedMap(HashMap<String,Integer> map) throws IOException {
		Map<String, Integer>sortedScore = sortByValue(map);
		int count = 0;
			PrintWriter out = new PrintWriter(new FileWriter("TopFiveScore.txt", false));
		
			for(Map.Entry<String,Integer> element: sortedScore.entrySet()) {
				if(count < 5) {
				String info = element.getKey() + "   " + element.getValue();
				out.println(info);
				message = message + "\n" + info;
				count++;
				}
			}
			out.close();
		JOptionPane.showMessageDialog(null, message);	
		message = "Name" +"     " + "Score";
	}
}//end of class
