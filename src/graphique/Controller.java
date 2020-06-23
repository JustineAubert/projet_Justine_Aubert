package graphique;



import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import application.Search;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import javafx.event.ActionEvent;


public class Controller implements Initializable {
	@FXML
	private Pane pane3D;
	@FXML
	private Slider sliderA;
	@FXML
	private TextField textA;
	@FXML
	private RadioButton deuxD;
	@FXML
	private RadioButton quadri;
	@FXML
	private RadioButton histo;
	@FXML
	private Button play;
	@FXML
	private Button pause;
	@FXML
	private Button stop;
	@FXML
	private TextField textVitt;

	private PhongMaterial darkred;
	private PhongMaterial red;
	private PhongMaterial orange;
	private PhongMaterial white;
	private PhongMaterial lightgreen;
	private PhongMaterial green;
	private PhongMaterial blue;
	private PhongMaterial darkblue;
	private PhongMaterial yellow;
	private PhongMaterial black;
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
	private static final float TEXTURE_LON_OFFSET = 2.8f;

	final Duration timeout = Duration.seconds(30);
	ExecutorService executor = Executors.newSingleThreadExecutor();
	private List<MeshView> earthMeshViews = new ArrayList<>();

	Timeline timeline = new Timeline();
	Boolean isStop = false;

	Group earth;
	
	//sert a supprimer les versions precedentes de visionnage
	public void clearQuadriMesh() {
		earth.getChildren().removeAll(earthMeshViews);
		earthMeshViews.clear();
	}
	//sert a supprimer les versions precedentes de visionnage
	public void clearHistoMesh() {
		earth.getChildren().removeIf(
				Cylinder.class::isInstance
		);
	}
	//prepare les couleurs pour apres, autant pour histogramme que pour quadrillage
	public void prepareColor() {
		darkred = new PhongMaterial();
		darkred.setDiffuseColor(Color.DARKRED);
		darkred.setSpecularColor(Color.DARKRED);

		 red = new PhongMaterial();
		red.setDiffuseColor(Color.RED);
		red.setSpecularColor(Color.RED);

		orange = new PhongMaterial();
		orange.setDiffuseColor(Color.ORANGE);
		orange.setSpecularColor(Color.ORANGE);

		yellow = new PhongMaterial();
		yellow.setDiffuseColor(Color.YELLOW);
		yellow.setSpecularColor(Color.YELLOW);

		white = new PhongMaterial();
		white.setDiffuseColor(Color.WHITE);
		white.setSpecularColor(Color.WHITE);

		lightgreen = new PhongMaterial();
		lightgreen.setDiffuseColor(Color.LIGHTGREEN);
		lightgreen.setSpecularColor(Color.LIGHTGREEN);

		green = new PhongMaterial();
		green.setDiffuseColor(Color.GREEN);
		green.setSpecularColor(Color.GREEN);

		blue = new PhongMaterial();
		blue.setDiffuseColor(Color.BLUE);
		blue.setSpecularColor(Color.BLUE);

		darkblue = new PhongMaterial();
		darkblue.setDiffuseColor(Color.DARKBLUE);
		darkblue.setSpecularColor(Color.DARKBLUE);

		black = new PhongMaterial();
		black.setDiffuseColor(Color.BLACK);
		black.setSpecularColor(Color.BLACK);
	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

		try {
           //servait pour avoir certaines valeurs affichees pour la verification
//			System.out.println(Search.getInstance().findByYear(1952));
//			System.out.println(Search.getInstance().findByYearAndByLongAndLat(1881, "12", "154"));
//			System.out.println(Search.getInstance().getTempMax());
//			System.out.println(Search.getInstance().getTempMin());
//			System.out.println(Search.getInstance().findByLongAndLat("12","154"));
			System.out.println("dxjn");
			sliderA.setValue(Search.getInstance().getIndexOfYear().get(0));
			textA.setText(String.valueOf(Search.getInstance().getIndexOfYear().get(0)));
			textVitt.setText("1");
			prepareColor();

			final ToggleGroup option = new ToggleGroup();
			deuxD.setToggleGroup(option);
			quadri.setToggleGroup(option);
			histo.setToggleGroup(option);

			textA.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					sliderA.valueProperty().setValue(Double.valueOf(newValue));
				}
			});

			final Group root3D = new Group();
			
			// Load earth
			ObjModelImporter objImporter = new ObjModelImporter();
			System.out.println("1");
			URL modelUrl = this.getClass().getResource("Earth/earth.obj");
			objImporter.read(modelUrl);
			System.out.println("2");
			MeshView[] meshViews = objImporter.getImport();
			earth = new Group(meshViews);

			//essaie pour le tutoriel, permet de se reperer dans l espace 
			Group citys = new Group();
			displayTown(citys, "Brest", 48.447911f, -4.418539f);
			displayTown(citys, "Marseille", 43.43f, 5.21f);
			displayTown(citys, "NewYork", 40.43f, -73.77f);
			displayTown(citys, "CapeTown", -33.9f, 18.601f);
			displayTown(citys, "Istanbul", 40.976f, 28.814f);
			displayTown(citys, "Reykjavik", 64.132f, -21.9405f);
			displayTown(citys, "Singapore", 1.3501f, 103.9944f);
			displayTown(citys, "Seoul", 37.46f, 126.45f);

			root3D.getChildren().addAll(citys);
			
			//commande pour les buttons play stop pause
			play.addEventHandler(MouseEvent.MOUSE_CLICKED, this.startButton());
			stop.addEventHandler(MouseEvent.MOUSE_CLICKED, this.stopButton());
			pause.addEventHandler(MouseEvent.MOUSE_CLICKED, this.pauseButton());
			
			//la partie 2D n a pas ete achevee mais lorsque le bouton 2D est selectionne la sphere se nettoie comme voulue 
			deuxD.setOnAction(e -> {
				if (deuxD.isSelected()) {
					clearQuadriMesh();
					clearHistoMesh();
					sliderA.valueProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observableValue, Number oldValue,
											Number newValue) {
								textA.textProperty().setValue(String.valueOf(newValue.intValue()));
						}
					});
				}
			});
			
			sliderA.valueProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number oldValue,
									Number newValue) {
						textA.textProperty().setValue(String.valueOf(newValue.intValue()));
				}
			});

			//permet de faire les actions qui dessinent les quadrillages en fonction de la valeur du slider, 
			quadri.setOnAction((e) -> {

					if (quadri.isSelected()) {
						clearQuadriMesh();
						clearHistoMesh();
						drawQuadrilateral(earth);
						sliderA.valueProperty().addListener(new ChangeListener<Number>() {
							@Override
							public void changed(ObservableValue<? extends Number> observableValue, Number oldValue,
												Number newValue) {
								if (quadri.isSelected()) {
									textA.textProperty().setValue(String.valueOf(newValue.intValue()));
									clearQuadriMesh();
									drawQuadrilateral(earth);
								}
							}
						});
							
					}
				});

			//permet de faire les actions histogramme quand le button histo est selectionné va se mettre à jour en fonction de la valeur du slider
			
				histo.setOnAction((e) -> {

					if (histo.isSelected()) {
						clearQuadriMesh();
						clearHistoMesh();
						drawHisto(earth);
						sliderA.valueProperty().addListener(new ChangeListener<Number>() {
							@Override
							public void changed(ObservableValue<? extends Number> observableValue, Number oldValue,
												Number newValue) {
								if (histo.isSelected()) {
									textA.textProperty().setValue(String.valueOf(newValue.intValue()));
									clearHistoMesh();
									drawHisto(earth);
								}
							}
						});

					}
				});

			// Add a camera group
			final PerspectiveCamera camera = new PerspectiveCamera(true);
			Group cameraGroup = new Group(camera);
			root3D.getChildren().add(cameraGroup);

			new CameraManager(camera, pane3D, root3D);  // permet à la sphere de tourner sur elle-meme avec la souris

			// d ambient light
			AmbientLight ambientLight = new AmbientLight(Color.WHITE);
			ambientLight.getScope().addAll(root3D);
			root3D.getChildren().add(ambientLight);

			root3D.getChildren().add(earth);
			

			SubScene subscene = new SubScene(root3D, 500, 500, true, SceneAntialiasing.BALANCED);
			subscene.setCamera(camera);
			subscene.setFill(Color.GREY);
			pane3D.getChildren().addAll(subscene);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	//fonction fournie mais legerement modifiee, notamment pour la couleur du trait
	public static Cylinder createLine(Point3D origin, Point3D target, PhongMaterial color) {
		Point3D yAxis = new Point3D(0, 1, 0);
		Point3D diff = target.subtract(origin);
		double height = diff.magnitude();

		Point3D mid = target.midpoint(origin);
		Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

		Point3D axisOfRotation = diff.crossProduct(yAxis);
		double angle = Math.acos(diff.normalize().dotProduct(yAxis));
		Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

		Cylinder line = new Cylinder(0.01f, height);
		line.setMaterial(color);

		line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);

		return line;
	}

	private void drawQuadrilateral(Group parent) {

		//on recupere la valeur de l'annee que l'utilisateur veut
		int anneeCourante = (int) sliderA.getValue();
		
		//on decoupe la sphere en 4 par 4 degres
		for (int lat = -88; lat < 90; lat = lat + 4) {

			for (int lon = -178; lon < 180; lon = lon + 4) {
				
				//on recupere la valeur du fichier csv
				float res = Search.getInstance().findByYearAndByLongAndLat(anneeCourante, String.valueOf(lat),Integer.toString(lon));
				Point3D bottomLeft = geoCoordTo3dCoord(lat - 2, lon - 2, 1.05f);
				Point3D bottomRight = geoCoordTo3dCoord(lat - 2, lon + 2, 1.05f);
				Point3D topLeft = geoCoordTo3dCoord(lat + 2, lon - 2, 1.05f);
				Point3D topRight = geoCoordTo3dCoord(lat + 2, lon + 2, 1.05f);
				
				//on la compare et on lui attribue une zone geo et une couleur
				if (res> 8f) {
					AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, darkred);
				} else if (res > 6f
						&& res < 8f) {
					AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, red);
				} else if (res > 4f
						&& res < 6f) {
					AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, orange);
				} else if (res > 2f
						&& res < 4f) {
					AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, yellow);
				} else if (res > 0f
						&&res< 2f) {
					AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, white);
				} else if (res > -2f
						&& res < 0f) {
					AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, lightgreen);
				} else if (res > -4f
						&& res < -2f) {
					AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, green);
				} else if (res > -6f
						&& res< -4f) {
					AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, blue);
				} else if (res > -8f
						&& res < -6f) {
					AddQuadrilateral(parent, topRight, bottomRight, bottomLeft, topLeft, darkblue);
				}

			}
		}

		parent.getChildren().addAll(earthMeshViews);
	}

	// conversion GPS en 3D
	public static Point3D geoCoordTo3dCoord(float lat, float lon, float radius) {

		float lat_cor = lat + TEXTURE_LAT_OFFSET;
		float lon_cor = lon + TEXTURE_LON_OFFSET;
		return new Point3D(
				-java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
						* java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)) * radius,
				-java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)) * radius,
				java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
						* java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)) * radius);
	}

	//fonction qui dessine les histogrammes en creant des lignes entre les points
	private void drawHisto(Group parent) {
		Point3D origin = new Point3D(0, 0, 0);
		
		//meme schema que pour les quadrillages
		int anneeCourante = (int) sliderA.getValue();
		
		for (int lat = -88; lat < 90; lat = lat + 4) {

			for (int lon = -178; lon < 180; lon = lon + 4) {

				float res = Search.getInstance().findByYearAndByLongAndLat(anneeCourante, String.valueOf(lat),
						Integer.toString(lon));
				
				if (res > 8f) {
					Point3D target = geoCoordTo3dCoord(lat, lon, 2.6f);
					parent.getChildren().addAll(createLine(origin, target, darkred));
				} else if (res > 6f
						&& res < 8f) {
					Point3D target = geoCoordTo3dCoord(lat, lon, 2.3f);
					parent.getChildren().addAll(createLine(origin, target, red));
				} else if (res > 4f
						&& res < 6f) {
					Point3D target = geoCoordTo3dCoord(lat, lon, 2.0f);
					parent.getChildren().addAll(createLine(origin, target, orange));
				} else if (res > 2f
						&& res < 4f) {
					Point3D target = geoCoordTo3dCoord(lat, lon,1.7f);
					parent.getChildren().addAll(createLine(origin, target, yellow));
				} else if (res > 0f
						&& res < 2f) {
					Point3D target = geoCoordTo3dCoord(lat, lon, 1.4f);
					parent.getChildren().addAll(createLine(origin, target, white));
				} else if (res > -2f
						&& res < 0f) {
					Point3D target = geoCoordTo3dCoord(lat, lon, 1.0f);
					parent.getChildren().addAll(createLine(origin, target, lightgreen));
				} else if (res > -4f
						&& res < -2f) {
					Point3D target = geoCoordTo3dCoord(lat, lon, 1.0f);
					parent.getChildren().addAll(createLine(origin, target, green));
				} else if (res > -6f
						&& res < -4f) {
					Point3D target = geoCoordTo3dCoord(lat, lon, 1.0f);
					parent.getChildren().addAll(createLine(origin, target, blue));
				} else if (res > -8f
						&& res < -6f) {
					Point3D target = geoCoordTo3dCoord(lat, lon, 1.0f);
					parent.getChildren().addAll(createLine(origin, target, darkblue));
				}
			}
		}

	}

	//fonction qui ajoute les villes (tutoriel)
	public void displayTown(Group parent, String name, float latitude, float longitude) {
		Sphere sphere = new Sphere(0.01);
		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.GREEN);
		greenMaterial.setSpecularColor(Color.GREEN);
		sphere.setMaterial(greenMaterial);
		Group city = new Group();

		Point3D pos = geoCoordTo3dCoord(latitude, longitude, 1);
		city.setTranslateX(pos.getX());
		city.setTranslateY(pos.getY());
		city.setTranslateZ(pos.getZ());
		city.getChildren().addAll(sphere);
		city.setId(name);
		parent.getChildren().addAll(city);
	}

	private void AddQuadrilateral(Group parent, Point3D topRight, Point3D bottomRight, Point3D bottomLeft,
			Point3D topLeft, PhongMaterial red) {
		final TriangleMesh triangleMesh = new TriangleMesh();
		final float[] points = { (float) topRight.getX(), (float) topRight.getY(), (float) topRight.getZ(),
				(float) topLeft.getX(), (float) topLeft.getY(), (float) topLeft.getZ(), (float) bottomLeft.getX(),
				(float) bottomLeft.getY(), (float) bottomLeft.getZ(), (float) bottomRight.getX(),
				(float) bottomRight.getY(), (float) bottomRight.getZ(), };
		final float[] texCoords = { 1, 1, 1, 0, 0, 1, 0, 0 };
		final int[] faces = { 0, 1, 1, 0, 2, 2, 0, 1, 2, 2, 3, 3 };

		triangleMesh.getPoints().setAll(points);
		triangleMesh.getTexCoords().setAll(texCoords);
		triangleMesh.getFaces().setAll(faces);

		final MeshView meshView = new MeshView(triangleMesh);
		meshView.setMaterial(red);
		earthMeshViews.add(meshView);
	}

	//arrete le slider et permet de reprendre apres, la timeline ou on s etait arrete juste avant 
	private EventHandler<Event> pauseButton() {
		return event -> {
			isStop = !isStop;
			if (isStop)
				timeline.stop();
			else
				timeline.play();
		};

	}

	//arret la timeline et remet le slider a 1880
	private EventHandler<Event> stopButton() {
		return event -> {
			timeline.getKeyFrames().clear();
			timeline.stop();
			sliderA.setValue(Search.getInstance().getIndexOfYear().get(0));
		};

	}
	
//fonction pour le bouton play, qui met en route la timeline
	public EventHandler<Event> startButton()
	{

		return event -> {

			timeline.getKeyFrames().clear();

			final int[] i= {0};
			final int[] year = {Search.getInstance().getIndexOfYear().get(0)};

			System.out.println(Duration.millis((1d / Double.parseDouble(textVitt.getText())) * 1000));
			KeyFrame Kf = new KeyFrame(Duration.millis((1d / Double.parseDouble(textVitt.getText())) * 1000), new EventHandler<ActionEvent>() {
				@Override 
				public void handle(ActionEvent actionEvent) {
					if (i[0] < Search.getInstance().getIndexOfYear().size()) {
						year[0] = Search.getInstance().getIndexOfYear().get(i[0]);
					}
					else {
						timeline.stop();
						timeline.getKeyFrames().clear();
					}
					sliderA.setValue(year[0]);
					i[0]++;
				}
			});
			timeline.getKeyFrames().add(Kf);
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.play();
		};
	}
}