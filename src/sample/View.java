package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import sample.worldElements.Vector;
import sample.worldElements.World;
import sample.worldElements.WorldConfiguration;
import sample.worldElements.animals.Animal;
import sample.worldElements.animals.Fox;
import sample.worldElements.animals.Rabbit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class View {

    private World world;
    private Pane root;
    private Canvas canvas;
    private WorldConfiguration worldConfiguration;

    public View(World world, WorldConfiguration worldConfiguration) {
        this.world = world;
        this.root = new StackPane();
        this.worldConfiguration = worldConfiguration;

        VBox vBox = new VBox();
        this.canvas = new Canvas();
        canvas.setHeight(500);
        canvas.setWidth(800);
        double cellWidth = 600 / worldConfiguration.getWidth();
        double cellHeight = 500 / worldConfiguration.getHeight();

        drawMap(cellWidth, cellHeight);

        AnimationTimer animationTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                // only update once every second
                if ((now - lastUpdate) >= TimeUnit.MILLISECONDS.toNanos(500)) {
                    world.run();
                    drawMap(cellWidth, cellHeight);
                    lastUpdate = now;
                    System.out.println("Working");
                }
            }
        };

        animationTimer.start();


        vBox.getChildren().add(canvas);
        root.getChildren().add(vBox);

    }

    public void drawMap(double cellWidth, double cellHeight) {
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        gc.setFill(Color.LAVENDER);
        gc.fillRect(0 ,0, canvas.getWidth(), canvas.getHeight());

        List<Rabbit> rabbits = world.getRabbitList();
        gc.setFill(Color.YELLOWGREEN);
        for (Rabbit r : rabbits) {
            gc.fillRect((r.getPosition().getX() * cellWidth)+1, (r.getPosition().getY() * cellHeight)+1,
                    cellWidth - 2, cellHeight -2) ;
        }

        List<Fox> foxes = world.getFoxList();
        gc.setFill(Color.BLUE);
        for (Fox f : foxes) {
            gc.fillRect((f.getPosition().getX() * cellWidth)+1, (f.getPosition().getY() * cellHeight)+1,
                    cellWidth - 2, cellHeight -2) ;
        }

    }



    public Pane getVisualization() {
        return root;
    }
}
