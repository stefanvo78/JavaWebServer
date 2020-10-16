package sv.javawebserver.headerimplementation;

import java.util.*;

public class HttpHeader {
    private String name;
    private List<String> values;

    // public HttpHeader(String name) {
    //     this(name, new ArrayList<>());
    // }

    public HttpHeader(String name, Collection<String> values) {
        this.name = name;
        this.values = new ArrayList<>(values);
    }

    public HttpHeader(String name, String value) {
        this(name, Collections.singletonList(value));
    }

    public HttpHeader(String name, String... values) {
        this(name, Arrays.asList(values));
    }

    public String getName() {
        return name;
    }

    public List<String> getValues() {
        return values;
    }

    public String getValue() {
        return !values.isEmpty() ? values.get(0) : null;
    }

    public HttpHeader add(String value) {
        values.add(value);
        return this;
    }

    public HttpHeader add(String... values) {
        Collections.addAll(this.values, values);
        return this;
    }

    @Override
	public String toString() {
		return name + ": " + String.join(",", values);
	}
}
