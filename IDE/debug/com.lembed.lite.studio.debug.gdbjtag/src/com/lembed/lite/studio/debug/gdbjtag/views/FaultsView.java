
package com.lembed.lite.studio.debug.gdbjtag.views;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.IMemoryBlockListener;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.internal.ui.views.variables.VariablesView;


/**
 * about gdb high end usage
 *  http://www.sohu.com/a/120076507_464041 
 *  
 *  about tracepoint of gdb
 *  http://blog.csdn.net/defeattroy/article/details/5833897
 *  
 *  about dsf
 *  http://help.eclipse.org/kepler/index.jsp?topic=%2Forg.eclipse.cdt.doc.isv%2Fguide%2Fdsf%2Fintro%2Fdsf_programming_intro.html
 *  
 * @author Administrator
 *
 */
@SuppressWarnings("restriction")
public class FaultsView extends VariablesView implements IMemoryBlockListener, IDebugEventSetListener {

	@Override
	public void handleDebugEvents(DebugEvent[] events) {

	}
 
	@Override
	public void memoryBlocksAdded(IMemoryBlock[] memory) {
		
	}

	@Override
	public void memoryBlocksRemoved(IMemoryBlock[] memory) {
		
	}

	
}
