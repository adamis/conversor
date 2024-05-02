package br.com.conversor;

import java.io.IOException;

import br.com.conversor.request.RequestClientFactory;
import okhttp3.Response;

public class RequestCep {

	public static void main(String[] args) {
		
		RequestClientFactory requestClientFactory = new RequestClientFactory();
		
		for (int i = 10009000; i < 99999999; i++) {
			
			String cep = Utils.format(""+i,8,'L','0');
			
			try {
				Response sendGetRequest = requestClientFactory.sendGetRequest("http://localhost:8080/BRTMP/rest/address/cep/?cep="+cep, null, null, null, false);
				if(sendGetRequest.code() == 200) {
					System.out.println("OK");
				}
				
			} catch (IOException e) {			
				//e.printStackTrace();
			}
			if(i % 100 == 0) {
				try {
					Thread.sleep(1000);
				} catch (Exception e) {				
					//e.printStackTrace();
				}
			}
		}
		
	}
}
