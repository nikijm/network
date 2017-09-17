package com.jk.ndtetl.schedule.conf;

public class TurnOnOff {

	
	private boolean isAuto = false;
	
	private TurnOnOff() {
	}
	private static TurnOnOff too = null;
	public static TurnOnOff getInstance() {
		synchronized (TurnOnOff.class) {
			if (too == null) {
				too = new TurnOnOff();
			}
		}
		return too;
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}
}
