package at.fhtw.server.server;

import java.util.HashMap;
import java.util.Map;

public class HeaderMap {
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";
    public static final String CONTENT_TYPE_HEADER = "Content-Type";
    public static final String AUTHORIZATION_TOKEN_HEADER = "Authorization";
    public static final String HEADER_NAME_VALUE_SEPARATOR = ":";
    private Map<String, String> headers = new HashMap<>();
    public void ingest(String headerLine) {
        final String[] split = headerLine.split(HEADER_NAME_VALUE_SEPARATOR, 2);
        headers.put(split[0], split[1].trim());
    }
    public String getHeader(String headerName) {
        return headers.get(headerName);
    }
    public void setHeader(String key, String value) {
        headers.put(key, value);
    }
    public int getContentLengthHeader() {
        final String header = headers.get(CONTENT_LENGTH_HEADER);
        if (header == null) {
            return 0;
        }
        return Integer.parseInt(header);
    }
    public void setContentLengthHeader(String value) {
        headers.put(CONTENT_LENGTH_HEADER, value);
    }
    public String getContentTypeHeader() {
        return headers.get(CONTENT_TYPE_HEADER);
    }
    public void setContentTypeHeader(String value) {
        headers.put(CONTENT_TYPE_HEADER, value);
    }
    public String getAuthorizationTokenHeader() {
        String token = headers.get(AUTHORIZATION_TOKEN_HEADER);
        if(token != null && token.toLowerCase().startsWith("basic ")) {
            token = token.substring(6);
        }
        return token;
    }
    public void setAuthorizationTokenHeader(String value) {
        headers.put(AUTHORIZATION_TOKEN_HEADER, value);
    }
    public void print() {
        System.out.println(headers);
    }
}
