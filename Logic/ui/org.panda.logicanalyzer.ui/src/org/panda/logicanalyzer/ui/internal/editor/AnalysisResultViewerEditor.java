package org.panda.logicanalyzer.ui.internal.editor;

import org.panda.logicanalyzer.core.analyzer.AnalysisResultProperty;
import org.panda.logicanalyzer.core.analyzer.IAnalysisResult;
import org.panda.logicanalyzer.core.analyzer.TimedFrame;
import org.panda.logicanalyzer.core.util.TimeConverter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.panda.logicanalyzer.ui.Activator;

import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors; 

/**
 * This editor is able to display analysis results. It might be based on Eclipse Forms some
 * happy day.
 */
public class AnalysisResultViewerEditor extends EditorPart {

	public AnalysisResultViewerEditor() {

		super();
		System.out.println("AnalysisResultViewerEditor");
	}

	/**
	 * The ID of this editor
	 */
	public static final String EDITOR_ID = "org.panda.logicanalyzer.ui.analysisResultEditor";

	/**
	 * The label provider used to display tables.
	 */
	private class FrameLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			String result = "";

			if (element instanceof TimedFrame) {
				TimedFrame frame = (TimedFrame) element;

				switch (columnIndex) {
				case 0: result = frame.getSeverity().name(); break;
				case 1: result = TimeConverter.toString(frame.getTime()); break;
				case 2: result = Integer.toHexString(frame.getValue()); break;
				case 3: result = frame.getDescription(); break;
				}
			}

			return result;
		}

		public Color getBackground(Object element, int columnIndex) {
			Color result = null;

			if (element instanceof TimedFrame) {
				switch (((TimedFrame)element).getSeverity()) {
				case Error: result = getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED); break;
				case Warning: result = getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_YELLOW); break;
				}
			}

			return result;
		}

		public Color getForeground(Object element, int columnIndex) {
			return null;
		}

	}


	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Implement later
	}

	@Override
	public void doSaveAs() {
		// TODO Implement later
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);

		if (!(input.getAdapter(IAnalysisResult.class) instanceof IAnalysisResult)) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Analysis editor input must be adaptable to IAnalysisResult");
			throw new PartInitException(status);
		}
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {
		IAnalysisResult result = (IAnalysisResult) getEditorInput().getAdapter(IAnalysisResult.class);
		setPartName(result.getTitle());

		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		Form form = toolkit.createForm(parent);
		form.setText(result.getTitle());
		toolkit.decorateFormHeading(form);
		form.getBody().setLayout(new GridLayout(1, false));

		Section propertiesSection = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR);
		propertiesSection.setText("Properties");
		GridDataFactory.fillDefaults().grab(true, false).applyTo(propertiesSection);

		Composite propertiesComposite = new Composite(propertiesSection, SWT.NONE);
		propertiesSection.setClient(propertiesComposite);
		GridLayoutFactory.fillDefaults().numColumns(3).margins(0, 0).applyTo(propertiesComposite);
		for (AnalysisResultProperty property : result.getProperties()) {
			int color;
			switch (property.getSeverity()) {
			case Error: color = SWT.COLOR_RED; break;
			case Warning: color = SWT.COLOR_YELLOW; break;
			default: color = -1; break;
			}

			Label colorLabel = toolkit.createLabel(propertiesComposite, "  ");
			if (color != -1) colorLabel.setBackground(colorLabel.getDisplay().getSystemColor(color));

			toolkit.createLabel(propertiesComposite, property.getName());
			Label valueLabel = toolkit.createLabel(propertiesComposite, property.getValue());
			GridDataFactory.fillDefaults().grab(true, false).applyTo(valueLabel);
		}


		Section framesSection = toolkit.createSection(form.getBody(), Section.DESCRIPTION
		                        | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		framesSection.setText("Frames");
		GridDataFactory.fillDefaults().grab(true, true).applyTo(framesSection);

		TableViewer framesViewer = new TableViewer(toolkit.createTable(framesSection, SWT.FULL_SELECTION | SWT.H_SCROLL));
		Table table = framesViewer.getTable();
		framesSection.setClient(table);
		table.setHeaderVisible(true);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);

		TableColumn severityColumn = new TableColumn(table, SWT.NONE);
		severityColumn.setText("Severity");
		severityColumn.setWidth(50);

		TableColumn timeColumn = new TableColumn(table, SWT.NONE);
		timeColumn.setWidth(80);
		timeColumn.setText("Time");

		TableColumn valueColumn = new TableColumn(table, SWT.NONE);
		valueColumn.setWidth(50);
		valueColumn.setText("Value");

		TableColumn descriptionColumn = new TableColumn(table, SWT.NONE);
		descriptionColumn.setWidth(200);
		descriptionColumn.setText("Description");

		framesViewer.setContentProvider(new ArrayContentProvider());
		framesViewer.setLabelProvider(new FrameLabelProvider());
		framesViewer.setInput(result.getFrames());
	}

	@Override
	public void setFocus() {

	}

}
