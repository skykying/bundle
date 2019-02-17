package com.lembed.lite.studio.manager.analysis.index;

import org.eclipse.cdt.core.index.provider.IPDOMDescriptor;
import org.eclipse.cdt.core.index.provider.IReadOnlyPDOMProvider;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.settings.model.ICConfigurationDescription;

@SuppressWarnings("javadoc")
public class AcmeSDKProvider implements IReadOnlyPDOMProvider {
    @Override
    public boolean providesFor(ICProject project) {
        // e.g. decide by looking for acme project nature
//        return AcmeNature.isAcmeProject(project);
        return false;
    }

    @Override
    public IPDOMDescriptor[] getDescriptors(ICConfigurationDescription config) {
//        final IPath sdkBase = AcmeSDKAPI.getSDKBase(config);
        
        return null;
//        return new IPDOMDescriptor[] { new IPDOMDescriptor() {
//            @Override
//            public IIndexLocationConverter getIndexLocationConverter() {
//                return new URIRelativeLocationConverter(URIUtil.toURI(sdkBase));
//            }
//            @Override
//            public IPath getLocation() {
//                IPath path = sdkBase.append(AcmeSDKAPI.getPrebuiltPDOMFilename(config));
//                return path;
//            }
//        }};
    }
}