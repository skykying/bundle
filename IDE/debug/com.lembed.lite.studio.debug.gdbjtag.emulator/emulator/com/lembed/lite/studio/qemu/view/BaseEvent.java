package com.lembed.lite.studio.qemu.view;

import java.awt.event.ActionEvent;

public class BaseEvent extends ActionEvent{

	public BaseEvent(Object source, int id, String command) {
		super(source, id, command);
	}

}
