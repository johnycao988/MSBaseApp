/**
 * 
 */
package com.cs.baseapp.httpclient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.cs.baseapp.errorhandling.BaseAppException;
import com.cs.log.logs.LogInfoMgr;

/**
 * @author Donald.Wang
 *
 */
public class BaseHttpClient {

	private CloseableHttpClient http = HttpClients.createDefault();

	public String post(String uri, Map<String, String> headers, String jsonMessage) throws BaseAppException {
		HttpPost post = new HttpPost(uri);
		CloseableHttpResponse response;
		fillRequestHead(post, headers);
		String jsonResponse = null;
		try {
			StringEntity entity = new StringEntity(jsonMessage, Charset.forName("UTF-8"));
			post.setEntity(entity);
			response = this.http.execute(post);
			if (response.getStatusLine().getStatusCode() == (HttpStatus.SC_OK)) {
				jsonResponse = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
			} else {
				throw new BaseAppException(new Exception(), LogInfoMgr.getErrorInfo("ERR_0030", uri, jsonMessage,
						response.getStatusLine().getStatusCode()));
			}
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0032", uri, jsonMessage));
		}
		return jsonResponse;
	}

	public InputStream getReturnIs(String uri) throws BaseAppException {
		HttpGet get = new HttpGet(uri);
		CloseableHttpResponse response;
		try {
			response = this.http.execute(get);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			response.getEntity().writeTo(bos);
			return new ByteArrayInputStream(bos.toByteArray());
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0031", uri));
		}
	}

	public String get(String uri) throws BaseAppException {
		HttpGet get = new HttpGet(uri);
		CloseableHttpResponse response;
		try {
			response = this.http.execute(get);
			return EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
		} catch (Exception e) {
			throw new BaseAppException(e, LogInfoMgr.getErrorInfo("ERR_0031", uri));
		}
	}

	private void fillRequestHead(HttpEntityEnclosingRequestBase base, Map<String, String> header) {
		if (header == null || header.isEmpty()) {
			base.setHeader("contentType", "application/json");
			return;
		}
		Set<Entry<String, String>> set = header.entrySet();
		for (Entry<String, String> e : set) {
			base.setHeader(e.getKey(), e.getValue());
		}
	}

}
