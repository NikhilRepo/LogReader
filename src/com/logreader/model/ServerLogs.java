package com.logreader.model;


public class ServerLogs {
	private String id;
	private String state;
	private long timestamp;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public ServerLogs(String id, String state, long timestamp) {
		super();
		this.id = id;
		this.state = state;
		this.timestamp = timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "ServerLogs [id=" + id + ", state=" + state + ", timestamp=" + timestamp + "]";
	}
	
}
