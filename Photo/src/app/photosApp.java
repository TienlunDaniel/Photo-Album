package app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.loginPageController;
import view.userController;
import view.AlbumController;
import view.SearchController;
import view.adminSubsystemController;

/**This class is the main class that launch the photos application.
 *  It loads all the UIs, and show the login page at the beginnings.
 */
public class photosApp extends Application {
    @Override
    public void start(Stage primaryStage)
            throws Exception {
    	//loginPageController instance
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/loginPage.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        loginPageController login = loader.getController(); //instance
        //show login page
        Scene scene = new Scene(root, 650, 400);
        primaryStage.setScene(scene);
        loginPageController.stage = primaryStage;
        loginPageController.loginInstance = login;
        primaryStage.setResizable(false);
        primaryStage.show();
        
        //adminSubsystemController
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/adminSubsystem.fxml"));
        AnchorPane adminPage  = (AnchorPane) fxmlLoader.load();
        adminSubsystemController adminInstance = fxmlLoader.getController(); //instance
        Stage adminStage = new Stage(); //adminStage
        adminStage.setScene(new Scene(adminPage, 650,400));
        adminStage.setResizable(false);
        adminSubsystemController.stage = adminStage;
        adminSubsystemController.adminInstance = adminInstance;
        //start admin
        adminInstance.start();
        
        FXMLLoader userLoader = new FXMLLoader(getClass().getResource("/view/userPage.fxml"));
        AnchorPane userPage = (AnchorPane) userLoader.load();
        userController userInstance = userLoader.getController();
        Stage userStage = new Stage();
        userStage.setScene(new Scene(userPage,650 ,400));
        userStage.setResizable(false);
        userController.stage = userStage;
        userController.userInstance = userInstance;
        
        FXMLLoader albumLoader = new FXMLLoader(getClass().getResource("/view/AlbumPage.fxml"));
        AnchorPane albumPage = (AnchorPane) albumLoader.load();
        AlbumController albumInstance = albumLoader.getController();
        Stage albumStage = new Stage();
        albumStage.setScene(new Scene(albumPage,650 ,400));
        albumStage.setResizable(false);
        AlbumController.stage = albumStage;
        AlbumController.albumInstance = albumInstance;
        
        FXMLLoader searchLoader = new FXMLLoader(getClass().getResource("/view/SearchPage.fxml"));
        AnchorPane searchPage = (AnchorPane) searchLoader.load();
        SearchController searchInstance = searchLoader.getController();
        Stage searchStage = new Stage();
        searchStage.setScene(new Scene(searchPage,650 ,400));
        searchStage.setResizable(false);
        SearchController.stage = searchStage;
        SearchController.searchInstance = searchInstance;

    }
    /**Main method that runs photo app
     * 
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
