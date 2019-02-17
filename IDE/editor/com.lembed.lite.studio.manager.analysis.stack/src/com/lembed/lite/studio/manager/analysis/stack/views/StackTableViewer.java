package com.lembed.lite.studio.manager.analysis.stack.views;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class StackTableViewer {
    /**
     * 创建一个表格
     */
    public static TableViewer createTableViewer(Composite prarent){
        /**
         * 第一步:定义一个TableViewer对象.
         * 同时在构造方法中定义其式样. 这里设置成可以多选(SWT.MULTI),
         * 有水平滚动条(SWT.H_SCROLL),有垂直滚动条(SWT.V_SCROLL),
         * 有边框(SWT.BORDER),可以整行选择(SWT.FULL_SELECTION)
         */
    	TableViewer tableViewer = new TableViewer(prarent, SWT.MULTI | SWT.H_SCROLL 
               | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.FULL_SELECTION );
        
        /**
         * 第二步:通过TableViewer中的Table对其布局.
         */
        Table table = tableViewer.getTable();
        table.setHeaderVisible(true);//设置标头
        table.setLinesVisible(true);//显示表格线
        TableLayout tLayout = new TableLayout();//专用于表格的布局
        table.setLayout(tLayout);
       
        /**
         * 第三步:建立TableViewer中的列
         */
        tLayout.addColumnData(new ColumnWeightData(50));//这个是设置ID列的列宽为10像素
        new TableColumn(table, SWT.NONE).setText("Function");

        tLayout.addColumnData(new ColumnWeightData(20));//这个是设置ID列的列宽为40像素
        new TableColumn(table, SWT.NONE).setText("size");
        
        tLayout.addColumnData(new ColumnWeightData(20));//这个是设置ID列的列宽为10像素
        new TableColumn(table, SWT.NONE).setText("Type");
    
        tLayout.addColumnData(new ColumnWeightData(20));//这个是设置ID列的列宽为10像素
        new TableColumn(table, SWT.NONE).setText("Location");
        
        tLayout.addColumnData(new ColumnWeightData(20));//这个是设置ID列的列宽为70像素
        new TableColumn(table, SWT.NONE).setText("Path");
        
        tLayout.addColumnData(new ColumnWeightData(40));//这个是设置ID列的列宽为70像素
        new TableColumn(table, SWT.NONE).setText("Modified");
        
        tableViewer.setContentProvider(new TableViewerContentProvider());
        //5.设定标签器
        tableViewer.setLabelProvider(new TableViewerLabelProvider());
        //6.用setInput输入数据(把PeopleFactory产生的List集合传进来)
//        tableViewer.setInput(StackStore.getEmpty());
        
        
        //>>>>>>>>>>>>>>>>相比于TableViewer1.java中新增加的>>>>>>>>>>>>>>>>>
        //增加一个监听器
        addListener(tableViewer);  
        
        return tableViewer;
    }
    
    /**
     * 新增加的监听器
     */
    public static void addListener(TableViewer tableViewer){
        //TableViewer的双击事件的监听
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {//IDoubleClickListener是一个接口
            @Override
            public void doubleClick(DoubleClickEvent event) {
                event.getSelection();
                MessageDialog.openInformation(null, "INFO", "name");
            }
        });
    
    }
}