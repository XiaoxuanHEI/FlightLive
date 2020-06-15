package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Systeme {
	private ArrayList<Airport> airList;
	private ArrayList<String> paysList;
	private ArrayList<String> villeList;
	private HashMap<String,ArrayList<String>> villesD1Pays;
	private HashMap<String,ArrayList<Airport>> airD1Ville;

	public Systeme()
	{
		airList = new ArrayList<Airport>();
		paysList = new ArrayList<String>();
		villeList = new ArrayList<String>();
		villesD1Pays = new HashMap<String,ArrayList<String>>();
		airD1Ville = new HashMap<String,ArrayList<Airport>>();
	}
	
	public ArrayList<Airport> getAirList() {
		return airList;
	}

	public void setAirList(ArrayList<Airport> airList) {
		this.airList = airList;
	}
	
	public ArrayList<String> getPaysList() {
		return paysList;
	}

	public void setPaysList(ArrayList<String> paysList) {
		this.paysList = paysList;
	}

	public ArrayList<String> getVilleList() {
		return villeList;
	}

	public void setVilleList(ArrayList<String> villeList) {
		this.villeList = villeList;
	}

	public HashMap<String, ArrayList<String>> getVillesD1Pays() {
		return villesD1Pays;
	}

	public void setVillesD1Pays(HashMap<String, ArrayList<String>> villesD1Pays) {
		this.villesD1Pays = villesD1Pays;
	}
	
	public HashMap<String, ArrayList<Airport>> getAirD1Ville() {
		return airD1Ville;
	}

	public void setAirD1Ville(HashMap<String, ArrayList<Airport>> airD1Ville) {
		this.airD1Ville = airD1Ville;
	}

	public void getAirport() {
		try{
			FileReader file= new FileReader("/Users/jn/Desktop/cours/IHM/projet/airports.csv");
			BufferedReader bufRead = new BufferedReader(file);
			String line = bufRead.readLine();		
			while(line!=null) {
				String[] array = line.split(",");
				Airport airport = new Airport();
				airport.setNom(array[0]);
				airport.setNomVille(array[1]);
				airport.setNomPays(array[2]);
				airport.setIcao(array[3]);
				airport.setLatitude(Double.parseDouble(array[4]));
				airport.setLongitude(Double.parseDouble(array[5]));
				airList.add(airport);
				line = bufRead.readLine();
			}
			bufRead.close();
			file.close();
		} catch(IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void getPays() {
		for(Airport a : airList) {
			String pays = new String(a.getNomPays());
			if(!paysList.contains(pays)) {
				paysList.add(pays);
			}
		}
		Collections.sort(paysList);
	}
	
	public void getVille() {
		for(Airport a : airList) {
			String ville = new String(a.getNomVille());
			if(!villeList.contains(ville)) {
				villeList.add(ville);
			}
		}
		Collections.sort(villeList);
	}
	
	public void chargeVillesD1Pays() {
		for(String p : paysList) {
			ArrayList<String> villes = new ArrayList<String>();
			for(Airport a : airList) {
				if(a.getNomPays().equals(p)) {
					if(!villes.contains(a.getNomVille())) {
						villes.add(a.getNomVille());
					}
				}
			}
			villesD1Pays.put(p, villes);
		}
	}	
		
	public void chargeAirD1Ville() {
		for(String v : villeList) {
			ArrayList<Airport> air = new ArrayList<Airport>();
			for(Airport a : airList) {
				if(a.getNomVille().equals(v)) {
					air.add(a);
				}
			}
			airD1Ville.put(v, air);
		}
	}
	
	public ArrayList<Flight> getFlightFromAir(String from) {
		String fromCode = null; 
		ParserFlight parserFlight = new ParserFlight();
		for(Airport a : airList) {
			if(a.getNom().equals(from)) {
				fromCode = a.getICAO();
				parserFlight.getFlight(a.getICAO());
			}		
		}
		ArrayList<Flight> flightList = new ArrayList<Flight>();		
		for(Flight f : parserFlight.flights.getAcList()) {
			if(f.From != null) {
				if(f.From.substring(0,4).equals(fromCode))
					flightList.add(f);
			}
		}
//		if(!flightList.isEmpty()) {
//			result += "The Flights From " + from + ":" + "\n";
//			for(Flight f : flightList) {
//				result += "ID: " + f.Id +" Type: " + f.Type + " To: " + f.To + "\n";				
//			}
//		}
		return flightList;
	}
	
	public ArrayList<Flight> getFlightToAir(String to) {
		String toCode = null; 
		ParserFlight parserFlight = new ParserFlight();
		for(Airport a : airList) {
			if(a.getNom().equals(to)) {
				toCode = a.getICAO();
				parserFlight.getFlight(a.getICAO());
			}		
		}
		ArrayList<Flight> flightList = new ArrayList<Flight>();		
		for(Flight f : parserFlight.flights.getAcList()) {
			if(f.To != null) {
				if(f.To.substring(0,4).equals(toCode))
					flightList.add(f);
			}
		}
//		if(!flightList.isEmpty()) {
//			result += "The Flights From " + from + ":" + "\n";
//			for(Flight f : flightList) {
//				result += "ID: " + f.Id +" Type: " + f.Type + " To: " + f.To + "\n";				
//			}
//		}
		return flightList;
	}
	
	
	public ArrayList<Flight> getFlightFromToAir(String from, String to) {		
		ParserFlight parserFlight = new ParserFlight();
		String fromCode = "";
		String toCode = "";
		for(Airport a : airList) {
			if(a.getNom().equals(from))
				fromCode = a.getICAO();
			if(a.getNom().equals(to))
				toCode = a.getICAO();
		}
		parserFlight.getFlight(fromCode);
		ArrayList<Flight> flightList = new ArrayList<Flight>();	
		for(Flight f : parserFlight.flights.getAcList()) {
			if(f.From != null && f.To != null) {
				if(f.From.substring(0,4).equals(fromCode) && f.To.substring(0,4).equals(toCode)) {				
					flightList.add(f);
				}
			}
		}
//		if(!flightList.isEmpty()) {
//			result += "The Flights From " + from;			
//			result += " To " + to + ": " + "\n";
//			for(Flight f : flightList) {
//				result += "ID: " + f.Id +" Type: " + f.Type + "\n";				
//			}					
//		}
		return flightList;
	}
	
	public String getFlightFromVille(String from) {		
		ParserFlight parserFlight = new ParserFlight();
		String fromCode = "";
		for(String v : villeList) {
			for(Airport a : airList) {
				if(a.getNomVille().equals(v))
					fromCode = a.getICAO();									
			}
		}
		parserFlight.getFlight(fromCode);
		String result = "";
		ArrayList<Flight> flightList = new ArrayList<Flight>();		
		for(Flight f : parserFlight.flights.getAcList()) {
			if(f.From != null) {
				if(f.From.substring(5,f.From.indexOf(",")).equals(from)){
					flightList.add(f);					
				}
			}
		}
		if(!flightList.isEmpty()) {
			result += "The Flights From " + from + ":" + "\n";
			for(Flight f : flightList) {
				result += "ID: " + f.Id +" Type: " + f.Type + " To: " + f.To + "\n";				
			}
		}	
		return result;
	}
	
	public String getFlightToVille(String to) {		
		ParserFlight parserFlight = new ParserFlight();
		String toCode = "";
		for(String v : villeList) {
			for(Airport a : airList) {
				if(a.getNomVille().equals(v))
					toCode = a.getICAO();									
			}
		}
		parserFlight.getFlight(toCode);
		String result = "";
		ArrayList<Flight> flightList = new ArrayList<Flight>();		
		for(Flight f : parserFlight.flights.getAcList()) {
			if(f.To != null) {
				if(f.To.substring(5,f.To.indexOf(",")).equals(to)){
					flightList.add(f);					
				}
			}
		}
		if(!flightList.isEmpty()) {
			result += "The Flights To " + to + ":" + "\n";
			for(Flight f : flightList) {
				result += "ID: " + f.Id +" Type: " + f.Type + " From: " + f.From + "\n";				
			}
		}	
		return result;
	}
		
	public String getFlightFromToVille(String from, String to) {		
		ParserFlight parserFlight = new ParserFlight();
		String fromCode = "";
		for(Airport a : airList) {
			if(a.getNomVille().equals(from))
				fromCode = a.getICAO();
		}
		parserFlight.getFlight(fromCode);
		String result = "";
		ArrayList<Flight> flightList = new ArrayList<Flight>();	
		for(Flight f : parserFlight.flights.getAcList()) {
			if(f.From != null && f.To != null) {
				if(f.From.substring(5,f.To.indexOf(",")).equals(from) && f.To.substring(5,f.To.indexOf(",")).equals(to)) {				
					flightList.add(f);
				}
			}
		}
		if(!flightList.isEmpty()) {
			result += "The Flights From " + from;			
			result += " To " + to + ": " + "\n";
			for(Flight f : flightList) {
				result += "ID: " + f.Id +" Type: " + f.Type + "\n";				
			}					
		}
		return result;
	}
	
	public String getPosition(String s) {
		ParserFlight parserFlight = new ParserFlight();
		parserFlight.getPositionD1Vol();
		String result = "";
		for(Flight f : parserFlight.flights.getAcList()) {
			if(f.Id == Integer.parseInt(s)) {
				result += "Longitude: " + f.Long + " Latitude: " + f.Lat + "\n";
			}
		}
		return result;
	}
	
	public String getFlightsIn(double longitude, double latitude) {
		ParserFlight parserFlight = new ParserFlight();
		parserFlight.getPositionD1Vol();
		String result = "";
		ArrayList<Flight> flightList = new ArrayList<Flight>();
		for(Flight f : parserFlight.flights.getAcList()) {
			if((f.Long-longitude)*(f.Long-longitude)+(f.Lat-latitude)*(f.Lat-latitude)<100) {
				result += "ID: " + f.Id +" Type: " + f.Type + " From: "+ f.From +" To: " + f.To +"\n";				
				flightList.add(f);
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		Systeme s = new Systeme();
		s.getAirport();
		s.getPays();
		s.getVille();
//		s.chargeVillesD1Pays();
//		s.chargeAirD1Ville();
//		System.out.println(s.getFlightFromAir("KDCA"));
//		s.getFlightToAir();
//		System.out.println(s.getFlightFromToAir());
//		s.getFlightFromToAir("KDCA");
//		System.out.println(s.getFlightFromVille());
//		System.out.println(s.getFlightToVille());
//		s.getFlightFromToVille();

	}
}
