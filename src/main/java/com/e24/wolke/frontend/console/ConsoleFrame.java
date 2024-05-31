package com.e24.wolke.frontend.console;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.console.ConsoleConstants;
import com.e24.wolke.backend.models.console.ConsoleConstants.WLogLevel;
import com.e24.wolke.backend.models.console.ConsoleModel;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.utils.logger.WConsole;
import com.e24.wolke.utils.logger.WLogEvent;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import net.miginfocom.swing.MigLayout;
import org.apache.logging.log4j.Level;

/**
 * ConsolePane
 *
 * <p>Panel pour afficher les logs de l'application
 *
 * @author n-o-o-d-l-e
 * @author Nilon123456789
 */
public class ConsoleFrame extends JFrame implements Runnable {

  /** UUID pour la serialisation */
  private static final long serialVersionUID = -3969767063379855647L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Zone de texte pour les logs */
  private final JTextPane txtPaneLogs = new JTextPane();

  /** Bouton a deux états pour le défilement automatique */
  private final JToggleButton btnAutoScroll = new JToggleButton();

  /** Bouton pour effacer les logs */
  private JButton btnClearLogs;

  /** Menu pour le filtrage des classes */
  private JMenu mnClassFilter;

  /** Menu pour le choix du niveau de log */
  private JMenu mnLogLevels;

  /** Taille minimale de la fenêtre */
  private static final Dimension MINIMUM_SIZE = new Dimension(540, 400);

  /** Taille initiale de la fenêtre */
  private static final Dimension INITIAL_SIZE = new Dimension(800, 600);

  /** Petit espace entre les composants */
  private final int SMALL_GAP = 5;

  /** Taille d'un bouton */
  private final Dimension BUTTON_SIZE = new Dimension(30, 30);

  /** Si le thread d'actualisation tourne */
  private boolean isRunning;

  /** Index du nombre de logs recu non traité */
  private volatile int logIndex = 0;

  /** Si le défilement automatique est activé */
  private boolean autoScroll = true;

  /** ScrollPane pour les logs */
  private JScrollPane scrollPane;

  /** Liste des composants de filtrage de classe */
  private LinkedList<JCheckBoxMenuItem> classFilterItems = new LinkedList<>();

  /** Constructeur par défaut */
  public ConsoleFrame() {
    // Méthode par défaut
    start();
  }

  /**
   * Constructeur du panneau de la console
   *
   * @param controller {@code Controller} de l'application
   */
  public ConsoleFrame(Controller controller) {
    super(LocaleManager.getLocaleResourceBundle().getString("ui.console.title"));

    this.controller = controller;
    scrollPane = new JScrollPane();

    setIconImages(ApplicationConstants.ICONS);

    setMinimumSize(ConsoleFrame.MINIMUM_SIZE);
    setSize(ConsoleFrame.INITIAL_SIZE);
    setLocationRelativeTo(this.rootPane);
    setAlwaysOnTop(ConsoleConstants.DEFAULT_CONSOLE_FLOATING);

    initializeComponents();

    setupSubscribers();
    setupInitialValues();

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            controller.getConsoleModel().setConsoleVisibility(false);
            setVisible(controller.getConsoleModel().isConsoleVisible());
          }
        });

    start();
  }

  /** Initialisation des composants de l'interface */
  private void initializeComponents() {
    createTitleBar();
    createConsolePaneElements();
  }

  /** Création de la barre de titre */
  private void createTitleBar() {
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnFile =
        new JMenu(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.file.lbl"));
    menuBar.add(mnFile);

    JMenuItem mntmExport =
        new JMenuItem(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.file.export"));
    mntmExport.addActionListener(
        e -> {
          exportLogs();
        });
    mnFile.add(mntmExport);

    mnFile.add(new JSeparator());

    JMenuItem mntmClose =
        new JMenuItem(LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.file.close"));
    mntmClose.addActionListener(
        e -> {
          this.controller.getConsoleModel().setConsoleVisibility(false);
          setVisible(this.controller.getConsoleModel().isConsoleVisible());
        });
    mnFile.add(mntmClose);

    mnClassFilter =
        new JMenu(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.class_filter.lbl"));

    mnClassFilter.addMenuListener(
        new MenuListener() {
          @Override
          public void menuSelected(MenuEvent e) {
            onClassFilterMenuSelection();
          }

          @Override
          public void menuDeselected(MenuEvent e) {
            // Nothing
          }

          @Override
          public void menuCanceled(MenuEvent e) {
            // Nothing
          }
        });

    mnLogLevels =
        new JMenu(LocaleManager.getLocaleResourceBundle().getString("ui.console.log_levels.lbl"));

    ButtonGroup logLevelGroup = new ButtonGroup();
    for (WLogLevel level : WLogLevel.values()) {
      JRadioButtonMenuItem item = new JRadioButtonMenuItem(level.toString());
      item.addActionListener(
          e -> {
            this.controller.getConsoleModel().setCurrentLogLevel(level);
            refreshLogs();
          });
      logLevelGroup.add(item);
      mnLogLevels.add(item);
      if (level == this.controller.getConsoleModel().getCurrentLogLevel()) {
        item.setSelected(true);
      }
    }

    menuBar.add(mnLogLevels);

    JMenuItem mntmSelectAll =
        new JMenuItem(
            LocaleManager.getLocaleResourceBundle().getString("ui.menu_bar.class_filter.all"));
    mntmSelectAll.addActionListener(
        e -> {
          for (JCheckBoxMenuItem item : classFilterItems) {
            item.setSelected(true);
            if (!this.controller
                .getConsoleModel()
                .getCurrentLogClassNames()
                .contains(item.getText())) {
              this.controller.getConsoleModel().getCurrentLogClassNames().add(item.getText());
            }
          }
          refreshLogs();
        });
    mnClassFilter.add(mntmSelectAll);

    mnClassFilter.add(new JSeparator());

    menuBar.add(mnClassFilter);
  }

  /** Création des éléments du panneau de la console */
  private void createConsolePaneElements() {
    getContentPane().setLayout(new MigLayout("gap 5, ins 5", "[grow,grow]", "[grow][]"));

    scrollPane.setFocusable(false);
    getContentPane().add(scrollPane, "grow");

    this.txtPaneLogs.setEditable(false);
    this.txtPaneLogs.setContentType("text/html");
    scrollPane.setViewportView(this.txtPaneLogs);

    JPanel panelClear = new JPanel();
    panelClear.setBorder(null);
    getContentPane().add(panelClear, "newline,grow");
    panelClear.setLayout(new BoxLayout(panelClear, BoxLayout.X_AXIS));

    setupButton(btnAutoScroll, "icons/autoscroll.svg");
    btnAutoScroll.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.console.auto_scroll"));
    btnAutoScroll.setSelected(this.autoScroll);
    btnAutoScroll.addActionListener(
        e -> {
          setAutoScroll(btnAutoScroll.isSelected());
        });
    panelClear.add(this.btnAutoScroll);

    panelClear.add(Box.createHorizontalStrut(SMALL_GAP));
    JButton btnToBottom = new JButton();
    setupButton(btnToBottom, "icons/clamp_down.svg");
    btnToBottom.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.console.to_bottom"));
    btnToBottom.addActionListener(
        e -> {
          scrollPane
              .getVerticalScrollBar()
              .setValue(scrollPane.getVerticalScrollBar().getMaximum());
        });
    panelClear.add(btnToBottom);

    panelClear.add(Box.createHorizontalGlue());

    btnClearLogs = new JButton();
    setupButton(btnClearLogs, "icons/delete.svg");
    btnClearLogs.addActionListener(
        e -> {
          if (JOptionPane.showConfirmDialog(
                  this,
                  LocaleManager.getLocaleResourceBundle()
                      .getString("ui.console.clear_logs_confirm"),
                  LocaleManager.getLocaleResourceBundle()
                      .getString("ui.console.clear_logs_confirm_title"),
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE)
              == JOptionPane.NO_OPTION) return;
          ConsoleModel.clearLogs();
          refreshLogs();
        });
    panelClear.add(btnClearLogs);
  }

  /**
   * Définit la taille et l'icon d'un bouton
   *
   * @param button {@code AbstractButton} du bouton dont la taille et l'icon doivent être définies
   * @param iconPath {@code String} du chemin de l'icon
   */
  private void setupButton(AbstractButton button, String iconPath) {
    button.setMinimumSize(BUTTON_SIZE);
    button.setPreferredSize(BUTTON_SIZE);
    button.setMaximumSize(BUTTON_SIZE);

    button.setIcon(
        new FlatSVGIcon(iconPath)
            .setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground"))));
  }

  /** Methode dans laquelle les valeurs intiiales des composants sont initialisees */
  private void setupInitialValues() {
    refreshLogs();
  }

  /** Gestion du clic sur le menu de filtrage de classe */
  private void onClassFilterMenuSelection() {
    Component[] components = mnClassFilter.getMenuComponents();

    LinkedList<String> classNames = new LinkedList<>();
    LinkedList<String> consoleClassNames = WConsole.getClassNames();
    boolean allSelected = true;

    for (Component component : components) {
      if (!(component instanceof JCheckBoxMenuItem)) continue;
      JCheckBoxMenuItem item = (JCheckBoxMenuItem) component;

      if (!consoleClassNames.contains(item.getText())) {
        mnClassFilter.remove(item);
        classFilterItems.remove(item);
        this.controller.getConsoleModel().getCurrentLogClassNames().remove(item.getText());
        continue;
      }

      classNames.add(item.getText());
      if (item.isSelected()) continue;
      allSelected = false;
    }

    for (String className : consoleClassNames) {
      if (classNames.contains(className)) continue;
      JCheckBoxMenuItem item = new JCheckBoxMenuItem(className);
      classFilterItems.add(item);
      item.setSelected(allSelected);
      if (allSelected) {
        this.controller.getConsoleModel().getCurrentLogClassNames().add(className);
      }

      item.addActionListener(
          e -> {
            if (item.isSelected()) {
              if (!this.controller.getConsoleModel().getCurrentLogClassNames().contains(className))
                this.controller.getConsoleModel().getCurrentLogClassNames().add(className);
            } else this.controller.getConsoleModel().getCurrentLogClassNames().remove(className);
            refreshLogs();
          });
      mnClassFilter.add(item);
    }
    refreshLogs();
  }

  /**
   * Méthode pour mettre à jour les logs
   *
   * @param event {@code WLogEvent}
   */
  private synchronized void newLogReceived(WLogEvent event) {
    Level level = event.getLevel();
    if (controller.getConsoleModel().getCurrentLogLevel().getLevel().intLevel()
        < level.intLevel()) {
      return;
    }

    this.notifyAll();
    logIndex++;
  }

  /** Initialisation des abonnements */
  private void setupSubscribers() {
    this.controller
        .getApplicationModel()
        .getSubscriber()
        .subscribe(
            Subject.ON_APP_LANGUAGE_LOCALE_CHANGED,
            (locale) -> {
              getContentPane().removeAll();
              initializeComponents();
              revalidate();
            });
    this.controller
        .getConsoleModel()
        .getSubscriber()
        .subscribe(Subject.ON_CONSOLE_LOGS_CHANGED, event -> newLogReceived((WLogEvent) event));
  }

  /**
   * Active ou désactive le défilement automatique
   *
   * @param autoScroll {@code boolean}
   */
  public void setAutoScroll(boolean autoScroll) {
    this.autoScroll = autoScroll;
  }

  /** Rafraîchit les logs */
  public void refreshLogs() {
    txtPaneLogs.setText(controller.getConsoleModel().getCurrentLogs());
    if (this.autoScroll) {
      txtPaneLogs.setCaretPosition(txtPaneLogs.getDocument().getLength());
      scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
    }

    updateClassSelectors();
  }

  /** Met a jour la listes des classes dans le menu de filtrage */
  private void updateClassSelectors() {
    boolean allClassFound = true;
    for (String className : WConsole.getClassNames()) {
      boolean contains =
          this.controller.getConsoleModel().getCurrentLogClassNames().contains(className);
      boolean found = false;
      for (JCheckBoxMenuItem item : classFilterItems) {
        if (item.getText().equals(className)) {
          item.setSelected(contains);
          found = true;
        }
      }
      allClassFound &= found;
    }

    if (allClassFound) return;

    onClassFilterMenuSelection(); // Recharge les classes
  }

  /** Exporte les logs */
  private void exportLogs() {
    FileDialog dialog =
        new FileDialog(
            new JFrame(),
            LocaleManager.getLocaleResourceBundle().getString("ui.console.export.title"),
            FileDialog.SAVE);

    dialog.setFile("wolke-logs.html");
    dialog.setVisible(true);

    ConsoleModel.exportLogs(dialog.getDirectory(), dialog.getFile());

    dialog.dispose();
  }

  /** Démarre le thread de mise à jour des logs */
  public void start() {
    if (isRunning) return;

    isRunning = true;

    Thread thread = new Thread(this);
    thread.setName(getClass().getSimpleName());
    thread.start();
  }

  /** Arrête le thread de mise à jour des logs */
  public void stop() {
    isRunning = false;
    synchronized (this) {
      notifyAll();
    }
  }

  /** Boucle d'attente pour la mise à jour des logs */
  @Override
  public void run() {
    while (isRunning) {

      refreshLogs();

      logIndex--;
      if (logIndex > 0) continue;

      synchronized (this) {
        try {
          wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
