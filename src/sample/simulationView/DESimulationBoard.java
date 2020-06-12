package sample.simulationView;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.DEElements.DESimulation;
import sample.DEElements.DESimulationConfiguration;
import sample.SimulationConfiguration;
import sample.worldElements.World;
import sample.worldElements.WorldConfiguration;
import sample.worldElements.animals.Animal;
import sample.worldElements.animals.Fox;
import sample.worldElements.animals.Rabbit;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DESimulationBoard {

    private DESimulation simulation;
    private Pane root;
    private AnimationTimer animationTimer = null;

    private int animationFrame;
    private boolean running;
    private int simulationDay = 0;

    private TotalChart totalChart;
    private VBox controlPanel;

    private XYChart.Series<Number, Number> rabbitSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> foxSeries = new XYChart.Series<>();



    public DESimulationBoard() {
        this.running = false;

        rabbitSeries.setName("Rabbit count");
        foxSeries.setName("Fox count");

        HBox hBox = new HBox();
        List<XYChart.Series<Number, Number>> series = new LinkedList<>();
        series.add(rabbitSeries);
        series.add(foxSeries);

        TotalChart totalChart = new TotalChart(series, 800 ,600, hBox);
        this.totalChart = totalChart;
        prepareControlPanel();
        hBox.getChildren().add(this.controlPanel);
        hBox.getChildren().add(this.totalChart.getLineChart());

        root = hBox;
    }



    private void startSimulation(DESimulationConfiguration simulationConfiguration) {

        if (this.animationTimer != null) {
            this.animationTimer.stop();
        }

        rabbitSeries.getData().removeIf( k -> 1==1);
        rabbitSeries.getData().add(new XYChart.Data<>(this.simulationDay, simulationConfiguration.getInitialRabbitCount()));
        foxSeries.getData().add(new XYChart.Data<>(this.simulationDay, simulationConfiguration.getInitialFoxCount()));
        DESimulation simulation = new DESimulation(simulationConfiguration);

        this.animationTimer = new AnimationTimer(){
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {

                if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos(animationFrame)) {
                    simulation.calculateNextDay();
                    simulationDay++;
                    rabbitSeries.getData().add(new XYChart.Data<>(simulationDay, simulation.getRabbitCount()));
                    foxSeries.getData().add(new XYChart.Data<>(simulationDay, simulation.getFoxCount() * 10));
                    lastUpdate = now;
                }
            }
        };
        animationTimer.start();
    }




    private void prepareControlPanel() {
        VBox controlPanel = new VBox();
        controlPanel.setAlignment(Pos.CENTER);
        controlPanel.setSpacing(10);
        controlPanel.setPadding(new Insets(5, 5, 5, 5));
        controlPanel.setAlignment(Pos.CENTER);

        for (int i = 0; i < 6; i++) {
            controlPanel.getChildren().add(new Text());
            controlPanel.getChildren().add(new TextField());
        }

        ( (Text) controlPanel.getChildren().get(0)).setText("Time of step in ms");
        ( (TextField) controlPanel.getChildren().get(1)).setPromptText("Time of step in ms");
        ( (Text) controlPanel.getChildren().get(2)).setText("Initial count of foxes");
        ( (TextField) controlPanel.getChildren().get(3)).setPromptText("Initial count of foxes");
        ( (Text) controlPanel.getChildren().get(4)).setText("Initial count of rabbits");
        ( (TextField) controlPanel.getChildren().get(5)).setPromptText("Initial count of rabbits");
        ( (Text) controlPanel.getChildren().get(6)).setText("Average age of fox (integer)");
        ( (TextField) controlPanel.getChildren().get(7)).setPromptText("Average age of fox (integer)");
        ( (Text) controlPanel.getChildren().get(8)).setText("Chances of fox bearing an offspring (double)");
        ( (TextField) controlPanel.getChildren().get(9)).setPromptText("Chances of fox bearing an offspring (double)");
        ( (Text) controlPanel.getChildren().get(10)).setText("Chances of rabbit bearing an offspring (double)");
        ( (TextField) controlPanel.getChildren().get(11)).setPromptText("Chances of rabbit bearing an offspring (double)");

        HBox buttons = new HBox();
        buttons.setSpacing(10);

        Button button = new Button();
        button.setText("New simulation");
        button.setOnMouseClicked( k->{

            int step = Integer.parseInt(( (TextField) controlPanel.getChildren().get(1)).getCharacters().toString());
            int foxCount = Integer.parseInt(( (TextField) controlPanel.getChildren().get(3)).getCharacters().toString());
            int rabbitCount = Integer.parseInt(( (TextField) controlPanel.getChildren().get(5)).getCharacters().toString());
            double deathPropability = 1 / Double.parseDouble(( (TextField) controlPanel.getChildren().get(7)).getCharacters().toString());
            double foxBirthPropability = Double.parseDouble(( (TextField) controlPanel.getChildren().get(9)).getCharacters().toString());
            double rabbitBirthPropability = Double.parseDouble(( (TextField) controlPanel.getChildren().get(11)).getCharacters().toString());

            rabbitSeries.getData().removeIf(v -> 1==1);
            foxSeries.getData().removeIf(v -> 1==1);
            simulationDay = 0;

            animationFrame = step;
            DESimulationConfiguration simulationConfiguration = new DESimulationConfiguration(foxCount, rabbitCount,
                    deathPropability, foxBirthPropability, rabbitBirthPropability);

            running = true;
            startSimulation(simulationConfiguration);

        });
        buttons.getChildren().add(button);

        Button stopStartButton = new Button();
        stopStartButton.setText("Stop simulation");
        stopStartButton.setOnMouseClicked(k -> {
            if(running) {
                animationTimer.stop();
                running = false;
                stopStartButton.setText("Start simulation");
            }
            else {
                stopStartButton.setText("Stop simulation");
                animationTimer.start();
                running = true;
            }
        });
        buttons.getChildren().add(stopStartButton);

        Button saveButton = new Button();
        saveButton.setText("Save chart");
        saveButton.setOnMouseClicked(k -> {
            Stage saveStage = totalChart.prepareSaveStage();
            saveStage.show();
        });
        buttons.getChildren().add(saveButton);

        controlPanel.getChildren().add(buttons);
        this.controlPanel  = controlPanel;
    }



    public Pane getVisualization() {
        return root;
    }
}
