package com.github.bcap.trafficmonitor.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class Road implements Serializable {

	private static final long serialVersionUID = 6818426534145063185L;

	@Id
	@GeneratedValue
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	private RoadWay outgoingRoadWay;

	@OneToOne(cascade = CascadeType.ALL)
	private RoadWay incomingRoadWay;

	private String name;

	private TrafficCondition generalCondition;

	@Transient
	private String link;

	public Long getId() {
		return this.id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public RoadWay getOutgoingRoadWay() {
		return this.outgoingRoadWay;
	}

	public void setOutgoingRoadWay(final RoadWay outgoingRoadWay) {
		this.outgoingRoadWay = outgoingRoadWay;
	}

	public RoadWay getIncomingRoadWay() {
		return this.incomingRoadWay;
	}

	public void setIncomingRoadWay(final RoadWay incomingRoadWay) {
		this.incomingRoadWay = incomingRoadWay;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public TrafficCondition getGeneralCondition() {
		return this.generalCondition;
	}

	public void setGeneralCondition(final TrafficCondition generalCondition) {
		this.generalCondition = generalCondition;
	}

	public String toString() {
		final StringBuilder builder = new StringBuilder();

		builder.append("[").append(this.getClass().getSimpleName()).append(": ");
		builder.append("id=").append(this.id).append(", ");
		builder.append("name=").append(this.name).append(", ");
		builder.append("link=").append(this.link).append(", ");
		builder.append("generalCondition=").append(this.generalCondition).append(", ");
		builder.append("outgoingRoadWay=").append(this.outgoingRoadWay).append(", ");
		builder.append("incomingRoadWay=").append(this.incomingRoadWay).append("]");

		return builder.toString();
	}
}
