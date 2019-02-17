package com.lembed.lite.studio.report.core.views;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * The Class TableViewerContentProvider.
 */
public class TableViewerContentProvider implements IStructuredContentProvider {

	    @Override
	    public void dispose() {
	        
	    }

	    @Override
	    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	        
	    }
	    

	    @Override
	    public Object[] getElements(Object inputElement) {
	        if(inputElement instanceof List){
	            return ((List<?>)inputElement).toArray();
	        }
            return new Object[0];
	    }
	

}
