
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Hlavni extends Application{

	
	/**
	 * inicializace všech GUI prvků
	 */
	private ImageView iv;
	private Timeline tm = new Timeline();
	private Button h = new Button("Přejít horizontálně");
    private Button v = new Button("Přejít vertikálně");
    private Button stop = new Button("Stop");
    private Button start = new Button("Start");
	Image s1 = new Image("Stavy/S1.png");
	Image s12 = new Image("Stavy/S12.png");
	Image s13 = new Image("Stavy/S13.png");
	Image s2 = new Image("Stavy/S2.png");
	Image s21 = new Image("Stavy/S21.png");
	Image s23 = new Image("Stavy/S23.png");
	Image s3 = new Image("Stavy/S3.png");
	Image s31 = new Image("Stavy/S31.png");
	Image s34 = new Image("Stavy/S34.png");
	Image s4 = new Image("Stavy/S4.png");
	Image s41 = new Image("Stavy/S41.png");
	Image s43 = new Image("Stavy/S43.png");
	Image sv = new Image("Stavy/SV.png");
	Image sv0 = new Image("Stavy/SV0.png");
	Image sv1 = new Image("Stavy/SV1.png");
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//inicializace okna
		iv = new ImageView();
		// nastaveni prvniho snimku
		iv.setImage(sv);
		// nastaveni velikosti obrazku
		iv.setFitWidth(1000);
		iv.fitWidthProperty().bind(primaryStage.widthProperty().subtract(20));
		iv.fitHeightProperty().bind(primaryStage.heightProperty().subtract(100));
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        iv.setCache(true);
        
        //animace se nastavi na prepinani mezi vypnutym stavem SV a stavem ve kterem sviti jen oranzova svetla SV1
        //posledni snimek animaci restartuje, aby bezela porad dokola
        tm = new Timeline(
        		new KeyFrame(Duration.ZERO,new KeyValue(iv.imageProperty(), sv)),
        		new KeyFrame(Duration.seconds(1), new KeyValue(iv.imageProperty(), sv1)),
        		new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
        						@Override 
        						public void handle(ActionEvent event) {
        									reset();
        									}
        						},  
        				new KeyValue(iv.imageProperty(), sv1))
        		
        		);
        //spusteni animace
        tm.playFromStart();
        
        //nastaveni GUI tlacitek
        HBox controls = new HBox();
        controls.setPadding(new Insets(10));
        controls.setSpacing(10);
        //tlacitko start po stisknuti nastavi animaci na 2 snimky, prvni je prechodovy SV0 a druhy snimek SV1 je uz 
        //zacatek sedeho cyklu, ten pak okamzite metodou zaklad() nastavi animaci na sedy cyklus
        start.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				tm.stop();
				start.setDisable(true);
				stop.setDisable(false);
				tm = new Timeline(
		        		new KeyFrame(Duration.seconds(1), new KeyValue(iv.imageProperty(), sv0)),
		        		new KeyFrame(Duration.seconds(2),
		        					new EventHandler<ActionEvent>() {
		        						@Override 
		        						public void handle(ActionEvent event) {
		        									zaklad();
		        									}
		        						},  
		        					new KeyValue(iv.imageProperty(), s1))
		        		);
				tm.playFromStart();
			}
		});
        
        stop.setDisable(true);
        //tlacitko stop funguje podobne jako start akorat misto sedeho cyklu prejde do fialoveho(pocatecniho)
        stop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				tm.stop();
				start.setDisable(true);
				stop.setDisable(false);
				tm = new Timeline(
		        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), sv0)),
		        		new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
		        						@Override 
		        						public void handle(ActionEvent event) {
		        									stoped();
		        									}
		        						},  
		        				new KeyValue(iv.imageProperty(), sv))
		        		);
				tm.playFromStart();
			}
		});
        
        resetVertical();
       
        resetHorizontal();
        //prida GUI do okna a zobrazi okno
        controls.setMinHeight(30);
        controls.getChildren().addAll(start,h,v,stop);
		BorderPane root = new BorderPane();
		root.setCenter(iv);
		root.setBottom(controls);
		Scene scena = new Scene(root, 800,400, Color.WHITE);
		scena.setFill(Color.WHITE);
		primaryStage.setScene(scena);
		primaryStage.show();
		
	}
	
	/**
	 * Nastavi animaci na zakladni sedy cyklus.
	 * V tomto cyklu se prepinaji zakladni stavy a posledni snimek animaci restartuje
	 * 
	 */
	public void zaklad(){
		tm.stop();
		tm = new Timeline(
				//vysvetleni casovace T
				//
				//Do Timeline musim pridat KeyFrame, to jsou klicove snimky, ktere musi mit jako parametry
				//cas, kdy se maji zobrazit a klicovou hodnotu (co maji udelat)
				//
				//Tyto snimky se meni po 1 a 3 vterinach, ale tento cas se musi neustale zvysovat
				//prvni tedy zacina pri vzniku animace a je prekreslen druhym ktery zacne po 3 vterinach
				//ten je prekreslen po vterine (3+1=4) a ten je prekreslen po 3 vterinach (4+3=7) atd.
				//vim ze tato implementace neni dobra a kdyby neslo jen a takovouto primitivni aplikaci
				//vytvoril bych si alespon globalni konstantu kterou bych v kazdem kroku pricital
				//neuvedomil jsem si ze jsou soucasti odevzdani i zdrojove kody a opravit to ve vsech vyskytech 
				//by bylo znacne pracne
				//
        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s1)),
        		new KeyFrame(Duration.seconds(3), new KeyValue(iv.imageProperty(), s12)),
        		new KeyFrame(Duration.seconds(4), new KeyValue(iv.imageProperty(), s2)),
        		new KeyFrame(Duration.seconds(7), new KeyValue(iv.imageProperty(), s23)),
        		new KeyFrame(Duration.seconds(8), new KeyValue(iv.imageProperty(), s3)),
        		new KeyFrame(Duration.seconds(11), new KeyValue(iv.imageProperty(), s34)),
        		new KeyFrame(Duration.seconds(12), new KeyValue(iv.imageProperty(), s4)),
        		new KeyFrame(Duration.seconds(15), new KeyValue(iv.imageProperty(), s41)),
        		new KeyFrame(Duration.seconds(16), new EventHandler<ActionEvent>() {
        						@Override 
        						public void handle(ActionEvent event) {
        									reset();
        									}
        						},  
        				new KeyValue(iv.imageProperty(), s1))
        		);
		tm.playFromStart();
	}
	
	/**
	 * Tato metoda funguje jako zaklad(), pri programovani jsem si neuvedomil ze se do sedeho cyklu varcim ze 2 mist
	 * proto jsem musel pridelat metodu ktera nastavi animaci na stejny sedy cyklus ale zacinajici na jinem snimku
	 */
	public void posunutyZaklad(){
		tm.stop();
		tm = new Timeline(
        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s3)),
        		new KeyFrame(Duration.seconds(3), new KeyValue(iv.imageProperty(), s34)),
        		new KeyFrame(Duration.seconds(4), new KeyValue(iv.imageProperty(), s4)),
        		new KeyFrame(Duration.seconds(7), new KeyValue(iv.imageProperty(), s41)),
        		new KeyFrame(Duration.seconds(8), new KeyValue(iv.imageProperty(), s1)),
        		new KeyFrame(Duration.seconds(11), new KeyValue(iv.imageProperty(), s12)),
        		new KeyFrame(Duration.seconds(12), new KeyValue(iv.imageProperty(), s2)),
        		new KeyFrame(Duration.seconds(15), new KeyValue(iv.imageProperty(), s23)),
        		new KeyFrame(Duration.seconds(16), new EventHandler<ActionEvent>() {
        						@Override 
        						public void handle(ActionEvent event) {
        									reset();
        									}
        						},  
        				new KeyValue(iv.imageProperty(), s3))
        		);
		tm.playFromStart();
	}

	/**
	 * Pri stisknuti 1. tlacitka prejdu touto metodou do zluteho cyklu
	 * nastavi animaci na cyklus ve kterem automat nereaguje na vstup od chodcu
	 * v implementaci by nemuselo byt ale snazil jsem se drzet navrhnuteho schematu
	 */
	public void kruhZluty(){
		tm.stop();
		tm = new Timeline(
        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s1)),
        		new KeyFrame(Duration.seconds(3), new KeyValue(iv.imageProperty(), s12)),
        		new KeyFrame(Duration.seconds(4), new KeyValue(iv.imageProperty(), s2)),
        		new KeyFrame(Duration.seconds(7), new KeyValue(iv.imageProperty(), s23)),
        		new KeyFrame(Duration.seconds(8), new KeyValue(iv.imageProperty(), s3)),
        		new KeyFrame(Duration.seconds(11), new KeyValue(iv.imageProperty(), s34)),
        		new KeyFrame(Duration.seconds(12), new KeyValue(iv.imageProperty(), s4)),
        		new KeyFrame(Duration.seconds(15), new KeyValue(iv.imageProperty(), s41)),
        		new KeyFrame(Duration.seconds(16), new EventHandler<ActionEvent>() {
        						@Override 
        						public void handle(ActionEvent event) {
        									resetVertical();
        									resetHorizontal();
        									zaklad();
        									reset();
        									}
        						},  
        				new KeyValue(iv.imageProperty(), s1))
        		);
		tm.playFromStart();
	}
	
	/**
	 * Pri stisknuti 2. tlacitka prejdu touto metodou do zeleneho cyklu
	 * nastavi animaci na cyklus ve kterem automat nereaguje na vstup od chodcu
	 * v implementaci by nemuselo byt ale snazil jsem se drzet navrhnuteho schematu
	 */
	public void kruhZeleny(){
		tm.stop();
		tm = new Timeline(
        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s3)),
        		new KeyFrame(Duration.seconds(3), new KeyValue(iv.imageProperty(), s34)),
        		new KeyFrame(Duration.seconds(4), new KeyValue(iv.imageProperty(), s4)),
        		new KeyFrame(Duration.seconds(7), new KeyValue(iv.imageProperty(), s41)),
        		new KeyFrame(Duration.seconds(8), new KeyValue(iv.imageProperty(), s1)),
        		new KeyFrame(Duration.seconds(11), new KeyValue(iv.imageProperty(), s12)),
        		new KeyFrame(Duration.seconds(12), new KeyValue(iv.imageProperty(), s2)),
        		new KeyFrame(Duration.seconds(15), new KeyValue(iv.imageProperty(), s23)),
        		new KeyFrame(Duration.seconds(16), new EventHandler<ActionEvent>() {
        						@Override 
        						public void handle(ActionEvent event) {
        									resetVertical();
        									resetHorizontal();
        									posunutyZaklad();
        									reset();
        									}
        						},  
        				new KeyValue(iv.imageProperty(), s3))
        		);
		tm.playFromStart();
	}
	
	/**
	 * Nastavuje tlacitko pro horizontalni prechod
	 * Nemohu ho nastavit jen jednou, protoye ho po stisknuti zbavim EventHandleru 
	 * Je zavolana po spusteni programu a pokazde kdyz chci tlacitku vratit funkcnost
	 */
	public void resetHorizontal(){
		  h.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					
					if(iv.getImage().equals(s34)){
						tm.stop();
						//odebere tlacitkum funkci
						h.setOnAction(null);
						v.setOnAction(null);
						//prejde do zeleneho stavu pres 3 mezistavy
						tm = new Timeline(
				        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s34)),
				        		new KeyFrame(Duration.seconds(1), new KeyValue(iv.imageProperty(), s4)),
				        		new KeyFrame(Duration.seconds(4), new KeyValue(iv.imageProperty(), s43)),
				        		new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
				        						@Override 
				        						public void handle(ActionEvent event) {
				        									kruhZeleny();
				        									}
				        						},  
				        				new KeyValue(iv.imageProperty(), s3))
				        		);
						tm.playFromStart();

					}else if(iv.getImage().equals(s4)){
						tm.stop();
						h.setOnAction(null);
						v.setOnAction(null);
						tm = new Timeline(
				        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s4)),
				        		new KeyFrame(Duration.seconds(3), new KeyValue(iv.imageProperty(), s43)),
				        		new KeyFrame(Duration.seconds(4), new EventHandler<ActionEvent>() {
				        						@Override 
				        						public void handle(ActionEvent event) {
				        									kruhZeleny();
				        									}
				        						},  
				        				new KeyValue(iv.imageProperty(), s3))
				        		);
						tm.playFromStart();

					}else if(iv.getImage().equals(s41)){
						tm.stop();
						h.setOnAction(null);
						v.setOnAction(null);
						tm = new Timeline(
				        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s41)),
				        		new KeyFrame(Duration.seconds(1), new KeyValue(iv.imageProperty(), s1)),
				        		new KeyFrame(Duration.seconds(4), new KeyValue(iv.imageProperty(), s13)),
				        		new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
				        						@Override 
				        						public void handle(ActionEvent event) {
				        									kruhZeleny();
				        									}
				        						},  
				        				new KeyValue(iv.imageProperty(), s3))
				        		);
						tm.playFromStart();

					}else if(iv.getImage().equals(s1)){
						tm.stop();
						h.setOnAction(null);
						v.setOnAction(null);
						tm = new Timeline(
				        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s1)),
				        		new KeyFrame(Duration.seconds(3), new KeyValue(iv.imageProperty(), s13)),
				        		new KeyFrame(Duration.seconds(4), new EventHandler<ActionEvent>() {
				        						@Override 
				        						public void handle(ActionEvent event) {
				        									kruhZeleny();
				        									}
				        						},  
				        				new KeyValue(iv.imageProperty(), s3))
				        		);
						
						tm.playFromStart();
					}
				}
			});
	}
	
	/**
	 * Nastavuje tlacitko pro vertikalni prechod
	 * Nemohu ho nastavit jen jednou, protoye ho po stisknuti zbavim EventHandleru 
	 * Je zavolana po spusteni programu a pokazde kdyz chci tlacitku vratit funkcnost
	 */
	public void resetVertical(){
		 v.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					
					if(iv.getImage().equals(s12)){
						tm.stop();
						
						h.setOnAction(null);
						v.setOnAction(null);
						tm = new Timeline(
				        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s12)),
				        		new KeyFrame(Duration.seconds(1), new KeyValue(iv.imageProperty(), s2)),
				        		new KeyFrame(Duration.seconds(4), new KeyValue(iv.imageProperty(), s21)),
				        		new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
				        						@Override 
				        						public void handle(ActionEvent event) {
				        									kruhZluty();
				        									}
				        						},  
				        				new KeyValue(iv.imageProperty(), s1))
				        		);
						tm.playFromStart();

					}else if(iv.getImage().equals(s2)){
						tm.stop();
						h.setOnAction(null);
						v.setOnAction(null);
						tm = new Timeline(
				        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s2)),
				        		new KeyFrame(Duration.seconds(3), new KeyValue(iv.imageProperty(), s21)),
				        		new KeyFrame(Duration.seconds(4), new EventHandler<ActionEvent>() {
				        						@Override 
				        						public void handle(ActionEvent event) {
				        									kruhZluty();
				        									}
				        						},  
				        				new KeyValue(iv.imageProperty(), s1))
				        		);
						tm.playFromStart();

					}else if(iv.getImage().equals(s23)){
						tm.stop();
						h.setOnAction(null);
						v.setOnAction(null);
						tm = new Timeline(
				        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s23)),
				        		new KeyFrame(Duration.seconds(1), new KeyValue(iv.imageProperty(), s3)),
				        		new KeyFrame(Duration.seconds(4), new KeyValue(iv.imageProperty(), s31)),
				        		new KeyFrame(Duration.seconds(5), new EventHandler<ActionEvent>() {
				        						@Override 
				        						public void handle(ActionEvent event) {
				        									kruhZluty();
				        									}
				        						},  
				        				new KeyValue(iv.imageProperty(), s1))
				        		);
						tm.playFromStart();

					}else if(iv.getImage().equals(s3)){
						tm.stop();
						h.setOnAction(null);
						v.setOnAction(null);
						tm = new Timeline(
				        		new KeyFrame(Duration.ZERO, new KeyValue(iv.imageProperty(), s3)),
				        		new KeyFrame(Duration.seconds(3), new KeyValue(iv.imageProperty(), s31)),
				        		new KeyFrame(Duration.seconds(4), new EventHandler<ActionEvent>() {
				        						@Override 
				        						public void handle(ActionEvent event) {
				        									kruhZluty();
				        									}
				        						},  
				        				new KeyValue(iv.imageProperty(), s1))
				        		);
						
						tm.playFromStart();
					}
					
				}
			});
	        
	}
	
	/**
	 * Pokud chci prechod zastavit tato metoda prejde do fialoveho cyklu(zastaveny prechod) pres mezi stav SV
	 */
	public void stoped(){
		tm.stop();
		start.setDisable(false);
		  tm = new Timeline(
	        		new KeyFrame(Duration.ZERO,new KeyValue(iv.imageProperty(), sv)),
	        		new KeyFrame(Duration.seconds(1), new KeyValue(iv.imageProperty(), sv1)),
	        		new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
	        						@Override 
	        						public void handle(ActionEvent event) {	
	        									reset();
	        									}
	        						},  
	        				new KeyValue(iv.imageProperty(), sv1))
	        		
	        		);
		  tm.playFromStart();
	}
	
	/**
	 * Restartovani animace
	 */
	public void reset(){
		tm.stop();
		tm.playFromStart();
	}
}
