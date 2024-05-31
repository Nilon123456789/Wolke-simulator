package com.e24.wolke.frontend;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.keybinds.KeybindConstants;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.ApplicationConstants.Resolution;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.renderer.RendererConstants;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.filesystem.WFileSystemConstant;
import com.e24.wolke.filesystem.scenes.WSceneConstants;
import com.e24.wolke.frontend.help.AboutMenu;
import com.e24.wolke.frontend.help.InstructionMenu;
import com.e24.wolke.frontend.keybinds.KeybindFrame;
import com.e24.wolke.utils.interfaces.Reinitializable;
import java.awt.Desktop;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.FileUtils;

/**
 * Barre de menu
 *
 * @author n-o-o-d-l-e
 * @author MeriBouisri
 */
public class MenuBar extends JMenuBar implements WEventComponent, Reinitializable {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Un {@code HashMap} permettant d'associer les boutons a un {@code Locale} */
  private HashMap<Locale, AbstractButton> languageGroupMap;

  /** {@code HashMap} permettant d'associer les boutons a un {@code Resolution} */
  private HashMap<Resolution, AbstractButton> resolutionGroupMap;

  /** {@code HashMap} permettant d'associer les boutons a un {@code AvailableLAF} */
  private HashMap<Class<?>, AbstractButton> themeGroupMap;

  /** Représente le menu "Fichier" dans la barre de menu. */
  private JMenu mnFile;

  /** Représente le menu "Affichage" dans la barre de menu. */
  private JMenu mnView;

  /** Représente le menu "Options" dans la barre de menu. */
  private JMenu mnOptions;

  /** Représente le menu "Aide" dans la barre de menu. */
  private JMenu mnHelp;

  /** Représente l'élément de menu "Charger" dans le menu "Fichier". */
  private JMenu mnLoad;

  /** Represente l'element de menu pour charger les donnees de {@code ApplicationModel} */
  private JMenuItem mntmLoadApplicationModel;

  /** Represente l'element de menu pour charger les donnees de {@code SimulationModel} */
  private JMenuItem mntmLoadSimulationModel;

  /** Represente l'element de menu pour charger les donnees de {@code RendererModel} */
  private JMenuItem mntmLoadRendererModel;

  /** Represente l'element de menu pour sauvegarder les donnees de {@code ApplicationModel} */
  private JMenuItem mntmSaveApplicationModel;

  /** Represente l'element de menu pour sauvegarder les donnees de {@code SimulationModel} */
  private JMenuItem mntmSaveSimulationModel;

  /** Represente l'element de menu pour sauvegarder les donnees de {@code RendererModel} */
  private JMenuItem mntmSaveRendererModel;

  /** Représente l'élément de menu "Enregistrer" dans le menu "Fichier". */
  private JMenu mnSave;

  /** Représente l'élément de menu avec case à cocher "Console" dans le menu "Affichage". */
  private JCheckBoxMenuItem chckbxmntmConsole;

  /** Représente l'élément de menu avec case à cocher "Mode Zen" dans le menu "Affichage". */
  private JCheckBoxMenuItem chckbxmntmZenMode;

  /** Représente l'élément de menu avec case à cocher "Légende" dans le menu "Affichage". */
  private JCheckBoxMenuItem chckbxmntmLegend;

  /** Représente le séparateur d'éléments dans le menu "Affichage" après "Légende". */
  private JSeparator separatorExamples;

  /** Separateur d'elements avant proprietes */
  private JSeparator separatorProperties;

  /** Import de la scene */
  private JMenuItem mntmImportScene;

  /** Export de la scene */
  private JMenuItem mntmExportScene;

  /** Représente l'élément de menu radio "Basse résolution" dans le menu "Affichage". */
  private JRadioButtonMenuItem rdbtnmntmLowRes;

  /** Représente l'élément de menu radio "Résolution moyenne" dans le menu "Affichage". */
  private JRadioButtonMenuItem rdbtnmntmMediumRes;

  /** Représente l'élément de menu radio "Haute résolution" dans le menu "Affichage". */
  private JRadioButtonMenuItem rdbtnmntmHighRes;

  /** Représente l'élément de menu radio "Ultra résolution" dans le menu "Affichage". */
  private JRadioButtonMenuItem rdbtnmntmUltraRes;

  /** Représente le groupe de boutons pour les éléments de menu de résolution. */
  private final ButtonGroup buttonGroupResolution = new ButtonGroup();

  /** Représente le groupe de boutons pour les éléments de menu de choix de thème. */
  private final ButtonGroup buttonGroupTheme = new ButtonGroup();

  /** Représente l'élément de menu radio "Français" dans le menu "Options". */
  private JRadioButtonMenuItem rdbtnmntmFrench;

  /** Représente l'élément de menu radio "Anglais" dans le menu "Options". */
  private JRadioButtonMenuItem rdbtnmntmEnglish;

  /** Représente le groupe de boutons pour les éléments de menu de langue. */
  private final ButtonGroup buttonGroupLanguage = new ButtonGroup();

  /** Représente l'élément de menu "Afficher boutons d'aide" dans le menu "Options". */
  private JCheckBoxMenuItem chckbxmntmShowHelpButtons;

  /** Représente l'élément de menu "Instructions" dans le menu "Aide". */
  private JMenuItem mntmInstructions;

  /** Représente l'élément de menu "À propos" dans le menu "Aide". */
  private JMenuItem mntmAbout;

  /** Représente l'élément de menu "Raccoucis clavier" dans le menu "Aide". */
  private JMenuItem mntmKeybinds;

  /** Le menu "À propos" de l'application */
  private AboutMenu aboutMenu;

  /** Le menu "Instructions" de l'application */
  private InstructionMenu instructionMenu;

  /** Le menu "Raccourcis clavier" de l'application */
  private KeybindFrame keybindFrame;

  /** Identifiant des abonnements */
  private int subscriptionID;

  /** Logger de la classe */
  private static final Logger LOGGER = LogManager.getLogger(MenuBar.class.getSimpleName());

  /** Constructeur vide pour assurer fonctionalité avec WindowBuilder */
  public MenuBar() {
    // Nécessaire afin que WindowBuilder puisse reconnaitre le composant comme
    // valide
  }

  /**
   * Créer la barre de menu
   *
   * @param controller le {@code Controller} de l'application
   */
  public MenuBar(Controller controller) {

    // ===== Controller ===== //

    this.controller = controller;
    this.subscriptionID = hashCode();
    setupSubscribers();

    // ===== Construction des fenêtres popup ===== //

    instructionMenu = new InstructionMenu(controller);
    keybindFrame = new KeybindFrame(controller);
    aboutMenu = new AboutMenu(controller);

    // ===== Composants ====== //

    mnFile = new JMenu(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.file.lbl"));
    mnFile.setMnemonic('F');
    this.add(mnFile);

    mntmImportScene =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.file.import_scene"));
    mntmImportScene.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_O, KeybindConstants.KEYBIND_MODIFIER));
    mnFile.add(mntmImportScene);
    mntmExportScene =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.file.export_scene"));
    mntmExportScene.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_S, KeybindConstants.KEYBIND_MODIFIER));
    mnFile.add(mntmExportScene);

    separatorProperties = new JSeparator();
    mnFile.add(separatorProperties);

    mnLoad = new JMenu(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.file.load"));
    mnFile.add(mnLoad);

    mnSave = new JMenu(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.file.save"));
    mnFile.add(mnSave);

    mnView = new JMenu(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.view.lbl"));
    mnView.setMnemonic('A');
    this.add(mnView);

    chckbxmntmConsole =
        new JCheckBoxMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.view.console"));
    chckbxmntmConsole.addActionListener(
        e -> {
          this.controller.getConsoleModel().setConsoleVisibility(chckbxmntmConsole.isSelected());
        });
    mnView.add(chckbxmntmConsole);

    chckbxmntmLegend =
        new JCheckBoxMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.view.legend"));
    chckbxmntmLegend.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_L, KeybindConstants.KEYBIND_MODIFIER));
    chckbxmntmLegend.setSelected(controller.getApplicationModel().getLegendVisibility());
    chckbxmntmLegend.addActionListener(
        e ->
            this.controller
                .getApplicationModel()
                .setLegendVisibility(chckbxmntmLegend.isSelected()));
    mnView.add(chckbxmntmLegend);

    chckbxmntmZenMode =
        new JCheckBoxMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.view.zen_mode"));
    chckbxmntmZenMode.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_F, KeybindConstants.KEYBIND_MODIFIER));
    if (RendererConstants.USE_OPENGL) {
      chckbxmntmZenMode.setEnabled(false);
    }
    chckbxmntmZenMode.addActionListener(
        e -> {
          this.controller
              .getApplicationModel()
              .setZenModeVisibility(chckbxmntmZenMode.isSelected());
        });
    mnView.add(chckbxmntmZenMode);

    mnOptions =
        new JMenu(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.options.lbl"));
    mnOptions.setMnemonic('O');
    add(mnOptions);
    if (RendererConstants.USE_OPENGL) {
      mnOptions.setEnabled(false);
    }

    JMenu mnThemes =
        new JMenu(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.options.themes.lbl"));
    mnOptions.add(mnThemes);

    addThemesOptions(mnThemes);

    JMenu mnSimRes =
        new JMenu(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.options.res.lbl"));
    mnOptions.add(mnSimRes);

    rdbtnmntmLowRes =
        new JRadioButtonMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.options.res.low"));
    buttonGroupResolution.add(rdbtnmntmLowRes);
    mnSimRes.add(rdbtnmntmLowRes);

    rdbtnmntmMediumRes =
        new JRadioButtonMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.options.res.medium"));
    buttonGroupResolution.add(rdbtnmntmMediumRes);
    mnSimRes.add(rdbtnmntmMediumRes);

    rdbtnmntmHighRes =
        new JRadioButtonMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.options.res.high"));
    buttonGroupResolution.add(rdbtnmntmHighRes);
    mnSimRes.add(rdbtnmntmHighRes);

    rdbtnmntmUltraRes =
        new JRadioButtonMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.options.res.ultra"));
    buttonGroupResolution.add(rdbtnmntmUltraRes);
    mnSimRes.add(rdbtnmntmUltraRes);

    JMenu mnLanguageSelect =
        new JMenu(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.options.res.language"));
    mnOptions.add(mnLanguageSelect);

    rdbtnmntmEnglish = new JRadioButtonMenuItem("English");

    rdbtnmntmFrench = new JRadioButtonMenuItem("Français");

    buttonGroupLanguage.add(rdbtnmntmFrench);
    buttonGroupLanguage.add(rdbtnmntmEnglish);

    mnLanguageSelect.add(rdbtnmntmEnglish);
    mnLanguageSelect.add(rdbtnmntmFrench);

    JMenuItem mntmOpenWolkeDir =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.menu_bar.options.open_wolke_dir"));

    mntmOpenWolkeDir.addActionListener(
        e -> {
          File configFile = new File(WFileSystemConstant.SAVE_PATH);
          try {
            Desktop.getDesktop().open(configFile);
          } catch (IOException e1) {
            LOGGER.error(
                LocaleManager.getLocaleResourceBundle()
                    .getString("ui.menu_bar.wolke_dir_not_found"),
                e1);
          }
        });

    mnOptions.add(mntmOpenWolkeDir);

    chckbxmntmShowHelpButtons =
        new JCheckBoxMenuItem(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.menu_bar.options.show_help_buttons"));
    chckbxmntmShowHelpButtons.addActionListener(
        e -> {
          controller
              .getApplicationModel()
              .setHelpButtonVisibility(chckbxmntmShowHelpButtons.isSelected());
        });
    mnOptions.add(chckbxmntmShowHelpButtons);

    mnHelp = new JMenu(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.help.lbl"));
    mnHelp.setMnemonic('A');
    add(mnHelp);

    mntmAbout =
        new JMenuItem(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.help.about"));
    mntmAbout.addActionListener(
        e -> {
          if (!aboutMenu.isVisible()) {
            aboutMenu.setVisible(true);
          }
        });
    mnHelp.add(mntmAbout);

    mntmKeybinds =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.help.keybinds"));
    mntmKeybinds.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_K, KeybindConstants.KEYBIND_MODIFIER));
    if (!RendererConstants.USE_OPENGL) {
      mntmKeybinds.addActionListener(
          e -> {
            if (!keybindFrame.isVisible()) {
              keybindFrame.setVisible(true);
            }
          });
    } else {
      mntmKeybinds.setEnabled(false);
    }
    mnHelp.add(mntmKeybinds);

    mntmInstructions =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.help.instructions"));
    mntmInstructions.setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, KeybindConstants.KEYBIND_MODIFIER));
    mntmInstructions.addActionListener(
        e -> {
          if (!instructionMenu.isVisible()) {
            instructionMenu.setVisible(true);
          }
        });
    mnHelp.add(mntmInstructions);

    // ========== SAVE FILES ========== //

    // TODO :Options de sauvegarde ne s'affichent pas ?

    mntmSaveApplicationModel =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.menu_bar.file.save.application_model"));
    mntmSaveSimulationModel =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.menu_bar.file.save.simulation_model"));
    mntmSaveRendererModel =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.menu_bar.file.save.renderer_model"));

    mnSave.add(mntmSaveApplicationModel);
    mnSave.add(mntmSaveSimulationModel);
    mnSave.add(mntmSaveRendererModel);

    // ========== LOAD FILES =========== //

    mntmLoadApplicationModel =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.menu_bar.file.load.application_model"));
    mntmLoadSimulationModel =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.menu_bar.file.load.simulation_model"));
    mntmLoadRendererModel =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle()
                .getString("ui.menu_bar.file.load.renderer_model"));

    mnLoad.add(mntmLoadApplicationModel);
    mnLoad.add(mntmLoadSimulationModel);
    mnLoad.add(mntmLoadRendererModel);

    // =========== IMPORT/EXPORT SCENE ========= //

    // ============ SETUP =========== //

    setupLocaleButtons();
    setupResolutionButtons();
    setupInitialValues();
    setupSaveSystemListeners();
  }

  /**
   * Methode permettant d'ajouter les options de thèmes dans le menu "Options" en ordre
   * alphabétique.
   *
   * @param mnThemes Le menu "Options" dans lequel les options de thèmes seront ajoutées
   */
  private void addThemesOptions(JMenu mnThemes) {
    TreeSet<Map.Entry<String, Class<?>>> sortedThemes = new TreeSet<>(new ThemeComparator());

    for (Map.Entry<Class<?>, String> theme :
        controller.getApplicationModel().getAvailableThemes().entrySet1()) {
      sortedThemes.add(Map.entry(theme.getValue(), theme.getKey()));
    }

    themeGroupMap = new HashMap<Class<?>, AbstractButton>();

    for (Map.Entry<String, Class<?>> laf : sortedThemes) {
      JRadioButtonMenuItem rdbtnmntmThemeItem = new JRadioButtonMenuItem(laf.getKey());
      themeGroupMap.put(laf.getValue(), rdbtnmntmThemeItem);
      mnThemes.add(rdbtnmntmThemeItem);
      rdbtnmntmThemeItem.addActionListener(this::onThemeSelection);
      buttonGroupTheme.add(rdbtnmntmThemeItem);
    }
  }

  /**
   * Classe permettant de comparer les thèmes pour les trier en ordre alphabétique.
   *
   * @see java.util.Comparator
   * @author Nilon123456789
   */
  class ThemeComparator implements Comparator<Entry<String, Class<?>>> {
    /**
     * Compare deux thèmes en ordre alphabétique.
     *
     * @param o1 Le premier thème {@code Entry<String, Class<?>>}
     * @param o2 Le deuxième thème {@code Entry<String, Class<?>>}
     * @return La valeur de comparaison
     */
    @Override
    public int compare(Entry<String, Class<?>> o1, Entry<String, Class<?>> o2) {
      return o1.getKey().compareTo(o2.getKey());
    }
  }

  /** Méthode qui initialise les abonnements aux événements du modèle de l'application. */
  @Override
  public void setupSubscribers() {
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_LEGEND_VISIBILITY_CHANGED,
            msg -> {
              chckbxmntmLegend.setSelected((Boolean) msg);
            },
            subscriptionID);
    controller
        .getConsoleModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_CONSOLE_VISIBILITY_CHANGED,
            msg -> {
              chckbxmntmConsole.setSelected((Boolean) msg);
            },
            subscriptionID);
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_ZEN_MODE_VISIBILITY_CHANGED,
            msg -> {
              chckbxmntmZenMode.setSelected((Boolean) msg);
            },
            subscriptionID);
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_HELP_BUTTON_VISIBILITY_CHANGED,
            msg -> {
              chckbxmntmShowHelpButtons.setSelected((Boolean) msg);
            },
            subscriptionID);
    this.controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.KEYBIND_SHOW_INSTRUCTIONS_PRESSED,
            msg -> {
              if (!this.instructionMenu.isVisible()) {
                this.instructionMenu.setVisible(true);
              }
            },
            subscriptionID);
    this.controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.KEYBIND_SHOW_KEYBIND_MENU_PRESSED,
            msg -> {
              if (!this.keybindFrame.isVisible()) {
                this.keybindFrame.setVisible(true);
              }
            },
            subscriptionID);
    this.controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.KEYBIND_IMPORT_SCENE_PRESSED,
            msg -> {
              onImportScene();
            },
            subscriptionID);
    this.controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.KEYBIND_EXPORT_SCENE_PRESSED,
            msg -> {
              onExportScene();
            },
            subscriptionID);
    this.controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_COMPONENT_HELP_BUTTON_PRESSED,
            msg -> {
              instructionMenu.setPageByKey((String) msg);
              if (!this.instructionMenu.isVisible()) {
                this.instructionMenu.setVisible(true);
              }
              instructionMenu.toFront();
            },
            subscriptionID);
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
    instructionMenu.reinitialize();
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    controller.getApplicationModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
    controller.getConsoleModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }

  /** Methode dans laquelle la valeur initiale ds composants est initialisee */
  private void setupInitialValues() {
    buttonGroupTheme.setSelected(
        themeGroupMap.get(controller.getApplicationModel().getCurrentTheme()).getModel(), true);

    selectLocaleButton(controller.getApplicationModel().getCurrentLocale());

    selectResolutionButton(controller.getApplicationModel().getCurrentResolution());

    chckbxmntmLegend.setSelected(controller.getApplicationModel().getLegendVisibility());

    chckbxmntmConsole.setSelected(controller.getConsoleModel().isConsoleVisible());
    if (controller.getConsoleModel().isConsoleVisible()) {
      controller.getConsoleModel().setConsoleVisibility(true);
    }

    chckbxmntmShowHelpButtons.setSelected(
        controller.getApplicationModel().getHelpButtonVisibility());
  }

  /**
   * Methode dans laquelle les actions des boutons de selection de {@code Locale} sont initialises.
   */
  private void setupLocaleButtons() {
    languageGroupMap = new HashMap<Locale, AbstractButton>();

    languageGroupMap.put(Locale.FRENCH, rdbtnmntmFrench);
    languageGroupMap.put(Locale.ENGLISH, rdbtnmntmEnglish);

    for (AbstractButton btn : languageGroupMap.values())
      btn.addActionListener(this::onLanguageLocaleSelection);
  }

  /**
   * Methode dans laquelle les actions des boutons de selection de {@code Resolution} sont
   * initialises.
   */
  private void setupResolutionButtons() {
    resolutionGroupMap = new HashMap<Resolution, AbstractButton>();

    resolutionGroupMap.put(Resolution.LOW, rdbtnmntmLowRes);
    resolutionGroupMap.put(Resolution.MEDIUM, rdbtnmntmMediumRes);
    resolutionGroupMap.put(Resolution.HIGH, rdbtnmntmHighRes);
    resolutionGroupMap.put(Resolution.ULTRA, rdbtnmntmUltraRes);

    for (AbstractButton btn : resolutionGroupMap.values())
      btn.addActionListener(this::onResolutionSelection);
  }

  // ==================
  // THEME METHODS
  // ==================

  /**
   * Methode a invoquer lors de la selection d'un bouton permettant de choisir le thème de
   * l'application.
   *
   * @param e Le {@code ActionEvent}
   */
  private void onThemeSelection(ActionEvent e) {
    JRadioButtonMenuItem item = (JRadioButtonMenuItem) e.getSource();

    Class<?> newTheme = ApplicationConstants.DEFAULT_THEME;
    for (Map.Entry<Class<?>, String> laf :
        controller.getApplicationModel().getAvailableThemes().entrySet1()) {
      if (laf.getValue().equals(item.getText())) {
        newTheme = laf.getKey();
      }
    }
    updateTheme(newTheme);
  }

  /**
   * Methode permettant d'actualiser le thème de l'application en avertissant le {@code Controller}.
   * Rien ne sera changé si la valeur de {@code Class<?>} est identique au thème actuel.
   *
   * @param newTheme Le nouveau thème à actualiser
   */
  private void updateTheme(Class<?> newTheme) {
    if (controller.getApplicationModel().getCurrentTheme() == newTheme) {
      return;
    }
    controller.getApplicationModel().setCurrentTheme(newTheme);
  }

  // ==================
  // RESOLUTION METHODS
  // ==================

  /**
   * Methode permettant de selectionner le bouton approprié selon le {@code Resolution} actuel.
   *
   * @param resolution La {@code Resolution} actuelle de l'application
   */
  private void selectResolutionButton(Resolution resolution) {
    resolutionGroupMap.get(resolution).setSelected(true);
    updateResolution(resolution);
  }

  /**
   * Methode a invoquer lors de la selection d'un bouton permettant de choisir la resolution de
   * l'application.
   *
   * @param e Le {@code ActionEvent}
   */
  private void onResolutionSelection(ActionEvent e) {
    for (Resolution res : Resolution.values()) {
      if (e.getSource().equals(resolutionGroupMap.get(res))) {
        updateResolution(res);
        return;
      }
    }
  }

  /**
   * Methode permettant d'actualiser la {@code Resolution} de l'application en avertissant le {@code
   * Controller}. Rien ne sera changé si la valeur de {@code Resolution} est identique a la {@code
   * Resolution} actuelle.
   *
   * @param res La {@code Resolution} a actualiser
   */
  private void updateResolution(Resolution res) {
    if (res.equals(controller.getApplicationModel().getCurrentResolution())) return;

    controller.getApplicationModel().saveResolutionOnRestart(res);
    JOptionPane.showMessageDialog(
        null,
        LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.resolution_change_warning"),
        LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.resolution_change_title"),
        JOptionPane.WARNING_MESSAGE);
  }

  // =======================
  // LANGUAGE LOCALE METHODS
  // =======================

  /**
   * Methode permettant de selectionner le bouton approprié selon le {@code Locale} actuel.
   *
   * @param locale le {@code Locale} actuel de l'application
   */
  private void selectLocaleButton(Locale locale) {
    buttonGroupLanguage.setSelected(languageGroupMap.get(locale).getModel(), true);

    updateLanguageLocale(locale);
  }

  /**
   * Methode permettant de modifier la localisation de l'application lors de la selection d'un
   * bouton de localisation.
   *
   * @param e {@code ActionEvent}
   */
  private void onLanguageLocaleSelection(ActionEvent e) {
    for (Locale locale : ApplicationConstants.VALID_LOCALES) {
      if (e.getSource().equals(languageGroupMap.get(locale))) {
        updateLanguageLocale(locale);
        return;
      }
    }
  }

  /**
   * Methode permettant de modifier la localisation de l'application et d'avertir le {@code
   * ApplicationModel} du changement. Rien ne sera changé si la valeur de {@code Locale} passee en
   * parametre est identique a la {@code Locale} actuelle.
   *
   * @param locale Le {@code Locale} de l'application
   */
  private void updateLanguageLocale(Locale locale) {
    if (locale.equals(controller.getApplicationModel().getCurrentLocale())) return;

    controller.getApplicationModel().setCurrentLocale(locale);
    controller.getApplicationModel().updateLanguageLocale();
  }

  // ======================
  // METHODES DE SAUVEGARDE
  // ======================

  /** Methode permettant d'initialiser les {@code ActionListener} du systeme de sauvegarde */
  private void setupSaveSystemListeners() {
    mntmSaveApplicationModel.addActionListener(
        e -> controller.getModelSaver().saveApplicationModel());

    mntmSaveSimulationModel.addActionListener(
        e -> controller.getModelSaver().saveSimulationModel());

    mntmSaveRendererModel.addActionListener(e -> controller.getModelSaver().saveRendererModel());

    mntmLoadApplicationModel.addActionListener(
        e -> controller.getModelLoader().loadApplicationModel());

    mntmLoadSimulationModel.addActionListener(
        e -> controller.getModelLoader().loadSimulationModel());

    mntmLoadRendererModel.addActionListener(e -> controller.getModelLoader().loadRendererModel());

    // ==== import/export =====//

    mntmExportScene.addActionListener(e -> onExportScene());
    mntmImportScene.addActionListener(e -> onImportScene());
  }

  /** Methode permettant d'importer la scene */
  private void onImportScene() {
    FileDialog dialog =
        new FileDialog(
            new JFrame(),
            LocaleManager.getLocaleResourceBundle().getString("ui.editor.scene_file_import.title"),
            FileDialog.LOAD);
    dialog.setDirectory(WFileSystemConstant.SAVE_PATH);
    dialog.setFile("*." + WSceneConstants.WOLKE_EXTENSION);
    dialog.setFilenameFilter(WSceneConstants.WOLKE_FILE_FILTER);
    dialog.setVisible(true);
    File sceneFile = null;
    boolean noSceneSelected = false;
    try {
      sceneFile = dialog.getFiles()[0];
    } catch (Exception e) {
      noSceneSelected = true;
    }
    if (noSceneSelected) {
      return;
    }
    this.controller.getSceneHandler().importScene(sceneFile);
  }

  /** Méthode permettant d'exporter une scène */
  private void onExportScene() {
    FileDialog dialog =
        new FileDialog(
            new JFrame(),
            LocaleManager.getLocaleResourceBundle().getString("ui.editor.scene_file_output.title"),
            FileDialog.SAVE);
    dialog.setDirectory(WFileSystemConstant.SAVE_PATH);
    dialog.setFile("scene." + WSceneConstants.WOLKE_EXTENSION);
    dialog.setFilenameFilter(WSceneConstants.WOLKE_FILE_FILTER);
    dialog.setVisible(true);
    File imageFile = null;
    boolean noFileSelected = false;
    try {
      imageFile = dialog.getFiles()[0];
    } catch (Exception e) {
      noFileSelected = true;
    }
    if (noFileSelected) return;
    if (imageFile == null) return;

    String name = imageFile.getName();
    if (FileUtils.getFileExtension(imageFile) != null
        && FileUtils.getFileExtension(imageFile).equalsIgnoreCase(WSceneConstants.WOLKE_EXTENSION))
      name =
          imageFile
              .getName()
              .substring(0, imageFile.getName().lastIndexOf('.')); // remove extension

    this.controller.getSceneHandler().exportScene(imageFile.getParentFile(), name);
  }
}
