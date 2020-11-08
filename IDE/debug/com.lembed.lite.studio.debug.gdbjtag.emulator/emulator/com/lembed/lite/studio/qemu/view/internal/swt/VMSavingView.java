package com.lembed.lite.studio.qemu.view.internal.swt;

import java.io.File;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.lembed.lite.studio.qemu.view.IemultorStore;

public class VMSavingView extends DeviceViewWithFileChooser {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public VMSavingView(JPanel jPanel) {
		super(jPanel);
		setChoosertitle("Please, choose a file to save this VM!");
	}

	public boolean chooseFile() {
		this.getChooser().setDialogTitle(this.getChoosertitle());
		this.getChooser().setFileSelectionMode(JFileChooser.FILES_ONLY);

		this.getChooser().setFileFilter(new XMLFileFilter());

		this.getChooser().setAcceptAllFileFilterUsed(false);

		if (this.getCurrentDirectory() == null) {
			this.setCurrentDirectory(".");
		}

		this.getChooser().setCurrentDirectory(new File(this.getCurrentDirectory()));

		if (this.getChooser().showSaveDialog(this.getJpanel()) == JFileChooser.APPROVE_OPTION) {
			File selectedfile = this.getChooser().getSelectedFile();
			this.setChoice(selectedfile.getAbsolutePath());
			return true;
		}
		return false;
	}

	static class XMLFileFilter extends javax.swing.filechooser.FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase(Locale.ENGLISH).endsWith(".xml") || f.isDirectory();
		}

		@Override
		public String getDescription() {
			return "XML - JavaQemu VM Files";
		}

	}

	@Override
	public void applyView(IemultorStore store) {

	}

}
