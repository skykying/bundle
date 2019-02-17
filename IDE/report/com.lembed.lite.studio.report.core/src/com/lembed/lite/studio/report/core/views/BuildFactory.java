package com.lembed.lite.studio.report.core.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.cdt.codan.core.model.ICodanProblemMarker;

import com.lembed.lite.studio.report.core.manager.BuildManager;

@SuppressWarnings("javadoc")
public class BuildFactory {

	public static List<CompilerOption> getProblems() {
		// 声明一个list对象来放People类的对象
		List<CompilerOption> list = new ArrayList<>();

		BuildManager manager = BuildManager.getInstance();
		HashMap<Long, ICodanProblemMarker> cache = manager.getProblemCache();
		
		for (Entry<Long, ICodanProblemMarker> pm : cache.entrySet()) {
			CompilerOption ele = new CompilerOption();
			ele.setDescription(pm.getValue().createMessage());
			ele.setResource(pm.getValue().getResource().getName());
			ele.setLocation(pm.getValue().getLocation().getLineNumber() + ""); //$NON-NLS-1$
			ele.setPath(pm.getValue().getResource().getLocation().toString());
			ele.setType(pm.getValue().getProblem().getSeverity().name());
			list.add(ele);
		}

		return list;
	}
	
	
	public static List<CompilerOption> getEmpty(){
		List<CompilerOption> list = new ArrayList<>();
		return list;
	}
}