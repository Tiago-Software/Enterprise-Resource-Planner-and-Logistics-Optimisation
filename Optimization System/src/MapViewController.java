import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import phaki.optimization.apicommunication.GMapsApiCommunication;
import phaki.optimization.apicommunication.PhakiAPICommunication;
import phaki.optimization.kmeansclustering.KMeansLocationClustering;
import phaki.optimization.models.Location;
import phaki.optimization.models.OrderDto;
import phaki.optimization.models.OrderUpdateDto;
import phaki.optimization.models.Route;
import phaki.optimization.models.RouteDto;

public class MapViewController extends Application implements Initializable
{
	@FXML
	ImageView mapHolder;
	
	@FXML
	MenuItem menuItem_setAPIUrl;
	
	@FXML
	MenuItem menuItem_GetData;
	
	@FXML
	MenuItem menuItem_Cluster;
	
	@FXML
	MenuItem menuItem_Optimize;
	
	@FXML
	MenuItem menuItem_StartServer;
	
	@FXML
	MenuItem menuItem_StopServer;

	@FXML
	Circle circle_Status;
	
	@FXML
	TextArea txtLog;
	
	TextInputDialog dialog;
	
	HttpServer server;
	APIRequestHandler reqHandler;
	
	static final int PORT_NUM = 12434;
	static boolean running = false;
	
	public static final String DEFAULT_TEXT_STYLE = "-fx-text-fill: black";
	public static final String ERROR_TEXT_STYLE = "-fx-text-fill: red";
	
	public MapViewController(){ }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		reqHandler = new APIRequestHandler();
		setupGUIActions();
		circle_Status.setFill(Color.RED);
		reqHandler.setInitialMap();
		PhakiAPICommunication.setOutputArea(txtLog);
	}
	
	private void setupGUIActions() 
	{
		menuItem_StartServer.setDisable(true);
		menuItem_Cluster.setDisable(true);
		menuItem_GetData.setDisable(true);
		menuItem_Optimize.setDisable(true);
		menuItem_StopServer.setDisable(true);
		
		menuItem_GetData.setOnAction(e -> {
			reqHandler.getInitialData();
		});
		
		menuItem_Cluster.setOnAction(e -> {
			reqHandler.clusterLocations();
		});
		
		menuItem_Optimize.setOnAction(e -> {
			reqHandler.optimize();
		});
		
		menuItem_StartServer.setOnAction(e -> {
			startServer();
		});
		
		menuItem_StopServer.setOnAction(e -> {
			stopServer();
		});
		
		menuItem_setAPIUrl.setOnAction(e -> {
		
			TextInputDialog dialog = new TextInputDialog();
			
			dialog.setHeaderText("PHAKI API URL");
			dialog.setContentText("Enter Phaki API url: ");
			
			Optional<String> result = dialog.showAndWait();
			
			if(result.isPresent() && result.get() != "")
			{
				PhakiAPICommunication.setURL(result.get());
			}
			
			menuItem_StartServer.setDisable(false);
			menuItem_Cluster.setDisable(false);
			menuItem_GetData.setDisable(false);
			menuItem_Optimize.setDisable(false);
			menuItem_StopServer.setDisable(false);
			
		});
	}

	private void stopServer()
	{
		server.stop(0);
		
		running = false;
		
		circle_Status.setFill(Color.RED);
		
		txtLog.appendText("\nServer has stopped\n");
	}

	private void startServer() 
	{
		if(!running)
		{
			try 
			{
				server = HttpServer.create(new InetSocketAddress("localhost", 4000), 0);
				server.createContext("/BeginOptimization", reqHandler);
				server.setExecutor(null);
				
				circle_Status.setFill(Color.LIMEGREEN);
				
				server.start();
				
				running = true;
				
				txtLog.appendText("\nServer is running on port: " + server.getAddress().getPort() + "...");
				
			} catch (IOException e) 
			{
				txtLog.appendText("\nServer could not start");
				circle_Status.setFill(Color.RED);
				e.printStackTrace();
			}
		}
		else
		{
			txtLog.appendText("\nServer already running!");
		}
		
	}
	
	
	/**
	 * 
	 * @author Maikel Achi
	 * 
	 * Static class that handles incoming http requests
	 *
	 */
	private class APIRequestHandler implements HttpHandler
	{
		private ArrayList<Route> clusteredRoutes;
		private ArrayList<Route> optRoutes;
		private Location branchLoc;
		
		@Override
		public void handle(HttpExchange exch) throws IOException 
		{
			if(getInitialData())
			{
				if(clusterLocations())
				{
					if(optimize())
					{
						exch.sendResponseHeaders(200, -1);
					}
					
					exch.sendResponseHeaders(400, -1);
				}
				
				exch.sendResponseHeaders(400, -1);
			}
			
			exch.sendResponseHeaders(400, -1);
		}
		
		public void setInitialMap()
		{
			byte[] defaultImageBytes = GMapsApiCommunication.getStaticMap();
			
			mapHolder.setImage(new Image(new ByteArrayInputStream(defaultImageBytes)));
		}
		
		
		public boolean getInitialData()
		{
			if(PhakiAPICommunication.Authenticate())
			{
				branchLoc = PhakiAPICommunication.GetBranchLocation();
				
				if(branchLoc == null)
				{
					txtLog.setStyle(ERROR_TEXT_STYLE);
					txtLog.appendText("Branch Location request returned null\n");
					txtLog.setStyle(DEFAULT_TEXT_STYLE);
					return false;
				}
				
				ArrayList<OrderDto> orders = PhakiAPICommunication.GetPendingOrders();
				
				if(orders == null)
				{
					txtLog.setStyle(ERROR_TEXT_STYLE);
					txtLog.appendText("There are no orders to optimize today\n");
					txtLog.setStyle(DEFAULT_TEXT_STYLE);
					return false;
				}
				
				byte[] imageBytes = GMapsApiCommunication.getStaticMapInitialOrders(orders);
				
				mapHolder.setImage(new Image(new ByteArrayInputStream(imageBytes)));
				
				return true;
			}
			else
			{
				txtLog.setStyle(ERROR_TEXT_STYLE);
				txtLog.appendText("Service authentication failed\n");
				txtLog.setStyle(DEFAULT_TEXT_STYLE);
				return false;
			}
		}
		
		
		public boolean clusterLocations()
		{
			clusteredRoutes = KMeansLocationClustering.initiateClustering();
			
			if(clusteredRoutes == null)
			{
				Text txt = new Text("Order clustering failed\n");
				txt.setFill(Color.RED);
				return false;
			}
			
			byte[] imageBytes2 = GMapsApiCommunication.getStaticMapClusteredOrders(clusteredRoutes);
			
			mapHolder.setImage(new Image(new ByteArrayInputStream(imageBytes2)));
			
			return true;
		}
		
		public boolean optimize()
		{
			ArrayList<RouteDto> optimizedRoutes = new ArrayList<RouteDto>();
			optRoutes = new ArrayList<Route>();
			
			for(int x = 0; x < clusteredRoutes.size(); x++)
			{
				Route optimalRoute = GMapsApiCommunication.getOptimalRoute(clusteredRoutes.get(x), branchLoc);
				
				optRoutes.add(optimalRoute);
				
				RouteDto route = new RouteDto();
				
				route.route_ETA_In_Seconds = optimalRoute.route_ETA_In_Seconds;
				route.route_Total_Distance = optimalRoute.route_Total_Distance;
				route.route_Truck_Id = optimalRoute.route_Truck_Id;
				route.orders = new ArrayList<OrderUpdateDto>();
				
				for(int y = 0; y < optimalRoute.orders.size(); y++)
				{
					OrderUpdateDto order = new OrderUpdateDto(optimalRoute.orders.get(y).order_Id, y + 1);
					
					route.orders.add(order);
				}
				
				optimizedRoutes.add(route);
			}
			
			byte[] imageBytes3 = GMapsApiCommunication.getStaticMapWithPaths(optRoutes, branchLoc);
			
			mapHolder.setImage(new Image(new ByteArrayInputStream(imageBytes3)));
			
			return PhakiAPICommunication.PostOptimizationResults(optimizedRoutes);
		}
		
	}
	
	public static void main(String[] args) 
	{
		configureSSLVerification();
		System.setProperty("https.protocols", "TLSv1.1");
		launch(args);
		
	}
	
	/**
	 * @see <a href="https://stackoverflow.com/questions/7443235/getting-java-to-accept-all-certs-over-https">Source of this method</a>
	 */
	public static void configureSSLVerification()
	{
		// Create a trust manager that does not validate certificate chains
        X509TrustManager[] trustAllCerts = new X509TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					// TODO Auto-generated method stub
					
				}
        }
        };
 
        // Install the all-trusting trust manager
       
        try {
        	SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			
		} catch (KeyManagementException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}


		// Create all-trusting host name verifier
        HostnameVerifier allHostsValid = (HostnameVerifier) (hostname, session) -> true;
 
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("OptimizationGUI.fxml"));
		Parent root = loader.load();
		MapViewController controller = loader.<MapViewController>getController();
		Scene scene = new Scene(root, 600, 550);
		
		stage.setOnCloseRequest(closeEvent -> {
			if(running)
			{
				controller.stopServer();
			}
			
		});
		stage.setScene(scene);
		stage.setTitle("Optimization GUI");
		stage.show();
	}
	
}
