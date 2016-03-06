import java.util.Observable;
import java.util.Observer;

import processing.core.*;

public class Logica implements Observer {

	PApplet app;
	Comunicacion c;
	int posx;
	int posy;
	int vel;
	int tam = 50;
	boolean mostrar = false;
	int id;

	public Logica(PApplet app) {
		this.app = app;
		c = new Comunicacion();
		Thread t = new Thread(c);
		t.start();

		c.addObserver(this);

	}

	public void aplicacion() {

		app.fill(255);
		app.text("Pantalla # :" + c.id, 200, 20);

		if (mostrar == true) {
			graphics();
			mover();
		}

		if (posy == app.height) {
			c.enviarPos(posx, 0, true);
			
		}
	

		//System.out.println(mostrar);
	}

	public void click() {
		// c.enviarPos(app.mouseX, app.mouseY, true);
		if (c.id == 1) {
			mostrar = true;
		}
		posx = app.mouseX;
		posy = app.mouseY;

	}

	public void graphics() {

		app.ellipse(posx, posy, tam, tam);

	}

	public void mover() {
		posy += 1;
	}

	@Override
	public void update(Observable o, Object arg) {

	
			String pos = (String) arg;
			String[] separar = arg.toString().split(":");

			posx = Integer.parseInt(separar[0]);
			posy = Integer.parseInt(separar[1]);
			mostrar = Boolean.parseBoolean(separar[2]);
	
	

	}
}
