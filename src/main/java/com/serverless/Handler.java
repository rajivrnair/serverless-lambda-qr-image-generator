package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(Handler.class);

	@SuppressWarnings("rawtypes")
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("received: " + input);
		Map qrContent = (Map) input.get("queryStringParameters");
		LOG.info("qrContent: " + qrContent);
		String content = (String) qrContent.get("content");

		byte[] image = new QRCodeGenerator().generate(content);
		Map<String, String> headers = new HashMap<>();
		headers.put("X-Powered-By", "Cats");
		headers.put("Content-Type", "image/png");
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setBinaryBody(image)
				.setHeaders(headers)
				.build();
	}
}
