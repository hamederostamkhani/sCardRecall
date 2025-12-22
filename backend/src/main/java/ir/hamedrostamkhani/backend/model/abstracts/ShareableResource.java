package ir.hamedrostamkhani.backend.model.abstracts;

import ir.hamedrostamkhani.backend.model.enums.ResourceType;

public interface ShareableResource {
    Long getResourceId();
    ResourceType getResourceType();
}
