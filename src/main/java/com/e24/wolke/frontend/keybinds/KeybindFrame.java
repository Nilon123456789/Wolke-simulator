package com.e24.wolke.frontend.keybinds;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.keybinds.KeybindConstants;
import com.e24.wolke.backend.keybinds.KeybindConstants.KeybindScope;
import com.e24.wolke.backend.keybinds.KeybindItem;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Panneau affichant les différents raccourcis clavier et leur fonction dans l'application, de
 * manière organisée par onglets selon leur portée dans l'application.
 *
 * @author adrienles
 */
public class KeybindFrame extends JFrame {

  /** Identifiant de sérialisation. */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application. */
  private Controller controller;

  /** Panneau d'onglets contenant les différentes portées possibles des raccourcis clavier. */
  private JTabbedPane tabbedPane;

  /**
   * Constructeur de la fenêtre des raccourcis clavier avec un {@code Controller} afin de pouvoir
   * aller chercher les raccourcis clavier.
   *
   * @param controller Le {@code Controller} de l'application.
   */
  public KeybindFrame(Controller controller) {
    super(LocaleManager.getLocaleResourceBundle().getString("ui.keybinds.title"));
    this.controller = controller;
    this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    this.setSize(640, 480);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.tabbedPane = new JTabbedPane();
    tabbedPane.setBorder(null);
    this.add(tabbedPane);
    this.setIconImages(ApplicationConstants.ICONS);

    // Ajout des onglets
    for (KeybindScope scope : KeybindScope.values()) {
      addTabWithKeybindListByScope(scope);
    }

    setupSubscribers();
  }

  /** Met en place les abonnements aux événements de l'application. */
  private void setupSubscribers() {
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribe(
            Subject.ON_APP_LANGUAGE_LOCALE_CHANGED,
            (locale) -> {
              setVisible(false);
            });
  }

  /**
   * Ajoute un onglet contenant un liste des raccourcis clavier avec leur fonction pour une portée
   * donnée.
   *
   * @param scope La portée des raccourcis clavier à afficher dans la liste.
   */
  private void addTabWithKeybindListByScope(KeybindScope scope) {
    ArrayList<String[]> keybindListData = new ArrayList<String[]>();
    for (KeybindItem item : KeybindConstants.KEYBIND_ITEMS) {
      if (item.getScope() == scope) {
        keybindListData.add(new String[] {item.toString(), item.getKeybindString()});
      }
    }
    String[] columnNames = {
      LocaleManager.getLocaleResourceBundle().getString("ui.keybinds.table.column.function"),
      LocaleManager.getLocaleResourceBundle().getString("ui.keybinds.table.column.keybind")
    };
    DefaultTableModel model =
        new DefaultTableModel(keybindListData.toArray(new String[0][0]), columnNames) {
          private static final long serialVersionUID = 1L;

          @Override
          public boolean isCellEditable(int row, int column) {
            return false;
          }
        };
    JTable table = new JTable(model);
    table.getTableHeader().setReorderingAllowed(false);
    table.getTableHeader().setResizingAllowed(false);
    table.getTableHeader().setEnabled(false);
    table.setFocusable(false);
    table.setRowSelectionAllowed(false);

    JScrollPane scrollPane = new JScrollPane(table);
    tabbedPane.addTab(scope.toString(), scrollPane);
    table.getColumnModel().getColumn(1).setMinWidth(200);
    table.getColumnModel().getColumn(1).setMaxWidth(200);
  }
}
