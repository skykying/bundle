package com.lembed.lite.studio.managedbuild.cross.llvm;

import org.eclipse.cdt.managedbuilder.language.settings.providers.GCCBuiltinSpecsDetector;

/**
 * The Class ClangBuiltinSpecsDetector.
 * 
 * org.eclipse.cdt.core.gcc
 * org.eclipse.cdt.core.g++
 */
public class ClangBuiltinSpecsDetector extends GCCBuiltinSpecsDetector{
    private final String LLVM_CLANG_TOOLCHAIN_BASE_ID = "com.lembed.lite.studio.managedbuild.cross.llvm.toolchain.base"; //$NON-NLS-1$

    @Override
    public String getToolchainId() {
        return LLVM_CLANG_TOOLCHAIN_BASE_ID;
    }
    
    
    
}
