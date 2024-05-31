package com.e24.wolke.frontend.input;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.editor.layers.LayerModel;
import com.e24.wolke.backend.models.editor.layers.WLayer;
import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;

/**
 * Panneau utilisé pour gérer les calques de l'éditeur
 *
 * @author n-o-o-d-l-e
 * @author MeriBouisri
 */
public class EditorLayerInputPane extends AbstractInputPane
    implements WEventComponent, Reinitializable {
  /** Numéro de sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Le panneau de défilement contenant les calques de l'éditeur */
  private JScrollPane scrollPane;

  /** Le panneau contenant les calques de l'éditeur */
  private JPanel layerPanel;

  /** Le panneau contenant les boutons du calques de l'éditeur */
  private JPanel buttonPanel;

  /** Drapeau indiquant si l'utilisateur est en train de renommer un calque */
  private boolean currentlyRenaming = false;

  /** Identifiant des abonnements */
  private int subscriptionID;

  /**
   * {@code ArrayList} contenant tous les {@code EditorLayerComponent} pour représenter tous les
   * calques
   */
  private ArrayList<EditorLayerComponent> layerObjects = new ArrayList<>();

  /**
   * Contructeur du panneau de calques de l'éditeur prennant le {@code Controller} de l'application
   *
   * @param controller Le {@code Controller} de l'application
   */
  public EditorLayerInputPane(Controller controller) {
    super(controller, "ui.editor.layers", "layer_manager");
    this.controller = controller;
    subscriptionID = hashCode();

    PANEL_LAYOUT.setRowConstraints("[][grow][]");

    addHideableElements();

    setupSubscribers();
  }

  /** Mettre en place les abonnements aux événements de l'application */
  @Override
  public void setupSubscribers() {
    LayerModel layerModel = controller.getEditorModel().getLayerModel();

    layerModel
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_LAYER_ADD_PRESSED,
            (e) -> {
              controller.getEditorModel().getLayerModel().addLayerAboveSelection();
              updateLayers(true);
            },
            subscriptionID);
    layerModel
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_LAYER_DELETE_PRESSED,
            (e) -> {
              controller.getEditorModel().getLayerModel().removeSelectedLayer();
              controller.getEditorModel().stopPainting();
              updateLayers(true);
              controller
                  .getEditorModel()
                  .getPublisher()
                  .publish(Subject.ON_EDITOR_PAINTING_DONE, null);
            },
            subscriptionID);
    layerModel
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_LAYER_CLEAR_PRESSED,
            (e) -> {
              controller.getEditorModel().getLayerModel().removeAllLayers();
              updateLayers(true);
              controller
                  .getEditorModel()
                  .getPublisher()
                  .publish(Subject.ON_EDITOR_PAINTING_DONE, null);
            },
            subscriptionID);

    layerModel
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_LAYER_UP_PRESSED,
            (e) -> {
              this.controller.getEditorModel().getLayerModel().moveSelectedLayerUp();
              updateLayers(true);
              controller
                  .getEditorModel()
                  .getPublisher()
                  .publish(Subject.ON_EDITOR_PAINTING_DONE, null);
            },
            subscriptionID);
    layerModel
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_LAYER_DOWN_PRESSED,
            (e) -> {
              this.controller.getEditorModel().getLayerModel().moveSelectedLayerDown();
              updateLayers(true);
              controller
                  .getEditorModel()
                  .getPublisher()
                  .publish(Subject.ON_EDITOR_PAINTING_DONE, null);
            },
            subscriptionID);
    layerModel
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_LAYER_DUPLICATE_PRESSED,
            (e) -> {
              this.controller.getEditorModel().getLayerModel().duplicateSelectedLayer();
              updateLayers(true);
            },
            subscriptionID);
    layerModel
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_LAYER_PREVIEW_REFRESH, (e) -> updateLayers(false), subscriptionID);
    layerModel
        .getSubscriber()
        .subscribeWithID(Subject.EDITOR_LAYER_CHANGED, (e) -> updateFocus(), subscriptionID);

    layerModel
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_LAYER_UPDATE_IMAGE, (e) -> updateLayers(true), subscriptionID);
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    controller
        .getEditorModel()
        .getLayerModel()
        .getSubscriber()
        .unsubscribeAllWithID(subscriptionID);
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
  }

  /** Ajoute les éléments cachables du panneau */
  private void addHideableElements() {
    scrollPane = new JScrollPane();
    scrollPane.setBorder(null);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    add(scrollPane, "newline,grow");
    scrollPane.setViewportView(addLayerPanel());

    add(addFooterToolBarPanel(), "newline,grow");
  }

  /** Mettre à jour le focus des calques */
  private void updateFocus() {
    for (EditorLayerComponent layer : layerObjects) {
      if (controller.getEditorModel().getLayerModel().getSelectedLayer().getID()
          == layer.getLayer().getID()) {
        layer.setBackgroundFocused();
      } else {
        layer.setBackgroundUnfocused();
      }
    }
  }

  /**
   * Crée le panneau contenant les calques actuels dans l'éditeur
   *
   * @return Le panneau contenant les calques de l'éditeur
   */
  private JPanel addLayerPanel() {
    layerPanel = new JPanel();
    layerPanel.putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");

    layerPanel.setLayout(new MigLayout("gap 5, ins 5", "[grow]", "[]"));

    updateLayers(true);

    return layerPanel;
  }

  /**
   * Méthode actualisant le {@code ArrayList} des composants affichant les calques
   *
   * @param newLayerAdded Indique si on vient mettre à jour l'affichage des calques après
   *     l'ajout/retrait/déplacement d'un calque
   */
  private void updateLayers(boolean newLayerAdded) {
    if (newLayerAdded) {
      layerPanel.removeAll();
      layerObjects.clear();
    }

    List<WLayer> layers = controller.getEditorModel().getLayerModel().filterWritableLayers();
    for (int j = 0; j < layers.size(); j++) {
      WLayer layer = layers.get(j);

      if (newLayerAdded) {
        EditorLayerComponent layerComponent = new EditorLayerComponent(controller, layer);
        if (controller.getEditorModel().getLayerModel().getSelectedLayer().getID()
            == layer.getID()) {
          layerComponent.setBackgroundFocused();
        }
        layerObjects.add(layerComponent);
      } else {
        layerObjects.get(j).updateImage();
      }
    }
    if (newLayerAdded) {
      for (int i = layerObjects.size() - 1; i >= 0; i--) {
        layerPanel.add(layerObjects.get(i), "grow,newline");
      }
    }
    layerPanel.updateUI();
    repaint();
    // TODO : Trouver une façon non "hacky" de faire ça lol
    ToolType backupTool = controller.getEditorModel().getToolbox().getCurrentToolType();
    controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.ERASER);
    controller.getEditorModel().startPainting(-20, -20);
    controller.getEditorModel().stopPainting();
    controller.getEditorModel().getToolbox().setCurrentToolType(backupTool);
    controller.getEditorModel().getPublisher().publish(Subject.ON_EDITOR_PAINTING_DONE, null);
  }

  /**
   * Crée le panneau en bas du menu avec les boutons pour intéragir avec les calques
   *
   * @return Panneau contenant les boutons qui permettent l'utilisation de panneau de calques
   */
  private JPanel addFooterToolBarPanel() {
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

    buttonPanel.add(
        new EditorLayerButtonComponent(
            controller,
            Subject.EDITOR_LAYER_DOWN_PRESSED,
            "ui.tooltips.lower_layer",
            "icons/down_arrow.svg"));
    buttonPanel.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));
    buttonPanel.add(
        new EditorLayerButtonComponent(
            controller,
            Subject.EDITOR_LAYER_UP_PRESSED,
            "ui.tooltips.raise_layer",
            "icons/up_arrow.svg"));

    buttonPanel.add(Box.createHorizontalGlue());

    buttonPanel.add(
        new EditorLayerButtonComponent(
            controller,
            Subject.EDITOR_LAYER_ADD_PRESSED,
            "ui.tooltips.add_layer",
            "icons/add_layer.svg"));
    buttonPanel.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));
    buttonPanel.add(
        new EditorLayerButtonComponent(
            controller,
            Subject.EDITOR_LAYER_DUPLICATE_PRESSED,
            "ui.tooltips.duplicate_layer",
            "icons/duplicate.svg"));
    buttonPanel.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));
    buttonPanel.add(
        new EditorLayerButtonComponent(
            controller,
            Subject.EDITOR_LAYER_RENAMED,
            "ui.tooltips.rename_layer",
            "icons/rename.svg"));
    buttonPanel.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));
    buttonPanel.add(
        new EditorLayerButtonComponent(
            controller,
            Subject.EDITOR_LAYER_DELETE_PRESSED,
            "ui.tooltips.delete_layer",
            "icons/delete.svg"));
    buttonPanel.add(Box.createHorizontalStrut(SMALL_STRUCT_WIDTH));
    buttonPanel.add(
        new EditorLayerButtonComponent(
            controller,
            Subject.EDITOR_LAYER_CLEAR_PRESSED,
            "ui.tooltips.clear_layers",
            "icons/clear_layers.svg"));

    buttonPanel.putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanelContents");

    return buttonPanel;
  }

  /** Gére l'affichage ou non des calques */
  @Override
  public void handleShowProperties() {
    if (chckbxShow.isSelected()) {
      scrollPane.setVisible(true);
      buttonPanel.setVisible(true);
      scrollPane.setEnabled(true);
      buttonPanel.setEnabled(true);
    } else {
      scrollPane.setVisible(false);
      buttonPanel.setVisible(false);
      scrollPane.setEnabled(false);
      buttonPanel.setEnabled(false);
    }
    super.handleShowProperties();
  }

  /**
   * Panneau représentant un calque de l'éditeur et affichant un aperçu de son image ainsi que son
   * nom
   *
   * @author n-o-o-d-l-e
   */
  public class EditorLayerComponent extends JPanel {
    /** Numéro de sérialisation de la classe */
    private static final long serialVersionUID = 1L;

    /** Le {@code ImageIcon} affichant un aperçu du calque */
    private ImageIcon layerImage = new ImageIcon();

    /** L'étiquette affichant le {@code ImageIcon} du calque */
    private JLabel layerImageLbl = new JLabel();

    /** Étiquette affichant le nom du calque */
    private JLabel lblName;

    /** La couleur du panneau non-focusé */
    private final Color NON_FOCUSED_COLOR;

    /** La bordure du panneau non-focusé */
    private final Border NON_FOCUSED_BORDER;

    /** Le calque */
    private WLayer layer;

    /** La hauteur de l'aperçu du calque */
    private final int ICON_HEIGHT = 60;

    /**
     * Constructeur du panneau représentant un calque de l'éditeur
     *
     * @param controller Le {@code Controller} de l'application
     * @param layer Le calque {@code WLayer} que le panneau doit représenter
     */
    public EditorLayerComponent(Controller controller, WLayer layer) {
      this.layer = layer;
      setLayout(new MigLayout("", "[][grow,right]", "[]"));

      lblName = new JLabel(layer.getName());

      layerImageLbl.setBorder(this.getBorder());

      updateImage();
      add(layerImageLbl, "alignx left,aligny center");
      putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");

      NON_FOCUSED_COLOR = this.getBackground();
      NON_FOCUSED_BORDER = getBorder();

      this.add(lblName, "cell 1 0");
      setFocusable(true);
      addMouseListener(
          new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              controller.getEditorModel().getLayerModel().setSelectedLayer(layer);
              updateFocus();
            }
          });
    }

    /** Méthode permettant de mettre en avant le panneau du calque */
    public void setBackgroundFocused() {
      setBackground(UIManager.getColor("Button.background"));
      setBorder(UIManager.getBorder("Button.border"));
    }

    /** Méthode permettant de mettre en arrière-plan le panneau du calque */
    public void setBackgroundUnfocused() {
      setBackground(NON_FOCUSED_COLOR);
      setBorder(NON_FOCUSED_BORDER);
    }

    /** Méthode permettant de mettre à jour le nom du calque */
    public void updateLayerName() {
      lblName.setText(layer.getName());
    }

    /**
     * Méthode permettant de récupérer le nom du calque
     *
     * @return Le nom du calque
     */
    public String getLayerName() {
      return lblName.getText();
    }

    /** Méthode mettant à jour l'aperçu du calque */
    public void updateImage() {
      int newWidth = ICON_HEIGHT * layer.getImage().getWidth() / layer.getImage().getHeight();
      BufferedImage imgResize =
          new BufferedImage(newWidth, ICON_HEIGHT, layer.getImage().getType());
      Graphics2D g2dTemp = imgResize.createGraphics();
      g2dTemp.setRenderingHint(
          RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
      g2dTemp.drawImage(
          layer.getImage(),
          0,
          0,
          newWidth,
          ICON_HEIGHT,
          0,
          0,
          layer.getImage().getWidth(),
          layer.getImage().getHeight(),
          null);
      g2dTemp.dispose();
      layerImage = new ImageIcon(imgResize);
      layerImageLbl.setIcon(layerImage);
      repaint();
    }

    /**
     * Méthode permettant de récupérer le calque {@code WLayer} du panneau
     *
     * @return Le calque {@code WLayer}
     */
    public WLayer getLayer() {
      return layer;
    }
  }

  /**
   * Sous-classe représentant un bouton réutilisable du panneau de calques
   *
   * @author n-o-o-d-l-e
   */
  private class EditorLayerButtonComponent extends JButton {
    /** Numéro de sérialisation de la classe */
    private static final long serialVersionUID = 1L;

    /** Le {@code Controller} de l'application */
    private Controller controller;

    /** La taille d'un bouton */
    private final Dimension BUTTON_SIZE = new Dimension(30, 30);

    /** Le {@code Subject} que le bouton doit envoyé au {@code EditorModel} après un appui */
    private Subject BTN_SUBJECT;

    /**
     * Constructeur d'un bouton du panneau calque
     *
     * @param controller Le {@code Controller} de l'application
     * @param subject Le {@code Subject} à envoyer après un appui
     * @param tooltipLocaleKey La clé de localisation du tooltip
     * @param iconPath Le chemin vers l'icone du bouton
     */
    public EditorLayerButtonComponent(
        Controller controller, Subject subject, String tooltipLocaleKey, String iconPath) {
      this.controller = controller;
      if (!tooltipLocaleKey.isEmpty())
        setToolTipText(LocaleManager.getLocaleResourceBundle().getString(tooltipLocaleKey));
      BTN_SUBJECT = subject;
      if (BTN_SUBJECT == Subject.EDITOR_LAYER_RENAMED) {
        this.controller
            .getEditorModel()
            .getLayerModel()
            .getSubscriber()
            .subscribe(Subject.KEYBIND_EDITOR_RENAME_PRESSED, e -> publishEvent());
      }
      this.setMinimumSize(BUTTON_SIZE);
      setSize(BUTTON_SIZE);
      setPreferredSize(BUTTON_SIZE);
      setMaximumSize(BUTTON_SIZE);

      try {
        FlatSVGIcon svgIcon = new FlatSVGIcon(iconPath, 0.75f);

        svgIcon.setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground")));

        setIcon(svgIcon);
      } catch (Exception e) {
        e.printStackTrace();
      }

      addActionListener(
          e -> {
            publishEvent();
          });
    }

    /** Méthode permettant de renommer un calque */
    public void renameLayer() {
      currentlyRenaming = true;
      String newName =
          JOptionPane.showInputDialog(
              null,
              LocaleManager.getLocaleResourceBundle().getString("ui.editor.rename_message"),
              LocaleManager.getLocaleResourceBundle().getString("ui.editor.rename"),
              JOptionPane.INFORMATION_MESSAGE);
      currentlyRenaming = false;
      if (newName == null) {
        return;
      }
      this.controller.getEditorModel().getLayerModel().getSelectedLayer().setName(newName);
      updateLayers(true);
      return;
    }

    /** Méthode permettant de publier un événement après un appui sur le bouton */
    public void publishEvent() {
      if (BTN_SUBJECT == Subject.EDITOR_LAYER_RENAMED && !currentlyRenaming) {
        renameLayer();
      } else if (BTN_SUBJECT == Subject.EDITOR_LAYER_CLEAR_PRESSED) {
        if (JOptionPane.showConfirmDialog(
                null,
                LocaleManager.getLocaleResourceBundle().getString("ui.editor.clear_layers_message"),
                LocaleManager.getLocaleResourceBundle().getString("ui.editor.clear_layers_title"),
                JOptionPane.YES_NO_OPTION)
            == JOptionPane.NO_OPTION) return;
      }
      this.controller.getEditorModel().getLayerModel().getPublisher().publish(BTN_SUBJECT, null);
    }
  }
}
