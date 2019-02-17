package org.panda.logicanalyzer.core.internal.sources;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.panda.logicanalyzer.core.Activator;
import org.panda.logicanalyzer.core.pipeline.IDataPacket;
import org.panda.logicanalyzer.core.pipeline.IDataSource;
import org.panda.logicanalyzer.core.pipeline.IDataSourceFactory;
import org.panda.logicanalyzer.core.pipeline.IFrame;
import org.panda.logicanalyzer.core.util.ArrayBackedFrame;
import org.panda.logicanalyzer.core.util.DataPacket;

/**
 * This dummy data source simply generates random data on an arbitrary amount of channels.
 */
public class RandomDataSource implements IDataSource {

	/**
	 * The factory of the {@link RandomDataSource}
	 *
	 */
	public static class RandomDataSourceFactory implements IDataSourceFactory {

		public static final String KEY__AMOUNT_OF_CHANNELS = "amountOfChannels";
		public static final String KEY__AMOUNT_OF_SAMPLES = "amountOfSamples";
		public static final String KEY__FRAME_DISTANCE = "frameDistance";


		public IDataSource createSource(Map<String, Object> configuration) throws CoreException {
			System.out.println("createSource @RandomDataSourceFactory");

			int amountOfChannels;

			if (!(configuration.get(KEY__AMOUNT_OF_CHANNELS) instanceof Integer)) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "RandomDataSourceFactory is missing the KEY__AMOUNT_OF_CHANNELS in it's configuration");
				throw new CoreException(status);
			}

			try {
				amountOfChannels = (Integer) configuration.get(KEY__AMOUNT_OF_CHANNELS);
				if (amountOfChannels < 0) throw new NumberFormatException(); // I'm too lazy to do this right
			} catch (NumberFormatException e) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "The KEY__AMOUNT_OF_CHANNELS value is not a positive number", e);
				throw new CoreException(status);
			}

			long amountOfSamples;
			if (!(configuration.get(KEY__AMOUNT_OF_SAMPLES) instanceof Long)) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "RandomDataSourceFactory is missing the KEY__AMOUNT_OF_SAMPLES in it's configuration");
				throw new CoreException(status);
			}

			try {
				amountOfSamples = (Long) configuration.get(KEY__AMOUNT_OF_SAMPLES);
				if (amountOfSamples < 0) throw new NumberFormatException(); // I'm too lazy to do this right
			} catch (NumberFormatException e) {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "The KEY__AMOUNT_OF_SAMPLES value is not a positive number", e);
				throw new CoreException(status);
			}

			RandomDataSource result = new RandomDataSource(amountOfChannels, amountOfSamples);
			if ((configuration.get(KEY__FRAME_DISTANCE) instanceof Long)) {
				try {
					long frameDistance = (Long) configuration.get(KEY__FRAME_DISTANCE);
					if (frameDistance < 0) {
						throw new NumberFormatException(); // I'm too lazy to do this right
					}

					result.setFrameDistance(frameDistance);
				} catch (NumberFormatException e) {
					IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "The KEY__FRAME_DISTANCE value is not a positive number", e);
					throw new CoreException(status);
				}
			}

			return result;
		}

	}

	/**
	 * The amount of channels we want to have
	 */
	private final int noOfChannels;

	/**
	 * The amount of samples we're supposed to generate
	 */
	private final long amountOfSamples;

	/**
	 * The distance between the frames in nanoseconds
	 */
	private long frameDistance = 1000;

	public RandomDataSource(int noOfChannels, long amountOfSamples) {

		this.noOfChannels = noOfChannels;
		this.amountOfSamples = amountOfSamples;

		System.out.println("RandomDataSource !");
	}

	/**
	 * Sets a new frame distance. If the given parameter is less than or equal to zero,
	 * the old value is preserved.
	 *
	 * @param dt the new frame distance
	 */
	public void setFrameDistance(long dt) {
		if (dt > 0) frameDistance = dt;
	}

	public int getAmountOfChannels() {
		return noOfChannels;
	}

	public long getAmountOfSamples() {
		return amountOfSamples;
	}

	public IDataPacket run() throws CoreException {
		List<IFrame> frames = new LinkedList<IFrame>();
		boolean[] frameData = null;

		for (long i = 0; i < getAmountOfSamples(); i++) {

			if (frameData == null || (i % 3 == 0 && Math.random() > 0.6)) {
				frameData = buildFrameData();
			}

			frames.add(new ArrayBackedFrame(i * frameDistance, frameData));
		}
		return new DataPacket(frames);
	}

	/**
	 * Builds new randomly generated frame data.
	 *
	 * @param t The current point in time
	 * @return Thew newly generated frame data
	 */
	private boolean[] buildFrameData() {
		Random rand = new Random();

		boolean[] data = new boolean[getAmountOfChannels()];
		for (int i = 0; i < data.length; i++) data[i] = rand.nextBoolean();
		return data;
	}

}
