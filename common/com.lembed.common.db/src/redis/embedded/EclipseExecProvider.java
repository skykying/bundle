package redis.embedded;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import redis.embedded.util.Architecture;
import redis.embedded.util.OS;
import redis.embedded.util.OsArchitecture;

public class EclipseExecProvider implements IExecProvider {
	


	 private final Map<OsArchitecture, String> executables = Maps.newHashMap();

	    public static IExecProvider defaultProvider() {
	        return new EclipseExecProvider();
	    }
	    
	    private EclipseExecProvider() {
	        try {
				initExecutables();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

	    private void initExecutables() throws IOException {
	    	String s = System.getProperty("file.separator");
	    	String xpath = getPath("Redis-x64-5.0.9"+s+"redis-server.exe");
	    	
	        executables.put(OsArchitecture.WINDOWS_x86, xpath);
	        executables.put(OsArchitecture.WINDOWS_x86_64, xpath);
	        
	        xpath = getPath("redis-server-2.8.19-32");
	        executables.put(OsArchitecture.UNIX_x86, xpath);
	        executables.put(OsArchitecture.UNIX_x86_64, xpath);

	        xpath = getPath("redis-server-2.8.19.app");
	        executables.put(OsArchitecture.MAC_OS_X_x86, xpath);
	        executables.put(OsArchitecture.MAC_OS_X_x86_64, xpath);
	    }

	    public EclipseExecProvider override(OS os, String executable) {
	        Preconditions.checkNotNull(executable);
	        for (Architecture arch : Architecture.values()) {
	            override(os, arch, executable);
	        }
	        return this;
	    }

	    public EclipseExecProvider override(OS os, Architecture arch, String executable) {
	        Preconditions.checkNotNull(executable);
	        executables.put(new OsArchitecture(os, arch), executable);
	        return this;
	    }
	    
	    public File get() throws IOException {
	    	
	        OsArchitecture osArch = OsArchitecture.detect();
	        String executablePath = executables.get(osArch);
	        
	        if(fileExists(executablePath)) {
	        	return new File(executablePath);
	        }
	        throw new IOException();
	    }

	    
	    private String getPath(String name) throws IOException {
	    	String s = System.getProperty("file.separator");
	    	
	    	Bundle bundle = FrameworkUtil.getBundle(EclipseExecProvider.class);
	    	Path repath = new Path("resources" + s + name.strip());
	    	
	    	URL url = FileLocator.find(bundle, repath);
	    	URL fileUrl = FileLocator.toFileURL(url);
	    	File file = new File(fileUrl.getFile());
	    	System.out.println("!!!!! "+file.getAbsolutePath());
	    	
	    	
	    	return file.getAbsolutePath();
	    }
	    
	    private boolean fileExists(String executablePath) {
	        return new File(executablePath).exists();
	    }
	

}
