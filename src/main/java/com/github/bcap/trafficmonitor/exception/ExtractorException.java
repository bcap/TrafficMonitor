package com.github.bcap.trafficmonitor.exception;

public class ExtractorException extends Exception {

	private static final long serialVersionUID = -4704799360229587826L;

	public ExtractorException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ExtractorException(final String message) {
		super(message);
	}
}
