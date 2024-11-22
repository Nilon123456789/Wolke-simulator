package com.e24.wolke.frontend.help;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;

/**
 * La classe {@code InstructionMenu} sert à afficher le menu "Instructions" de l'application.
 *
 * @author adrienles
 */
public class InstructionMenu extends JFrame implements WEventComponent, Reinitializable {

  /** Numéro de série de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** La taille initiale de la fenêtre */
  private static final Dimension INITIAL_SIZE = new Dimension(800, 600);

  /** La taille minimale de la fenêtre */
  private static final Dimension MINIMUM_SIZE = new Dimension(480, 320);

  /** Le panneau d'onglets pour les pages d'instructions */
  private JTabbedPane tabbedPane;

  /** L'écart entre les composants de la fenêtre */
  private final int GAP = 5;

  /** Les items du carrousel des pages de jeu d'essai */
  private CarouselItem[] trialGameCarouselItems = {
    new CarouselItem("choose-preset"),
    new CarouselItem("start-simulation"),
    new CarouselItem("visualisation-options"),
    new CarouselItem("draw-on-canvas"),
    new CarouselItem("inspect-simulation"),
    new CarouselItem("play-zen-mode"),
    new CarouselItem("help-buttons"),
    new CarouselItem("instructions-functionalities")
  };

  /** Les items du carrousel des pages de fonctionnalités globales */
  private CarouselItem[] globalFeaturesCarouselItems = {
    new CarouselItem("resizable"),
    new CarouselItem("collapsible_components"),
    new CarouselItem("localization"),
    new CarouselItem("theme"),
    new CarouselItem("logs"),
    new CarouselItem("zen_mode"),
    new CarouselItem("experimental_mode")
  };

  /** Les items du carrousel des pages de fonctionnalités de la simulation */
  private CarouselItem[] simulationFeaturesCarouselItems = {
    new CarouselItem("simulation_controls"),
    new CarouselItem("properties_pane"),
    new CarouselItem("simulation_settings"),
    new CarouselItem("preset_pane"),
    new CarouselItem("viscosity_field"),
    new CarouselItem("density_field"),
    new CarouselItem("vortex_field"),
    new CarouselItem("physicalSize_field"),
    new CarouselItem("time_step"),
    new CarouselItem("cell_inspector"),
    new CarouselItem("reset_all")
  };

  /** Les items du carrousel des pages de fonctionnalités de l'éditeur */
  private CarouselItem[] editorFeaturesCarouselItems = {
    new CarouselItem("editor_toolpane"),
    new CarouselItem("tool_image"),
    new CarouselItem("tool_pen"),
    new CarouselItem("tool_erase"),
    new CarouselItem("tool_shape"),
    new CarouselItem("tool_color"),
    new CarouselItem("tool_size"),
    new CarouselItem("layer_manager"),
    new CarouselItem("layer_button"),
  };

  /** L'identifiant de l'abonnement */
  private int subscriptionID;

  /**
   * L'énumération pour la direction de changement de page
   *
   * @author adrienles
   */
  private enum PAGE_DIRECTION {
    /** La page suivante */
    NEXT,
    /** La page précédente */
    BACK
  }

  /** Constructeur de base de la classe {@code InstructionMenu}. */
  public InstructionMenu() {}

  /**
   * Constructeur de la classe {@code InstructionMenu}
   *
   * @param controller Le {@code Controller} de l'application
   */
  public InstructionMenu(Controller controller) {
    // TODO : Localiser les descriptions des instructions
    this.controller = controller;
    this.subscriptionID = hashCode();
    setTitle(LocaleManager.getLocaleResourceBundle().getString("ui.help.instructions.title"));
    setMinimumSize(InstructionMenu.MINIMUM_SIZE);
    setSize(InstructionMenu.INITIAL_SIZE);
    setLocationRelativeTo(this.rootPane);
    setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    setAlwaysOnTop(true);
    setIconImages(ApplicationConstants.ICONS);

    tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    getContentPane().add(tabbedPane, BorderLayout.CENTER);
    tabbedPane.addTab(
        LocaleManager.getLocaleResourceBundle().getString("ui.help.instructions.overview"),
        null,
        new InstructionSection(controller, trialGameCarouselItems),
        null);

    JTabbedPane featuresTabbedPane = new JTabbedPane(JTabbedPane.TOP);
    featuresTabbedPane.addTab(
        LocaleManager.getLocaleResourceBundle().getString("ui.help.instructions.global"),
        null,
        new InstructionSection(controller, globalFeaturesCarouselItems),
        null);
    featuresTabbedPane.addTab(
        LocaleManager.getLocaleResourceBundle().getString("ui.help.instructions.simulation"),
        null,
        new InstructionSection(controller, simulationFeaturesCarouselItems),
        null);
    featuresTabbedPane.addTab(
        LocaleManager.getLocaleResourceBundle().getString("ui.help.instructions.editor"),
        null,
        new InstructionSection(controller, editorFeaturesCarouselItems),
        null);
    tabbedPane.addTab(
        LocaleManager.getLocaleResourceBundle().getString("ui.help.instructions.functionalities"),
        null,
        featuresTabbedPane,
        null);

    setupSubscribers();
  }

  /**
   * Constructeur de la classe {@code InstructionMenu} qui représente une section du mmenu
   * instructions
   *
   * @author adrienles
   */
  public class InstructionSection extends JPanel {

    /** UUID pour la serialisation */
    private static final long serialVersionUID = -3969767063379855647L;

    /** L'étiquette indiquant la page actuelle */
    private JLabel lblCurrentPage;

    /** L'étiquette affichant la description de la page actuelle */
    private JTextArea textPaneDescription;

    /** Le composant d'image pour afficher les images des pages */
    private ImageComponent imageComponent;

    /** Les items du carrousel de la section */
    private CarouselItem[] items;

    /** La taille des boutons de navigation */
    private final Dimension BUTTON_SIZE = new Dimension(24, 24);

    /** La page actuelle du carrousel */
    private int currentPage = 0;

    /**
     * Constructeur de la classe {@code InstructionSection}
     *
     * @param controller Le {@code Controller} de l'application
     * @param items Les items du carrousel de la section
     */
    public InstructionSection(Controller controller, CarouselItem[] items) {
      this.items = items;
      this.setLayout(new MigLayout("gap " + GAP + ", ins " + GAP, "[grow]", "[grow][]"));

      JPanel panelContent = new JPanel();
      this.add(panelContent, ",grow");
      panelContent.setLayout(new MigLayout("gap 0, ins 0", "[grow]", "[grow][]"));
      panelContent.setBorder(null);

      JPanel panelImage = new JPanel();
      panelImage.setLayout(new BorderLayout(0, 0));
      this.imageComponent = new ImageComponent(this.items[this.currentPage].getImageURL());
      panelImage.add(this.imageComponent);
      panelContent.add(panelImage, "grow");

      JPanel panelDescription = new JPanel();
      panelDescription.setBorder(null);
      panelDescription.setLayout(new BorderLayout(0, 0));
      panelContent.add(panelDescription, "newline,grow");

      JScrollPane scrollPaneDescription = new JScrollPane();
      scrollPaneDescription.setBorder(null);
      scrollPaneDescription.setFocusable(false);
      panelDescription.add(scrollPaneDescription, BorderLayout.CENTER);
      this.textPaneDescription = new JTextArea(this.items[this.currentPage].getDescription());
      scrollPaneDescription.setViewportView(this.textPaneDescription);
      this.textPaneDescription.setWrapStyleWord(true);
      this.textPaneDescription.setLineWrap(true);
      this.textPaneDescription.setEditable(false);
      this.textPaneDescription.setFocusable(false);
      this.textPaneDescription.setMargin(new Insets(GAP * 2, GAP * 10, GAP * 2, GAP * 10));

      JPanel panelNav = new JPanel();
      panelNav.setBorder(null);
      this.add(panelNav, "newline,grow");
      panelNav.setLayout(new BoxLayout(panelNav, BoxLayout.X_AXIS));

      this.lblCurrentPage =
          new JLabel(
              LocaleManager.getLocaleResourceBundle().getString("ui.help.instructions.page.lbl")
                  + " "
                  + (this.currentPage + 1)
                  + " / "
                  + this.items.length);
      panelNav.add(this.lblCurrentPage);

      panelNav.add(Box.createHorizontalGlue());

      JButton btnBack =
          new JButton(
              new FlatSVGIcon("icons/back.svg")
                  .setColorFilter(
                      new ColorFilter(color -> UIManager.getColor("Button.foreground"))));

      btnBack.setMinimumSize(BUTTON_SIZE);
      btnBack.setMaximumSize(BUTTON_SIZE);
      btnBack.setPreferredSize(BUTTON_SIZE);
      btnBack.setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.backward"));

      btnBack.addActionListener(
          e -> {
            handlePageChange(PAGE_DIRECTION.BACK);
          });
      panelNav.add(btnBack);

      panelNav.add(Box.createHorizontalStrut(GAP));

      JButton btnNext =
          new JButton(
              new FlatSVGIcon("icons/next.svg")
                  .setColorFilter(
                      new ColorFilter(color -> UIManager.getColor("Button.foreground"))));

      btnNext.setMinimumSize(BUTTON_SIZE);
      btnNext.setMaximumSize(BUTTON_SIZE);
      btnNext.setPreferredSize(BUTTON_SIZE);
      btnNext.setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.foward"));

      btnNext.addActionListener(
          e -> {
            handlePageChange(PAGE_DIRECTION.NEXT);
          });
      panelNav.add(btnNext);
    }

    /**
     * Méthode pour gérer le changement de page du carrousel. Elle tourne en rond si l'utilisateur
     * précède la première page ou suit la dernière page.
     *
     * @param direction La direction du changement de page selon l'énumération {@code
     *     PAGE_DIRECTION}
     */
    private void handlePageChange(PAGE_DIRECTION direction) {
      switch (direction) {
        case NEXT:
          if (this.currentPage < this.items.length - 1) {
            this.currentPage++;
          } else {
            this.currentPage = 0;
          }
          break;
        case BACK:
          if (currentPage > 0) {
            this.currentPage--;
          } else {
            this.currentPage = this.items.length - 1;
          }
          break;
      }
      changePage();
    }

    /**
     * Méthode pour changer la page du carrousel
     *
     * @param page L'index de la page à afficher
     */
    public void changePage(int page) {
      currentPage = page;
      lblCurrentPage.setText("Page " + (currentPage + 1) + " / " + items.length);
      textPaneDescription.setText(items[currentPage].getDescription());
      imageComponent.setImageURL(items[currentPage].getImageURL());
    }

    /**
     * Méthode pour changer la page du carrousel à partir de l'index actuel dans {@code currentPage}
     */
    private void changePage() {
      changePage(currentPage);
    }

    /**
     * Retourne les items du carrousel
     *
     * @return Les items du carrousel
     */
    private CarouselItem[] getItems() {
      return items;
    }
  }

  /**
   * Méthode pour changer la page du carrousel en fonction de la clé de la page
   *
   * @param key La clé de la page
   * @return {@code true} si la page a été changée, {@code false} sinon, dans le cas où la clé
   *     n'existe pas.
   */
  public boolean setPageByKey(String key) {
    for (Component section : tabbedPane.getComponents()) {
      if (section instanceof JTabbedPane) {
        JTabbedPane innerTabbedPane = (JTabbedPane) section;
        for (Component tab : innerTabbedPane.getComponents()) {
          if (tab instanceof InstructionSection) {
            InstructionSection sectionComponent = (InstructionSection) tab;
            for (int i = 0; i < sectionComponent.getItems().length; i++) {
              if (sectionComponent.getItems()[i].getKey().equals(key)) {
                sectionComponent.changePage(i);
                innerTabbedPane.setSelectedComponent(sectionComponent);
                tabbedPane.setSelectedComponent(innerTabbedPane);
                return true;
              }
            }
          }
        }
      } else if (section instanceof InstructionSection) {
        InstructionSection sectionComponent = (InstructionSection) section;
        for (int i = 0; i < sectionComponent.getItems().length; i++) {
          if (sectionComponent.getItems()[i].getKey().equals(key)) {
            sectionComponent.changePage(i);
            tabbedPane.setSelectedComponent(sectionComponent);
            return true;
          }
        }
      }
    }
    return false;
  }

  /** Met en place les abonnements aux événements de l'application. */
  @Override
  public void setupSubscribers() {
    this.controller
        .getApplicationModel()
        .getSubscriber()
        .subscribe(
            Subject.ON_APP_LANGUAGE_LOCALE_CHANGED,
            (locale) -> {
              setVisible(false);
            });
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    this.controller.getApplicationModel().getSubscriber().unsubscribeAllWithID(this.subscriptionID);
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
    CarouselItem.clearItems();
    dispose();
  }

  /**
   * Affiche ou cache le menu "À propos" de l'application en s'assurant qu'il soit centré par
   * rapport à l'écran de l'utilisateur.
   *
   * @param b {@code true} pour afficher le menu, {@code false} pour le cacher
   */
  @Override
  public void setVisible(boolean b) {
    super.setVisible(b);
    setLocationRelativeTo(this.rootPane);
  }

  /**
   * La classe interne pour les items du carrousel des pages d'instructions
   *
   * @author adrienles
   */
  private class CarouselItem {
    /** La description de la page */
    private String description;

    /** L'URL de l'image de la page */
    private URL imageURL;

    /** Les clés des pages */
    private static HashSet<String> keys = new HashSet<>();

    /** La clé de la page. Elle devrait être représentative de son contenu, et doit être unique. */
    private String key;

    /**
     * Constructeur de la classe {@code CarouselItem}
     *
     * @param key La clé de la page. Elle devrait être représentative de son contenu, et doit être
     *     unique. Elle est aussi utilisée afin d'aller chercher l'image et la description de la
     *     page dans les ressources de l'application.
     */
    public CarouselItem(String key) {
      if (keys.contains(key)) {
        throw new IllegalArgumentException("La clé de la page doit être unique.");
      } else {
        try (InputStream reader =
            Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(
                    "instructions/descriptions/"
                        + key
                        + "-"
                        + LocaleManager.getLocaleResourceBundle().getLocale().getLanguage()
                        + ".txt")) {

          if (reader != null) {
            byte[] buffer = new byte[reader.available()];
            reader.read(buffer);
            this.description = new String(buffer, Charset.forName("UTF-8"));
          } else {
            this.description = "";
          }

        } catch (IOException e) {
          e.printStackTrace();
        }
        try {
          this.imageURL =
              Thread.currentThread()
                  .getContextClassLoader()
                  .getResource("instructions/images/" + key + ".gif");
          if (this.imageURL == null) {
            this.imageURL =
                Thread.currentThread()
                    .getContextClassLoader()
                    .getResource("instructions/images/" + key + ".png");
          }
        } catch (Exception e) {
          this.imageURL = null;
          throw new IllegalArgumentException("L'image de la page n'a pas été trouvée.");
        }
        keys.add(key);
        this.key = key;
      }
    }

    /** Méthode pour vider les clés des pages */
    public static void clearItems() {
      keys.clear();
    }

    /**
     * Retourne la clé de la page
     *
     * @return La clé de la page
     */
    public String getKey() {
      return key;
    }

    /**
     * Retourne la description de la page
     *
     * @return La description de la page
     */
    public String getDescription() {
      return this.description;
    }

    /**
     * Retourne l'URL de l'image de la page
     *
     * @return L'URL de l'image de la page
     */
    public URL getImageURL() {
      try {
        return this.imageURL;
      } catch (Exception e) {
        return null;
      }
    }
  }
}
