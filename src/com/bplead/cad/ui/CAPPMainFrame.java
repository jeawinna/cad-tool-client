package com.bplead.cad.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import org.apache.log4j.Logger;

import com.bplead.cad.bean.io.CAPP;
import com.bplead.cad.model.CustomStyleToolkit;

import priv.lee.cad.model.Callback;
import priv.lee.cad.model.StyleToolkit;
import priv.lee.cad.ui.AbstractFrame;
import priv.lee.cad.util.Assert;
import priv.lee.cad.util.PropertiesUtils;
import priv.lee.cad.util.XmlUtils;

public class CAPPMainFrame extends AbstractFrame {

	private static Logger logger = Logger.getLogger(CADMainFrame.class);
	private static final long serialVersionUID = -583597043543602853L;

	public static void main(String[] args) {
		new CAPPMainFrame().activate();
	}

	private BasicAttributePanel attributePanel;
	private CAPP capp;
	private final String CAPP_REPOSITORY = "capp.xml.repository";
	private ContainerPanel containerPanel;
	private TabAttributePanel tabAttributePanel;

	private StyleToolkit toolkit = new CustomStyleToolkit();

	public CAPPMainFrame() {
		super(CAPPMainFrame.class);
		setToolkit(toolkit);
	}

	@Override
	public double getHorizontalProportion() {
		return 0.6d;
	}

	@Override
	public double getVerticalProportion() {
		return 0.99d;
	}

	private void initCAPP() {
		File xml = new File(CAPPMainFrame.class.getResource(PropertiesUtils.readProperty(CAPP_REPOSITORY)).getPath());
		this.capp = XmlUtils.read(xml, CAPP.class);
		logger.debug("capp:" + capp);
	}

	@Override
	public void initialize() {
		logger.info("initialize " + getClass() + " CAPP...");
		initCAPP();

		Assert.notNull(capp, "CAD initialize failed.Please check the " + PropertiesUtils.readProperty(CAPP_REPOSITORY));

		logger.info("initialize " + getClass() + " menu bar...");
		setJMenuBar(toolkit.getStandardMenuBar(new CheckinActionListenner(), new CheckoutActionListenner()));

		logger.info("initialize " + getClass() + " container panel...");
		containerPanel = new ContainerPanel();
		getContentPane().add(containerPanel);

		logger.info("initialize " + getClass() + " basic attribute panel...");
		attributePanel = new BasicAttributePanel(capp);
		attributePanel.setLabelProportion(0.1d);
		getContentPane().add(attributePanel);

		logger.info("initialize " + getClass() + " tab attribute panel...");
		tabAttributePanel = new TabAttributePanel(capp.getMpmParts());
		getContentPane().add(tabAttributePanel);
	}

	public class CheckinActionListenner implements ActionListener, FilenameFilter {

		@Override
		public boolean accept(File dir, String name) {
			// TODO
			return false;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO
		}
	}

	public class CheckoutActionListenner implements ActionListener, Callback {

		@Override
		public void actionPerformed(ActionEvent e) {
			new SearchForDownloadDialog(this).activate();
		}

		@Override
		public void call(Object object) {

		}
	}
}
