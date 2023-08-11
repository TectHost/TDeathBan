package minealex.tdeathban;

public class VidaJugador {
	
	private String uuid;
	private int vidas;
	public VidaJugador(String uuid, int vidas) {
		super();
		this.uuid = uuid;
		this.vidas = vidas;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public int getVidas() {
		return vidas;
	}
	public void setVidas(int vidas) {
		this.vidas = vidas;
	}
	

}
