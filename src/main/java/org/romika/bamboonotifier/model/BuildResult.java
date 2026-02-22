package org.romika.bamboonotifier.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class BuildResult {

	@XmlElement(name = "buildState")
	private String buildState;
}
