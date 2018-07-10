package view;

import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**Album class that defines album
 */
public class Album {
	
	String title;
	ObservableList<String> thumbnailImages;
	HashMap<String, PhotoInfo> photoInfo;
	
	public Album(String title){
		this.title = title;
		thumbnailImages = FXCollections.observableArrayList();
		photoInfo = new HashMap<String, PhotoInfo>(1000, 2.0f);
	}
}
