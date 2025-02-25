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
public class ListenerTuio implements TuioListener{
    private Pane _contenedor;//contenedor
    private Arc _semiCirculo;//arco para usar como semicirculo
    private ImageView _imagen;//sirve para mostrar imagenes
    private HashMap<Long, Circle> _circulos = new HashMap<>();
    private HashMap<Long, Arc> _semicirculos= new HashMap<>();
    
    
    public ListenerTuio(Pane contenedor) {
        this._contenedor = contenedor;
        this._imagen = new ImageView();
        this._contenedor.getChildren().add(this._imagen);//se agrega la imagen al contenedor
    }
    
    @Override
    public void addTuioObject(TuioObject to) {
        System.out.println("Object added: " + to.getSymbolID());
        Platform.runLater(() -> {/*javaFX solo permite actualizar la UI desde si propio thread para evitar errores, esta linea hace que el codigo se ejecute en el thread principal,
                                    TUIO ejecuta las funciones desde un thread secundario*/
            int id_simbolo= to.getSymbolID();
            double x = to.getX() * this._contenedor.getWidth();/*TUIO usa coordenadas de 0.0(arriba izquierda) a 1.1(abajo derecha) el contenedor de javafx tiene como coordenadas
                                                                    pixeles, para convertirlo se multiplican, si x=0.5 , 0.5 * 800 pixeles = 400*/
            double y = to.getY() * this._contenedor.getHeight();
                     
            
            if(id_simbolo == 3){//el simbolo fiducial 3 se usa para establecer los valores segun su angulo en grados
                if(esCuadranteInferiorIzquierdo(to.getX(),to.getY()))//se pasa la ubicacion del objeto TUIO para ver si se encuentra abajo a la izquierda
                    CrearSemicirculoValores(id_simbolo,x,y,to.getSessionID());//crea el semicirculo en donde se encuentra el simbolo fiducial
            }
            else {
                if(esCuadranteInferiorDerecho(to.getX(),to.getY()))//se pasa la ubicacion del objeto TUIO para ver si se encuentra abajo a la derecha
                    CrearCirculoNormal(id_simbolo,x,y,to.getSessionID());//crea el semicirculo en donde se encuentra el simbolo fiducial
            }
            

        });
    }
    ;
    
    private boolean esCuadranteInferiorIzquierdo(double x, double y){
        return ((x<0.5)&&(y>0.5));
    }
    
    private boolean esCuadranteInferiorDerecho(double x, double y){
        return ((x>0.5) && (y> 0.5));
    }
    
    private void CrearSemicirculoValores(int id,double x, double y, long idSesion){
        if(id == 3){
            Arc semiCirculo = new Arc();
                semiCirculo.setCenterX(x);//se ubica el semicirculo en la ubicacion del simbolo fiducial
                semiCirculo.setCenterY(y);
                semiCirculo.setRadiusX(20);//se define el tamaño
                semiCirculo.setRadiusY(20);
                semiCirculo.setStartAngle(0);//se define como 0 el angulo en donde comienza el semicirculo
                semiCirculo.setLength(180);//cuanto se extiende el semicirculo en grados
                semiCirculo.setType(ArcType.ROUND);//el arco se cierra con borde redondeado
                semiCirculo.setFill(Color.BLUE);//color por defecto
                semiCirculo.setVisible(true);//visible
                
                this._semicirculos.put(idSesion, semiCirculo);//se asigna al semicirculo el sessionid del objeto que se esta mostrando
                this._contenedor.getChildren().add(semiCirculo);//se agrega al contenedor pane el semicirculo
        }
    }
    private void CrearCirculoNormal(int id,double x, double y, long idSesion){
        Circle circulo = new Circle(x, y, 50, Color.TRANSPARENT);//se crea el circulo
                circulo.setStroke(Color.RED);//color del borde
                circulo.setStrokeWidth(3);//tamaño del borde
                circulo.setVisible(true);//visible

                this._circulos.put(idSesion, circulo);//asigno el circulo al objeto añadido
                this._contenedor.getChildren().add(circulo);//agrego el circulo al pane
                
    }
    private void reaccionarSimboloSimple(int id){//funcion para detectar los simbolos y cambiar las imagenes dependiendo del mismo
        String imagenDirec;
        
        switch(id){//segun el id del simbolo se muestra una imagen distinta
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
        this._imagen.setImage(img);//cargo la imagen al imageview
        
        this._imagen.setFitWidth(200); //asigno ancho
        this._imagen.setFitHeight(200);//asigno alto
        this._imagen.setX((this._contenedor.getWidth() - this._imagen.getFitWidth()) / 2); /* se centra horizontalmente y se elimina el tamaño de la imagen, 
                                                                                            al dividir esto por 2 queda centrada la imagen*/
        this._imagen.setY(this._contenedor.getHeight() * 0.1); //posiciona la imagen verticalmente cerca del borde superior
        
        System.out.println("Mostrando imagen para ID " + id + ": " + this._imagen);
    }

    @Override
    public void updateTuioObject(TuioObject obj) {//esta funcion actualiza automaticamente el objeto cuando se detecta movimiento en el mismo. la maneja TUIO internamente
        double x = obj.getX();
        double y = obj.getY();
        
        Platform.runLater(() -> {// al actualizar la UI de javafx se debe pasar al thread principal
            if((obj.getSymbolID()!= 3)&&(esCuadranteInferiorDerecho(x,y))){//solo se actualiza dentro del cuadrante abajo derecha, sino no se muestra el simbolo
                Circle circulo = this._circulos.get(obj.getSessionID());//se busca el circulo correspondiente al objeto
                if (circulo != null) {//verifica que el objeto exista 
                    circulo.setCenterX(obj.getX() * this._contenedor.getWidth());// se actualiza x
                    circulo.setCenterY(obj.getY() * this._contenedor.getHeight());//se actualiza y
                }
            }else if(esCuadranteInferiorIzquierdo(x,y)){//solo se actualiza dentro del cuadrante abajo izquierda, sino no se muestra el simbolo
                Arc semicirculo = this._semicirculos.get(obj.getSessionID());
                if(semicirculo != null){//verifica que el objeto exista 
                    semicirculo.setCenterX(obj.getX() * this._contenedor.getWidth());//se actualiza x
                    semicirculo.setCenterY(obj.getY() * this._contenedor.getHeight());//se actualiza y
                    reaccionarConRotacion(obj.getAngleDegrees(),semicirculo);//actualiza rotacion
                    
                }
            }
        });
    
    }
    
    private void reaccionarConRotacion(float angulo, Arc semicirculo){
        
        //dependiendo el angulo cambia el color
        if (angulo >= 0 && angulo < 120) {
                    semicirculo.setFill(Color.RED);
                } else if (angulo >= 120 && angulo < 240) {
                    semicirculo.setFill(Color.GREEN);
                } else {
                    semicirculo.setFill(Color.BLUE);
                }
    }

    @Override
    public void removeTuioObject(TuioObject obj) {// un objeto se elimina automaticamente cuando desaparece del campo visual
       
        long idSesion= obj.getSessionID();
         Platform.runLater(() -> {
            if(obj.getSymbolID()!=3){
                Circle circulo = this._circulos.remove(idSesion);//elimina el circulo del hashmap
                if (circulo != null) {
                    this._contenedor.getChildren().remove(circulo);//elimina al circulo del contenedor pane
                }
            }else{
                Arc semicirculo = this._semicirculos.remove(idSesion);//elimina el semicirculo del hashmap
                if (semicirculo != null) {
                    this._contenedor.getChildren().remove(semicirculo);//elimina al circulo del contenedor pane
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
