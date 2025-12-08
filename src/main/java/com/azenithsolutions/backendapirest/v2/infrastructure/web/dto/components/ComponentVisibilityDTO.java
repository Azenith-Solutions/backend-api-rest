package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components;

public class ComponentVisibilityDTO {
    private Boolean isVisibleCatalog;

    public ComponentVisibilityDTO() {}

    public ComponentVisibilityDTO(Boolean isVisibleCatalog) {
        this.isVisibleCatalog = isVisibleCatalog;
    }

    public Boolean getIsVisibleCatalog() {
        return isVisibleCatalog;
    }

    public void setIsVisibleCatalog(Boolean visibleCatalog) {
        isVisibleCatalog = visibleCatalog;
    }
}
