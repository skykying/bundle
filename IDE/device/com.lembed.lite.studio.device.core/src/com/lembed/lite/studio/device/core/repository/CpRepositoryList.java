/*******************************************************************************
 *  Copyright (c) 2016 ARM Ltd. and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Liviu Ionescu - initial implementation
 *     ARM Ltd and ARM Germany GmbH - application-specific implementation
 *******************************************************************************/

package com.lembed.lite.studio.device.core.repository;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.core.CpPlugIn;
import com.lembed.lite.studio.device.core.preferences.CpPreferenceInitializer;

/**
 * The singleton class that contains the repository urls
 */
public class CpRepositoryList {


	public static final String[] TYPES = { CmsisConstants.REPO_PACK_TYPE };

	protected List<ICpRepository> fList;

	public CpRepositoryList() {
		List<String> repos = CpPreferenceInitializer.getCpRepositories();
		for (String repoAttr : repos) {
			ICpRepository repo = new CpRepository(repoAttr);
			if (fList == null) {
				fList = new ArrayList<ICpRepository>();
			}
			fList.add(repo);
		}
	}

	/**
	 * @return the default file lists
	 */
	public List<ICpRepository> getDefaultList() {

		List<ICpRepository> list;
		list = new ArrayList<ICpRepository>();

		list.add(new CpRepository(CpPreferenceInitializer.getDefaultCpRepository()));

		fList = list;

		return list;
	}

	public String[] convertToArray(ICpRepository repo) {

		String sa[] = new String[repo.getAttrCount()];
		sa[0] = repo.getType();
		sa[1] = repo.getName();
		sa[2] = repo.getUrl();

		return sa;
	}

	public ICpRepository convertToCpRepository(String[] sa) {
		if (sa.length != getDefaultList().get(0).getAttrCount()) {
			return null;
		}

		return new CpRepository(sa[0], sa[1], sa[2]);
	}

	// Return a list of urls where repositories are stored
	public List<ICpRepository> getList() {

		if (fList == null || fList.isEmpty()) {
			return getDefaultList();
		}
		return fList;
	}

	public void putList(List<ICpRepository> repoList) {
		fList = repoList;
		IEclipsePreferences instancePreferences = InstanceScope.INSTANCE.getNode(CpPlugIn.PLUGIN_ID);
		for (int i = 0; i < repoList.size(); i++) {
			ICpRepository repo = repoList.get(i);
			String key = CpPlugIn.CMSIS_PACK_REPOSITORY_PREFERENCE + '.' + i;
			instancePreferences.put(key, repo.toString());
		}
		int i = repoList.size();
		String key = CpPlugIn.CMSIS_PACK_REPOSITORY_PREFERENCE + '.' + i;
		while (!instancePreferences.get(key, CmsisConstants.EMPTY_STRING).isEmpty()) {
			instancePreferences.remove(key);
			i++;
			key = CpPlugIn.CMSIS_PACK_REPOSITORY_PREFERENCE + '.' + i;
		}
	}

}
