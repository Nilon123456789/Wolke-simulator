package com.e24.wolke.frontend.input;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.renderer.RendererConstants;
import com.e24.wolke.backend.models.renderer.RendererConstants.VisualizationType;
import com.e24.wolke.backend.models.simulation.SimulationConstants;
import com.e24.wolke.backend.models.simulation.SimulationConstants.*;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.frontend.help.HelpButtonComponent;
import com.e24.wolke.utils.math.WMath;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import net.miginfocom.swing.MigLayout;

/**
 * Panneau de saisie des entrées de la simulation Il contient les préréglages de la simulation,
 * ainsi que des réglages avancés de la simulation et de sa visualisation
 *
 * @author adrienles
 * @author MeriBouisri
 * @author Nilon123456789
 */
public class SimulationInputPane extends AbstractInputPane {
  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** HashMap permettant d'associer les {@code FLUID} a leur bouton respectif */
  private HashMap<Fluid, AbstractButton> mapFluidPresets;

  /** Bouton de réinitialisation de la simulation */
  private JButton btnReinit;

  /** Panneau de défilement des propriétés */
  private JScrollPane scrollPane;

  /** Sélecteur de la viscosité */
  private JSpinner spinnerViscosity;

  /** Étiquette de la vitesse de sortie */
  private JLabel lblOutputSpeed;

  /** Sélecteur de la vitesse de sortie */
  private JSpinner spinnerOutputSpeed;

  /** Sélecteur de la masse volumique */
  private JSpinner spinnerDensity;

  /** Sélecteur du type de bordure */
  private JComboBox<BORDER_TYPE> comboBoxBorderType;

  /** Sélecteur du pas de temps */
  private JSpinner spinnerTimeStep;

  /** Sélecteur de l'écoulement tourbillonnaire */
  private JSpinner spinnerVortex;

  /** Sélecteur de la taille réelle de la grille en X */
  private JSpinner spinnerPhysicalSizeX;

  /** Sélecteur de la taille réelle de la grille en Y */
  private JSpinner spinnerPhysicalSizeY;

  /** Bouton de préréglage de simulation d'air */
  private JButton btnAir;

  /** Bouton de préréglage de simulation d'eau */
  private JButton btnWater;

  /** Bouton de préréglage de simulation d'huile */
  private JButton btnOil;

  /** Bouton de préréglage de simulation de miel */
  private JButton btnHoney;

  /** Menu déroulant du paramètre visualisé */
  private JComboBox<VisualizationType> comboBoxVisualizedParam;

  /** Case à cocher pour activer ou non la visualisation en nuances de gris */
  private JCheckBox chckbxGrayscaleVisualisation;

  /** Case à cocher pour activer ou non l'affichage des vecteurs */
  private JCheckBox chckbxVectors;

  /** Petit espace */
  private final int SMALL_GAP = 5;

  /** Taille minimale des boutons de préréglages */
  private static final Dimension MINIMUM_PRESET_BTN_SIZE = new Dimension(20, 50);

  /** Taille minimale des composants de saisie */
  private static final Dimension MINIMUM_INPUT_COMPONENT_SIZE = new Dimension(50, 26);

  /** Taille maximale des composants de saisie */
  private static final Dimension MAXIMUM_INPUT_COMPONENT_SIZE = new Dimension(150, 26);

  /** Taille préférée des composants de saisie */
  private static final Dimension PREFERRED_INPUT_COMPONENT_SIZE = new Dimension(120, 26);

  /** Taille des étiquettes des unités */
  private static final Dimension UNIT_LBL_SIZE = new Dimension(MEDIUM_STRUCT_WIDTH, 16);

  /**
   * Constructeur de la classe SimulationInputPane initialisant le panneau de saisie des entrées de
   * la simulation
   *
   * @param controller Le {@code Controller} de l'application
   */
  public SimulationInputPane(Controller controller) {
    super(controller, "ui.simulation.properties.lbl", "simulation_settings");
    this.controller = controller;

    PANEL_LAYOUT.setRowConstraints("[][][grow]");

    addHideableElements();

    setupControls();
    setupInitialValues();
    setupSubscribers();
  }

  /** Méthode initialisant les abonnements aux événements de l'application */
  private void setupSubscribers() {
    controller
        .getRendererModel()
        .getSubscriber()
        .subscribe(
            Subject.KEYBIND_VISUALIZATION_UP_PRESSED,
            e -> {
              comboBoxVisualizedParam.setSelectedIndex(
                  WMath.positiveModulus(
                      comboBoxVisualizedParam.getSelectedIndex() - 1,
                      comboBoxVisualizedParam.getItemCount()));
              onVisualizationTypeSelection(null);
            });
    controller
        .getRendererModel()
        .getSubscriber()
        .subscribe(
            Subject.KEYBIND_VISUALIZATION_DOWN_PRESSED,
            e -> {
              comboBoxVisualizedParam.setSelectedIndex(
                  WMath.positiveModulus(
                      comboBoxVisualizedParam.getSelectedIndex() + 1,
                      comboBoxVisualizedParam.getItemCount()));
              onVisualizationTypeSelection(null);
            });

    controller
        .getModelLoader()
        .getSubscriber()
        .subscribe(Subject.ON_LOAD_SIMULATION_MODEL, msg -> setupInitialValues());

    controller
        .getModelLoader()
        .getSubscriber()
        .subscribe(Subject.ON_LOAD_EDITOR_MODEL, msg -> setupInitialValues());

    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribe(
            Subject.ON_APP_HELP_BUTTON_VISIBILITY_CHANGED,
            e -> {
              this.remove(this.getComponentCount() - 1);
              add(createReinitPanel((boolean) e), "newline,grow");
            });

    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribe(
            Subject.ON_APP_SCENE_LOADED,
            e -> {
              setupInitialValues();
            });
  }

  /** Ajoute les éléments cachables du panneau */
  private void addHideableElements() {
    scrollPane = new JScrollPane();
    scrollPane.setBorder(null);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    add(scrollPane, "newline,grow");

    JPanel panelProperties = new JPanel();
    scrollPane.setViewportView(panelProperties);
    panelProperties.setLayout(new MigLayout(LAYOUT_CONSTRAINTS, "[grow]", "[][][grow]"));
    panelProperties.putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");

    panelProperties.add(createPresetPanel(), "grow");

    panelProperties.add(createSettingsPanel(), "newline,grow");

    panelProperties.add(createRenderPanel(), "newline,grow");

    add(
        createReinitPanel(controller.getApplicationModel().getHelpButtonVisibility()),
        "newline,grow");
  }

  /**
   * Crée le panneau de reinitialisation ainsi que son bouton aide
   *
   * @param isHelpButtonVisible {@code true} pour afficher le bouton d'aide, {@code false} pour le
   *     cacher
   * @return Le panneau de reinitialisation
   */
  private JPanel createReinitPanel(boolean isHelpButtonVisible) {
    JPanel panelReinit = new JPanel();
    String colConstraints = "[grow]";
    if (isHelpButtonVisible) {
      colConstraints += "[]";
    }
    panelReinit.setLayout(new MigLayout("gap 5, ins 0", colConstraints, "[]"));
    panelReinit.putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanelContents");
    panelReinit.setBorder(null);

    btnReinit =
        new JButton(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.simulation.reinitialize_everything"));

    Arrays.stream(btnReinit.getActionListeners()).forEach(btnReinit::removeActionListener);
    btnReinit.addActionListener(this::onReinitialize);
    panelReinit.add(btnReinit, "grow");

    if (isHelpButtonVisible) {
      HelpButtonComponent helpButton = new HelpButtonComponent(controller, "reset_all");
      panelReinit.add(helpButton);
    }

    if (RendererConstants.USE_OPENGL) {
      btnReinit.setEnabled(false);
    }
    btnReinit.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.reset_app"));

    return panelReinit;
  }

  /**
   * Crée le panneau de préréglages
   *
   * @return Le panneau de préréglages
   */
  private JPanel createPresetPanel() {
    JPanel panelPresets = new JPanel();
    panelPresets.putClientProperty(FlatClientProperties.STYLE_CLASS, "subPanel");
    panelPresets.setLayout(new MigLayout(LAYOUT_CONSTRAINTS, "[grow]", "[grow][]"));

    JLabel lblPresets =
        new JLabel(LocaleManager.getLocaleResourceBundle().getString("ui.simulation.presets.lbl"));
    lblPresets.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
    panelPresets.add(lblPresets, "grow");

    JPanel panelPresetBtns = new JPanel();
    panelPresets.add(panelPresetBtns, "newline,grow");
    panelPresetBtns.setLayout(new GridLayout(2, 2, SMALL_GAP, SMALL_GAP));

    btnAir = new JButton(Fluid.AIR.toString());
    btnWater = new JButton(Fluid.WATER.toString());
    btnOil = new JButton(Fluid.OIL.toString());
    btnHoney = new JButton(Fluid.HONEY.toString());

    btnAir.setToolTipText(LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.presets"));
    btnWater.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.presets"));
    btnOil.setToolTipText(LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.presets"));
    btnHoney.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.presets"));

    btnAir.setHorizontalTextPosition(SwingConstants.CENTER);
    btnAir.setMinimumSize(MINIMUM_PRESET_BTN_SIZE);
    panelPresetBtns.add(btnAir);

    btnWater.setHorizontalTextPosition(SwingConstants.CENTER);
    btnWater.setMinimumSize(MINIMUM_PRESET_BTN_SIZE);
    panelPresetBtns.add(btnWater);

    btnOil.setHorizontalTextPosition(SwingConstants.CENTER);
    btnOil.setMinimumSize(MINIMUM_PRESET_BTN_SIZE);
    panelPresetBtns.add(btnOil);

    btnHoney.setHorizontalTextPosition(SwingConstants.CENTER);
    btnHoney.setMinimumSize(MINIMUM_PRESET_BTN_SIZE);
    panelPresetBtns.add(btnHoney);

    return panelPresets;
  }

  /**
   * Crée le panneau de réglages
   *
   * @return Le panneau de réglages
   */
  private JPanel createSettingsPanel() {
    JPanel panelSettings = new JPanel();
    panelSettings.putClientProperty(FlatClientProperties.STYLE_CLASS, "subPanel");

    panelSettings.setLayout(new MigLayout(LAYOUT_CONSTRAINTS, "[grow]", "[grow][][][][][][][]"));

    JLabel lblSettings =
        new JLabel(LocaleManager.getLocaleResourceBundle().getString("ui.simulation.settings.lbl"));
    lblSettings.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
    panelSettings.add(lblSettings, "grow");

    JPanel panelViscosity = new JPanel();

    panelSettings.add(panelViscosity, "newline,grow");
    panelViscosity.setLayout(new BoxLayout(panelViscosity, BoxLayout.X_AXIS));
    panelViscosity.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.dynamic_viscosity"));

    JLabel lblViscosity =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.viscosity.lbl"));
    panelViscosity.add(lblViscosity);

    panelViscosity.add(Box.createGlue());

    spinnerViscosity = new JSpinner();
    spinnerViscosity.setMaximumSize(MAXIMUM_INPUT_COMPONENT_SIZE);
    spinnerViscosity.setMinimumSize(MINIMUM_INPUT_COMPONENT_SIZE);
    spinnerViscosity.setPreferredSize(PREFERRED_INPUT_COMPONENT_SIZE);
    spinnerViscosity.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.dynamic_viscosity"));
    panelViscosity.add(spinnerViscosity);

    panelViscosity.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));

    JLabel lblMeterSquareSecond =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.viscosity.unit"));
    lblMeterSquareSecond.setMaximumSize(UNIT_LBL_SIZE);
    lblMeterSquareSecond.setMinimumSize(UNIT_LBL_SIZE);
    lblMeterSquareSecond.setPreferredSize(UNIT_LBL_SIZE);
    panelViscosity.add(lblMeterSquareSecond);

    JPanel panelVolumicMass = new JPanel();
    panelSettings.add(panelVolumicMass, "newline,grow");
    panelVolumicMass.setLayout(new BoxLayout(panelVolumicMass, BoxLayout.X_AXIS));

    JLabel lblVolumicMass =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.volumic_mass.lbl"));
    panelVolumicMass.add(lblVolumicMass);
    panelVolumicMass.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.density"));

    panelVolumicMass.add(Box.createGlue());

    spinnerDensity = new JSpinner();
    spinnerDensity.setMaximumSize(MAXIMUM_INPUT_COMPONENT_SIZE);
    spinnerDensity.setMinimumSize(MINIMUM_INPUT_COMPONENT_SIZE);
    spinnerDensity.setPreferredSize(PREFERRED_INPUT_COMPONENT_SIZE);
    spinnerDensity.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.density"));
    panelVolumicMass.add(spinnerDensity);

    panelVolumicMass.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));

    JLabel lblKgPerCubicMeter =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.volumic_mass.unit"));
    lblKgPerCubicMeter.setMaximumSize(UNIT_LBL_SIZE);
    lblKgPerCubicMeter.setMinimumSize(UNIT_LBL_SIZE);
    lblKgPerCubicMeter.setPreferredSize(UNIT_LBL_SIZE);
    panelVolumicMass.add(lblKgPerCubicMeter);

    JPanel panelBorderType = new JPanel();
    panelSettings.add(panelBorderType, "newline,grow");
    panelBorderType.setLayout(new BoxLayout(panelBorderType, BoxLayout.X_AXIS));

    JLabel lblBorderType =
        new JLabel(LocaleManager.getLocaleResourceBundle().getString("ui.simulation.border.lbl"));
    panelBorderType.add(lblBorderType);

    panelBorderType.add(Box.createGlue());
    panelBorderType.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.border_type"));

    comboBoxBorderType = new JComboBox<BORDER_TYPE>();
    panelBorderType.add(comboBoxBorderType);
    comboBoxBorderType.setModel(new DefaultComboBoxModel<BORDER_TYPE>(BORDER_TYPE.values()));
    comboBoxBorderType.setPreferredSize(PREFERRED_INPUT_COMPONENT_SIZE);
    comboBoxBorderType.setMinimumSize(MINIMUM_INPUT_COMPONENT_SIZE);
    comboBoxBorderType.setMaximumSize(MAXIMUM_INPUT_COMPONENT_SIZE);
    comboBoxBorderType.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.border_type"));

    panelBorderType.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));

    panelBorderType.add(Box.createHorizontalStrut(MEDIUM_STRUCT_WIDTH));

    JPanel panelOutputSpeed = new JPanel();
    panelSettings.add(panelOutputSpeed, "newline,grow");
    panelOutputSpeed.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.output_speed"));
    panelOutputSpeed.setLayout(new BoxLayout(panelOutputSpeed, BoxLayout.X_AXIS));

    lblOutputSpeed =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.output_speed.lbl"));
    panelOutputSpeed.add(lblOutputSpeed);

    panelOutputSpeed.add(Box.createGlue());

    spinnerOutputSpeed = new JSpinner();
    spinnerOutputSpeed.setMaximumSize(MAXIMUM_INPUT_COMPONENT_SIZE);
    spinnerOutputSpeed.setPreferredSize(PREFERRED_INPUT_COMPONENT_SIZE);
    spinnerOutputSpeed.setMinimumSize(MINIMUM_INPUT_COMPONENT_SIZE);
    panelOutputSpeed.add(spinnerOutputSpeed);

    panelOutputSpeed.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));

    JLabel lblMetersPerSecond =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.output_speed.unit"));
    lblMetersPerSecond.setMaximumSize(UNIT_LBL_SIZE);
    lblMetersPerSecond.setMinimumSize(UNIT_LBL_SIZE);
    lblMetersPerSecond.setPreferredSize(UNIT_LBL_SIZE);
    panelOutputSpeed.add(lblMetersPerSecond);

    setOutputSpeedTooltip();

    JPanel panelVortexFlow = new JPanel();
    panelSettings.add(panelVortexFlow, "newline,grow");
    panelVortexFlow.setLayout(new BoxLayout(panelVortexFlow, BoxLayout.X_AXIS));
    panelVortexFlow.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.vortex_factor"));

    JLabel lblVortexFlow =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.vortex_flow.lbl"));
    panelVortexFlow.add(lblVortexFlow);

    panelVortexFlow.add(Box.createGlue());

    spinnerVortex = new JSpinner();
    spinnerVortex.setMaximumSize(MAXIMUM_INPUT_COMPONENT_SIZE);
    spinnerVortex.setMinimumSize(MINIMUM_INPUT_COMPONENT_SIZE);
    spinnerVortex.setPreferredSize(PREFERRED_INPUT_COMPONENT_SIZE);
    spinnerVortex.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.vortex_factor"));
    panelVortexFlow.add(spinnerVortex);

    panelVortexFlow.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));

    panelVortexFlow.add(Box.createHorizontalStrut(MEDIUM_STRUCT_WIDTH));

    JPanel panelPhysicalSizeX = new JPanel();

    panelSettings.add(panelPhysicalSizeX, "newline,grow");
    panelPhysicalSizeX.setLayout(new BoxLayout(panelPhysicalSizeX, BoxLayout.X_AXIS));
    panelPhysicalSizeX.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.physical_x_size"));

    JLabel lblPhysicalSizeX =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.physical_size_x.lbl"));
    panelPhysicalSizeX.add(lblPhysicalSizeX);

    panelPhysicalSizeX.add(Box.createGlue());

    spinnerPhysicalSizeX = new JSpinner();
    spinnerPhysicalSizeX.setMaximumSize(MAXIMUM_INPUT_COMPONENT_SIZE);
    spinnerPhysicalSizeX.setMinimumSize(MINIMUM_INPUT_COMPONENT_SIZE);
    spinnerPhysicalSizeX.setPreferredSize(PREFERRED_INPUT_COMPONENT_SIZE);
    spinnerPhysicalSizeX.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.physical_x_size"));
    panelPhysicalSizeX.add(spinnerPhysicalSizeX);

    panelPhysicalSizeX.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));

    JLabel lblMetersX =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.physical_size.unit"));
    lblMetersX.setMaximumSize(UNIT_LBL_SIZE);
    lblMetersX.setMinimumSize(UNIT_LBL_SIZE);
    lblMetersX.setPreferredSize(UNIT_LBL_SIZE);

    panelPhysicalSizeX.add(lblMetersX);

    JPanel panelPhysicalSizeY = new JPanel();
    panelPhysicalSizeY.setLayout(new BoxLayout(panelPhysicalSizeY, BoxLayout.X_AXIS));
    panelPhysicalSizeY.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.physical_y_size"));

    panelSettings.add(panelPhysicalSizeY, "newline,grow");

    JLabel lblPhysicalSizeY =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.physical_size_y.lbl"));
    panelPhysicalSizeY.add(lblPhysicalSizeY);

    panelPhysicalSizeY.add(Box.createGlue());

    spinnerPhysicalSizeY = new JSpinner();
    spinnerPhysicalSizeY.setMaximumSize(MAXIMUM_INPUT_COMPONENT_SIZE);
    spinnerPhysicalSizeY.setMinimumSize(MINIMUM_INPUT_COMPONENT_SIZE);
    spinnerPhysicalSizeY.setPreferredSize(PREFERRED_INPUT_COMPONENT_SIZE);
    spinnerPhysicalSizeY.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.physical_y_size"));
    panelPhysicalSizeY.add(spinnerPhysicalSizeY);

    panelPhysicalSizeY.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));

    JLabel lblMetersY =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.physical_size.unit"));
    lblMetersY.setMaximumSize(UNIT_LBL_SIZE);
    lblMetersY.setMinimumSize(UNIT_LBL_SIZE);
    lblMetersY.setPreferredSize(UNIT_LBL_SIZE);

    panelPhysicalSizeY.add(lblMetersY);

    JPanel panelTimeStep = new JPanel();

    panelSettings.add(panelTimeStep, "newline,grow");
    panelTimeStep.setLayout(new BoxLayout(panelTimeStep, BoxLayout.X_AXIS));
    panelTimeStep.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.time_step"));

    JLabel lblTimeStep =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.time_step.lbl"));
    panelTimeStep.add(lblTimeStep);

    panelTimeStep.add(Box.createGlue());

    spinnerTimeStep = new JSpinner();
    spinnerTimeStep.setMaximumSize(MAXIMUM_INPUT_COMPONENT_SIZE);
    spinnerTimeStep.setMinimumSize(MINIMUM_INPUT_COMPONENT_SIZE);
    spinnerTimeStep.setPreferredSize(PREFERRED_INPUT_COMPONENT_SIZE);
    spinnerTimeStep.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.time_step"));

    panelTimeStep.add(spinnerTimeStep);

    panelTimeStep.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));

    JLabel lblSeconds =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.time_step.unit"));

    lblSeconds.setMaximumSize(UNIT_LBL_SIZE);
    lblSeconds.setMinimumSize(UNIT_LBL_SIZE);
    lblSeconds.setPreferredSize(UNIT_LBL_SIZE);

    panelTimeStep.add(lblSeconds);

    return panelSettings;
  }

  /**
   * Crée le panneau de rendu
   *
   * @return Le panneau de rendu
   */
  private JPanel createRenderPanel() {
    JPanel panelRender = new JPanel();
    panelRender.putClientProperty(FlatClientProperties.STYLE_CLASS, "subPanel");
    panelRender.setLayout(new MigLayout(LAYOUT_CONSTRAINTS, "[grow]", "[20px][][][][]"));

    JPanel renderTitlePanel = new JPanel();
    renderTitlePanel.setLayout(new BoxLayout(renderTitlePanel, BoxLayout.X_AXIS));
    panelRender.add(renderTitlePanel, "grow");
    JLabel lblRender =
        new JLabel(
            LocaleManager.getLocaleResourceBundle().getString("ui.simulation.visualization.lbl"));
    lblRender.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
    renderTitlePanel.add(lblRender);
    renderTitlePanel.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));
    HelpButtonComponent helpButton = new HelpButtonComponent(controller, "properties_pane");
    renderTitlePanel.add(helpButton, "cell 1 0");
    renderTitlePanel.add(Box.createVerticalStrut(HelpButtonComponent.BUTTON_SIZE.height));

    JPanel panelVisualizedParam = new JPanel();
    panelRender.add(panelVisualizedParam, "newline,grow");
    panelVisualizedParam.setLayout(new BoxLayout(panelVisualizedParam, BoxLayout.X_AXIS));
    panelVisualizedParam.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.vizualtion_parameter"));

    JLabel lblVisualizedParam =
        new JLabel(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.simulation.visualization.visualized_parameter.lbl"));
    panelVisualizedParam.add(lblVisualizedParam);

    panelVisualizedParam.add(Box.createGlue());

    comboBoxVisualizedParam = new JComboBox<VisualizationType>();
    comboBoxVisualizedParam.setModel(
        new DefaultComboBoxModel<VisualizationType>(VisualizationType.values()));
    panelVisualizedParam.add(comboBoxVisualizedParam);
    comboBoxVisualizedParam.setPreferredSize(new Dimension(120, 26));
    comboBoxVisualizedParam.setMinimumSize(MINIMUM_INPUT_COMPONENT_SIZE);
    comboBoxVisualizedParam.setMaximumSize(MAXIMUM_INPUT_COMPONENT_SIZE);
    comboBoxVisualizedParam.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.vizualtion_parameter"));

    JPanel panelGrayscale = new JPanel();
    panelRender.add(panelGrayscale, "newline,grow");
    panelGrayscale.setLayout(new BoxLayout(panelGrayscale, BoxLayout.X_AXIS));
    panelGrayscale.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.grayscale"));

    JLabel lblGrayscale =
        new JLabel(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.simulation.visualization.grayscale.lbl"));
    panelGrayscale.add(lblGrayscale);

    panelGrayscale.add(Box.createGlue());

    chckbxGrayscaleVisualisation = new JCheckBox();
    panelGrayscale.add(chckbxGrayscaleVisualisation);
    if (RendererConstants.USE_OPENGL) {
      chckbxGrayscaleVisualisation.setSelected(true);
      onCheckGrayscaleVisualisation(null);
      chckbxGrayscaleVisualisation.setEnabled(false);
    }
    chckbxGrayscaleVisualisation.setHorizontalTextPosition(SwingConstants.LEFT);
    chckbxGrayscaleVisualisation.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxGrayscaleVisualisation.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.grayscale"));

    JPanel panelVectors = new JPanel();
    panelRender.add(panelVectors, "newline,grow");
    panelVectors.setLayout(new BoxLayout(panelVectors, BoxLayout.X_AXIS));
    panelVectors.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.vectors"));

    JLabel lblVectors =
        new JLabel(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.simulation.visualization.vectors.lbl"));
    panelVectors.add(lblVectors);

    panelVectors.add(Box.createGlue());

    chckbxVectors = new JCheckBox();
    panelVectors.add(chckbxVectors);
    if (RendererConstants.USE_OPENGL) {
      chckbxVectors.setEnabled(false);
    }
    chckbxVectors.setHorizontalTextPosition(SwingConstants.LEFT);
    chckbxVectors.setHorizontalAlignment(SwingConstants.LEFT);
    chckbxVectors.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.vectors.disable"));
    chckbxVectors.setEnabled(false);

    return panelRender;
  }

  /** Gère l'affichage ou non des propriétés */
  @Override
  public void handleShowProperties() {
    updateSize();
    if (chckbxShow.isSelected()) {
      scrollPane.setEnabled(true);
      if (!RendererConstants.USE_OPENGL) {
        btnReinit.setEnabled(true);
      }
      scrollPane.setVisible(true);
      btnReinit.setVisible(true);
    } else {
      scrollPane.setEnabled(false);
      btnReinit.setEnabled(false);
      scrollPane.setVisible(false);
      btnReinit.setVisible(false);
    }
    super.handleShowProperties();
  }

  /** Met à jour le tooltip de la vitesse de sortie, puisqu'elle est conditionnelle */
  private void setOutputSpeedTooltip() {
    if (controller.getSimulationModel().getBorderType() != BORDER_TYPE.WIND_TUNNEL) {
      spinnerOutputSpeed.setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.output_speed.disabled"));
    } else {
      spinnerOutputSpeed.setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.output_speed"));
    }
  }

  // =============
  // SETUP METHODS
  // =============

  /** Methode dans laquelle les {@code Listeners} sont definis. */
  private void setupControls() {
    setupSpinnerControls();
    setupFluidPresetControls();

    comboBoxVisualizedParam.addActionListener(this::onVisualizationTypeSelection);
    comboBoxBorderType.addActionListener(this::onBorderTypeSelection);

    Arrays.stream(btnReinit.getActionListeners()).forEach(btnReinit::removeActionListener);
    btnReinit.addActionListener(this::onReinitialize);

    chckbxVectors.addActionListener(this::onCheckShowVectors);
    chckbxGrayscaleVisualisation.addActionListener(this::onCheckGrayscaleVisualisation);

    btnAir.addActionListener(this::onFluidPresetSelection);
    btnWater.addActionListener(this::onFluidPresetSelection);
    btnOil.addActionListener(this::onFluidPresetSelection);
    btnHoney.addActionListener(this::onFluidPresetSelection);

    mapFluidPresets = new HashMap<>();
    mapFluidPresets.put(Fluid.AIR, btnAir);
    mapFluidPresets.put(Fluid.WATER, btnWater);
    mapFluidPresets.put(Fluid.OIL, btnOil);
    mapFluidPresets.put(Fluid.HONEY, btnHoney);
  }

  /** Methode dans laquelle les valeurs intiiales des composants sont initialisees */
  private void setupInitialValues() {
    spinnerOutputSpeed.getModel().setValue(controller.getSimulationModel().getOutputSpeed());

    spinnerViscosity
        .getModel()
        .setValue(
            controller.getSimulationModel().getViscosity()
                * SimulationConstants.VISCOSITY_SPINNER_FACTOR);

    spinnerDensity.getModel().setValue(controller.getSimulationModel().getVolumeDensity());

    spinnerTimeStep.getModel().setValue(controller.getSimulationModel().getTimeStep() * 1000);

    spinnerVortex.getModel().setValue(controller.getSimulationModel().getVortexConfinementFactor());

    comboBoxBorderType.getModel().setSelectedItem(controller.getSimulationModel().getBorderType());
    setOutputSpeedTooltip();

    comboBoxVisualizedParam
        .getModel()
        .setSelectedItem(controller.getRendererModel().getCurrentVisualizationType());

    chckbxVectors.setSelected(controller.getRendererModel().getShowVectors());

    chckbxGrayscaleVisualisation.setSelected(
        controller.getRendererModel().getGrayscaleVisualisation());
  }

  /** Methode dans laquelle les controles des spinners sont intiialises */
  private void setupSpinnerControls() {
    spinnerOutputSpeed.setModel(
        new SpinnerNumberModel(
            controller.getSimulationModel().getOutputSpeed(), 0, Double.MAX_VALUE, 0.1));

    spinnerDensity.setModel(
        new SpinnerNumberModel(
            controller.getSimulationModel().getVolumeDensity(), 0, Double.MAX_VALUE, 0.1));

    spinnerTimeStep.setModel(
        new SpinnerNumberModel(
            controller.getSimulationModel().getTimeStep() * 1000,
            WMath.EPSILON,
            Double.MAX_VALUE,
            1));

    spinnerVortex.setModel(
        new SpinnerNumberModel(
            controller.getSimulationModel().getVortexConfinementFactor(),
            SimulationConstants.VORTEX_CONFINEMENT_MIN_FACTOR,
            SimulationConstants.VORTEX_CONFINEMENT_MAX_FACTOR,
            0.05));

    spinnerPhysicalSizeX.setModel(
        new SpinnerNumberModel(
            controller.getSimulationModel().getPhysicalXSize(),
            WMath.EPSILON,
            Double.MAX_VALUE,
            1));

    spinnerPhysicalSizeY.setModel(
        new SpinnerNumberModel(
            controller.getSimulationModel().getPhysicalYSize(),
            WMath.EPSILON,
            Double.MAX_VALUE,
            1));

    spinnerViscosity.setModel(
        new SpinnerNumberModel(
            controller.getSimulationModel().getViscosity()
                * SimulationConstants.VISCOSITY_SPINNER_FACTOR,
            SimulationConstants.MIN_VISCOSITY,
            SimulationConstants.MAX_VISCOSITY,
            0.1));

    spinnerViscosity.addChangeListener(this::onViscosityChange);
    spinnerOutputSpeed.addChangeListener(this::onOutputSpeedChange);
    spinnerDensity.addChangeListener(this::onVolumeDensityChange);
    spinnerVortex.addChangeListener(this::onVortexFactorChange);
    spinnerTimeStep.addChangeListener(this::onTimeStepChange);
    spinnerPhysicalSizeX.addChangeListener(this::onPhysicalXSizeChange);
    spinnerPhysicalSizeY.addChangeListener(this::onPhysicalYSizeChange);
  }

  /** Methode dans laquelle les controles des boutons de prereglages de fluide sont initialises */
  private void setupFluidPresetControls() {}

  // =============
  // EVENT METHODS
  // =============

  /**
   * Methode a invoquer lors de la selection d'un {@code VisualizationType}
   *
   * @param e {@code ActionEvent}
   */
  private void onVisualizationTypeSelection(ActionEvent e) {
    controller
        .getRendererModel()
        .setCurrentVisualizationType((VisualizationType) comboBoxVisualizedParam.getSelectedItem());
    controller
        .getRendererModel()
        .getPublisher()
        .publish(Subject.ON_SIMULATION_VISUALIZATION_TYPE_CHANGED, null);
    if (this.controller.getSimulationModel().getCurrentTime() > 0.0f) {
      controller.getRendererModel().handleCurrentFrame();
    }

    if (controller.getRendererModel().getCurrentVisualizationType() == VisualizationType.VELOCITY) {
      chckbxVectors.setEnabled(true);
      chckbxVectors.setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.vectors"));
    } else {
      chckbxVectors.setEnabled(false);
      chckbxVectors.setSelected(false);
      chckbxVectors.setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.vectors.disable"));
      controller.getRendererModel().setShowVectors(false);
    }
  }

  /**
   * Methode a invoquer lors de la selection d'un {@code FLUID}
   *
   * @param e {@code ActionEvent}
   */
  private void onFluidPresetSelection(ActionEvent e) {
    for (Fluid preset : Fluid.values()) {
      if (e.getSource().equals(mapFluidPresets.get(preset))) {
        updateFluidPreset(preset);
        setupInitialValues();
        return;
      }
    }
  }

  /**
   * Methode a invoquer lors de la selection d'un {@code BORDER_TYPE}
   *
   * @param e {@code ActionEvent}
   */
  private void onBorderTypeSelection(ActionEvent e) {
    controller
        .getSimulationModel()
        .setBorderType((BORDER_TYPE) comboBoxBorderType.getSelectedItem());
    spinnerOutputSpeed.setEnabled(
        comboBoxBorderType.getSelectedItem().equals(BORDER_TYPE.WIND_TUNNEL));
    setOutputSpeedTooltip();
  }

  /**
   * Methode a invoquer lors du changement de viscosite
   *
   * @param e {@code ChangeEvent}
   */
  private void onViscosityChange(ChangeEvent e) {
    controller
        .getSimulationModel()
        .setViscosity(
            (Double) spinnerViscosity.getValue() / SimulationConstants.VISCOSITY_SPINNER_FACTOR);
  }

  /**
   * Methode a invoquer lors du changement de la vitesse de sortie
   *
   * @param e {@code ChangeEvent}
   */
  private void onOutputSpeedChange(ChangeEvent e) {
    controller.getSimulationModel().setOutputSpeed((Double) spinnerOutputSpeed.getValue());
  }

  /**
   * Methode a invoquer lors du changement de la masse volumique
   *
   * @param e {@code ChangeEvent}
   */
  private void onVolumeDensityChange(ChangeEvent e) {
    controller.getSimulationModel().setVolumeDensity((Double) spinnerDensity.getValue());
  }

  /**
   * Methode a invoquer lors du changement du pas de temps
   *
   * @param e {@code ChangeEvent}
   */
  private void onTimeStepChange(ChangeEvent e) {
    controller.getSimulationModel().setTimeStep((Double) spinnerTimeStep.getValue() / 1000);
  }

  /**
   * Methode a invoquer lors du changement de la taille reelle de la grille en X
   *
   * @param e {@code ChangeEvent}
   */
  public void onPhysicalXSizeChange(ChangeEvent e) {
    controller.getSimulationModel().setPhysicalXSize((Double) spinnerPhysicalSizeX.getValue());
    spinnerPhysicalSizeY.getModel().setValue(controller.getSimulationModel().getPhysicalYSize());
  }

  /**
   * Methode a invoquer lors du changement de la taille reelle de la grille en Y
   *
   * @param e {@code ChangeEvent}
   */
  public void onPhysicalYSizeChange(ChangeEvent e) {
    controller.getSimulationModel().setPhysicalYSize((Double) spinnerPhysicalSizeY.getValue());
    spinnerPhysicalSizeX.getModel().setValue(controller.getSimulationModel().getPhysicalXSize());
  }

  /**
   * Methode a invoquer lors du changement de l'ecoulement tourbillonnaire
   *
   * @param e {@code ChangeEvent}
   */
  private void onVortexFactorChange(ChangeEvent e) {
    controller.getSimulationModel().setVortexConfinementFactor((Double) spinnerVortex.getValue());
  }

  /**
   * Methode a invoquer lors de la selection du CheckBox d'affichage de vecteurs
   *
   * @param e {@code ActionEvent}
   */
  private void onCheckShowVectors(ActionEvent e) {
    controller.getRendererModel().setShowVectors(chckbxVectors.isSelected());
  }

  /**
   * Methode a invoquer lors de la selection du CheckBox d'affichage en niveaux de gris.
   *
   * @param e {@code ActionEvent}
   */
  private void onCheckGrayscaleVisualisation(ActionEvent e) {
    controller
        .getRendererModel()
        .setGrayscaleVisualisation(chckbxGrayscaleVisualisation.isSelected());
    controller
        .getRendererModel()
        .getPublisher()
        .publish(Subject.ON_SIMULATION_VISUALIZATION_TYPE_CHANGED, null);
    controller
        .getRendererModel()
        .getPublisher()
        .publish(Subject.ON_GRAYSCALE_VISUALISATION_CHANGED, null);
    controller.getRendererModel().handleCurrentFrame();
  }

  /**
   * Methode permettant de modifier le prereglage actuel de fluide.
   *
   * @param fluid Le nouveau {@code FLUID}
   */
  private void updateFluidPreset(Fluid fluid) {
    controller.getSimulationModel().setCurrentFluid(fluid);
  }

  /**
   * Methode invoquee lors de la reinitialisation de l'application
   *
   * @param e {@code ActionEvent}
   */
  private void onReinitialize(ActionEvent e) {
    controller.getApplicationModel().onReinitialize();
  }
}
