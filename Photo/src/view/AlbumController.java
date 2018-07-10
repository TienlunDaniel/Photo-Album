package view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
/**
 * A album controller
 */
public class AlbumController {
	public static Stage stage;
	public static AlbumController albumInstance;
	public static Album currentAlbum;
	FileChooser fileChooser;
	ChangeListener<Number> listener;
	
	@FXML Text title;
	@FXML ListView<String> thumbnail;
	@FXML StackPane display;
	@FXML Text photoInfo;

	public void start(Album currentAlbum){
		stage.show();
		title.setText(currentAlbum.title);
		clearPane();
		photoInfo.setText("");
		AlbumController.currentAlbum = currentAlbum;
		//set up file Chooser
		fileChooser = new FileChooser();
		fileChooser.setTitle("Open Source File");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		thumbnail.setItems(currentAlbum.thumbnailImages);
		int index = thumbnail.getSelectionModel().getSelectedIndex();
		if(index >=0){
			display(index);
		}
		
		setCellFactory();
		
		this.listener = (obs, oldv, newv) ->{
			display(thumbnail.getSelectionModel().getSelectedIndex());
		};
		
		thumbnail.getSelectionModel().selectedIndexProperty().addListener(this.listener);
	}
	
	private void clearPane(){
		if(!display.getChildren().isEmpty()){ //not empty
			display.getChildren().clear();
		}
	}
	
	private void display(int index){
		if(index < 0) return;
		
		String uri = currentAlbum.thumbnailImages.get(index);
		
		String message = "";
		
		PhotoInfo information = currentAlbum.photoInfo.get(uri);
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
		
		this.photoInfo.setText(message);
		
		
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
	
	public void search(){
		SearchController.searchInstance.start();
		SearchController.stage.show();
	}
	
	public void add(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		
		try{
			File selectedFile = fileChooser.showOpenDialog(stage);
			String uri = selectedFile.toURI().toURL().toString();
			currentAlbum.thumbnailImages.add(uri);
			String lastModified = sdf.format(selectedFile.lastModified()).toString();
			currentAlbum.photoInfo.put(uri, new PhotoInfo(lastModified));
		}catch(Exception e){
			
		}
		
		int index = currentAlbum.thumbnailImages.size()-1;
		thumbnail.getSelectionModel().select(index);
	}
	
	public void remove(){
		int index = thumbnail.getSelectionModel().getSelectedIndex();
		if(index <0 ) return;
		
		currentAlbum.thumbnailImages.remove(index);
		
		int size = currentAlbum.thumbnailImages.size();
		if (size == 0) {
			display.getChildren().clear();
		}else if (index == size) {
			thumbnail.getSelectionModel().select(index-1);
			display(index-1);
		}else {
			thumbnail.getSelectionModel().select(index);
			display(index);
		}
		
	}
	
	public void caption(){
		popupWindow(0);
	}
	
	public void edit(){ //tags
		popupWindow(1);
	}

	public void copy(){
		popupWindow(2);
	}

	public void back(){
		this.thumbnail.getSelectionModel().selectedIndexProperty().removeListener(this.listener);
		AlbumController.stage.hide();
		int index = userController.userInstance.album.getSelectionModel().getSelectedIndex();
		if(index >= 0){
			userController.userInstance.display(index);
		}
		userController.stage.show();
	}
	
	private void popupWindow(int type){
		 //set popup
		 Popup pop = new Popup();
		 pop.setHeight(500);
		 pop.setWidth(500);
		 //set hbox
		 HBox hbox = new HBox();
		 //set button and textfield
		 Button enter = new Button("enter");
		 Button cancel = new Button("cancel");
		 TextField text = new TextField();
		 text.setPrefWidth(500);
		 
		 if(type == 1){
			 text.setPromptText("(key,value) for add [key,value] for remove");
		 }
		 if(type == 2){
			 text.setPromptText("cp albumName for copy mv albumName for move" );
		 }
		 //add
		 hbox.getChildren().add(text);
		 hbox.getChildren().add(enter);
		 hbox.getChildren().add(cancel);
		 pop.getContent().add(hbox);
		 enter.requestFocus();
		 
		 //set on action
		 enter.setOnAction(e ->{
				if(type == 0){ //caption for 0
					String temp = text.getText();
				 	
					if(!temp.isEmpty()){
						int index = thumbnail.getSelectionModel().getSelectedIndex();
						if(index < 0) return;
						PhotoInfo information = currentAlbum.photoInfo.get(currentAlbum.thumbnailImages.get(index)); 
						information.caption = temp;
							
						display(index);
						pop.hide();
					}
				}else if(type == 1){
						String temp = text.getText();
						int index = thumbnail.getSelectionModel().getSelectedIndex();
						if(index < 0) return;
						PhotoInfo information = currentAlbum.photoInfo.get(currentAlbum.thumbnailImages.get(index));
						
						if(!temp.isEmpty()){
							if(temp.charAt(0) == '('){
								information.tags.add(temp);
								pop.hide();
							}else{
								temp = '(' + temp.substring(1, temp.length()-1) + ')';
								if(information.tags.contains(temp)){
									information.tags.remove(temp);
									pop.hide();
								}else{
									adminSubsystemController.showItem(2);
								}
							}
							display(index);
						}
				}else if (type == 2){
					String temp = text.getText();
					int index = thumbnail.getSelectionModel().getSelectedIndex();
					if(index < 0) return;
					PhotoInfo information = currentAlbum.photoInfo.get(currentAlbum.thumbnailImages.get(index));
					
					if(!temp.isEmpty()){
						String albumName = "";
						try{
							albumName = temp.substring(3,temp.length());
						}catch(Exception ex){
							adminSubsystemController.showItem(4);
							return;
						}
						
						if(!userController.currentUser.albumMap.containsKey(albumName)){
							adminSubsystemController.showItem(3);
							return;
						}
						
						if(temp.charAt(0) == 'c' && temp.charAt(1) == 'p'){//copy 
							Album cpAlbum = userController.currentUser.albumMap.get(albumName);
							String uri = currentAlbum.thumbnailImages.get(index);
							cpAlbum.thumbnailImages.add(uri);
							cpAlbum.photoInfo.put(uri, information);
							pop.hide();
							return;
						}else if (temp.charAt(0) == 'm' && temp.charAt(1) == 'v'){ //move
							Album mvAlbum = userController.currentUser.albumMap.get(albumName);
							String uri = currentAlbum.thumbnailImages.get(index);
							mvAlbum.thumbnailImages.add(uri);
							mvAlbum.photoInfo.put(uri, information);
							currentAlbum.thumbnailImages.remove(index);
							currentAlbum.photoInfo.remove(uri);
							pop.hide();
							return;
						}else{
							adminSubsystemController.showItem(4);
							return;
						}
					}
				}
		 });
		 
		 cancel.setOnAction(e->{
			 pop.hide();
		 });
		 
		 pop.show(AlbumController.stage);
	 }
}
