package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;

public class ZAPI {

	static int serviceCallCount = 0;
	
	private static final Log log = LogFactory.getLog(ZAPI.class);
	private static final String authString = "Basic bmd1cHRhOlZpbml0YUAwOQ==";

	public static String get(String urlString) throws InterruptedException {

		StringBuffer stringBuffer = new StringBuffer();
		try {

			// Closable Http Client which can be closed after it is used
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();

			// HttpGet for Get Calls
			HttpGet get = new HttpGet(urlString);

			get.addHeader("Authorization", authString);
			get.addHeader("Accept", "application/json");

			// Response as Closable Http Response
			CloseableHttpResponse response = httpClient.execute(get);
			// Fetching Code from Closable Http response object
			int responsehttp = response.getStatusLine().getStatusCode();

			if (responsehttp != 200) {
				if (responsehttp == 401 && serviceCallCount != 3) {
					Thread.sleep(3000);
					httpClient.close();
					serviceCallCount++;
					get(urlString);
					String serviceCall = "";
					if (serviceCallCount == 1) {
						serviceCall = "1st";
					} else if (serviceCallCount == 1) {
						serviceCall = "2nd";
					} else if (serviceCallCount == 1) {
						serviceCall = "3rd";
					}
					log.info(serviceCall + " Call to Get Service - " + urlString);
				}
				throw new RuntimeException("Failed : HTTP error code : " + responsehttp);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			log.info("Service Called - Get : " + urlString);

			while ((output = br.readLine()) != null) {
				stringBuffer.append(output);
			}

			httpClient.close();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return stringBuffer.toString();
	}

	@SuppressWarnings("deprecation")
	public static String postOrPut(String urlString, String data, String method) throws InterruptedException {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		StringBuffer stringBuffer = new StringBuffer();
		try {

			// Closable Http Client which can be closed after it is used
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			CloseableHttpResponse response = null;
			int responsehttp = 0;

			if (method.equalsIgnoreCase("post")) {
				// HttpPost for Post Calls
				HttpPost postorput = new HttpPost(urlString);
				postorput.addHeader("Authorization", authString);
				postorput.addHeader("Accept", "application/json");
				postorput.addHeader("Content-type", "application/json");
				postorput.setEntity(new StringEntity(data, HTTP.UTF_8));

				response = httpClient.execute(postorput);
				responsehttp = response.getStatusLine().getStatusCode();
			}

			else {
				// HttpPut for Put Calls
				HttpPut postorput = new HttpPut(urlString);
				postorput.addHeader("Authorization", authString);
				postorput.addHeader("Accept", "application/json");
				postorput.addHeader("Content-type", "application/json");
				postorput.setEntity(new StringEntity(data, HTTP.UTF_8));

				response = httpClient.execute(postorput);
				responsehttp = response.getStatusLine().getStatusCode();
			}
			
			log.info("Service Called " + method + " : " + urlString);

			if (responsehttp != 200) {
				if (responsehttp == 401 && serviceCallCount != 3) {
					Thread.sleep(3000);
					httpClient.close();
					serviceCallCount++;
					postOrPut(urlString, data, method);
					String serviceCall = "";
					if (serviceCallCount == 1) {
						serviceCall = "1st";
					} else if (serviceCallCount == 1) {
						serviceCall = "2nd";
					} else if (serviceCallCount == 1) {
						serviceCall = "3rd";
					}
					log.info(serviceCall + " Call to " + method + " Service - " + urlString);
				}
				throw new RuntimeException("Failed : HTTP error code : " + responsehttp);
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			while ((output = br.readLine()) != null) {
				stringBuffer.append(output);
			}

			httpClient.close();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		return stringBuffer.toString();
	}
	
	public static void delete(String urlString) throws InterruptedException {
		
		try {

			// Closable Http Client which can be closed after it is used
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();

			// HttpGet for Delete Calls
			HttpDelete delete = new HttpDelete(urlString);

			delete.addHeader("Authorization", authString);
			delete.addHeader("Accept", "application/json");

			// Response as Closable Http Response
			CloseableHttpResponse response = httpClient.execute(delete);
			// Fetching Code from Closable Http response object
			int responsehttp = response.getStatusLine().getStatusCode();

			if (responsehttp != 200) {
				if (responsehttp == 401 && serviceCallCount != 3) {
					Thread.sleep(3000);
					httpClient.close();
					serviceCallCount++;
					get(urlString);
					String serviceCall = "";
					if (serviceCallCount == 1) {
						serviceCall = "1st";
					} else if (serviceCallCount == 1) {
						serviceCall = "2nd";
					} else if (serviceCallCount == 1) {
						serviceCall = "3rd";
					}
					System.out.println(serviceCall + " Call to Get Service - " + urlString);
				}
				throw new RuntimeException("Failed : HTTP error code : " + responsehttp);
			}

			httpClient.close();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}