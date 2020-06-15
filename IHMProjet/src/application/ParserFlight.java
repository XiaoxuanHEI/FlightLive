package application;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Response;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ParserFlight {

	public FlightList flights;	
	public boolean finished = false;
	
public void getFlight() {	
		
		finished = false;
		
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
		
		BoundRequestBuilder getRequest = client.prepareGet("https://public-api.adsbexchange.com/VirtualRadar/AircraftList.json");
		
		getRequest.execute(new AsyncCompletionHandler<Object>() {
			@Override
		    public Object onCompleted(Response response) throws Exception {
				//System.out.println(response.getResponseBody());
		    	ObjectMapper mapper = new ObjectMapper();
		    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		    	flights = mapper.readValue(response.getResponseBody(), FlightList.class);			
				finished = true;
		    	return response;
			}
		});	
		
		while(true) {
			System.out.print("");
			boolean b = finished;
			if(b)
				break;
		}
		
		try {
			client.close();
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void getFlight(String s) {	
		
		finished = false;
		
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
		
		BoundRequestBuilder getRequest = client.prepareGet("https://public-api.adsbexchange.com/VirtualRadar/AircraftList.json?fAirQ=" + s );
		
		getRequest.execute(new AsyncCompletionHandler<Object>() {
			@Override
		    public Object onCompleted(Response response) throws Exception {
		    	ObjectMapper mapper = new ObjectMapper();
		    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		    	flights = mapper.readValue(response.getResponseBody(), FlightList.class);			
				finished = true;
		    	return response;
			}
		});	
		
		while(true) {
			System.out.print("");
			boolean b = finished;
			if(b)
				break;
		}
		
		try {
			client.close();
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void getPositionD1Vol() {	
		finished = false;
		DefaultAsyncHttpClientConfig.Builder clientBuilder = Dsl.config()
						  .setConnectTimeout(500)
						  .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36")
						  .setKeepAlive(false);
		AsyncHttpClient client = Dsl.asyncHttpClient(clientBuilder);
		
		BoundRequestBuilder getRequest = client.prepareGet("https://public-api.adsbexchange.com/VirtualRadar/AircraftList.json?trFmt=f");
		
		getRequest.execute(new AsyncCompletionHandler<Object>() {
			@Override
		    public Object onCompleted(Response response) throws Exception {
		    	ObjectMapper mapper = new ObjectMapper();
		    	mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		    	flights = mapper.readValue(response.getResponseBody(), FlightList.class);			
				finished = true;
		    	return response;
			}
		});	
		
		while(true) {
			System.out.print("");
			boolean b = finished;
			if(b)
				break;
		}
		try {
			client.close();
		}catch(IOException e) {
			e.printStackTrace();
		}	
	}
	
	public ParserFlight() {
	}
}
