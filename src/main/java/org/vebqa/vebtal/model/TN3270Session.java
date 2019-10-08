package org.vebqa.vebtal.model;

public class TN3270Session {

	private String host;
	private String port;
	private String type;

	public TN3270Session() {
	}

	public TN3270Session(String aHost, String aPort, String aType) {
		this.host = aHost;
		this.port = aPort;
		this.type = aType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getType() {
		return type;
	}

	public void setType(String aType) {
		this.type = aType;
	}

	@Override
	public String toString() {
		return "TNCreateSession [host=" + this.host + ", port=" + this.port + ", type=" + this.type + "]";
	}
}
