package MyPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String apiKey = "0386a5c1acbaecd4b35b8361e18e3848";
		String city = request.getParameter("city");
		// Encode the city name to handle spaces and special characters
		String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());
		String apiURL = "https://api.openweathermap.org/data/2.5/weather?q="+ encodedCity + "&appid=" + apiKey;
		//API integration 
		URL url = new URL(apiURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		// reading the data from network
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		//storing data in string
		StringBuilder responseContent = new StringBuilder();
		Scanner scanner = new Scanner(reader);
		while(scanner.hasNextLine()) {
			responseContent.append(scanner.nextLine());
		}
		scanner.close();
		
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
				
		//place
		String place = jsonObject.get("name").toString();
		
		//temp
		double temp = (int) ((jsonObject.getAsJsonObject("main").get("temp").getAsDouble()) - 273);
		//humidity
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
		//wind speed
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
		//weather condition
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
		
		request.setAttribute("place", place);
		request.setAttribute("temp", temp);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherCondition", weatherCondition);
		connection.disconnect();
		
		//Forward
		request.getRequestDispatcher("index.jsp").forward(request, response);
		
		doGet(request, response);
	}

}
