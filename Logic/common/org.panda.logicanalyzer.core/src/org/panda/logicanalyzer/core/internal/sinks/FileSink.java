package org.panda.logicanalyzer.core.internal.sinks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.panda.logicanalyzer.core.Activator;
import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IDataSink;
import org.panda.logicanalyzer.core.pipeline.IDataSinkFactory;
import org.panda.logicanalyzer.core.pipeline.IFrame;

/**
 * The file sink stores data in a CSV file format.
 * The sink exented point is defined in schema/dataSinkFactory.exsd
 * and extended point defineded instance is register in pulgin.xml/extensions/*
 */
public class FileSink implements IDataSink {

	/**
	 * The name of the file we're going to write
	 */
	private final String filename;

	/**
	 * The factory for the file sink
	 *
	 */
	public static class FileSinkFactory implements IDataSinkFactory {

		public IDataSink createSink(Map<String, Object> configuration) throws CoreException {
			if (!(configuration.get("file") instanceof String)) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Configuration does not contain the filename");
				throw new CoreException(status);
			}

			return new FileSink((String) configuration.get("file"));
		}

	}

	public FileSink(String filename) {
		this.filename = filename;
	}

	public void accept(IDataPacket packet) throws CoreException {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));

			for (IFrame frame : packet.getFrames()) {
				writer.write(frame.getTime() + " ");
				for (int i = 0; i < frame.getAmountOfChannels(); i++) {
					writer.write((frame.getValue(i) ? "1" : "0") + " ");
				}
				writer.write("\n");
			}

			writer.close();
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error while writing file", e);
			throw new CoreException(status);
		}
	}

}
