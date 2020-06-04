package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sample.worldElements.World;
import sample.worldElements.WorldConfiguration;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Pane root = new StackPane();

        WorldConfiguration worldConfiguration = new WorldConfiguration(100, 500, 100, 100, 1);
        World world = new World(worldConfiguration);
        View view = new View(world, worldConfiguration);
        root = view.getVisualization();



        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
