/*******************************************************************************
 * Copyright (c) 2017 LEMBED 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package com.lembed.lite.studio.device.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ICElementVisitor;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

public class ResourceUtils {
	private static IPath fpath = null;
	/**
	 * //http://stackoverflow.com/questions/12421787/get-the-list-of-source\
	 * -files-from-existing-eclipse-project-using-cdt
	 * 
	 * @param project
	 */
	public static void findSourceFiles(final IProject project) {
		final ICProject cproject = CoreModel.getDefault().create(project);
		if (cproject != null) {
			try {
				cproject.accept(new ICElementVisitor() {

					@Override
					public boolean visit(final ICElement element) throws CoreException {
						System.out.println(element.getElementName());

						if (element.getElementType() == ICElement.C_UNIT) {
							ITranslationUnit unit = (ITranslationUnit) element;
							if (unit.isSourceUnit()) {
								log(element.getElementName());
								log(element.getClass().getName().toString());
								log(element.getUnderlyingResource().getFullPath().toString());
							}

							return false;
						} else {
							return true;
						}
					}
				});
			} catch (final CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * list the files of the directory, with extension filter
	 * 
	 * @param folder
	 * @param ext file name extension
	 * @return file list
	 */
	public static List<String> getFolderFiles(String folder, final String ext) {

		List<String> list = new ArrayList<String>();
		
		File local = new File(folder);
		if (!local.isDirectory()) {
			return null;
		}
		
		File[] files = local.listFiles(new FilenameFilter() {

			/**
			 * Filter to select only
			 */
			@Override
			public boolean accept(File dir, String name) {				
				return name.toLowerCase().endsWith(ext);				
			}
		});

		if (files == null || files.length == 0) {
			return null;
		}
		
		for(File f : files){
			list.add(f.getAbsolutePath());
		}

		// The sort criteria is the lexicographical order on folder name.
		Collections.sort(list);

		return list;
	}
	
	/**
	 * list the files of the directory, with extension filter
	 * 
	 * @param folder
	 * @param ext file name extension
	 * @return file list
	 */
	public static List<String> listFolderAllFiles(String folder,String ext) {

		List<String> list = new ArrayList<String>();
		
		File local = new File(folder);
		if (!local.isDirectory()) {
			return null;
		}
		
		File[] files = local.listFiles();
		for(File f : files){
			if(f.isDirectory()){
				List<String> sfiles = listFolderAllFiles(f.getAbsolutePath(),ext);
				list.addAll(sfiles);
			}else{
				list.add(f.getAbsolutePath());
			}
		}

		if (files == null || files.length == 0) {
			return null;
		}
		
		for(String path : list){
			if(!path.toLowerCase().endsWith(ext)){
				list.remove(path);
			}
		}

		// The sort criteria is the lexicographical order on folder name.
		Collections.sort(list);

		return list;
	}
	
	public static IPath findSourceFiles(final IProject project, String name) {
		final ICProject cproject = CoreModel.getDefault().create(project);
		
		
		if (cproject != null) {
			try {
				
				cproject.accept(new ICElementVisitor() {
					
					@Override
					public boolean visit(final ICElement element) throws CoreException {
						
						if (element.getElementType() == ICElement.C_UNIT) {
							String eName = element.getElementName();
							log(element.getPath().toFile().getName());
							if(eName.equals(name)){
								IPath path = element.getPath();
								fpath = path;
								return false;
							} else {
								return true;
							}
						}
						return true;
					}
					
					
				});				
		
			} catch (final CoreException e) {
				e.printStackTrace();
			}
		}
		
		return fpath;
	}

	public static void log(String msg) {
		System.out.println(msg);
	}
}
