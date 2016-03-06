import processing.core.*;
public class Ejecutable extends PApplet {

	Logica lo;
	public void setup(){
		size(300,300);
		lo=new Logica(this);
	}
	public void draw(){
		background(200);
		lo.aplicacion();
	}
	
	public void mousePressed(){
		lo.click();
	}
}
