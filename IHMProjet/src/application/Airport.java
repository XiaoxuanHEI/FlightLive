package application;

public class Airport {
	private String nom;
	private String nomVille;
	private String nomPays;
	private String ICAO;
	private double latitude;
	private double longitude;
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getNomVille() {
		return nomVille;
	}
	public void setNomVille(String nomVille) {
		this.nomVille = nomVille;
	}
	public String getNomPays() {
		return nomPays;
	}
	public void setNomPays(String nomPays) {
		this.nomPays = nomPays;
	}
	public String getICAO() {
		return ICAO;
	}
	public void setIcao(String ICAO) {
		this.ICAO = ICAO;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
