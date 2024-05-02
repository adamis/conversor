/**
 * 
 */
package br.com.conversor.request;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;



/**
 * Class Request's with POST and GET 
 * @author F5K5WQI
 */

public class RequestClientFactory {


	//private Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.96.131.84", 8008));
	private Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy-br.fiserv.one", 8080));
	

	private OkHttpClient client = null;
	private static long callTimeOutMsTemp = 10000;
	private static long readTimeOutMsTemp = 15000;

	/**
	 * IGNORAR CERTIFICADOS
	 */
	private static final TrustManager[] trustAllCerts = new TrustManager[] {
			new X509TrustManager() {
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return new java.security.cert.X509Certificate[]{};
				}
			}
	};

	/**
	 * IGNORAR CERTIFICADOS
	 */
	private static final SSLContext trustAllSslContext;

	/**
	 * IGNORAR CERTIFICADOS
	 */
	static {
		try {			
			trustAllSslContext = SSLContext.getInstance("TLSv1.2");
			trustAllSslContext.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException | KeyManagementException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * IGNORAR CERTIFICADOS
	 */
	private static final SSLSocketFactory trustAllSslSocketFactory = trustAllSslContext.getSocketFactory();

	/**
	 * Send GET Request
	 *@author F5K5WQI
	 *4 de abr. de 2023	  
	 *@param url
	 *@param headers
	 *@param callTimeOutMs (Default=10000)
	 *@param readTimeOutMs (Default=15000)
	 *@return Response
	 *@throws IOException
	 */
	public Response sendGetRequest(String url, HeaderFactory headers, Long callTimeOutMs, Long readTimeOutMs, boolean proxyActive) throws IOException {

		okhttp3.Request.Builder builder = new Request.Builder();

		builder.url(url);

		if(headers != null) {
			builder.headers(headers.build());
		}		

		Request request = builder.build();

		callTimeOutMsTemp = callTimeOutMs == null ?callTimeOutMsTemp:callTimeOutMs;
		readTimeOutMsTemp = readTimeOutMs == null ?readTimeOutMsTemp:readTimeOutMs;

		client = getClient(callTimeOutMsTemp,readTimeOutMsTemp, proxyActive);

		return client.newCall(request).execute();
	}

	/**
	 * Send GET Request with Params
	 *@author F5K5WQI
	 *10 de abr. de 2023 
	 *@param url
	 *@param headers
	 *@param paramsFactory
	 *@param callTimeOutMs (Default=10000)
	 *@param readTimeOutMs (Default=15000)
	 *@return Response
	 *@throws IOException
	 */
	public Response sendGetRequest(String url, HeaderFactory headers, ParamsFactory paramsFactory , Long callTimeOutMs, Long readTimeOutMs, boolean proxyActive) throws IOException {

		okhttp3.Request.Builder builder = new Request.Builder();

		if(headers != null) {
			builder.headers(headers.build());
		}		

		if(paramsFactory != null) {
			builder.url(url+"?"+paramsFactory.build());	
		}else{
			builder.url(url);
		}



		Request request = builder.build();

		callTimeOutMsTemp = callTimeOutMs == null ?callTimeOutMsTemp:callTimeOutMs;
		readTimeOutMsTemp = readTimeOutMs == null ?readTimeOutMsTemp:readTimeOutMs;

		client = getClient(callTimeOutMsTemp, readTimeOutMsTemp, proxyActive);

		return client.newCall(request).execute();
	}


	/**
	 * Send POST Request With JSON
	 *@author F5K5WQI
	 *6 de abr. de 2023 
	 *@param url
	 *@param headers
	 *@param json
	 *@param callTimeOutMs (Default=10000)
	 *@param readTimeOutMs (Default=15000)
	 *@return Response
	 *@throws IOException
	 */
	public Response sendPostRequest(String url, HeaderFactory headers, String json, Long callTimeOutMs, Long readTimeOutMs,boolean proxyActive) throws IOException {
		RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

		okhttp3.Request.Builder builder = new Request.Builder();

		builder.url(url);
		builder.post(body);

		if(headers != null) {
			headers.addHeader("User-Agent", "PostmanRuntime/7.31.3");
			builder.headers(headers.build());			
		}else {
			headers = new HeaderFactory();
			headers.addHeader("User-Agent", "PostmanRuntime/7.31.3");
			builder.headers(headers.build());
		}

		Request request = builder.build(); 

		callTimeOutMsTemp = callTimeOutMs == null ?callTimeOutMsTemp:callTimeOutMs;
		readTimeOutMsTemp = readTimeOutMs == null ?readTimeOutMsTemp:readTimeOutMs;

		client = getClient(callTimeOutMsTemp,readTimeOutMsTemp, proxyActive);

		return client.newCall(request).execute();
	}






	/**
	 * Send POST Request With OBJECT
	 *@author F5K5WQI
	 *10 de abr. de 2023 
	 *@param <T>
	 *@param url
	 *@param headers
	 *@param object
	 *@param callTimeOutMs (Default=10000)
	 *@param readTimeOutMs (Default=15000)
	 *@return Response
	 *@throws IOException
	 */
	public <T> Response sendPostRequest(String url, HeaderFactory headers, T object, Long callTimeOutMs, Long readTimeOutMs, boolean proxyActive) throws IOException {

		ObjectWriter ow = new ObjectMapper().writer();
		String json = ow.writeValueAsString(object);

		RequestBody body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));

		okhttp3.Request.Builder builder = new Request.Builder();

		builder.url(url);
		builder.post(body);

		if(headers != null) {
			builder.headers(headers.build());
		}			

		Request request = builder.build(); 

		callTimeOutMsTemp = callTimeOutMs == null ?callTimeOutMsTemp:callTimeOutMs;
		readTimeOutMsTemp = readTimeOutMs == null ?readTimeOutMsTemp:readTimeOutMs;

		client = getClient(callTimeOutMsTemp,readTimeOutMsTemp,proxyActive);

		return client.newCall(request).execute();
	}



	/**
	 * Send POST Request With FORM 
	 *@author F5K5WQI
	 *6 de abr. de 2023 
	 *@param url
	 *@param headers
	 *@param form
	 *@param callTimeOutMs (Default=10000)
	 *@param readTimeOutMs (Default=15000)
	 *@param proxyActive 
	 *@return Response
	 *@throws IOException
	 */
	public Response sendPostRequest(String url, HeaderFactory headers, FormsFactory form, Long callTimeOutMs, Long readTimeOutMs, boolean proxyActive ) throws IOException {


		okhttp3.Request.Builder builder = new Request.Builder();

		builder.url(url);

		if(form != null) {
			builder.post(form.build());		
		} else {			
			builder.post(RequestBody.create(new byte[0], MediaType.parse("application/x-www-form-urlencoded")));			
		}

		if(headers != null) {
			builder.headers(headers.build());
		}	

		Request request = builder.build(); 

		callTimeOutMsTemp = callTimeOutMs == null ?callTimeOutMsTemp:callTimeOutMs;
		readTimeOutMsTemp = readTimeOutMs == null ?readTimeOutMsTemp:readTimeOutMs;

		client = getClient(callTimeOutMsTemp,readTimeOutMsTemp,proxyActive);

		return client.newCall(request).execute();
	}

	/**
	 *Get Client to Request 
	 *13 de abr. de 2023 
	 *@author F5K5WQI
	 *@param callTimeOutMs (Default=10000)
	 *@param readTimeOutMs (Default=15000)
	 *@param proxyActive
	 *@return
	 */
	private OkHttpClient getClient(long callTimeOutMsTemp,long readTimeOutMsTemp, boolean proxyActive ) {

		HttpLoggingInterceptor loggingInterceptorBody = new HttpLoggingInterceptor(message ->  System.err.println("Body> "+message));
		loggingInterceptorBody.setLevel(HttpLoggingInterceptor.Level.BODY);
		HttpLoggingInterceptor loggingInterceptorHeader = new HttpLoggingInterceptor(message -> System.err.println("Header> "+message));
		loggingInterceptorHeader.setLevel(HttpLoggingInterceptor.Level.HEADERS);

		OkHttpClient.Builder clientB = new OkHttpClient.Builder()
		.callTimeout(callTimeOutMsTemp, TimeUnit.MILLISECONDS)
		.readTimeout(readTimeOutMsTemp, TimeUnit.MILLISECONDS)
		.addInterceptor(loggingInterceptorBody)
		.addInterceptor(loggingInterceptorHeader)
		;
		
		
		//SSL
		
		clientB	.sslSocketFactory(trustAllSslSocketFactory, (X509TrustManager)trustAllCerts[0]).hostnameVerifier((hostname, session) -> true);				
		
		if(proxyActive) {
			clientB.proxy(proxy);
		}
		
		return clientB.build();
		
	}


	/**
	 * Provides a new header factory 
	 *@author F5K5WQI
	 *6 de abr. de 2023 
	 *@return HeaderFactory
	 */
	public HeaderFactory getHeaderFactory() {
		return new HeaderFactory();
	}

	/**
	 * Provides a new form factory
	 *@author F5K5WQI
	 *6 de abr. de 2023 
	 *@return FormsFactory
	 */
	public FormsFactory getFormFactory() {
		return new FormsFactory();
	}


	/**
	 * Provides a new params factory
	 *@author F5K5WQI
	 *10 de abr. de 2023 
	 */
	public ParamsFactory getParamsFactory() {
		return new ParamsFactory();
	}

}
