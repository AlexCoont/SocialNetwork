package project.models.util.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImagePath {

    @Value("${response.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    public String getDefaultImagePath() {
        return "http://" + host + ":" + port + "/api/v1/storage/default";
    }

    public String getImagePath() {
        return "http://" + host + ":" + port + "/api/v1/storage/";
    }
}
