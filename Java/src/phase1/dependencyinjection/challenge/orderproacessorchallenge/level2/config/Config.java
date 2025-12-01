package phase1.dependencyinjection.challenge.orderproacessorchallenge.level2.config;

import java.util.Map;

public class Config {
    private final Map<String, String> props;

    public Config(Map<String, String> props) {
        this.props = Map.copyOf(props);
    }

    public String get(String key) { return props.get(key); }
    public double getDouble(String key, double defaultValue) {
        String val = props.get(key);
        return val != null ? Double.parseDouble(val) : defaultValue;
    }
}
