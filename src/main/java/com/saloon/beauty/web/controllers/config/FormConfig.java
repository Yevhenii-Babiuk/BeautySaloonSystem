package com.saloon.beauty.web.controllers.config;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Container for form's parameters  read
 * from {@code ActionServlet} config file
 */
@Getter
@Setter
@EqualsAndHashCode
@XmlRootElement(name = "form")
@XmlAccessorType(XmlAccessType.FIELD)
class FormConfig {

    /**
     * Form name in Action config with which this form bound
     */
    private String name;

    /**
     * Full qualified name of current form class
     */
    @EqualsAndHashCode.Exclude
    private String type;

}
