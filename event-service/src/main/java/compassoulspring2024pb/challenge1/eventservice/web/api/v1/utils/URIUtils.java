package compassoulspring2024pb.challenge1.eventservice.web.api.v1.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public class URIUtils {
    public static URI generateResourceURI(UUID resourceId) {
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();
    }
}
