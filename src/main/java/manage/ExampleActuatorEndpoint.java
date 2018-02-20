package manage;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExampleActuatorEndpoint implements Endpoint<List<String>> {

    @Override
    public String getId() {
        return "exampleEndpoint";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isSensitive() {
        return true;
    }

    @Override
    public List<String> invoke() {
        List<String> messages = new ArrayList<String>();
        messages.add("First object");
        messages.add("Second Object");
        return messages;
    }
}