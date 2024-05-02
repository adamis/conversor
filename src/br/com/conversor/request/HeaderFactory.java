/**
 * 
 */
package br.com.conversor.request;

import org.json.JSONObject;

import okhttp3.Headers;

/**
 * @author F5K5WQI
 *
 */
public class HeaderFactory {

	private okhttp3.Headers.Builder headers = null;
	
	/* ------------------------------------HEADERS----------------------------------------- */

	/**
	 * Add Header Key/Value
	 *@author F5K5WQI
	 *4 de abr. de 2023 
	 */
	public void addHeader(String key,String value) {
		if(headers == null) {
			headers = new Headers.Builder();
		}			 
		headers.add(key, value);	
	}

	/**
	 * Add Header JSON
	 *@author F5K5WQI
	 *4 de abr. de 2023 
	 *@param json
	 */
	public void addHeader(String json) {
		if(headers == null) {
			headers = new Headers.Builder();
		}	

		JSONObject jsonObject = new JSONObject(json);

		jsonObject.keySet().forEach(keyStr ->
		{
			headers.add(keyStr, jsonObject.get(keyStr).toString());    	
		});
	}	
	
	/**
	 * Add Token Authorization
	 *@author F5K5WQI
	 *4 de abr. de 2023 
	 */
	public void addAuthorization(String token) {
		if(headers == null) {
			headers = new Headers.Builder();
		}			 
		headers.add("Authorization", "Bearer "+token);
	}
	
	/**
	 *@author F5K5WQI
	 *6 de abr. de 2023 
	 * @return 
	 */
	public Headers build() {
		return headers.build();
	}
	
}
