package com.lembed.lite.studio.managedbuild.cross.llvm;

import org.eclipse.cdt.managedbuilder.core.IManagedIsToolChainSupported;
import org.eclipse.cdt.managedbuilder.core.IToolChain;
import org.osgi.framework.Version;

/**
 * The Class IsLLVMToolChainSupported.
 */
public class IsLLVMToolChainSupported implements IManagedIsToolChainSupported {

    @Override
    public boolean isSupported(IToolChain toolChain, Version version, String instance) {
        return true;
    }

}
