package com.saloon.beauty.web.controllers.config;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Container holds access configuration
 * for constrained resource
 */
@XmlRootElement(name = "resource")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
class ResourceAccessConfig {

    /**
     * URL-pattern of constrained resource
     */
    @XmlElement(name = "url-pattern")
    private String urlPattern;

    /**
     * List of roles need access to the
     * constrained resource
     */
    @XmlElementWrapper(name = "allowed-roles")
    @XmlElement(name = "role")
    private List<String> roles;
}
