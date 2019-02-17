package org.panda.logicanalyzer.ui.internal.editor;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import org.panda.logicanalyzer.core.pipeline.IDataPacket;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.panda.logicanalyzer.ui.Activator;
import org.panda.logicanalyzer.ui.editor.IChannelOverlay;
import org.panda.logicanalyzer.ui.editor.IOverlayAcceptor;
import org.panda.logicanalyzer.ui.editor.ISelectionBasedTool;
import org.panda.logicanalyzer.ui.editor.ISelectionListener;

/**
 * A tool descriptor adds some additional attributes and methods to a
 * {@link ISelectionBasedTool}. Thus it kind of serves as a decorator for that
 * particular interface.
 */
public class ToolDescriptor implements ISelectionBasedTool {

	/**
	 * The extension point ID where selection based tools are registered at
	 */
	private static final String TOOL_EXTENSION_POINT = "org.panda.logicanalyzer.ui.selectionBasedTool";

	/**
	 * Computes a list of all tools registered at the
	 * {@link #TOOL_EXTENSION_POINT}. Be aware that all tools come
	 * <i>uninitialized</i>.
	 *
	 * @return all registered tools
	 * @throws CoreException
	 *             In case something goes wrong
	 */
	public static List<ToolDescriptor> getAllDescriptors() throws CoreException {
		List<ToolDescriptor> result = new LinkedList<ToolDescriptor>();

		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(TOOL_EXTENSION_POINT).getExtensions();
		for (IExtension extension : extensions) {
			for (IConfigurationElement element : extension.getConfigurationElements()) {
				ToolDescriptor descriptor = ToolDescriptor.getDescriptor(element);
				result.add(descriptor);
			}
		}

		return result;
	}

	/**
	 * Creates a new tool descriptor from a configuration element.
	 *
	 * @param element The element to create the descriptor from
	 * @return the tool descriptor
	 * @throws CoreException If the element does not match the extension point definition
	 */
	public static ToolDescriptor getDescriptor(IConfigurationElement element) throws CoreException {
		String name = element.getAttribute("name");
		if (name == null || name.trim().length() == 0) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "A registered selection based tool does not have a name");
			throw new CoreException(status);
		}

		Object tool = element.createExecutableExtension("class");
		if (!(tool instanceof ISelectionBasedTool)) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Registered selection based tool (" + name + ") does not implement the ISelectionBasedTool interface");
			throw new CoreException(status);
		}

		boolean needsUI = "true".equals(element.getAttribute("needsUI"));
		boolean allChannelSelection = "true".equals(element.getAttribute("allChannelSelection"));

		URL iconUrl = null;
		if (element.getAttribute("icon") != null) {
			Bundle contributingBundle = Platform.getBundle(element.getContributor().getName());
			iconUrl = contributingBundle.getResource(element.getAttribute("icon"));
		}

		return new ToolDescriptor(name, (ISelectionBasedTool) tool, needsUI, allChannelSelection, iconUrl);
	}

	/**
	 * The name of this tool
	 */
	private final String name;

	/**
	 * The tool itself
	 */
	private final ISelectionBasedTool tool;

	/**
	 * If true, this tool requires a UI (thus
	 * {@link ISelectionBasedTool#createContents(org.eclipse.swt.widgets.Composite)}
	 * to be called.
	 */
	private final boolean needsUI;

	/**
	 * If true, this tool wants all channel selection
	 */
	private final boolean allChannelSelection;

	/**
	 * The platform URL to the tools icon (possibly null if no icon)
	 */
	private final URL iconUrl;

	/**
	 * The acceptor this tool can deliver overlays to
	 */
	private IOverlayAcceptor acceptor;

	/**
	 * The packet we're working with
	 */
	private IDataPacket packet;

	/**
	 * If this flag is true
	 * {@link ISelectionBasedTool#initialize(IOverlayAcceptor, IDataPacket)} has
	 * been called on {@link #tool}.
	 */
	private boolean decoratedToolHasBeenInitialized;

	/**
	 * If true, this tool is currently enabled.
	 */
	private boolean enabled;

	/**
	 * All overlays produced by this tool
	 */
	private final List<IChannelOverlay> overlays = new LinkedList<IChannelOverlay>();


	private ToolDescriptor(String name, ISelectionBasedTool tool, boolean needsUI, boolean allChannelSelection, URL iconUrl) {
		this.name = name;
		this.tool = tool;
		this.needsUI = needsUI;
		this.allChannelSelection = allChannelSelection;
		this.iconUrl = iconUrl;
	}


	/**
	 * @return the name of this tool
	 */
	public String getName() {
		return name;
	}

	/**
	 * If true, this tool requires a UI (thus
	 * {@link ISelectionBasedTool#createContents(org.eclipse.swt.widgets.Composite)}
	 * to be called.
	 *
	 * @return the needsUI flag
	 */
	public boolean needsUI() {
		return needsUI;
	}

	/**
	 * If true, this tool wants all channel selection
	 *
	 * @return the allChannelSelection flag
	 */
	public boolean isAllChannelSelection() {
		return allChannelSelection;
	}

	/**
	 * @return the platform URL to the tools icon (possibly null if no icon)
	 */
	public URL getIconUrl() {
		return iconUrl;
	}


	public void createContents(Composite parent) {
		tool.createContents(parent);
	}


	public void disable() {
		for (IChannelOverlay overlay : overlays) {
			if (!overlay.isDisposed()) overlay.dispose();
		}
		overlays.clear();

		enabled = false;
		tool.disable();
	}


	public void enable() throws CoreException {
		if (!decoratedToolHasBeenInitialized) {

			tool.initialize(new IOverlayAcceptor() {

				public void accept(int channelNr, IChannelOverlay overlay) {
					if (enabled) {
						overlays.add(overlay);
						acceptor.accept(channelNr, overlay);
					}
				}
			}, packet);

			decoratedToolHasBeenInitialized = true;
		}

		enabled = true;
		tool.enable();
	}


	public ISelectionListener getSelectionListener() {
		return tool.getSelectionListener();
	}

	/**
	 * This is some form of a delayed init. This method must be called before
	 * calling {@link #enable()}, since the contract of an
	 * {@link ISelectionBasedTool} requires
	 * {@link ISelectionBasedTool#initialize(IOverlayAcceptor, IDataPacket)} to
	 * be called before {@link ISelectionBasedTool#enable()}. This class takes
	 * care of fullfiling this contract.
	 */

	public void initialize(IOverlayAcceptor acceptor, IDataPacket packet) {
		this.acceptor = acceptor;
		this.packet = packet;
	}

}
