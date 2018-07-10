package view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * For searching
 */
public class SearchController {
	public static Stage stage; 
	public static SearchController searchInstance;
	ObservableList<String> thumbnailImages;
	HashMap<String, PhotoInfo> photoInfo;
	ChangeListener<Number> listener;
	
	
	@FXML TextField date;
	@FXML TextField tags;
	@FXML TextField albumName;
	
	@FXML StackPane display;
	@FXML ListView<String> thumbnail;
	@FXML Text photoInformation;
	
	public void start(){
		photoInformation.setText("");
		date.setText("");
		tags.setText("");
		albumName.setText("");
		stage.show();
		clear();
		
		setCellFactory();
		
		this.listener = (obs, oldv, newv) ->{
			display(thumbnail.getSelectionModel().getSelectedIndex());
		};
		
		thumbnail.getSelectionModel().selectedIndexProperty().addListener(this.listener);
	}
	/**
	 * clear what the user have searched
	 */
	private void clear(){
		thumbnailImages = FXCollections.observableArrayList();
		photoInfo = new HashMap<String, PhotoInfo>(1000, 2.0f);
		display.getChildren().clear();
		thumbnail.setItems(thumbnailImages);
		
	}
	/**
	 * display the searching result
	 * @param index
	 */
	private void display(int index){
		if(index < 0) return;
		
		String uri = thumbnailImages.get(index);
		
		String message = "";
		
		PhotoInfo information = photoInfo.get(uri);
		message += information.lastDateModified + "\n";
			
		if(!information.caption.isEmpty()){
			message += "Cap: " + information.caption + "\n";
		}
		
		if(!information.tags.isEmpty()){
			message += "Tags ";
			Iterator<String> it = information.tags.iterator();
			while(it.hasNext()){
				message += it.next() + "\n";
			}
		}
		
		this.photoInformation.setText(message);
		
		
		if(display.getChildren().isEmpty()){ // no picture
			ImageView imgView = new ImageView(uri);
			imgView.fitHeightProperty().bind(display.heightProperty());
			imgView.fitWidthProperty().bind(display.widthProperty());
			imgView.setPreserveRatio(true);
			display.getChildren().add(imgView);
		}else{ //yes picture
			display.getChildren().clear();
			ImageView imgView = new ImageView(uri);
			imgView.fitHeightProperty().bind(display.heightProperty());
			imgView.fitWidthProperty().bind(display.widthProperty());
			imgView.setPreserveRatio(true);
			display.getChildren().add(imgView);
		}
	}
	
	private void setCellFactory(){
		thumbnail.setCellFactory(param -> new ListCell<String>(){
			private ImageView imageView = new ImageView();
			
			@Override
			public void updateItem(String URI, boolean empty){
				super.updateItem(URI, empty);
				imageView.setFitHeight(70);
				imageView.setPreserveRatio(true);
				if(empty){
					setText(null);
					setGraphic(null);
				}else{
					imageView.setImage(new Image(URI));
					setText(null);
					setGraphic(imageView);
				}
			}
		});
	}
	/**
	 * search by date
	 */
	public void searchDate(){
		clear();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		String temp = date.getText();
		String tokens[] = temp.split("~");
		
		Date first;
		Date second;
		try{
			first = sdf.parse(tokens[0]);
			second = sdf.parse(tokens[1]);
		}catch(Exception e){
			adminSubsystemController.showItem(5);
			return;
		}
		
		if(second.before(first)){ //second before first
			adminSubsystemController.showItem(5);
		}
		
		Users user = userController.currentUser;
		Iterator<String> albumIt = user.albumMap.keySet().iterator();
		
		while(albumIt.hasNext()){ //album
			Album album = user.albumMap.get(albumIt.next());
			Iterator<String> photoIt = album.photoInfo.keySet().iterator();
			
			while(photoIt.hasNext()){
				String uri = photoIt.next();
				PhotoInfo information = album.photoInfo.get(uri);
				String lastDateModified = information.lastDateModified;
				Date compare;
				try{
					 compare = sdf.parse(lastDateModified);
				}catch(Exception e){
					System.out.println("error reading date of file");
					return;
				}
				
				if(first.before(compare) && second.after(compare)){
					if(!thumbnailImages.contains(uri)){
						thumbnailImages.add(uri);
						photoInfo.put(uri, information);
					}
				}
			}
			
		}
		
		
		
	}
	/**
	 * search by tags
	 */
	public void searchTags(){
		clear();
		
		String temp = tags.getText();
		String tokens[] = temp.split(" ");
		
		List<String> compTags = new ArrayList<String>(Arrays.asList(tokens));
		
		Users user = userController.currentUser;
		Iterator<String> albumIt = user.albumMap.keySet().iterator();
		
		while(albumIt.hasNext()){ //album
			Album album = user.albumMap.get(albumIt.next());
			Iterator<String> photoIt = album.photoInfo.keySet().iterator();
			
			while(photoIt.hasNext()){
				String uri = photoIt.next();
				PhotoInfo information = album.photoInfo.get(uri);
				List<String> photoTags = information.tags;
				
				if(photoTags.containsAll(compTags)){
					if(!thumbnailImages.contains(uri)){
						thumbnailImages.add(uri);
						photoInfo.put(uri, information);
					}
				}
			}
			
		}
	}
	/**
	 * create a album
	 */
	public void createAlbum(){
		String temp = albumName.getText();
		
		if(temp.isEmpty()){
			return;
		}
		
		Users user = userController.currentUser;
		
		if(user.albumMap.containsKey(temp)){
			adminSubsystemController.showItem(0);
			return;
		}
		
		Album album = new Album(temp);
		album.photoInfo = this.photoInfo;
		album.thumbnailImages = this.thumbnailImages;
		user.album.add(temp);
		user.albumMap.put(temp, album);
	}
	/**
	 * return to previous stage
	 */
	public void back(){
		stage.hide();
		this.thumbnail.getSelectionModel().selectedIndexProperty().removeListener(this.listener);
	}
	
	
}
