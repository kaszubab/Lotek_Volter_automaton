package sample;

import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.worldElements.Vector;
import sample.worldElements.World;
import sample.worldElements.WorldConfiguration;
import sample.worldElements.animals.Animal;
import sample.worldElements.animals.Fox;
import sample.worldElements.animals.Rabbit;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SimulationBoard {

    private World world;
    private Pane root;
    private Canvas canvas;
    private WorldConfiguration worldConfiguration;
    private AnimationTimer animationTimer;
    private int animationFrame;
    private boolean running;
    private int simulationDay = 0;
    private LineChart<Number, Number> totalChart;
    private VBox controlPanel;

    private XYChart.Series<Number, Number> rabbitSeries = new XYChart.Series<>();
    private XYChart.Series<Number, Number> foxSeries = new XYChart.Series<>();



    public SimulationBoard() {
        this.world = null;
        this.worldConfiguration = null;
        this.running = false;

        rabbitSeries.setName("Rabbit count");
        foxSeries.setName("Fox count");

        HBox hBox = new HBox();
        hBox.getChildren().add(createCanvas());
        prepareControlPanel();
        hBox.getChildren().add(this.controlPanel);
        root = hBox;
    }

    public Pane createCanvas() {
        VBox vBox = new VBox();
        this.canvas = new Canvas();
        canvas.setHeight(800);
        canvas.setWidth(800);

        vBox.getChildren().add(canvas);
        return vBox;
    }

    private LineChart<Number, Number> createTotalChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day of simulation");
        yAxis.setLabel("Quantity");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Predator Prey Simulation");
        lineChart.getData().add(rabbitSeries);
        lineChart.getData().add(foxSeries);
        lineChart.setMaxWidth(400);
        lineChart.setMaxHeight(300);

        lineChart.setOnMouseClicked( k-> {
            Stage stage = new Stage();
            Pane newRoot = new StackPane();

            totalChart.setMinWidth(1000);
            totalChart.setMinHeight(800);

            EventHandler<? super javafx.scene.input.MouseEvent> oldHandler = totalChart.getOnMouseClicked();

            totalChart.setOnMouseClicked( event-> {
                if(!running) {
                    Stage saveStage = prepareSaveStage();
                    saveStage.show();
                }
            });
            newRoot.getChildren().add(totalChart);
            stage.setScene(new Scene(newRoot));


            stage.setOnCloseRequest( action ->{
                totalChart.setMinHeight(300);
                totalChart.setMinWidth(400);
                totalChart.setMaxHeight(300);
                totalChart.setMaxWidth(400);
                controlPanel.getChildren().add(totalChart);
                totalChart.setOnMouseClicked(oldHandler);
                stage.close();
            });
            stage.show();
        });

        lineChart.setAnimated(false);

        return lineChart;
    }

    private Stage prepareSaveStage() {
        Stage saveStage = new Stage();
        saveStage.setTitle("Save to png");
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setMinHeight(300);
        vBox.setMinWidth(400);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.setSpacing(10);
        Text text = new Text();
        text.setText("Enter a filename (saves to Png)");
        vBox.getChildren().add(text);

        TextField textField = new TextField();
        textField.setPromptText("Filename");
        vBox.getChildren().add(textField);

        Button button = new Button();
        button.setText("Save");
        button.setOnMouseClicked(k ->{
            String filename = textField.getCharacters().toString() + ".png";
            WritableImage img = totalChart.snapshot(null, null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", new File(filename));
            }
            catch (IOException e) {
                text.setText("Wrong filename");
            }
        });
        vBox.getChildren().add(button);
        saveStage.setScene(new Scene(vBox));
        return saveStage;
    }

    public void startSimulation(World world, WorldConfiguration worldConfiguration) {
        this.world = world;
        this.worldConfiguration = worldConfiguration;
        int cellWidth = 800 / worldConfiguration.getWidth();
        int cellHeight = 800 / worldConfiguration.getHeight();

        drawMap(cellWidth, cellHeight);
        this.animationTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // only update once every second
                if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos(animationFrame)) {
                    world.run();
                    simulationDay++;
                    drawMap(cellWidth, cellHeight);
                    lastUpdate = now;
                }
            }
        };
        animationTimer.start();
    }


    public void drawMap(double cellWidth, double cellHeight) {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.LAVENDER);
        gc.fillRect(0 ,0, canvas.getWidth(), canvas.getHeight());

        int foxCount = 0;
        int rabbitCount = 0;

        List<Animal> animalList = world.getAnimalList();
        for(Animal k : animalList) {
            if (k instanceof Rabbit) {
                rabbitCount++;
                gc.setFill(Color.YELLOWGREEN);
                gc.fillRect((k.getPosition().getX() * cellWidth)+1, (k.getPosition().getY() * cellHeight)+1,
                        cellWidth - 2, cellHeight -2) ;
            }
            else if(k instanceof Fox){
                foxCount++;
                gc.setFill(Color.BLUE);
                gc.fillRect((k.getPosition().getX() * cellWidth)+1, (k.getPosition().getY() * cellHeight)+1,
                        cellWidth - 2, cellHeight -2) ;
            }
        };

        rabbitSeries.getData().add(new XYChart.Data<Number, Number>(simulationDay, rabbitCount));
        foxSeries.getData().add(new XYChart.Data<Number, Number>(simulationDay, foxCount));



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
        ( (Text) controlPanel.getChildren().get(2)).setText("Initial percentage of foxes in%");
        ( (TextField) controlPanel.getChildren().get(3)).setPromptText("Initial percentage of foxes in%");
        ( (Text) controlPanel.getChildren().get(4)).setText("Initial percentage of rabbits in%");
        ( (TextField) controlPanel.getChildren().get(5)).setPromptText("Initial percentage of rabbits in%");
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
            double percentageOfFoxes = Double.parseDouble(( (TextField) controlPanel.getChildren().get(3)).getCharacters().toString()) / 100;
            double percentageOfRabbits = Double.parseDouble(( (TextField) controlPanel.getChildren().get(5)).getCharacters().toString()) / 100;
            double deathPropability = 1 / Double.parseDouble(( (TextField) controlPanel.getChildren().get(7)).getCharacters().toString());
            double foxBirthPropability = Double.parseDouble(( (TextField) controlPanel.getChildren().get(9)).getCharacters().toString());
            double rabbitBirthPropability = Double.parseDouble(( (TextField) controlPanel.getChildren().get(11)).getCharacters().toString());

            animationFrame = step;
            this.worldConfiguration = new WorldConfiguration(percentageOfFoxes, percentageOfRabbits, 150, 150,
                    deathPropability, foxBirthPropability, rabbitBirthPropability);
            this.world = new World(worldConfiguration);
            running = true;
            startSimulation(world, worldConfiguration);

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
            Stage saveStage = prepareSaveStage();
            saveStage.show();
        });
        buttons.getChildren().add(saveButton);



        controlPanel.getChildren().add(buttons);

        totalChart = createTotalChart();
        controlPanel.getChildren().add(totalChart);

        this.controlPanel  = controlPanel;
    }



    public Pane getVisualization() {
        return root;
    }
}
