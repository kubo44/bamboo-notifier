package org.romika.bamboonotifier.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class BambooPlans {

	@XmlElement(name = "plan")
	private List<BambooPlan> planList = new ArrayList<>();
}
