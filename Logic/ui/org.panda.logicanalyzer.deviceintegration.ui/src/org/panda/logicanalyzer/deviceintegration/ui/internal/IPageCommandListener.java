package org.panda.logicanalyzer.deviceintegration.ui.internal;

/**
 * Classes implementing this interface may listen for {@link PageCommand}s.
 */
interface IPageCommandListener {

	/**
	 * A new command has been received.
	 *
	 * @param command The command that has been received.
	 */
	public void commandReceived(PageCommand command);

}
