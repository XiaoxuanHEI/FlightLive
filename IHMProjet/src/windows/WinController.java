package windows;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import application.Airport;
import application.Flight;
import application.Systeme;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.*;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class WinController implements Initializable {

	@FXML
	private Pane pane3D;
	
	@FXML
	private ComboBox<String> paysdepart;
	
	@FXML
	private ComboBox<String> villedepart;
	
	@FXML
	private ComboBox<String> airportdepart;
	
	@FXML
	private ComboBox<String> paysarriver;
	
	@FXML
	private ComboBox<String> villearriver;
	
	@FXML
	private ComboBox<String> airportarriver;
	
	@FXML
	private Button myCheck;
	
	@FXML
	private Button myShow;
	
	@FXML
	private Button myClear;
	
	@FXML
	private Button myTrajet;
	
	@FXML
	private ListView<String> myList;
	
	@FXML
	private ListView<String> myDepart;
	
	@FXML
	private ListView<String> myArriver;
	
	@FXML
	private TextArea myArea;
	
	

	private static final float TEXTURE_LAT_OFFSET = -0.2f;
	private static final float TEXTURE_LON_OFFSET = 2.8f;
	
	static ArrayList<Flight> flightFrom = new ArrayList<>();
	static ArrayList<Flight> flightTo = new ArrayList<>();
	
	static Flight flightFromSelectionne = new Flight();
	static Flight flightToSelectionne = new Flight();
	static Flight flightFromSelectionne1 = new Flight();
	static Flight flightToSelectionne1 = new Flight();
	
	
	static Sphere partir = new Sphere();
	static Sphere arriver = new Sphere();
	static Sphere planeFromHighLight = new Sphere();
	static Sphere planeToHighLight = new Sphere();
	
	static Group planeFrom = new Group();
	static Group planeTo = new Group();
	static Group allTowns = new Group();
	
	static boolean ifpayspartirselected = false;
	static boolean ifpaysarriverselected = false;
	
	static boolean canSelect = false;
	
	static Boolean fois = true;
	static Boolean fois1 = true;
	

	@Override
	public void initialize(URL location,ResourceBundle resource) {
		
		Systeme systeme = new Systeme();
		systeme.getAirport();
		systeme.getPays();
		systeme.getVille();
		systeme.chargeVillesD1Pays();
		systeme.chargeAirD1Ville();
		
		myTrajet.setDisable(true);
		
		for(String pays: systeme.getPaysList()) {
			paysdepart.getItems().add(pays);
			paysarriver.getItems().add(pays);
		}
		
		paysdepart.setOnAction(event -> {
			ifpayspartirselected = false;
			villedepart.getItems().clear();
			airportdepart.getItems().clear();
			ifpayspartirselected = true;
			
			for(String ville: systeme.getVillesD1Pays().get(paysdepart.getValue())) {
				villedepart.getItems().add(ville);
			}	
		});
		
		villedepart.setOnAction(event -> {
			if(ifpayspartirselected) {
				airportdepart.getItems().clear();
			
				for(Airport airport: systeme.getAirD1Ville().get(villedepart.getValue())) {
					airportdepart.getItems().add(airport.getNom());	
				}	
			}
		});
				
		paysarriver.setOnAction(event -> {
			ifpaysarriverselected = false;
			villearriver.getItems().clear();
			ifpaysarriverselected = true;
			for(String ville: systeme.getVillesD1Pays().get(paysarriver.getValue())) {
				villearriver.getItems().add(ville);	
			}	
		});
		
		villearriver.setOnAction(event -> {
			if(ifpaysarriverselected){
				airportarriver.getItems().clear();
				for(Airport airport: systeme.getAirD1Ville().get(villearriver.getValue())) {
					airportarriver.getItems().add(airport.getNom());	
				}	
			}
		});	
		
		//la partie 3D
        //Create a Pane et graph scene root for the 3D content
        Group root3D = new Group();
        
        // Load geometry
        ObjModelImporter objImporter = new ObjModelImporter();
        try {
        	objImporter.read("C:/Users/jn/eclipse-workspace/Airport/src/application/Earth/earth.obj");
        }catch(ImportException e) {
        	System.out.println(e.getMessage());
        }
        MeshView[] meshViews = objImporter.getImport();
        Group earth = new Group(meshViews);
        root3D.getChildren().add(earth);

        // Draw city on the earth
		airportdepart.setOnAction(event -> {
			earth.getChildren().remove(partir);
			for(Airport air : systeme.getAirList()) {
				if(air.getNom().equals(airportdepart.getValue())) {
					displayTownDepart(earth, air.getLatitude(), air.getLongitude());	
				}
			}
		});
		
		airportarriver.setOnAction(event -> {
			earth.getChildren().remove(arriver);
			for(Airport air : systeme.getAirList()) {
				if(air.getNom().equals(airportarriver.getValue())) {
					displayTownArriver(earth, air.getLatitude(), air.getLongitude());	
				}
			}
		});
		
		myCheck.setOnAction(event ->{
			earth.getChildren().remove(planeFromHighLight);
			earth.getChildren().remove(planeToHighLight);
			setOn3DMap(earth, systeme);
			myList.getItems().add("From : "+paysdepart.getValue()+" - "+villedepart.getValue()+" - "+airportdepart.getValue());
			myList.getItems().add("To : "+paysarriver.getValue()+" - "+villearriver.getValue()+" - "+airportarriver.getValue());
			if(!fois) {
				flightFromSelectionne1 = flightFromSelectionne;
				for(Flight flight: flightFrom) {
					if(flight.Id == flightFromSelectionne.Id) {
						flightFromSelectionne = flight;
					}
				}
			}
			if(!fois1) {
				flightToSelectionne1 = flightToSelectionne;
				for(Flight flight: flightFrom) {
					if(flight.Id == flightToSelectionne.Id) {
						flightToSelectionne = flight;
					}
				}
			}
		});
		
		myDepart.setOnMouseClicked(event ->{
			fois = true;
			earth.getChildren().remove(planeFromHighLight);
			String planechoisi = myDepart.getFocusModel().getFocusedItem();
			
			for(Flight flight: flightFrom) {
				if(planechoisi.equals("Id : "+flight.Id+" Type : "+flight.Type)) {
					onePlaneFrom(earth, flight.Lat, flight.Long);
					myArea.setText(showFlightData(flight,systeme));
					if(fois) {
						flightFromSelectionne = flight;
						fois = false;
						myTrajet.setDisable(false);
					}
					
				}
			}
		});
		
		myArriver.setOnMouseClicked(event ->{
			fois1 = true;
			Boolean fois = false;
			earth.getChildren().remove(planeToHighLight);
			String planechoisi = myArriver.getFocusModel().getFocusedItem();
			
			for(Flight flight: flightTo) {
				if(planechoisi.equals("Id : "+flight.Id+" Type : "+flight.Type)) {
					onePlaneTo(earth, flight.Lat, flight.Long);
					flightToSelectionne = flight;
					myArea.setText(showFlightData(flight,systeme));
					if(fois) {
						flightToSelectionne = flight;
						fois = false;
						myTrajet.setDisable(false);
					}
				}
			}
		});
		
		myShow.setOnAction(event ->{
			for(Airport air : systeme.getAirList()) {
				displayAllTown(air.getLatitude(), air.getLongitude());
			}
			earth.getChildren().add(allTowns);
		});
		
		myClear.setOnAction(event ->{
			earth.getChildren().remove(allTowns);
		});
		
		myTrajet.setOnAction(event ->{
			Point3D selectfrom = geoCoordTo3dCoord(flightFromSelectionne.Lat,flightFromSelectionne.Long);
			Point3D selectfrom1 = geoCoordTo3dCoord(flightFromSelectionne1.Lat,flightFromSelectionne1.Long);
			Point3D selectto = geoCoordTo3dCoord(flightToSelectionne.Lat,flightToSelectionne.Long);
			Point3D selectto1 = geoCoordTo3dCoord(flightToSelectionne1.Lat,flightToSelectionne1.Long);
			if(!flightFromSelectionne1.From.equals(null)) {
				Cylinder line = createLine(selectfrom,selectfrom1);
				root3D.getChildren().add(line);
			}
			if(!flightToSelectionne1.From.equals(null)) {
				Cylinder line = createLine(selectto,selectto1);
				root3D.getChildren().add(line);
			}
		});
		
        // Add a camera group
        Camera camera = new PerspectiveCamera(true);
        new CameraManager(camera, pane3D, root3D);

        // Add point light
        PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);

        // Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);

        // Create scene
        SubScene subscene = new SubScene(root3D, 660, 700, true,SceneAntialiasing.BALANCED);
        subscene.setCamera(camera);
        subscene.setFill(Color.gray(0.01));
        pane3D.getChildren().addAll(subscene);	
	}
	
	
	 public Cylinder createLine(Point3D origin, Point3D target) {
	        Point3D yAxis = new Point3D(0, 1, 0);
	        Point3D diff = target.subtract(origin);
	        double height = diff.magnitude();

	        Point3D mid = target.midpoint(origin);
	        Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

	        Point3D axisOfRotation = diff.crossProduct(yAxis);
	        double angle = Math.acos(diff.normalize().dotProduct(yAxis));
	        Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

	        Cylinder line = new Cylinder(0.003f, height);

	        line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

	        return line;
	    }

	    public static Point3D geoCoordTo3dCoord(double lat, double lon) {
	        double lat_cor = lat + TEXTURE_LAT_OFFSET;
	        double lon_cor = lon + TEXTURE_LON_OFFSET;
	        return new Point3D(
	                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
	                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
	                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
	                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
	                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
	    }

	    public static void onePlaneFrom(Group parent, double latitude, double longitude) {
	    	Sphere sphere = new Sphere(0.009);
	    	yellowHighLightMaterial(sphere);
	    	Point3D position = geoCoordTo3dCoord(latitude,longitude);
	    	sphere.setTranslateX(position.getX());
	    	sphere.setTranslateY(position.getY());
	    	sphere.setTranslateZ(position.getZ());
	    	parent.getChildren().add(sphere);
	    	planeFromHighLight = sphere;
	    }
	    
	    public static void onePlaneTo(Group parent, double latitude, double longitude) {
	    	Sphere sphere = new Sphere(0.009);
	    	redHighLightMaterial(sphere);
	    	Point3D position = geoCoordTo3dCoord(latitude,longitude);
	    	sphere.setTranslateX(position.getX());
	    	sphere.setTranslateY(position.getY());
	    	sphere.setTranslateZ(position.getZ());
	    	parent.getChildren().add(sphere);
	    	planeToHighLight = sphere;
	    }
	    
	    public static void showPlaneFrom(Group parent, double latitude, double longitude) {
	    	
	    	Sphere sphere = new Sphere(0.005);
	    	yellowMaterial(sphere);
	    	Point3D position = geoCoordTo3dCoord(latitude,longitude);
	    	sphere.setTranslateX(position.getX());
	    	sphere.setTranslateY(position.getY());
	    	sphere.setTranslateZ(position.getZ());
	    	planeFrom.getChildren().add(sphere);	    	
	    }
	    
	    public static void showPlaneTo(Group parent, double latitude, double longitude) {
	    	
	    	Sphere sphere = new Sphere(0.005);
	    	redMaterial(sphere);
	    	Point3D position = geoCoordTo3dCoord(latitude,longitude);
	    	sphere.setTranslateX(position.getX());
	    	sphere.setTranslateY(position.getY());
	    	sphere.setTranslateZ(position.getZ());
	    	planeTo.getChildren().add(sphere);	    	
	    }
	    
	    public static void displayAllTown(double latitude, double longitude) {
	    	Sphere sphere = new Sphere(0.003);
	    	whiteMaterial(sphere);
	    	Point3D position = geoCoordTo3dCoord(latitude,longitude);
	    	sphere.setTranslateX(position.getX());
	    	sphere.setTranslateY(position.getY());
	    	sphere.setTranslateZ(position.getZ());
	    	allTowns.getChildren().add(sphere);
	    }
	    
	    public static void displayTownDepart(Group parent, double latitude, double longitude) {
	    	Sphere sphere = new Sphere(0.01);
	    	yellowMaterial(sphere);
	    	Point3D position = geoCoordTo3dCoord(latitude,longitude);
	    	sphere.setTranslateX(position.getX());
	    	sphere.setTranslateY(position.getY());
	    	sphere.setTranslateZ(position.getZ());
	    	parent.getChildren().add(sphere);
	    	partir = sphere;
	    }
	    
	    public static void displayTownArriver(Group parent, double latitude, double longitude) {
	    	Sphere sphere = new Sphere(0.01);
	    	redMaterial(sphere);
	    	Point3D position = geoCoordTo3dCoord(latitude,longitude);
	    	sphere.setTranslateX(position.getX());
	    	sphere.setTranslateY(position.getY());
	    	sphere.setTranslateZ(position.getZ());
	    	parent.getChildren().add(sphere);
	    	arriver = sphere;
	    }
	    
	    public static void redHighLightMaterial(Sphere sphere) {
	        //Add material
	        final PhongMaterial Material = new PhongMaterial();
	        Material.setDiffuseColor(Color.ORANGERED);
			Material.setSpecularColor(Color.ORANGERED);
			sphere.setMaterial(Material);
	    }
	    
	    public static void redMaterial(Sphere sphere) {
	        //Add material
	        final PhongMaterial redMaterial = new PhongMaterial();
	    	redMaterial.setDiffuseColor(Color.RED);
			redMaterial.setSpecularColor(Color.RED);
			sphere.setMaterial(redMaterial);
	    }
	    
	    public static void yellowHighLightMaterial(Sphere sphere) {
	        //Add material
	        final PhongMaterial Material = new PhongMaterial();
	        Material.setDiffuseColor(Color.YELLOWGREEN);
			Material.setSpecularColor(Color.YELLOWGREEN);
			sphere.setMaterial(Material);
	    }
	    
	    public static void yellowMaterial(Sphere sphere) {
	        //Add material
	        final PhongMaterial yellowMaterial = new PhongMaterial();
	    	yellowMaterial.setDiffuseColor(Color.YELLOW);
			yellowMaterial.setSpecularColor(Color.YELLOW);
			sphere.setMaterial(yellowMaterial);
	    }
	    
	    public static void whiteMaterial(Sphere sphere) {
	        //Add material
	        final PhongMaterial whiteMaterial = new PhongMaterial();
	    	whiteMaterial.setDiffuseColor(Color.WHITE);
			whiteMaterial.setSpecularColor(Color.WHITE);
			sphere.setMaterial(whiteMaterial);
	    }
	    
	    public String showFlightData(Flight flight, Systeme systeme) {
	    	String toAirport = "";
	    	String fromAirport = "";
	    	for(Airport airport : systeme.getAirList()) {
	    		if(airport.getICAO().equals(flight.To.substring(0, 4))) {
	    			toAirport = airport.getNom();
	    		}
	    		if(airport.getICAO().equals(flight.From.substring(0, 4))) {
	    			fromAirport = airport.getNom();
	    		}
	    	}
	    	String  flightdata = "";
	    	flightdata += "id : "+flight.Id +"\n";
	    	flightdata += "From : "+fromAirport+"\n";
	    	flightdata += "To : "+toAirport+"\n";
	    	flightdata += "Type : "+flight.Type +"\n";
	    	flightdata += "Speed : "+flight.Spd +"\n";
	    	flightdata += "Trak : "+flight.Trak +"\n";
	    	flightdata += "latitude : "+flight.Lat +"\n";
	    	flightdata += "longitude : "+flight.Long +"\n";
	    	return flightdata;
	    }
	
	    public void setOn3DMap(Group earth, Systeme systeme) {
	    	flightFrom.clear();
	    	flightTo.clear();
	    	myList.getItems().clear();
			myDepart.getItems().clear();
			myArriver.getItems().clear();
			
			earth.getChildren().remove(planeFrom);
			planeFrom.getChildren().clear();
			earth.getChildren().remove(planeTo);
			planeTo.getChildren().clear();
			
			for(Flight flight: systeme.getFlightFromAir(airportdepart.getValue())) {
				myDepart.getItems().add("Id : "+flight.Id+" Type : "+flight.Type);
				flightFrom.add(flight);
				showPlaneFrom(earth,flight.Lat,flight.Long);

			}
			earth.getChildren().add(planeFrom);
			
			for(Flight flight: systeme.getFlightToAir(airportarriver.getValue())) {
				myArriver.getItems().add("Id : "+flight.Id+" Type : "+flight.Type);
				flightTo.add(flight);
				showPlaneTo(earth,flight.Lat,flight.Long);
			}
			earth.getChildren().add(planeTo);
			
			for(Flight flight: systeme.getFlightFromToAir(airportdepart.getValue(),airportarriver.getValue())) {
				myList.getItems().add("Id : "+flight.Id+" Type : "+flight.Type);
			}
	    }
}
	
