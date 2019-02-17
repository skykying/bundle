package com.lembed.lite.studio.manager.analysis.stack.views;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

@SuppressWarnings("javadoc")
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
	            return ((List)inputElement).toArray();
	        }
            return new Object[0];
	    }
	

}
