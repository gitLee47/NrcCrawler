import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class crawler {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
	  URL u;
      InputStream is = null;
      DataInputStream dis;
      String s;
      
      String[] years = {"2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017"};
      //HashMap<String, ArrayList<String>> yearLinks = new HashMap<String, ArrayList<String>>();
      
      scraper scrape = new scraper();
      
      for(int i=0; i < years.length; i++) {
    	  u = new URL("https://www.nrc.gov/reading-rm/doc-collections/event-status/event/"+years[i]+"/");
    	  is = u.openStream(); 
          dis = new DataInputStream(new BufferedInputStream(is));
          while ((s = dis.readLine()) != null) {
        	  if(s.contains("href=\""+years[i])) {
        		  String link = scraper.cleanLine(years[i], "\">", s);
        		  if(!link.equals("20161121en.html")) {
	        		  String url = "https://www.nrc.gov/reading-rm/doc-collections/event-status/event/"+years[i]+"/"+link;
	        		  System.out.println(url);
	        		  scraper.scrapeAll(url);
        		  }
        	  }
          }
          is.close();
      }
      
      scraper.closeConnection();
   
      
      /*
      u = new URL("https://www.nrc.gov/reading-rm/doc-collections/event-status/event/2012/20120206en.html");
      is = u.openStream(); 
      dis = new DataInputStream(new BufferedInputStream(is));
      String p ="";
      while ((s = dis.readLine()) != null) {
    	  	if(s.contains("Event Number")) {
    	  		System.out.println(s);
    	  		System.out.println(p);
    	  	}
    	  	
    	  	if(!s.trim().isEmpty())
    	  		p = s; 
    		//System.out.println(s);
      }
      is.close(); */
	}
	
	
}
