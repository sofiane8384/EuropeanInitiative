package sample;



import controller.ProcessViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public Stage primaryStage;
    private BorderPane rootLayout;


    @Override
    public void start(Stage primaryStage) throws Exception{

        this.primaryStage = primaryStage;


        initRootLayout();
        showProcessview();

    }

    private void showProcessview() {
        try {
            // Load person overview.
            AnchorPane processView = FXMLLoader.load(getClass().getResource("ProcessView.fxml"));
            rootLayout.setCenter(processView);


            ProcessViewController controller =  new ProcessViewController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initRootLayout() throws IOException {
        rootLayout = FXMLLoader.load(getClass().getResource("RootLayout.fxml"));
        Scene scene = new Scene(rootLayout);

        this.primaryStage.setTitle("European Initiative");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
