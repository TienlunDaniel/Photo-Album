package view;

import javafx.collections.ObservableList;

import java.util.HashMap;

import javafx.collections.FXCollections;
/**This is the user class
 */
public class Users {
	/**
	 * A user has his own name, list of album, and hashmap that map the albums
	 */
    String name;
    ObservableList<String> album;
    HashMap<String, Album> albumMap;
    /**
     * construct a user
     * @param name
     */
    public Users(String name){
        this.name = name;
        album = FXCollections.observableArrayList();
        albumMap = new HashMap(1000, 2.0f);
    }
}
