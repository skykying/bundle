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
     * ����һ�����
     */
    public static TableViewer createTableViewer(Composite prarent){
        /**
         * ��һ��:����һ��TableViewer����.
         * ͬʱ�ڹ��췽���ж�����ʽ��. �������óɿ��Զ�ѡ(SWT.MULTI),
         * ��ˮƽ������(SWT.H_SCROLL),�д�ֱ������(SWT.V_SCROLL),
         * �б߿�(SWT.BORDER),��������ѡ��(SWT.FULL_SELECTION)
         */
    	TableViewer tableViewer = new TableViewer(prarent, SWT.MULTI | SWT.H_SCROLL 
               | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.FULL_SELECTION );
        
        /**
         * �ڶ���:ͨ��TableViewer�е�Table���䲼��.
         */
        Table table = tableViewer.getTable();
        table.setHeaderVisible(true);//���ñ�ͷ
        table.setLinesVisible(true);//��ʾ�����
        TableLayout tLayout = new TableLayout();//ר���ڱ��Ĳ���
        table.setLayout(tLayout);
       
        /**
         * ������:����TableViewer�е���
         */
        tLayout.addColumnData(new ColumnWeightData(50));//���������ID�е��п�Ϊ10����
        new TableColumn(table, SWT.NONE).setText("Function");

        tLayout.addColumnData(new ColumnWeightData(20));//���������ID�е��п�Ϊ40����
        new TableColumn(table, SWT.NONE).setText("size");
        
        tLayout.addColumnData(new ColumnWeightData(20));//���������ID�е��п�Ϊ10����
        new TableColumn(table, SWT.NONE).setText("Type");
    
        tLayout.addColumnData(new ColumnWeightData(20));//���������ID�е��п�Ϊ10����
        new TableColumn(table, SWT.NONE).setText("Location");
        
        tLayout.addColumnData(new ColumnWeightData(20));//���������ID�е��п�Ϊ70����
        new TableColumn(table, SWT.NONE).setText("Path");
        
        tLayout.addColumnData(new ColumnWeightData(40));//���������ID�е��п�Ϊ70����
        new TableColumn(table, SWT.NONE).setText("Modified");
        
        tableViewer.setContentProvider(new TableViewerContentProvider());
        //5.�趨��ǩ��
        tableViewer.setLabelProvider(new TableViewerLabelProvider());
        //6.��setInput��������(��PeopleFactory������List���ϴ�����)
//        tableViewer.setInput(StackStore.getEmpty());
        
        
        //>>>>>>>>>>>>>>>>�����TableViewer1.java�������ӵ�>>>>>>>>>>>>>>>>>
        //����һ��������
        addListener(tableViewer);  
        
        return tableViewer;
    }
    
    /**
     * �����ӵļ�����
     */
    public static void addListener(TableViewer tableViewer){
        //TableViewer��˫���¼��ļ���
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {//IDoubleClickListener��һ���ӿ�
            @Override
            public void doubleClick(DoubleClickEvent event) {
                event.getSelection();
                MessageDialog.openInformation(null, "INFO", "name");
            }
        });
    
    }
}