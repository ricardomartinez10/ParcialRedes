import java.io.Serializable;

public class Mensaje implements Serializable{
	String saludo="";
	public Mensaje(String saludo){
		this.saludo=saludo;
		
	}
	public String getSaludo(){
		return saludo;
	}

}
