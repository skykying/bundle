package com.lembed.lite.studio.report.core.views;

import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * The Class TableViewerLabelProvider.
 */
public class TableViewerLabelProvider implements ITableLabelProvider, ITableFontProvider, ITableColorProvider{
	
	FontRegistry registry = new FontRegistry();
	
    @Override
    public Image getColumnImage(Object element, int columnIndex) {
        return null;//这个和getColumnText()不同的是,这个方法可以返回一个null值.
//        return new Image(null, Icon.Size16.EXPORT);
    }

    /**
     * 由此方法决定数据记录在表格的每一列是显示什么文字
     * @param element 实体类对象
     * @param columnIndex 列号，0是第一列
     * @return 返回值一定要避免NULL值,否则出错
     */
    @Override
    public String getColumnText(Object element, int columnIndex) {
        CompilerOption people = (CompilerOption) element;
        if(columnIndex == 0){
            return people.getDescription();
        }
        
        if(columnIndex == 1){
            return people.getResource();
        }
        
        if(columnIndex == 2){
            return people.getPath();
        }
        
        if(columnIndex == 3){
            return people.getLocation();       
        }
        
        if(columnIndex == 4){
            return people.getType();
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
		CompilerOption problem = (CompilerOption) element;
		if(problem.getType().equals("Error")) { //$NON-NLS-1$
			return Display.getCurrent().getSystemColor(SWT.COLOR_RED);	
		}else if(problem.getType().equals("Info")) { //$NON-NLS-1$
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);	
		}

		return Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);				
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