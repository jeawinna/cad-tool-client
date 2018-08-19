package com.bplead.cad.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.bplead.cad.bean.client.CaxaTemporary;
import com.bplead.cad.bean.client.DefaultContainer;
import com.bplead.cad.bean.client.Preference;
import com.bplead.cad.util.ClientUtils;

import priv.lee.cad.ui.AbstractDialog;
import priv.lee.cad.ui.AbstractPanel;
import priv.lee.cad.ui.Option;
import priv.lee.cad.ui.OptionPanel;
import priv.lee.cad.ui.PromptTextField;
import priv.lee.cad.util.StringUtils;
import priv.lee.cad.util.XmlUtils;

public class PreferencesDialog extends AbstractDialog {

	private static final Logger logger = Logger.getLogger(PreferencesDialog.class);
	private static final long serialVersionUID = -2875157877197653599L;

	public static void main(String[] args) {
		new PreferencesDialog().activate();
	}

	private Preference preference = ClientUtils.temprary.getPreference();
	private PreferencesPanel preferencePanel;

	public PreferencesDialog() {
		super(PreferencesDialog.class);
	}

	@Override
	public double getHorizontalProportion() {
		return 0.4d;
	}

	@Override
	public double getVerticalProportion() {
		return 0.35d;
	}

	@Override
	public void initialize() {
		logger.info("initialize preferences content...");
		preferencePanel = new PreferencesPanel();
		add(preferencePanel);

		logger.info("initialize option content...");
		Option confirm = new Option(Option.CONFIRM_BUTTON, null, new ConfirmActionListener());
		add(new OptionPanel(Arrays.asList(confirm, Option.newCancelOption(this))));

		logger.info("initialize completed...");
	}

	private class ConfirmActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// ~ do confirm
			if (preference == null) {
				preference = new Preference();
			}

			logger.info("begin to write client temporay...");
			preference.setCaxa(new CaxaTemporary(preferencePanel.cache.getText().getText(),
					preferencePanel.exe.getText().getText()));
			preference.setContainer(new DefaultContainer(preferencePanel.folder.getText().getText(),
					preferencePanel.pdm.getText().getText()));
			XmlUtils.store(ClientUtils.temprary);

			dispose();
		}
	}

	private class FindCaxaCacheActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new LocalFileChooser(JFileChooser.DIRECTORIES_ONLY, preferencePanel.cache);
		}
	}

	private class FindCaxaExeActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new LocalFileChooser(JFileChooser.FILES_ONLY, preferencePanel.exe);
		}
	}

	private class FindDefaultFolderActionListenner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new PdmLinkProductChooseDialog().activate();
		}
	}

	private class FindDefaultPdmActionListenner implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			new PdmLinkProductChooseDialog().activate();
		}
	}

	class PreferencesPanel extends AbstractPanel {

		private static final long serialVersionUID = -5076567817803672184L;
		public PromptTextField cache;
		private final String CAXA_CACHE = "caxa.cache";
		private final String CAXA_EXE = "caxa.exe";
		private final String DEFAULT_FOLDER = "default.folder";
		private final String DEFAULT_PDM = "default.pdm";
		public PromptTextField exe;
		public PromptTextField folder;
		private final double HEIGHT_PROPORTION = 0.08d;
		private final double LABEL_PROPORTION = 0.18d;
		private final String OPEN = "open";
		public PromptTextField pdm;
		private final double TEXT_PROPORTION = 0.55d;
		private final String TITLE = "title";
		public PromptTextField url;
		private final String WINDCHILL_URL = "wnc.url";

		private String getCaxaCache() {
			return (isPreferenceCaxaNull() || StringUtils.isEmpty(preference.getCaxa().getCache())) ? ""
					: preference.getCaxa().getCache();
		}

		private String getCaxaExe() {
			return (preference == null || preference.getCaxa() == null
					|| StringUtils.isEmpty(preference.getCaxa().getLocation())) ? ""
							: preference.getCaxa().getLocation();
		}

		private String getDefaultFolder() {
			return (isPreferenceContainerNull() || StringUtils.isEmpty(preference.getContainer().getFolder())) ? ""
					: preference.getContainer().getFolder();
		}

		private String getDefaultPdm() {
			return (isPreferenceContainerNull() || StringUtils.isEmpty(preference.getContainer().getPdm())) ? ""
					: preference.getContainer().getPdm();
		}

		@Override
		public double getHorizontalProportion() {
			return 0.95d;
		}

		@Override
		public double getVerticalProportion() {
			return 0.75d;
		}

		private String getWnctUrl() {
			String wncUrl = ClientUtils.temprary.getServer();
			return StringUtils.isEmpty(wncUrl) ? "" : wncUrl;
		}

		@Override
		public void initialize() {
			setLayout(new FlowLayout(FlowLayout.LEFT));

			// set panel border to be title and etched type
			setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
					getResourceMap().getString((TITLE)), TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION, toolkit.getFont()));

			// ~ add components
			PromptTextField.PromptTextFieldDimension dimension = PromptTextField.newDimension(getPreferredSize(),
					LABEL_PROPORTION, TEXT_PROPORTION, HEIGHT_PROPORTION);
			url = PromptTextField.newInstance(getResourceMap().getString((WINDCHILL_URL)), getWnctUrl(), dimension);
			add(url);

			exe = PromptTextField.newInstance(getResourceMap().getString((CAXA_EXE)), getCaxaExe(), dimension);
			add(exe);
			JButton openExe = new JButton(getResourceMap().getString((OPEN)));
			openExe.addActionListener(new FindCaxaExeActionListener());
			add(openExe);

			cache = PromptTextField.newInstance(getResourceMap().getString((CAXA_CACHE)), getCaxaCache(), dimension);
			add(cache);
			JButton openCache = new JButton(getResourceMap().getString((OPEN)));
			openCache.addActionListener(new FindCaxaCacheActionListener());
			add(openCache);

			pdm = PromptTextField.newInstance(getResourceMap().getString((DEFAULT_PDM)), getDefaultPdm(), dimension);
			add(pdm);
			JButton openPdm = new JButton(getResourceMap().getString((OPEN)));
			openPdm.addActionListener(new FindDefaultPdmActionListenner());
			add(openPdm);

			folder = PromptTextField.newInstance(getResourceMap().getString((DEFAULT_FOLDER)), getDefaultFolder(),
					dimension);
			add(folder);
			JButton openFolder = new JButton(getResourceMap().getString((OPEN)));
			openPdm.addActionListener(new FindDefaultFolderActionListenner());
			add(openFolder);
		}

		private boolean isPreferenceCaxaNull() {
			return preference == null || preference.getCaxa() == null;
		}

		private boolean isPreferenceContainerNull() {
			return preference == null || preference.getContainer() == null;
		}
	}
}
