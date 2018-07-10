package view;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**This is the login page.
 */

public class loginPageController {
	
	public static Stage stage; 
	public static loginPageController loginInstance;
	private final String ADMIN = "admin";
    @FXML TextField usernameText;
    @FXML Button loginButton;
    

    public void login(ActionEvent event)throws Exception{
        String input = usernameText.getText();
        
        if(input.equals(ADMIN)){       
        	loginPageController.stage.close();
            adminSubsystemController.stage.show();
            clear(usernameText);
        }else if (adminSubsystemController.obslist.contains(input)){
        	loginPageController.stage.close();
        	userController.userInstance.start(adminSubsystemController.adminInstance.map.get(input));
        	clear(usernameText);
        }else{
        	adminSubsystemController.showItem(1);
        }
        
    }
    
    public static void clear(TextField tf){
    	tf.setText("");
    }
}
