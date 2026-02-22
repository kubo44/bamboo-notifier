package org.romika.bamboonotifier.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class BambooPlan {

    @XmlAttribute(name = "key")
    private String key;

    @XmlAttribute(name = "name")
    private String name;
}
