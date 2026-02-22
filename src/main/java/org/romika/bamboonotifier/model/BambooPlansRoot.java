package org.romika.bamboonotifier.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "plans")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class BambooPlansRoot {

	@XmlElement(name = "plans")
	private BambooPlans plans;
}
