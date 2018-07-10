package view;

import java.util.ArrayList;
/** Set up the informations for each photos
 * each photo has date, tag, and caption
 */
public class PhotoInfo {
	
	String lastDateModified = "";
	String caption = "";
	ArrayList<String> tags = new ArrayList<String>();
	
	public PhotoInfo(String lastDateModified){
		this.lastDateModified = lastDateModified;
	}
	
}
