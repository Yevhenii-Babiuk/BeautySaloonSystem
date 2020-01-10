package com.saloon.beauty.web.controllers.config;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dependency")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ServiceDependencyConfig {

    @XmlElement(name = "class")
    private String serviceClass;

    @XmlElement(name = "name")
    private String serviceVarName;
}
