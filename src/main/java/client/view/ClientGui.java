package client.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class ClientGui extends Application {

    private Stage window;

    public static void main(String[] args){launch(args); }

    public void start(Stage primaryStage)throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/InitialView.fxml"));
        window = primaryStage;
        window.setTitle("Adrenalina");
        window.setResizable(false);
        window.setScene(new Scene(root, 600, 400));
        window.show();
    }

}
