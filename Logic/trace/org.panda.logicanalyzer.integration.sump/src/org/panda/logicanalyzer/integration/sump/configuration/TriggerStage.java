package org.panda.logicanalyzer.integration.sump.configuration;

/**
 * This class configures a trigger stage
 *
 *
 *
 */
public abstract class TriggerStage {

	/**
	 * We don't want anyone outside this class to create subclasses
	 */
	TriggerStage() { 
		
	}

	/**
	 * the trigger mask
	 */
	private int mask;

	/**
	 * the value to trigger for. That is <code>DATA & MASK = VALUE</code>
	 */
	private int value;

	/**
	 * delay in samples to wait in between match and fire
	 */
	private int delay;

	/**
	 * if true, the trigger fire will cause a stage advance instead of starting capturing
	 */
	private boolean advanceStage;

	/**
	 * The level of this trigger
	 */
	private int level;


	/**
	 * @return the trigger mask
	 */
	public int getMask() {
		return mask;
	}

	/**
	 * @return the value to trigger for. That is <code>DATA & MASK = VALUE</code>
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return delay in samples to wait in between match and fire
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Sets the trigger mask
	 *
	 * @param mask the trigger mask to set
	 */
	public void setMask(int mask) {
		this.mask = mask;
	}

	/**
	 * Sets the trigger value
	 *
	 * @param value the value to trigger for
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Sets the delay in samples to wait in between match and fire
	 *
	 * @param delay the delay to set
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}

	/**
	 * Sets if this stage should advance or start capturing
	 *
	 * @param advanceStage if true, the trigger fire will cause a stage advance instead of starting capturing
	 */
	public void setAdvanceStage(boolean advanceStage) {
		this.advanceStage = advanceStage;
	}

	/**
	 * @return if true, the trigger fire will cause a stage advance instead of starting capturing
	 */
	public boolean isAdvanceStage() {
		return advanceStage;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

}
