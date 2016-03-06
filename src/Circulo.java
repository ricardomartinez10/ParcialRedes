import java.io.Serializable;

import processing.core.*;

public class Circulo implements Serializable {

	int posx;
	int posy;
	int id;
	boolean activo;

	public Circulo(int posx,int posy,int id,boolean activo){
		this.posx=posx;
		this.posy=posy;
		this.id=id;
		this.activo=activo;
	
	}
	
	public Circulo(int posx){
		this.posx=posx;
	}
	
	public int getPosX(){
		
		return posx;
	}
	public int getPosY(){
		return posy;
	}
	public int getID(){
		return id;
	}
	public boolean getActivo(){
		return activo;
	}


}

