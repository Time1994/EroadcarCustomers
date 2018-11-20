package com.amos.eroadcarcustomers.bean;

import java.io.Serializable;

public class CommonBean<T> implements Serializable {
	private String state;
	private String msg;
	private T data;
	private T infoList;
	private T mapCarList;

	public T getMapCarList() {
		return mapCarList;
	}

	public void setMapCarList(T mapCarList) {
		this.mapCarList = mapCarList;
	}

	public T getInfoList() {
		return infoList;
	}

	public void setInfoList(T infoList) {
		this.infoList = infoList;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
