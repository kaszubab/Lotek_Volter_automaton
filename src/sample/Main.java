package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sample.simulationView.AutomataSimulationBoard;
import sample.simulationView.DESimulationBoard;

public class Main extends Application {

    private Pane root;
    @Override
    public void start(Stage primaryStage) throws Exception{

        root = new StackPane();
        primaryStage.setTitle("Predator-prey simulator");

        VBox centralPanel = new VBox();
        centralPanel.setAlignment(Pos.CENTER);
        centralPanel.setSpacing(20);
        centralPanel.setPadding(new Insets(10, 10, 10, 10));
        Text text = new Text();
        text.setText("Welcome to the Predator-prey simulator");
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font("arial", 30));
        centralPanel.getChildren().add(text);
        text.setFill(Color.RED);

        text = new Text();
        text.setText("Choose the mode of simulation");
        text.setTextAlignment(TextAlignment.CENTER);
        centralPanel.getChildren().add(text);

        Button automatonSimulation = new Button();
        automatonSimulation.setAlignment(Pos.CENTER);
        automatonSimulation.setText("Automata simulation");
        centralPanel.getChildren().add(automatonSimulation);

        automatonSimulation.setOnMouseClicked( k->{
            AutomataSimulationBoard view = new AutomataSimulationBoard();
            root = view.getVisualization();
            primaryStage.setScene(new Scene(root));
        });

        Button DESimulation = new Button();
        DESimulation.setAlignment(Pos.CENTER);
        DESimulation.setText("DESimulation simulation");
        centralPanel.getChildren().add(DESimulation);

        DESimulation.setOnMouseClicked( k->{
            DESimulationBoard view = new DESimulationBoard();
            root = view.getVisualization();
            primaryStage.setScene(new Scene(root));
        });

        root.getChildren().add(centralPanel);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }




    public static void main(String[] args) {
        launch(args);
    }
}
