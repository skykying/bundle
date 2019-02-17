package com.lembed.lite.studio.manager.analysis.stack.views;

import java.util.ArrayList;
import java.util.List;

import com.lembed.lite.studio.manager.analysis.stack.model.StackElement;

@SuppressWarnings("javadoc")
public class StackStore {



	public static List<StackElement> getEmpty(){
		List<StackElement> list = new ArrayList<>();
		StackElement se = new StackElement();
		se.setFunction("led_task"); //$NON-NLS-1$
		se.setSize("8"); //$NON-NLS-1$
		se.setType("STATIC"); //$NON-NLS-1$
		se.setLocation("main.c:49"); //$NON-NLS-1$
		se.setPath("main.c"); //$NON-NLS-1$
		se.setTimeStamp("2017年9月12日 下午2:18:14"); //$NON-NLS-1$
		list.add(se);
		return list;
	}
}