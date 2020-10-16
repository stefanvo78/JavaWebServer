package sv.javawebserver.headerimplementation;

import java.util.*;

public class HttpHeaders {
    private Map<String, HttpHeader> headers;

    public HttpHeaders() {
        headers = new LinkedHashMap<>();
    }

    public HttpHeaders(HttpHeader... headers) {
		this();
		for (HttpHeader header : headers) {
			add(header);
		}
	}

    public boolean contains(String key) {
        return headers.containsKey(key.toLowerCase());
    }

    public String value(String key) {
        return contains(key) ? headers.get(key.toLowerCase()).getValue() : null;
    }

    public List<String> values(String key) {
        return contains(key) ? headers.get(key.toLowerCase()).getValues() : null;
    }

    public HttpHeaders add(HttpHeader header) {
        if (contains(header.getName())) {
            headers.get(header.getName().toLowerCase()).add(header.getValues().toArray(new String[] {}));
        } else {
            return set(header);
        }

        return this;
    }

    public HttpHeaders set(HttpHeader header) {
        headers.put(header.getName().toLowerCase(), new HttpHeader(header.getName(), header.getValues()));
        return this;
    }

    public HttpHeaders set(String key, String value) {
        headers.put(key.toLowerCase(), new HttpHeader(key, new String[]{value}));
        return this;
    }
    
    @Override
    public String toString() {
		StringBuilder output = new StringBuilder();
		for (HttpHeader header : headers.values()) {
			output.append(header.toString())
					.append("\r\n");
        }
		return output.toString();
	}
}
