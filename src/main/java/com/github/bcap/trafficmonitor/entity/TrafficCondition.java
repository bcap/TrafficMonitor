package com.github.bcap.trafficmonitor.entity;

public enum TrafficCondition {
	FREE("Livre"), LIGHT_TRAFFIC("---"), HEAVY_TRAFFIC("Lento");

	private String siteString;

	private TrafficCondition(final String siteString) {
		this.siteString = siteString;
	}

	public String getSiteString() {
		return this.siteString;
	}

	public static TrafficCondition getBySiteString(final String siteString) {
		final TrafficCondition[] enums = TrafficCondition.values();
		for (final TrafficCondition condition : enums) {
			if (condition.getSiteString().equalsIgnoreCase(siteString)) {
				return condition;
			}
		}
		return null;
	}
}
