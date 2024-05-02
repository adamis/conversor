/**
 * 
 */
package br.com.conversor.request;

import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.FormBody.Builder;

/**
 * @author F5K5WQI
 *
 */
public class FormsFactory {
	
	private Builder form = null;

	/* ------------------------------------FORMS----------------------------------------- */
	
	
	/**
	 * Add param with FORM
	 *@author F5K5WQI
	 *4 de abr. de 2023 
	 */
	public void addFormParam(String key, String value) {
		if(form == null) {
			form = new FormBody.Builder();
		}

		form.add(key, value);

	}

	/**
	 * Add param with FORM from JSON OBJECT
	 *@author F5K5WQI
	 *4 de abr. de 2023 
	 *@param json
	 */
	public void addFormParam(JSONObject jsonObject) {
		if(form == null) {
			form = new FormBody.Builder();
		}

		jsonObject.keySet().forEach(keyStr ->
		{
			form.add(keyStr, jsonObject.get(keyStr).toString());    	
		});

	}
	
	public FormBody build() {
		return form.build();
	}
	

}
