import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Observable;

public class Comunicacion extends Observable implements Runnable {

	MulticastSocket ms;
	InetAddress ip;
	int port = 5000;
	int id;
	boolean esperar;

	int x, y;

	public Comunicacion() {

		esperar = true;
		try {
			ms = new MulticastSocket(port);
			ip = InetAddress.getByName("224.0.0.103");

			ms.joinGroup(ip);
			ms.setSoTimeout(300);

			saludar();
			esperarSaludo();
			System.out.println("Mi id :" + id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		while (true) {
			try {
				recibir();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void recibir() throws IOException {
		byte[] datos = new byte[1000];
		DatagramPacket dp = new DatagramPacket(datos, datos.length);
		ms.receive(dp);

		Object c = deserializar(datos);

		if (c instanceof Circulo) {
			x = ((Circulo) c).getPosX();
			y = ((Circulo) c).getPosY();
			boolean b=((Circulo)c).getActivo();
			int xid = ((Circulo) c).getID();
			
			System.out.println(xid);
			
			if(id-1==xid){
				setChanged();
				notifyObservers(x + ":" + y+":"+b);
				clearChanged();
			}
			

		}

		if (c instanceof Mensaje) {
			String t = ((Mensaje) c).getSaludo();
			if (t.startsWith("hola")) {
				contestarSaludo();
			}
		}

	}

	public void esperarSaludo() throws IOException {
		while (esperar) {

			try {
				byte[] datos = new byte[1000];
				DatagramPacket dp = new DatagramPacket(datos, datos.length);
				ms.receive(dp);
				Object c = deserializar(datos);
				if (c instanceof Mensaje) {
					String s = ((Mensaje) c).getSaludo();
					if (s.startsWith("soy:")) {
						String[] separar = s.split(":");
						int idNuevo = Integer.parseInt(separar[1]);
						if (idNuevo >= id) {
							id = idNuevo + 1;
						}

					}
				}

			} catch (SocketTimeoutException e) {
				if (id == 0) {
					id = 1;
				}
				try {
					ms.setSoTimeout(0);
				} catch (SocketException x) {
					x.printStackTrace();
				}
				esperar = false;
			}
		}

	}

	public void saludar() {
		Mensaje m = new Mensaje("hola soy nuevo");
		byte[] datos = serializar(m);
		DatagramPacket dp = new DatagramPacket(datos, datos.length, ip, port);
		try {
			ms.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void contestarSaludo() {
		Mensaje m = new Mensaje("soy:" + id);
		byte[] datos = serializar(m);
		DatagramPacket dp = new DatagramPacket(datos, datos.length, ip, port);
		try {
			ms.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public byte[] serializar(Object c) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		try {
			ObjectOutputStream os = new ObjectOutputStream(bs);
			os.writeObject(c);
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return bs.toByteArray();
	}

	public Object deserializar(byte[] datos) {
		ByteArrayInputStream bs = new ByteArrayInputStream(datos);
		Object c = null;
		try {
			ObjectInputStream os = new ObjectInputStream(bs);
			c = (Object) os.readObject();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return c;
	}

	public void enviarPos(int x, int y,boolean activo) {

		Circulo c = new Circulo(x, y, id,activo);
		byte[] datos = serializar(c);

		DatagramPacket dp = new DatagramPacket(datos, datos.length, ip, port);
		try {
			ms.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

}
