/**
 * 
 */
package br.com.conversor.request;

import java.util.HashMap;

/**
 * @author F5K5WQI
 *
 */
public class ParamsFactory {

	private HashMap<String, Object> hm = new HashMap<>();
	
	/**
	 * Add a param
	 *@author F5K5WQI
	 *10 de abr. de 2023 
	 */
	public void addParams(String key, Object value) {
		hm.put(key, value);
	}	
	
	/**
	 * Remove a param
	 *@author F5K5WQI
	 *10 de abr. de 2023 
	 *@param key
	 */
	public void removeParams(String key) {
		hm.remove(key);
	}
	
	/**
	 * Alter a param
	 *@author F5K5WQI
	 *10 de abr. de 2023 
	 *@param key
	 */
	public void alterParams(String key, Object value) {
		hm.replace(key, value);
	}
	
	/**
	 *@author F5K5WQI
	 *10 de abr. de 2023 
	 */
	public String build() {
		String values = "";
		
		for ( String key : hm.keySet() ) {
			
			if(!values.equals("")) {
				values += "&"+key+"="+hm.get(key);
			}else {
				values += key+"="+hm.get(key);
			}
			
		}
		
		return values;
	}
	
}
