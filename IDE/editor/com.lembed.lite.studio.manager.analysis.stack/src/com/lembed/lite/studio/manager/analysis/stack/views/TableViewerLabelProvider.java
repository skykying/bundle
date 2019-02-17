package com.lembed.lite.studio.manager.analysis.stack.views;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import com.lembed.lite.studio.manager.analysis.stack.StackAnalyzerPlugin;
import com.lembed.lite.studio.manager.analysis.stack.model.StackElement;


@SuppressWarnings("javadoc")
public class TableViewerLabelProvider implements ITableLabelProvider, ITableFontProvider, ITableColorProvider{
	
	FontRegistry registry = new FontRegistry();
	
    @Override
    public Image getColumnImage(Object element, int columnIndex) {
    	LocalResourceManager jfaceRsManager = new LocalResourceManager(
                JFaceResources.getResources(),
                Display.getCurrent().getShells()[0]);
    	
    	ImageDescriptor imageDesc = StackAnalyzerPlugin.getImageDescriptor(StackAnalyzerPlugin.FUNCTION);
    	Image image = jfaceRsManager.createImage(imageDesc);
        if(columnIndex == 0) {
        	return image;
        }
        return null;
    }

    /**
     * 由此方法决定数据记录在表格的每一列是显示什么文字
     * @param element 实体类对象
     * @param columnIndex 列号，0是第一列
     * @return 返回值一定要避免NULL值,否则出错
     */
    @Override
    public String getColumnText(Object element, int columnIndex) {
        StackElement stack = (StackElement) element;
        if(columnIndex == 0){
            return stack.getFunction();
        }
        
        if(columnIndex == 1){
            return stack.getSize();
        }
        
        if(columnIndex == 2){
            return stack.getType();
        }
        
        if(columnIndex == 3){
            return stack.getLocation();       
        }
        
        if(columnIndex == 4){
            return stack.getPath();
        }
        
        if(columnIndex == 5){
            return stack.getTimeStamp();
        }
        
        //当都不符合的时候返回一个空字符串
        return ""; //$NON-NLS-1$
    }
    
    @Override
    public void addListener(ILabelProviderListener listener) {
        
    }
    
    @Override
    public void dispose() {
        
    }
    
    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }
    
    @Override
    public void removeListener(ILabelProviderListener listener) {
        
    }

	@Override
	public Color getForeground(Object element, int columnIndex) {
		StackElement problem = (StackElement) element;
		if(problem.getType().equals("Error")) { //$NON-NLS-1$
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);	
		}else if(problem.getType().equals("Info")) { //$NON-NLS-1$
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);	
		}

		return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);				
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE); 
	}

	@Override
	public Font getFont(Object element, int columnIndex) {

		return registry.get(Display.getCurrent().getSystemFont().getFontData()[0].getName());
	}
}