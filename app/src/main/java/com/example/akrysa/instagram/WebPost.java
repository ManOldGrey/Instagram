package com.example.akrysa.instagram;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class WebPost {

	public WebPost() {
	}

	public String post(String strUrl, List<NameValuePair> params) throws Exception {
		InputStream stream;
		String response;
		
		try {

			HttpClient httpClient 	= new DefaultHttpClient();
			HttpPost httpPost 		= new HttpPost(strUrl);
			
	        httpPost.setEntity(new UrlEncodedFormEntity(params));
	        
	        HttpResponse httpResponse 	= httpClient.execute(httpPost);			
			HttpEntity httpEntity 		= httpResponse.getEntity();
			
			if (httpEntity == null) {
				throw new Exception("Request returns empty result");
			}
			
			stream		= httpEntity.getContent();			
			response	= streamToString(stream);
			
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
			}			
		} catch (Exception e) {
			throw e;
		}
		
		return response;
	}

    public static String streamToString(InputStream is) throws IOException {
        String str  = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader 	= new BufferedReader(new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }
}