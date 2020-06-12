package sample.simulationView;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TotalChart {

    private LineChart<Number, Number> lineChart;
    private int width;
    private int height;
    private Pane container;

    public TotalChart(List<XYChart.Series<Number, Number>> series, int width, int height, Pane container)
    {
        this.container = container;
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Day of simulation");
        yAxis.setLabel("Quantity");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        this.lineChart = lineChart;
        lineChart.setTitle("Predator Prey Simulation");
        lineChart.getData().addAll(series);
        lineChart.setCreateSymbols(false);

        lineChart.setMaxWidth(width);
        lineChart.setMinWidth(width);
        lineChart.setMaxHeight(height);
        lineChart.setMinHeight(height);

        lineChart.setOnMouseClicked( k-> {
            Stage stage = new Stage();
            Pane newRoot = new StackPane();

            this.lineChart.setMinWidth(1000);
            this.lineChart.setMinHeight(800);

            EventHandler<? super MouseEvent> oldHandler = this.lineChart.getOnMouseClicked();

            lineChart.setOnMouseClicked( event-> {
                Stage saveStage = prepareSaveStage();
                saveStage.show();
            });
            newRoot.getChildren().add(this.lineChart);
            stage.setScene(new Scene(newRoot));


            stage.setOnCloseRequest( action ->{
                this.lineChart.setMinHeight(height);
                this.lineChart.setMinWidth(width);
                this.lineChart.setMaxHeight(height);
                this.lineChart.setMaxWidth(width);
                this.container.getChildren().add(this.lineChart);
                this.lineChart.setOnMouseClicked(oldHandler);
                stage.close();

            });
            stage.show();
        });

        lineChart.setAnimated(false);

    }



    public Stage prepareSaveStage(){
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
            WritableImage img = this.lineChart.snapshot(null, null);
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

    public LineChart<Number, Number> getLineChart() {
        return lineChart;
    }
}
