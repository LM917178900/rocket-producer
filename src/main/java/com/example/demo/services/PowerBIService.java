package com.example.demo.services;

//import com.embedsample.appownsdata.config.Config;
//import com.embedsample.appownsdata.models.EmbedConfig;
//import com.embedsample.appownsdata.models.EmbedToken;
//import org.json.JSONArray;
//import org.json.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.Config;
import com.example.demo.models.EmbedConfig;
import com.example.demo.models.EmbedToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Service with helper methods to get report's details and multi-resource embed token
 */
public class PowerBIService {
	
	static final Logger logger = LoggerFactory.getLogger(PowerBIService.class);

	// Prevent instantiation 
	private PowerBIService() {
		throw new IllegalStateException("Power BI service class");
	}
		
	/**
	 * Get required values like embedUrl and DatasetId for embedding the report
	 * @return EmbedConfig object
	 */
	public static EmbedConfig getReportEmbedDetails(String reportId, String groupId, String accessToken) {
		if (reportId.isEmpty()) {
			throw new RuntimeException("Empty Report Id");
		}
		if (groupId.isEmpty()) {
			throw new RuntimeException("Empty Group(Workspace) Id");
		}
		
		// Get Report In Group API: https://api.powerbi.com/v1.0/myorg/groups/{groupId}/reports/{reportId}
//		StringBuilder urlStringBuilder = new StringBuilder("https://api.powerbi.com/v1.0/myorg/groups/");
		StringBuilder urlStringBuilder = new StringBuilder("https://api.powerbi.cn/v1.0/myorg/groups/");
//		StringBuilder urlStringBuilder = new StringBuilder("https://app.powerbi.cn/groups/");

		urlStringBuilder.append(groupId);
		urlStringBuilder.append("/reports/");
		urlStringBuilder.append(reportId);
//		urlStringBuilder.append("/ReportSection");

		// REST API URL to get report details
		String endPointUrl = urlStringBuilder.toString();
		
		// Request header
    	HttpHeaders reqHeader = new HttpHeaders();
    	reqHeader.put("Content-Type", Arrays.asList("application/json"));
    	reqHeader.put("Authorization", Arrays.asList("Bearer " + accessToken));
    	
    	// HTTP entity object - holds header and body
		HttpEntity<String> reqEntity = new HttpEntity<>(reqHeader);
		
		// Rest API get report's details
		RestTemplate getReportRestTemplate = new RestTemplate();
		ResponseEntity<String> response = getReportRestTemplate.exchange(endPointUrl, HttpMethod.GET, reqEntity, String.class);
		
		HttpHeaders responseHeader = response.getHeaders();
		String responseBody = response.getBody();
		
		// Create embedding configuration object
		EmbedConfig reportEmbedConfig = new EmbedConfig();
		
		// Parse JSON and get Report details
		JSONObject responseObj = JSONObject.parseObject(responseBody);
		reportEmbedConfig.embedUrl = responseObj.getString("embedUrl");
		reportEmbedConfig.datasetId = responseObj.getString("datasetId");
		reportEmbedConfig.reportId = responseObj.getString("id");
		
		if (Config.DEBUG) {
			// Log the request Id
			List<String> reqIdList = responseHeader.get("RequestId");
			
			// Log progress
			logger.info("Retrieved report details");
						
			// Log Request Id
			if (!reqIdList.isEmpty()) {
				for (String reqId: reqIdList) {
					logger.info("Request Id: {}", reqId);
				}
			}
    	}
		
		return reportEmbedConfig;
	}


	/**
	 * Get embed token for multiple workspaces, datasets, and reports. 
	 * @see <a href="https://aka.ms/MultiResourceEmbedToken">Multi-Resource Embed Token</a>
	 * @param reportId
	 * @param datasetId
	 * @return EmbedToken 
	 */
	public static EmbedToken getMultiResourceEmbedToken(String reportId, String datasetId, String accessToken) {
		// Embed Token - Generate Token REST API
//		String uri = "https://api.powerbi.com/v1.0/myorg/GenerateToken";
		String uri = "https://api.powerbi.cn/v1.0/myorg/GenerateToken";


		RestTemplate restTemplate = new RestTemplate();
		
		// Create request header
    	HttpHeaders headers = new HttpHeaders();
    	headers.put("Content-Type", Arrays.asList("application/json"));
    	headers.put("Authorization", Arrays.asList("Bearer " + accessToken));
    	
    	// Request body
    	JSONObject requestBody = new JSONObject();
    	
    	// Add dataset id in body
    	JSONArray jsonDatasets = new JSONArray();
    	jsonDatasets.add(new JSONObject().put("id", datasetId));
    	
    	// Add report id in body
    	JSONArray jsonReports = new JSONArray();
    	jsonReports.add(new JSONObject().put("id", reportId));
    	
    	requestBody.put("datasets", jsonDatasets);
    	requestBody.put("reports", jsonReports);
    	
    	// Add (body, header) to HTTP entity
		HttpEntity<String> httpEntity = new HttpEntity<>(requestBody.toString(), headers);
		
		// Call the API
		ResponseEntity<String> response = restTemplate.postForEntity(uri, httpEntity, String.class);
		HttpHeaders responseHeader = response.getHeaders();
		String responseBody = response.getBody();
		
		// Parse responseBody
		JSONObject jsonResponse =JSONObject.parseObject(responseBody);
		
		String token = jsonResponse.getString("token");
		String tokenId = jsonResponse.getString("tokenId");
		String expiration = jsonResponse.getString("expiration");

		EmbedToken embedToken = new EmbedToken(token, tokenId, expiration);

		if (Config.DEBUG) {
			// Log the request Id
			List<String> reqIdList = responseHeader.get("RequestId");
			
			// Log progress
			logger.info("Retrieved Embed token\nEmbed Token Id: {}", embedToken.tokenId);
			
			// Log Request Id
			if (!reqIdList.isEmpty()) {
				for (String reqId: reqIdList) {
					logger.info("Request Id: {}", reqId);
				}
			}
		}
		
		return embedToken;
	}
}
