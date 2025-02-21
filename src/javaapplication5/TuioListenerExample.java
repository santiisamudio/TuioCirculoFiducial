/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication5;

/**
 *
 * @author santi
 */
import TUIO.TuioBlob;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.HashMap;
/**
 *
 * @author santi
 */
public class TuioListenerExample implements TuioListener{
    private Pane pane;
    private HashMap<Long, Circle> circles = new HashMap<>();
    
    
    public TuioListenerExample(Pane pane_) {
        this.pane = pane_;
        System.out.println("creado");
    }
    
    @Override
    public void addTuioObject(TuioObject to) {
        System.out.println("Object added: " + to.getSymbolID());
        Platform.runLater(() -> {
            double x = to.getX() * this.pane.getWidth();
            double y = to.getY() * this.pane.getHeight();

            Circle circle = new Circle(x, y, 50, Color.TRANSPARENT);
            circle.setStroke(Color.RED);
            circle.setStrokeWidth(3);

            circles.put(to.getSessionID(), circle);
            this.pane.getChildren().add(circle);
        });
    }
    ;

    @Override
    public void updateTuioObject(TuioObject to) {
        Platform.runLater(() -> {
            Circle circle = circles.get(to.getSessionID());
            if (circle != null) {
                circle.setCenterX(to.getX() * this.pane.getWidth());
                circle.setCenterY(to.getY() * this.pane.getHeight());
            }
        });
    
    }

    @Override
    public void removeTuioObject(TuioObject to) {
         Platform.runLater(() -> {
            Circle circle = circles.remove(to.getSessionID());
            if (circle != null) {
                this.pane.getChildren().remove(circle);
            }
        });
    
    }

    @Override
    public void addTuioCursor(TuioCursor tc) {
    System.out.println("Cursor added: " + tc.getCursorID());
            }

    @Override
    public void updateTuioCursor(TuioCursor tc) {
    System.out.println("Cursor updated: " + tc.getCursorID() + " at " + tc.getX() + ", " + tc.getY());
    }

    @Override
    public void removeTuioCursor(TuioCursor tc) {}

    @Override
    public void addTuioBlob(TuioBlob tb) {}

    @Override
    public void updateTuioBlob(TuioBlob tb) {}

    @Override
    public void removeTuioBlob(TuioBlob tb) {}

    @Override
    public void refresh(TuioTime tt) {}
    
}
