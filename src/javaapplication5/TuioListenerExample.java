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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

/**
 *
 * @author santi
 */
public class TuioListenerExample implements TuioListener{
    private Pane pane;
    private Arc semiCirculo;
    private ImageView imagen;
    private HashMap<Long, Circle> circulos = new HashMap<>();
    private HashMap<Long, Arc> semicirculos= new HashMap<>();
    
    
    public TuioListenerExample(Pane pane_) {
        this.pane = pane_;
        this.imagen = new ImageView();
        this.pane.getChildren().add(this.imagen);
    }
    
    @Override
    public void addTuioObject(TuioObject to) {
        System.out.println("Object added: " + to.getSymbolID());
        Platform.runLater(() -> {
            int id_simbolo= to.getSymbolID();
            double x = to.getX() * this.pane.getWidth();
            double y = to.getY() * this.pane.getHeight();
            
            /*
            else if(esCuadranteInferiorIzquierdo(to.getX(),to.getY())){
               mostrarSemiCirculo();
               reaccionarRotacion();
            }*/
            
            if(id_simbolo == 3){
                if(esCuadranteInferiorIzquierdo(to.getX(),to.getY()))
                    CrearSemicirculoValores(id_simbolo,x,y,to.getSessionID());
            }
            else {
                if(esCuadranteInferiorDerecho(to.getX(),to.getY()))
                    CrearCirculoNormal(id_simbolo,x,y,to.getSessionID());
            }
            

        });
    }
    ;
    
    private void mostrarSemiCirculo(){
        semiCirculo.setCenterX(this.pane.getWidth() * 0.25);
        
        
    }
    
    private boolean esCuadranteInferiorIzquierdo(double x, double y){
        return ((x<0.5)&&(y>0.5));
    }
    
    private boolean esCuadranteInferiorDerecho(double x, double y){
        return ((x>0.5) && (y> 0.5));
    }
    private void CrearSemicirculoValores(int id,double x, double y, long idSesion){
        if(id == 3){
            Arc semiCirculo = new Arc();
                semiCirculo.setCenterX(x);
                semiCirculo.setCenterY(y);
                semiCirculo.setRadiusX(20);
                semiCirculo.setRadiusY(20);
                semiCirculo.setStartAngle(0);
                semiCirculo.setLength(180);
                semiCirculo.setType(ArcType.ROUND);
                semiCirculo.setFill(Color.BLUE);
                semiCirculo.setVisible(true);//visible
                
                this.semicirculos.put(idSesion, semiCirculo);
                this.pane.getChildren().add(semiCirculo);
        }
    }
    private void CrearCirculoNormal(int id,double x, double y, long idSesion){
        Circle circulo = new Circle(x, y, 50, Color.TRANSPARENT);//creo el circulo
                circulo.setStroke(Color.RED);//color
                circulo.setStrokeWidth(3);//tamaño
                circulo.setVisible(true);//visible

                this.circulos.put(idSesion, circulo);//asigno el circulo al objeto añadido
                this.pane.getChildren().add(circulo);//agrego el circulo al pane
                
    }
    private void reaccionarSimboloSimple(int id){
        String imagenDirec;
        
        switch(id){
            case 0:
                imagenDirec = "file:C:/Users/santi/Downloads/drowzee.jpg";
                break;
            case 1:
                imagenDirec = "file:C:\\Users\\santi\\Downloads\\monoo.jpg";
                break;
            default:
                System.out.println("id desconocido: " + id);
            return;
        }
        Image img = new Image(imagenDirec);
        this.imagen.setImage(img);
        
        this.imagen.setFitWidth(200); 
        this.imagen.setFitHeight(200);
        this.imagen.setX((this.pane.getWidth() - this.imagen.getFitWidth()) / 2); // Centrar horizontalmente
        this.imagen.setY(this.pane.getHeight() * 0.1); 
        
        System.out.println("Mostrando imagen para ID " + id + ": " + this.imagen);
    }

    @Override
    public void updateTuioObject(TuioObject obj) {
        double x = obj.getX();
        double y = obj.getY();
        
        Platform.runLater(() -> {
            if((obj.getSymbolID()!= 3)&&(esCuadranteInferiorDerecho(x,y))){
                Circle circulo = circulos.get(obj.getSessionID());
                if (circulo != null) {
                    circulo.setCenterX(obj.getX() * this.pane.getWidth());
                    circulo.setCenterY(obj.getY() * this.pane.getHeight());
                }
            }else if(esCuadranteInferiorIzquierdo(x,y)){
                Arc semicirculo = semicirculos.get(obj.getSessionID());
                if(semicirculo != null){
                    semicirculo.setCenterX(obj.getX() * this.pane.getWidth());
                    semicirculo.setCenterY(obj.getY() * this.pane.getHeight());
                    reaccionarConRotacion(obj.getAngleDegrees(),semicirculo);
                    
                }
            }
        });
    
    }
    
    private void reaccionarConRotacion(float angulo, Arc semicirculo){
        
        
        if (angulo >= 0 && angulo < 120) {
                    semicirculo.setFill(Color.RED);
                } else if (angulo >= 120 && angulo < 240) {
                    semicirculo.setFill(Color.GREEN);
                } else {
                    semicirculo.setFill(Color.BLUE);
                }
    }

    @Override
    public void removeTuioObject(TuioObject obj) {
       
        long idSesion= obj.getSessionID();
         Platform.runLater(() -> {
            if(obj.getSymbolID()!=3){
                Circle circulo = this.circulos.remove(idSesion);
                if (circulo != null) {
                    this.pane.getChildren().remove(circulo);
                }
            }else{
                Arc semicirculo = this.semicirculos.remove(idSesion);
                if (semicirculo != null) {
                    this.pane.getChildren().remove(semicirculo);
                }
            }   
        }
         );
    
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
