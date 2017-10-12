import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;

public class scraper {
	private static final String FILE_HEADER = "id,Event_Number,Event_Type,Emergency_Class,"
			+ "Notification_Date,Notification_Time,Event_Date,Event_Time,State,Event_Text,Source";
	private static FileWriter wr =  null;
	static int i = 0;
	static HashSet<String> uniques;
	scraper() throws IOException{
		wr = new FileWriter("data//events2.csv");
		uniques = new HashSet<String>();
		wr.append(FILE_HEADER.toString());
		wr.append("\n");
		i = 0;
	}

	public static void main(String[] args) throws IOException {
		//BufferedReader scan = new BufferedReader(new FileReader("data//test.txt"));
	    URL u;
        InputStream is = null;
        DataInputStream dis;
	      
        u = new URL("https://www.nrc.gov/reading-rm/doc-collections/event-status/event/2012/20120206en.html");
        is = u.openStream();
        dis = new DataInputStream(new BufferedInputStream(is));
        
        wr = new FileWriter("data//events.csv");
		String curr ="";
		String prev = "";
		String eveNum = "";
		String eveType = "";
		String notDt = "";
		String notTime = "";
		String eveDt = "";
		String eveTime = "";
		String state = "";
		String eventtxt = "";
		int i = 0;
		wr.append(FILE_HEADER.toString());
		wr.append("\n");
		while((curr=dis.readLine())!=null) {
			if(curr.contains("Event Number")){
				eveType = cleanLine(">","</", prev);
				eveNum= cleanLine("Event","</", curr).split(":")[1].trim();
			}
			else if(curr.contains("Notification Date")){
				notDt = cleanLine("Notification", "<", curr).split(":")[1].trim();
			}
			else if(curr.contains("Notification Time")) {
				notTime = cleanLine("Notification", "<", curr).split(":")[1].trim();
			}
			else if(curr.contains("Event Date")) {
				eveDt = cleanLine("Event", "<", curr).split(":")[1].trim();
			}
			else if(curr.contains("Event Time")) {
				eveTime = cleanLine("Event", "<", curr).split(":")[1].trim();
			}
			else if(curr.contains("State:")) {
				state = cleanLine("State:", "<", curr).split(":")[1].trim();
			}
			else if(curr.contains("Event Text")) {
					while((curr = dis.readLine()) != null && !curr.contains("</TR>")) {
						if(curr.contains("<TD scope=\"row\""))
							eventtxt+=curr;
						else if(curr.contains("<BR>")) {
							String cleaned = cleanLine(">", "..", curr);
							if(!cleaned.equals("\n")) {
								eventtxt+=cleaned;
							}
						}
					}
					
				if(uniques.add(eveNum))
				{
					eventtxt = cleanLine(">","</TD>",eventtxt);
					wr.append(++i+","+eveNum+","+eveType+","+notDt+","+notTime+","+eveDt+","+eveTime+","+state+","+eventtxt);
					wr.append("\n");
				}
			}
			if(curr!= null && !curr.trim().isEmpty())
				prev = curr;
		}
		wr.close();
		is.close();
	}
	
	public static String cleanLine(String stxt, String etxt, String line) {
		int idxStart = line.indexOf(stxt);
		
		if(stxt.equals(">"))
			idxStart++;
		String clean = "";
		if(etxt.equals("..")) {
			clean = line.substring(idxStart);
		}
		else {
			int idxEnd = line.indexOf(etxt);
			//System.out.println(line);
			try {
			clean = line.substring(idxStart, idxEnd);
			}
			catch(Exception e) {
				System.out.println(line);
				System.out.println(stxt);
				System.out.println(etxt);
			}
		}
		return clean;
	}
	
	public static void scrapeAll(String url) throws IOException {
		URL u;
        InputStream is = null;
        DataInputStream dis;
	      
        u = new URL(url);
        is = u.openStream();
        dis = new DataInputStream(new BufferedInputStream(is));
        
		
		String curr ="";
		String prev = "";
		String eveNum = "";
		String eveType = "";
		String notDt = "";
		String notTime = "";
		String eveDt = "";
		String eveTime = "";
		String state = "";
		String eventtxt = "";
		String emergency = "";
		while((curr=dis.readLine())!=null) {
			if(curr.contains("Event Number")){
				
				eveType = cleanLine(">","</", prev);
				eveNum= cleanLine("Event","</", curr).split(":")[1].trim();
				
			}
			else if(curr.contains("Notification Date")){
				notDt = cleanLine("Notification", "<", curr).split(":")[1].trim();
			}
			else if(curr.contains("Notification Time")) {
				notTime = cleanLine("Notification", "<", curr).split("e:")[1].trim();
			}
			else if(curr.contains("Event Date")) {
				eveDt = cleanLine("Event", "<", curr).split(":")[1].trim();
			}
			else if(curr.contains("Event Time")) {
				eveTime = cleanLine("Event", "<", curr).split("e:")[1].trim();
			}
			else if(curr.contains("State:")) {
				try {
				state = cleanLine("State:", "<", curr).split(":")[1].trim();
				}
				catch (Exception e) {
					
				}
			}
			else if(curr.contains("Emergency Class")){
				emergency = cleanLine("Emergency", "<", curr).split(":")[1].trim();
			}
			else if(curr.contains("Event Text")) {
				try {
				while((curr = dis.readLine()) != null && !curr.contains("</TR>")) {
					if(curr.contains("<TD scope=\"row\""))
						eventtxt+=curr;
					else if(curr.contains("<BR>")) {
						String cleaned = cleanLine(">", "..", curr);
						if(!cleaned.trim().isEmpty()) {
							eventtxt+=cleaned;
						}
					}
				}
				} catch (Exception e) {
					
				}
				if(!uniques.add(eveNum))
					continue;
				eventtxt = cleanLine(">","</TD>",eventtxt);
				eventtxt.replaceAll(",", "");
				wr.append(++i+","+eveNum+","+eveType+","+emergency+","+notDt+","+notTime+","+eveDt+","+eveTime+","+state+","+eventtxt+","+url);
				wr.append("\n");
			}
			
			if(curr!=null && !curr.trim().isEmpty())
				prev = curr;
		}
		
		is.close();
	}
	
	public static void closeConnection() throws IOException{
		wr.close();
	}
}
