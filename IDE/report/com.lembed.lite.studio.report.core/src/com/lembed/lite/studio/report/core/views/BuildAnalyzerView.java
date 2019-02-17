/*******************************************************************************
 * Copyright (C) 2017 Lembed Electronic.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lembed Electronic - initial API and implementation
 ******************************************************************************/
package com.lembed.lite.studio.report.core.views;

import java.util.Random;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.ViewPart;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.IGrid;
import org.swtchart.ILegend;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;

import com.lembed.lite.studio.dataviewers.piechart.PieChart;
import com.lembed.lite.studio.report.core.Icon;
import com.lembed.lite.studio.report.core.BuildAnalyzerPlugin;
import com.lembed.lite.studio.report.core.manager.BuildManager;
import com.lembed.lite.studio.report.core.model.ReportStore;
import org.swtchart.ISeriesLabel;
import org.swtchart.LineStyle;
import org.swtchart.Range;
import org.eclipse.swt.graphics.RGBA;

/**
 * information group:
 * 1, project IDE information
 * 2, project compiler information
 * 3, project linker information
 * 4, project target device information
 * 
 * build group
 * 1, project build options
 * 2, project build times
 * 3, target object information
 * 4, stack usage information
 * 5, memory usage information
 * 
 * 
 * source code group
 * 1, project structure information
 * 2, project other information
 * 
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("javadoc")
public class BuildAnalyzerView extends ViewPart {

	public static final String VIEW_ID = "com.lembed.lite.studio.report.core.views.BuildAnalyzerView"; //$NON-NLS-1$

	Composite ui;
	Boolean flag = false;
	
	TableViewer tableviewer = null;

	@Override
	public void createPartControl(Composite parent) {
		ui = ViewInit(parent);
		createActionExport();
		addActionsToMenus(this);
		clear();
		ReportStore.getStore();
		
	}

	@Override
	public void setFocus() {
		ui.setFocus();
	}
	
	private static void clear() {
		BuildManager.getInstance().clearCache();
	}

	private void addActionsToMenus(ViewPart view) {

		IContributionManager[] managers = { view.getViewSite().getActionBars().getMenuManager(),
				view.getViewSite().getActionBars().getToolBarManager() };

		for (IContributionManager manager : managers) {
			
			manager.add(createActionSummary());
			manager.add(new Separator());
			manager.add(createActionProblem());
			manager.add(createActionMetric());
			manager.add(new Separator());
			manager.add(createActionConfigure());
			manager.add(createActionExport());
		}
	}

	private static Action createActionExport() {

		Action actionExport = new Action("Export") { //$NON-NLS-1$

			@Override
			public void runWithEvent(Event e) {
				IMenuCreator mc = this.getMenuCreator();
				Widget item = e.widget;
				ToolItem ti = (ToolItem) item;

				if (mc != null) {
					Menu m = mc.getMenu(ti.getParent());
					if (m != null) {
						// position the menu below the drop down item / next to
						// cursor
						Point point = item.getDisplay().getCursorLocation();
						m.setLocation(point.x, point.y);
						m.setVisible(true);
						return; // we don't fire the action
					}
				}

			}
		};
		actionExport.setImageDescriptor(BuildAnalyzerPlugin.getImageDescriptor(Icon.Size16.EXPORT));
		// actionExport.setMenuCreator(new ExportActionMenuCreator(this));

		return actionExport;
	}

	private Action createActionProblem() {
		Action actionCollapseAll = new Action("Problem", //$NON-NLS-1$
				BuildAnalyzerPlugin.getImageDescriptor(Icon.Size16.DBG)) {
			@Override
            public void run() {				
				tableviewer.setInput(BuildFactory.getProblems());
			}
		};

		return actionCollapseAll;
	}

	private Action createActionMetric() {
		Action actionCollapseAll = new Action("Metrics", //$NON-NLS-1$
				BuildAnalyzerPlugin.getImageDescriptor(Icon.Size16.METRIC)) {
			@Override
            public void run() {
				flag = !flag;
				ui.setVisible(flag);
			}
		};

		return actionCollapseAll;
	}
	
	private Action createActionSummary() {
		Action actionCollapseAll = new Action("Summary", //$NON-NLS-1$
				BuildAnalyzerPlugin.getImageDescriptor(Icon.Size16.SUMMARY)) {
			@Override
            public void run() {
				flag = !flag;
				ui.setVisible(flag);
			}
		};

		return actionCollapseAll;
	}
	
	private Action createActionConfigure() {
		Action actionCollapseAll = new Action("Option", //$NON-NLS-1$
				BuildAnalyzerPlugin.getImageDescriptor(Icon.Size16.CONFIGURE)) {
			@Override
            public void run() {
				flag = !flag;
				ui.setVisible(flag);
			}
		};

		return actionCollapseAll;
	}
	
	private Composite ViewInit(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		final Composite comp = new Composite(scrolledComposite, SWT.NONE);
		FillLayout clayout = new FillLayout();
		clayout.type = SWT.VIRTUAL;
		comp.setLayout(clayout);

//		historyChart(comp);
//		showStackChart(comp);

		final Composite pieGroup = new Composite(comp, SWT.NONE);
		FillLayout playout = new FillLayout();
		playout.type = SWT.HORIZONTAL;
		pieGroup.setLayout(playout);

//		categoryBarChart(pieGroup);
//		categoryPieChart(pieGroup);

		tableviewer = CompilerTableView.createTableViewer(comp);

		scrolledComposite.setContent(comp);
		scrolledComposite.setMinSize(new Point(550, 900));// 面板的最小大小

		return comp;
	}

	protected void categoryPieChart(Composite parent) {
		PieChart chart = new PieChart(parent, SWT.NONE);

		String labels[] = { "Warning", "Error", "Information" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		double val[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 4, 5, 6 }, { 4, 5, 6 }, { 4, 5, 6 }, { 4, 5, 6 }, { 4, 5, 6 } };

		chart.addPieChartSeries(labels, val);
		chart.getTitle().setText("metric"); //$NON-NLS-1$

		Font font = new Font(Display.getDefault(), "Tahoma", 8, SWT.NORMAL); //$NON-NLS-1$
		chart.getTitle().setFont(font);
		chart.getAxisSet().getXAxis(0).getTitle().setVisible(false);

		ILegend legend = chart.getLegend();
		legend.setPosition(SWT.RIGHT);
		legend.setFont(font);

		chart.setBackground(new Color(Display.getDefault(), new RGBA(255, 255, 255, 10)));

		chart.redraw();
	}

	/**
	 * Category bar chart.
	 *
	 * @param parent the parent
	 */
	protected static void categoryBarChart(Composite parent) {

		// create a chart
		Chart chart = new Chart(parent, SWT.NONE);

		double[] ySeries = new double[3];
		Random rand = new Random(25);
		for (int x = 0; x < ySeries.length; x++) {
			ySeries[x] = rand.nextInt(50);
		}

		chart.setBackground(new Color(Display.getDefault(), new RGBA(255, 255, 255, 10)));
		Font font = new Font(Display.getDefault(), "Tahoma", 8, SWT.NORMAL); //$NON-NLS-1$

		IGrid xGrid = chart.getAxisSet().getXAxis(0).getGrid();
		xGrid.setStyle(LineStyle.NONE);

		IGrid yGrid = chart.getAxisSet().getYAxis(0).getGrid();
		yGrid.setStyle(LineStyle.NONE);

		// set titles
		chart.getTitle().setText("problem category"); //$NON-NLS-1$
		chart.getAxisSet().getXAxis(0).getTitle().setText("category"); //$NON-NLS-1$
		chart.getAxisSet().getYAxis(0).getTitle().setText("problem"); //$NON-NLS-1$
		chart.getTitle().setFont(font);
		
		chart.getAxisSet().getXAxis(0).getTitle().setFont(font);
		chart.getAxisSet().getYAxis(0).getTitle().setFont(font);
		chart.getAxisSet().getXAxis(0).getTitle().setVisible(false);
		chart.getAxisSet().getYAxis(0).getTitle().setVisible(false);
		
		// set category
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0)
				.setCategorySeries(new String[] { "Info", "Warning", "Error"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		// create bar series
		IBarSeries barSeries1 = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "info"); //$NON-NLS-1$
		barSeries1.setYSeries(ySeries);
		barSeries1.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		barSeries1.setBarPadding(80);

		for (int x = 0; x < ySeries.length; x++) {
			ySeries[x] = rand.nextInt(50);
		}

		IBarSeries barSeries2 = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "error"); //$NON-NLS-1$
		barSeries2.setYSeries(ySeries);
		barSeries2.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		barSeries2.setBarPadding(80);

		for (int x = 0; x < ySeries.length; x++) {
			ySeries[x] = rand.nextInt(50);
		}
		IBarSeries barSeries3 = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "Warning"); //$NON-NLS-1$
		barSeries3.setYSeries(ySeries);
		barSeries3.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		barSeries3.setBarPadding(80);

		// enable stack series
		barSeries1.enableStack(true);
		barSeries2.enableStack(true);
		barSeries3.enableStack(true);

		ILegend legend = chart.getLegend();
		legend.setPosition(SWT.TOP);
		legend.setVisible(false);

		// adjust the axis range
		chart.getAxisSet().adjustRange();
		chart.redraw();
	}

	protected static void historyChart(Composite parent) {

		Chart lchart = new Chart(parent, SWT.NONE);
		lchart.setBackground(new Color(Display.getDefault(), new RGBA(255, 255, 255, 10)));

		double[] ySeries = new double[100];
		Random rand = new Random(25);
		for (int x = 0; x < ySeries.length; x++) {
			ySeries[x] = rand.nextInt(50);
		}

		// set titles
		lchart.getTitle().setText("Integrated with static analysis, unit testing, code review, and other "); //$NON-NLS-1$
		lchart.getAxisSet().getXAxis(0).getTitle().setText("Analysis"); //$NON-NLS-1$
		lchart.getAxisSet().getYAxis(0).getTitle().setText("Bugs"); //$NON-NLS-1$
		Font font = new Font(Display.getDefault(), "Tahoma", 8, SWT.NORMAL); //$NON-NLS-1$
		lchart.getTitle().setFont(font);
		lchart.getAxisSet().getXAxis(0).getTitle().setFont(font);
		lchart.getAxisSet().getYAxis(0).getTitle().setFont(font);

		IGrid xGrid = lchart.getAxisSet().getXAxis(0).getGrid();
		xGrid.setStyle(LineStyle.NONE);

		IGrid yGrid = lchart.getAxisSet().getYAxis(0).getGrid();
		yGrid.setStyle(LineStyle.NONE);
		lchart.getAxisSet().getYAxis(0).zoomOut(0.5);
		lchart.getAxisSet().getXAxis(0).zoomOut(0.5);

		// create line series
		ILineSeries lineSeries = (ILineSeries) lchart.getSeriesSet().createSeries(SeriesType.LINE, "checker"); //$NON-NLS-1$
		lineSeries.setYSeries(ySeries);
		lineSeries.setSymbolType(PlotSymbolType.PLUS);
		lineSeries.setSymbolSize(2);
		Color symbolColor = new Color(Display.getDefault(), 0, 0, 255);
		lineSeries.setSymbolColor(symbolColor);
		lineSeries.setLineStyle(LineStyle.DOT);
		lineSeries.setLineWidth(1);
		lineSeries.setAntialias(SWT.ON);
		lineSeries.enableStep(true);

		ISeriesLabel seriesLabel = lineSeries.getLabel();
		seriesLabel.setFormat("##.0"); //$NON-NLS-1$
		Color color = new Color(Display.getDefault(), 255, 0, 0);
		seriesLabel.setForeground(color);
		seriesLabel.setVisible(false);
		seriesLabel.setFont(font);

		ILegend legend = lchart.getLegend();
		legend.setPosition(SWT.TOP);
		legend.setVisible(false);

		lineSeries.setLineColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		lineSeries.enableArea(true);

		lchart.getAxisSet().getYAxis(0).setRange(new Range(1.5, 35));
		lchart.getAxisSet().getXAxis(0).setRange(new Range(1.5, 35));
		lchart.getAxisSet().getXAxis(0).getTick().setTickMarkStepHint(100);

		// adjust the axis range
		lchart.getAxisSet().adjustRange();
		lchart.redraw();
	}

	protected void showStackChart(Composite parent) {
		// create a chart
		Chart chart = new Chart(parent, SWT.NONE);

		double[] ySeries = new double[6];
		Random rand = new Random(25);
		for (int x = 0; x < ySeries.length; x++) {
			ySeries[x] = rand.nextInt(50);
		}

		chart.setBackground(new Color(Display.getDefault(), new RGBA(255, 255, 255, 10)));
		Font font = new Font(Display.getDefault(), "Tahoma", 8, SWT.NORMAL); //$NON-NLS-1$

		IGrid xGrid = chart.getAxisSet().getXAxis(0).getGrid();
		xGrid.setStyle(LineStyle.NONE);

		IGrid yGrid = chart.getAxisSet().getYAxis(0).getGrid();
		yGrid.setStyle(LineStyle.NONE);

		// set titles
		chart.getTitle().setText("problem category"); //$NON-NLS-1$
		chart.getAxisSet().getXAxis(0).getTitle().setText("category"); //$NON-NLS-1$
		chart.getAxisSet().getYAxis(0).getTitle().setText("problem"); //$NON-NLS-1$
		chart.getTitle().setFont(font);
		chart.getAxisSet().getXAxis(0).getTitle().setFont(font);
		chart.getAxisSet().getYAxis(0).getTitle().setFont(font);
		chart.getAxisSet().getXAxis(0).getTitle().setVisible(false);
		chart.getAxisSet().getYAxis(0).getTitle().setVisible(false);

		// set category
		chart.getAxisSet().getXAxis(0).enableCategory(true);
		chart.getAxisSet().getXAxis(0)
				.setCategorySeries(new String[] { "Style Problem", "Metric Problem", "MISRA 2004", "Potential Problem", "Security Problem", "Syntax Problem" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

		// create bar series
		IBarSeries barSeries1 = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "info"); //$NON-NLS-1$
		barSeries1.setYSeries(ySeries);
		barSeries1.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_GREEN));
		barSeries1.setBarPadding(60);

		for (int x = 0; x < ySeries.length; x++) {
			ySeries[x] = rand.nextInt(50);
		}

		IBarSeries barSeries2 = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "error"); //$NON-NLS-1$
		barSeries2.setYSeries(ySeries);
		barSeries2.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		barSeries2.setBarPadding(60);

		for (int x = 0; x < ySeries.length; x++) {
			ySeries[x] = rand.nextInt(50);
		}
		IBarSeries barSeries3 = (IBarSeries) chart.getSeriesSet().createSeries(SeriesType.BAR, "Warning"); //$NON-NLS-1$
		barSeries3.setYSeries(ySeries);
		barSeries3.setBarColor(Display.getDefault().getSystemColor(SWT.COLOR_BLUE));
		barSeries3.setBarPadding(60);

		// enable stack series
		barSeries1.enableStack(true);
		barSeries2.enableStack(true);
		barSeries3.enableStack(true);

		ILegend legend = chart.getLegend();
		legend.setPosition(SWT.TOP);
		legend.setVisible(false);

		// adjust the axis range
		chart.getAxisSet().adjustRange();
		chart.redraw();
	}
}
