/*******************************************************************************
* Copyright (c) 2017 LEMBED 
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* LEMBED- setup for LiteSTUDIO c/c++ project template
*******************************************************************************/
package com.lembed.lite.studio.device.project.template;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.lembed.lite.studio.device.common.CmsisConstants;
import com.lembed.lite.studio.device.project.Messages;
import com.lembed.lite.studio.device.ui.IStatusMessageListener;
import com.lembed.lite.studio.device.ui.StatusMessageListerenList;

public class LiteMiddleSelector extends Group{
	
	private Combo comboDebugger = null;
	private Combo comboRTOS = null;
	private Combo comboTRACE = null;
	private Combo comboUTILS = null;
	private Combo comboSYSCALL = null; 
	
	private Map<String, String> maps = new HashMap<String, String>();
	private StatusMessageListerenList listeners = new StatusMessageListerenList();
	
	private static final String[] debuggers = new String[] {Messages.DEBUGGER_JLINK, Messages.DEBUGGER_STLINK,  Messages.DEBUGGER_NULL};
	private static final String[] rtoss = new String[] { Messages.RTOS_NULL, Messages.RTOS_FREERTOS,Messages.RTOS_MNUTTX}; 
	private static final String[] utils = new String[] { Messages.UTILS_BUILD_IN, Messages.UTILS_NULL};  
	private static final String[] traces = new String[] { Messages.TRACE_STDOUT_CHANNEL, Messages.TRACE_DEBUG_CHANNEL, Messages.TRACE_NO_CHANNEL}; 
	private static final String[] syscalls = new String[] { Messages.SysCall_FREESTANDING, Messages.SysCall_POSIX,Messages.SysCall_SEMIHOSTING}; 
	
	private Label lblSelectName;
	

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public LiteMiddleSelector(Composite parent, int style) {
		super(parent, style);
		setText(Messages.LiteMiddleWareAdapterSelector_MiddleSelecter);
		setLayout(new GridLayout(3, false));
		
		initMap();
		
		new Label(this, SWT.NONE);

		lblSelectName = new Label(this, SWT.NONE);
		lblSelectName.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		lblSelectName.setText(""); //$NON-NLS-1$
		
		
		Label lbDebugger = new Label(this, SWT.NONE);
		lbDebugger.setToolTipText(Messages.LiteMiddleWareAdapterSelector_DebuggerTipText);
		lbDebugger.setText(Messages.LiteMiddleWareAdapterSelector_Debugger);

		comboDebugger = new Combo(this, SWT.READ_ONLY);
		comboDebugger.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateFeature(comboDebugger, CmsisConstants.FEATURE_PROJECT_TEMPLATE_DEBUGGER_KEY);
			}			
		});
		comboDebugger.setToolTipText(Messages.LiteMiddleWareAdapterSelector_DebuggerTipText_Combo);
		comboDebugger.setItems(debuggers);		
		comboDebugger.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboDebugger.select(0);		
		updateFeature(comboDebugger, CmsisConstants.FEATURE_PROJECT_TEMPLATE_DEBUGGER_KEY);
		
		Label lblRTOS = new Label(this, SWT.NONE);
		lblRTOS.setToolTipText(Messages.LiteMiddleWareAdapterSelector_RTOSTipText);
		lblRTOS.setText(Messages.LiteMiddleWareAdapterSelector_RTOS);

		comboRTOS = new Combo(this, SWT.READ_ONLY);
		comboRTOS.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {				
				updateFeature(comboRTOS, CmsisConstants.FEATURE_PROJECT_TEMPLATE_RTOS_KEY);
			}			
		});
		comboRTOS.setToolTipText(Messages.LiteMiddleWareAdapterSelector_RTOSTipText_Combo);
		comboRTOS.setItems(rtoss);
		comboRTOS.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboRTOS.select(0);
		updateFeature(comboRTOS, CmsisConstants.FEATURE_PROJECT_TEMPLATE_RTOS_KEY);

		Label lblTRACE = new Label(this, SWT.NONE);
		lblTRACE.setToolTipText(Messages.LiteMiddleWareAdapterSelector_TraceTipText);
		lblTRACE.setText(Messages.LiteMiddleWareAdapterSelector_Trace);

		comboTRACE = new Combo(this, SWT.READ_ONLY);
		comboTRACE.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {	
				updateFeature(comboTRACE, CmsisConstants.FEATURE_PROJECT_TEMPLATE_TRACE_KEY);
			}
		});
		comboTRACE.setToolTipText(Messages.LiteMiddleWareAdapterSelector_TraceTipText_Combo);
		comboTRACE.setItems(traces);
		comboTRACE.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboTRACE.select(0);
		updateFeature(comboTRACE, CmsisConstants.FEATURE_PROJECT_TEMPLATE_TRACE_KEY);
		
		Label lblSYSCALLS = new Label(this, SWT.NONE);
		lblSYSCALLS.setToolTipText(Messages.LiteMiddleWareAdapterSelector_SyscallsTipText);
		lblSYSCALLS.setText(Messages.LiteMiddleWareAdapterSelector_Syscalls);

		comboSYSCALL = new Combo(this, SWT.READ_ONLY);
		comboSYSCALL.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {		
				updateFeature(comboSYSCALL, CmsisConstants.FEATURE_PROJECT_TEMPLATE_SYSCALL_KEY);
			}			
		});
		comboSYSCALL.setToolTipText(Messages.LiteMiddleWareAdapterSelector_SyscallsTipText_Combo);
		comboSYSCALL.setItems(syscalls);
		comboSYSCALL.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboSYSCALL.select(2);
		updateFeature(comboSYSCALL, CmsisConstants.FEATURE_PROJECT_TEMPLATE_SYSCALL_KEY);
		
		Label lblUTILS = new Label(this, SWT.NONE);
		lblUTILS.setToolTipText(Messages.LiteMiddleWareAdapterSelector_UTILSTipText);
		lblUTILS.setText(Messages.LiteMiddleWareAdapterSelector_UTILS);

		comboUTILS = new Combo(this, SWT.READ_ONLY);
		comboUTILS.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateFeature(comboUTILS, CmsisConstants.FEATURE_PROJECT_TEMPLATE_UTILS_KEY);
			}			
		});
		comboUTILS.setToolTipText(Messages.LiteMiddleWareAdapterSelector_UTILSTipText_Combo);
		comboUTILS.setItems(utils);
		comboUTILS.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		comboUTILS.select(0);
		updateFeature(comboUTILS, CmsisConstants.FEATURE_PROJECT_TEMPLATE_UTILS_KEY);
		
		new Label(this, SWT.NONE);
		Group optionGroup = new Group(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(2, false);
		optionGroup.setLayout(gridLayout);
		optionGroup.setText(Messages.OPTIZIMATION_TITLES); 
		optionGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		new Label(optionGroup, SWT.NONE);
		Button litWaringCheck = new Button(optionGroup, SWT.CHECK);
		litWaringCheck.setText(Messages.OPTIZIMATION_USE_SOME_WARINGS); 
		litWaringCheck.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateFeature(litWaringCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_SOME_WARNINGS);
			}			
		});
		litWaringCheck.setSelection(true);
		updateFeature(litWaringCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_SOME_WARNINGS);
		
		new Label(optionGroup, SWT.NONE);
		Button mostWarCheck = new Button(optionGroup, SWT.CHECK);
		mostWarCheck.setText(Messages.OPTIMIZATION_USE_MOST_WARINGS); 
		mostWarCheck.setSelection(true);
		mostWarCheck.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateFeature(mostWarCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_MOSTWARNINGS);
			}			
		});
		mostWarCheck.setSelection(false);
		updateFeature(mostWarCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_MOSTWARNINGS);
		
		new Label(optionGroup, SWT.NONE);
		Button werrorCheck = new Button(optionGroup, SWT.CHECK);
		werrorCheck.setText(Messages.OPTIMIZATION_USE_WARING_AS_ERROR); 
		werrorCheck.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateFeature(werrorCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_WARNINGASERROR);
			}
		});
		werrorCheck.setSelection(false);
		updateFeature(werrorCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_WARNINGASERROR);
		
		new Label(optionGroup, SWT.NONE);
		Button ogDebugCheck = new Button(optionGroup, SWT.CHECK);
		ogDebugCheck.setText(Messages.OPTIMIZATION_USE_OG_ON_DEBUG); 
		ogDebugCheck.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {				
				updateFeature(ogDebugCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_OG);
			}			
		});
		ogDebugCheck.setSelection(true);
		updateFeature(ogDebugCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_OG);
		
		new Label(optionGroup, SWT.NONE);
		Button newLibCheck = new Button(optionGroup, SWT.CHECK);
		newLibCheck.setText(Messages.OPTIMIZATION_USE_NEWLIB_NANO); 
		newLibCheck.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {						
				updateFeature(newLibCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_NANO);
			}			
		});
		newLibCheck.setSelection(true);
		updateFeature(newLibCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_NANO);
		
		new Label(optionGroup, SWT.NONE);
		Button excludeCheck = new Button(optionGroup, SWT.CHECK);
		excludeCheck.setText(Messages.OPTIZIMATION_EXCLUDE_UNUSED); 
		excludeCheck.addSelectionListener(new SelectionAdapter(){

			@Override
			public void widgetSelected(SelectionEvent e) {			
				updateFeature(excludeCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_EXCLUDE_UNUSED);
			}
			
		});
		excludeCheck.setSelection(true);
		updateFeature(excludeCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_EXCLUDE_UNUSED);
		
		new Label(optionGroup, SWT.NONE);
		Button optCheck = new Button(optionGroup, SWT.CHECK);
		optCheck.setText(Messages.OPTIZIMATION_LINKER); 
		optCheck.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {					
				updateFeature(optCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_LTO);
			}			
		});
		optCheck.setSelection(true);
		updateFeature(optCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_LTO);
		
		new Label(optionGroup, SWT.NONE);
		Button coverageCheck = new Button(optionGroup, SWT.CHECK);
		coverageCheck.setText(Messages.OPTIZIMATION_GENERATOR_COVERAGE_INFO); 		
		coverageCheck.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {			
				updateFeature(coverageCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_COVERAGE);
			}			
		});
		coverageCheck.setSelection(false);
		updateFeature(coverageCheck, CmsisConstants.FEATURE_PROJECT_OPTION_USE_COVERAGE);
	
	}
	
	public void initMap(){		
		maps.put(Messages.DEBUGGER_NULL,CmsisConstants.FEATURE_PROJECT_TEMPLATE_DEBUGGER_NULL);
		maps.put(Messages.DEBUGGER_JLINK,CmsisConstants.FEATURE_PROJECT_TEMPLATE_DEBUGGER_JLINK);
		maps.put(Messages.DEBUGGER_STLINK,CmsisConstants.FEATURE_PROJECT_TEMPLATE_DEBUGGER_STLINK);
		
		maps.put( Messages.RTOS_FREERTOS,CmsisConstants.FEATURE_PROJECT_TEMPLATE_FREERTOS_VALUE);
		maps.put( Messages.RTOS_MNUTTX,CmsisConstants.FEATURE_PROJECT_TEMPLATE_MNUTTX_VALUE);	
		maps.put( Messages.RTOS_NULL,CmsisConstants.FEATURE_PROJECT_TEMPLATE_RTOS_NULL);
		
		maps.put( Messages.TRACE_STDOUT_CHANNEL,CmsisConstants.FEATURE_PROJECT_TEMPLATE_TRACE_STDOUT);
		maps.put( Messages.TRACE_DEBUG_CHANNEL, CmsisConstants.FEATURE_PROJECT_TEMPLATE_TRACE_DEBUG);
		maps.put( Messages.TRACE_ITM_CHANNEL,CmsisConstants.FEATURE_PROJECT_TEMPLATE_TRACE_ITM);
		maps.put( Messages.TRACE_NO_CHANNEL, CmsisConstants.FEATURE_PROJECT_TEMPLATE_TRACE_NULL); 
		
		maps.put(Messages.SysCall_FREESTANDING, CmsisConstants.FEATURE_PROJECT_TEMPLATE_SYSCALL_FREESTANDING);
		maps.put(Messages.SysCall_POSIX,CmsisConstants.FEATURE_PROJECT_TEMPLATE_POSIX_VALUE);
		maps.put( Messages.SysCall_SEMIHOSTING,CmsisConstants.FEATURE_PROJECT_TEMPLATE_SEMIHOSTING_VALUE);
		
		maps.put(Messages.UTILS_BUILD_IN,CmsisConstants.FEATURE_PROJECT_TEMPLATE_UTILS_BUILDIN);
		maps.put(Messages.UTILS_NULL,CmsisConstants.FEATURE_PROJECT_TEMPLATE_UTILS_NULL);
		
	}
	
	public String remapValue(String value){
		for(String key : maps.keySet()){
			if(value.equals(key)){
				return maps.get(key);
			}
		}
		return value;
	}
	
	public void updateFeature(Combo widget, String option){
		String value = remapValue(widget.getText());	
		log(value+widget.getText());
		Map<String, String> features = getProjectFeatures();
		features.put(option,value);
		updateFeatures(features);
	}
	
	public void updateFeature(Button widget, String option){
		Boolean sec = widget.getSelection();
		String value = sec?"true":"false"; //$NON-NLS-1$ $NON-NLS-2$
		log(value+widget.getText());
		Map<String, String> features = getProjectFeatures();
		features.put(option,value);
		updateFeatures(features);
	}

	public static Map<String, String> getProjectFeatures() {
		return LiteProjectTemplateProvider.getProjectFeatures();
	}
	
	public static void updateFeatures(Map<String, String> feature) {
		LiteProjectTemplateProvider.features.putAll(feature);
	}
	
	void updateStatus(String message) {
		listeners.notifyListeners(message);
	}

	public void addListener(IStatusMessageListener listener) {
		listeners.addListener(listener);
	}

	public void removeListener(IStatusMessageListener listener) {
		listeners.removeListener(listener);
	}

	protected void updateControls() {
		
		String description = Messages.LiteMiddleWareAdapterSelector_NO_Selection;
		updateStatus(description);		
	}
	
	private static void log(String msg){
		System.out.println(msg);
	}

	@Override
	protected void checkSubclass() {
		
	}

	
}
