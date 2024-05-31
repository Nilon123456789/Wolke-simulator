package com.e24.wolke.frontend.simulation;

import com.e24.wolke.application.OpenGLCanvas;
import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.renderer.RendererConstants;
import com.e24.wolke.backend.models.simulation.SimulationConstants;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.frontend.canvas.SimulationCanvasPane;
import com.e24.wolke.frontend.help.HelpButtonComponent;
import com.e24.wolke.frontend.input.AbstractInputPane;
import com.e24.wolke.frontend.input.SimulationInputPane;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

/**
 * Panneau de simulation
 *
 * @author n-o-o-d-l-e
 * @author Nilon123456789
 */
public class SimulationPane extends JPanel implements Reinitializable, WEventComponent {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Canvas de simulation */
  private SimulationCanvasPane simulationCanvasPane;

  /** Canvas OpenGL de simulation */
  private OpenGLCanvas openGLCanvas;

  /** Panneau de boutons de simulation */
  private SimulationButtonPane simulationButtonPane;

  /** Panneau d'entrée de simulation */
  private SimulationInputPane simulationInputPane;

  /** Panneau de légende de simulation */
  private SimulationLegendPane simulationLegendPane;

  /** Panneau d'inspection de simulation */
  private SimulationInspectPane simulationInspectPane;

  /** Panneau du temps actuel de la simulation */
  private JPanel panelCurrentTime =
      new JPanel() {
        /** Sérialisation de la classe */
        private static final long serialVersionUID = 1L;

        /**
         * Déssine une petite pastille en haut à gauche du temps actuel de la simulation pour
         * indiquer la stabilité de la simulation
         *
         * @param g Le {@code Graphics} de la fenêtre
         */
        public void paintComponent(Graphics g) {
          super.paintComponent(g);
          Graphics2D g2d = (Graphics2D) g;

          Color color = Color.GREEN;

          double cfl = controller.getSimulationModel().getCFLNumber();
          if (cfl >= SimulationConstants.MIN_CFL_WARN && cfl < SimulationConstants.MIN_CFL_ERR) {
            color = Color.ORANGE;
          } else if (cfl >= SimulationConstants.MIN_CFL_ERR) {
            color = Color.RED;
          }

          g2d.setColor(color);
          int width = getWidth();

          g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2d.fillOval(width - 10, 2, 7, 7);

          g2d.setColor(UIManager.getColor("Button.borderColor"));
          g2d.drawOval(width - 10, 2, 7, 7);
        }
      };

  /** Étiquette du temps actuel de la simulation */
  private JLabel lblCurrentTime = new JLabel(" 0.00s ");

  /** Bouton pour activer la pipette */
  private JToggleButton btnPicker;

  /** Composant du bouton d'aide de la pipette */
  private HelpButtonComponent pickerHelpButtonComponent;

  /** Composant du bouton d'aide du panneau de boutons de simulation */
  private HelpButtonComponent buttonPaneHelpButtonComponent;

  /** Taille du bouton de la pipette */
  private static final Dimension PICKER_BUTTON_SIZE = new Dimension(40, 40);

  /** Marge entre les composants */
  private static final int GAP = 10;

  /** Ancienne position du panneau de simulation */
  private int[] oldLegendPanePosition = new int[2];

  /** Identifiant des abonnements */
  private int subscriptionID;

  /**
   * Créer le panneau de l'onglet simulation
   *
   * @param controller Le {@code Controller} de l'application
   */
  public SimulationPane(Controller controller) {
    this.controller = controller;
    this.subscriptionID = hashCode();

    setLayout(new GridLayout(0, 1, 0, 0));

    JLayeredPane layeredPane = new JLayeredPane();

    add(layeredPane);
    layeredPane.setLayout(null);

    simulationButtonPane = new SimulationButtonPane(this.controller);
    simulationButtonPane.setBounds(
        GAP,
        (int)
            (ApplicationConstants.DEFAULT_WINDOW_SIZE.getHeight()
                - simulationButtonPane.getPreferredSize().height),
        simulationButtonPane.getPreferredSize().width,
        simulationButtonPane.getPreferredSize().height);
    layeredPane.setLayer(simulationButtonPane, JLayeredPane.MODAL_LAYER);
    layeredPane.add(simulationButtonPane);

    buttonPaneHelpButtonComponent = new HelpButtonComponent(controller, "simulation_controls");
    layeredPane.setLayer(buttonPaneHelpButtonComponent, JLayeredPane.MODAL_LAYER);
    layeredPane.add(buttonPaneHelpButtonComponent);

    if (RendererConstants.USE_OPENGL) {
      GLProfile glProfile = GLProfile.getMaxProgrammable(true);
      GLCapabilities glCapabilities = new GLCapabilities(glProfile);
      openGLCanvas = new OpenGLCanvas(controller, glCapabilities);
      openGLCanvas.setBounds(0, 0, 10, 10);
      layeredPane.add(openGLCanvas);
    } else {
      simulationCanvasPane = new SimulationCanvasPane(this.controller);
      simulationCanvasPane.setBounds(0, 0, 10, 10);
      layeredPane.add(simulationCanvasPane);
    }

    simulationInputPane = new SimulationInputPane(this.controller);
    layeredPane.setLayer(simulationInputPane, JLayeredPane.MODAL_LAYER);
    simulationInputPane.setBounds(109, 34, AbstractInputPane.PREFERRED_DIMENSION.width, 575);
    layeredPane.add(simulationInputPane);

    simulationLegendPane = new SimulationLegendPane(this.controller);
    layeredPane.setLayer(simulationLegendPane, JLayeredPane.DRAG_LAYER);
    simulationLegendPane.setBounds(GAP, GAP, 112, 350);
    simulationLegendPane.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
              oldLegendPanePosition[0] = e.getX();
              oldLegendPanePosition[1] = e.getY();
            } else if (e.getButton() == MouseEvent.BUTTON3) {
              simulationLegendPane.setBounds(
                  GAP, GAP, simulationLegendPane.getWidth(), simulationLegendPane.getHeight());
            }
          }
        });
    simulationLegendPane.addMouseMotionListener(
        new MouseAdapter() {
          @Override
          public void mouseDragged(MouseEvent e) {
            if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
              int[] newPosition = {
                simulationLegendPane.getX() + e.getX() - oldLegendPanePosition[0],
                simulationLegendPane.getY() + e.getY() - oldLegendPanePosition[1]
              };
              if (newPosition[0] < GAP) {
                newPosition[0] = GAP;
              } else if (newPosition[0] + simulationLegendPane.getWidth() > getWidth() - GAP) {
                newPosition[0] = getWidth() - simulationLegendPane.getWidth() - GAP;
              }
              if (newPosition[1] < GAP) {
                newPosition[1] = GAP;
              } else if (newPosition[1] + simulationLegendPane.getHeight() > getHeight() - GAP) {
                newPosition[1] = getHeight() - simulationLegendPane.getHeight() - GAP;
              }
              simulationLegendPane.setBounds(
                  newPosition[0],
                  newPosition[1],
                  simulationLegendPane.getWidth(),
                  simulationLegendPane.getHeight());
            }
          }
        });
    layeredPane.add(simulationLegendPane);

    panelCurrentTime.setLayout(new BoxLayout(panelCurrentTime, BoxLayout.X_AXIS));
    panelCurrentTime.putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");
    panelCurrentTime.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.elapsed_time"));
    layeredPane.setLayer(panelCurrentTime, JLayeredPane.MODAL_LAYER);
    lblCurrentTime.setText(
        " " + String.format("%.2f", controller.getSimulationModel().getCurrentTime()) + "s ");
    lblCurrentTime.setFont(lblCurrentTime.getFont().deriveFont(16.0f));
    lblCurrentTime.setHorizontalAlignment(JLabel.CENTER);
    lblCurrentTime.setHorizontalTextPosition(JLabel.CENTER);
    panelCurrentTime.add(lblCurrentTime);
    layeredPane.add(panelCurrentTime);

    FlatSVGIcon pickerIcon = new FlatSVGIcon("icons/picker.svg");
    pickerIcon.setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground")));
    btnPicker = new JToggleButton(pickerIcon);
    btnPicker.setToolTipText(
        LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.inspector"));
    btnPicker.addActionListener(
        e -> this.controller.getApplicationModel().setInspectorButtonState(btnPicker.isSelected()));
    layeredPane.setLayer(btnPicker, JLayeredPane.MODAL_LAYER);
    btnPicker.setMinimumSize(PICKER_BUTTON_SIZE);
    btnPicker.setMaximumSize(PICKER_BUTTON_SIZE);
    btnPicker.setPreferredSize(PICKER_BUTTON_SIZE);
    if (RendererConstants.USE_OPENGL) {
      btnPicker.setEnabled(false);
    }
    layeredPane.add(btnPicker);

    pickerHelpButtonComponent = new HelpButtonComponent(controller, "cell_inspector");
    layeredPane.setLayer(pickerHelpButtonComponent, JLayeredPane.MODAL_LAYER);
    layeredPane.add(pickerHelpButtonComponent);

    simulationInspectPane = new SimulationInspectPane(this.controller);
    layeredPane.setLayer(simulationInspectPane, JLayeredPane.MODAL_LAYER);
    simulationInspectPane.setBounds(
        200, 200, simulationInspectPane.getWidth(), simulationInspectPane.getHeight());
    simulationInspectPane.setVisible(controller.getApplicationModel().getInspectorButtonState());
    layeredPane.add(simulationInspectPane);

    handleResize();
    handlePickerButton();
    updateInspectPanePosition();

    addComponentListener(
        new ComponentAdapter() {
          @Override
          public void componentResized(ComponentEvent e) {
            handleResize();
          }
        });

    if (!RendererConstants.USE_OPENGL) {
      simulationCanvasPane.addMouseListener(
          new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
              if (controller.getApplicationModel().getInspectorButtonState()) {
                updateInspectPanePositionOnMouseMove(e);
              }
            }
          });
      simulationCanvasPane.addMouseMotionListener(
          new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
              if (controller.getApplicationModel().getInspectorButtonState()) {
                simulationInspectPane.setVisible(true);
                if (!controller.getApplicationModel().getInspectorPanePinned()) {
                  updateInspectPanePositionOnMouseMove(e);
                }
              }
            }
          });
    }
    setupSubscribers();

    this.panelCurrentTime.repaint();
  }

  /**
   * Méthode mettant à jour la position du panneau d'inspection de la simulation ainsi que cette
   * position dans le contexte de l'inspecteur
   *
   * @param e L'événement de la souris
   */
  private void updateInspectPanePositionOnMouseMove(MouseEvent e) {
    Integer[] cellPosition = {
      controller.getApplicationModel().getMousePositionRelativeToSimulation()[0],
      controller.getApplicationModel().getMousePositionRelativeToSimulation()[1]
    };
    if (cellPosition[0] == -1 && cellPosition[1] == -1) {
      return;
    }
    controller.getApplicationModel().setCurrentInspectorPosition(cellPosition);
    // Si clic droit, déepingler le panneau d'inspection
    if (e.getButton() == MouseEvent.BUTTON3) {
      controller.getApplicationModel().toggleInspectorPanePinned();
    } else if (e.getButton() == MouseEvent.BUTTON1) {
      controller.getApplicationModel().setInspectorPanePinned(true);
    }
    controller
        .getApplicationModel()
        .setMousePositionRelativeToFrame(new int[] {e.getX(), e.getY()});
    updateInspectPanePosition();
  }

  /** Mettre à jour la position du panneau d'inspection de la simulation */
  private void updateInspectPanePosition() {
    simulationInspectPane.setBounds(
        controller.getApplicationModel().getMousePositionRelativeToFrame()[0],
        controller.getApplicationModel().getMousePositionRelativeToFrame()[1],
        simulationInspectPane.getWidth(),
        simulationInspectPane.getHeight());
    controller
        .getApplicationModel()
        .getPublisher()
        .publish(
            Subject.ON_SIMULATION_CANVAS_PANE_CLICKED,
            controller.getApplicationModel().getCurrentInspectorPosition());
  }

  /** Gestion du bouton de la pipette */
  private void handlePickerButton() {
    if (controller.getApplicationModel().getInspectorButtonState()) {
      btnPicker.setSelected(true);
      simulationInspectPane.setVisible(true);
      if (!RendererConstants.USE_OPENGL) {
        simulationCanvasPane.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
      }
    } else {
      btnPicker.setSelected(false);
      controller.getApplicationModel().setInspectorPanePinned(false);
      simulationInspectPane.setVisible(false);
      if (!RendererConstants.USE_OPENGL) {
        simulationCanvasPane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setupSubscribers() {
    controller
        .getSimulationModel()
        .getSubscriber()
        .subscribeWithID(Subject.ON_RENDERING_DONE, e -> updateCurrentTime(), subscriptionID);
    controller
        .getSimulationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_SIMULATION_RESTARTED,
            e -> {
              lblCurrentTime.setText(" 0.00s ");
              handleTimeLblResize();
            },
            subscriptionID);
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(Subject.ON_UI_INPUT_PANE_MINIMIZED, e -> handleResize(), subscriptionID);
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_INSPECTOR_VISIBILITY_CHANGED, e -> handlePickerButton(), subscriptionID);
    if (!RendererConstants.USE_OPENGL) {
      controller
          .getApplicationModel()
          .getSubscriber()
          .subscribeWithID(
              Subject.INSPECTOR_REPOSITION_NEEDED,
              e -> handleInspectPaneReposition(),
              subscriptionID);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
    simulationLegendPane.reinitialize();
    simulationButtonPane.reinitialize();
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    controller.getSimulationModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
    controller.getApplicationModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }

  /** Met à jour le temps actuel de la simulation dans l'étiquette */
  private void updateCurrentTime() {
    lblCurrentTime.setText(
        " " + String.format("%.2f", controller.getSimulationModel().getCurrentTime()) + "s ");
    handleTimeLblResize();
  }

  /**
   * Gestion du redimensionnement du canvas de simulation lors du redimensionnement de la fenêtre
   */
  private void handleCanvasPaneResize() {
    if (RendererConstants.USE_OPENGL) {
      openGLCanvas.setBounds(0, 0, getWidth(), getHeight());
    } else {
      simulationCanvasPane.setBounds(0, 0, getWidth(), getHeight());
      this.panelCurrentTime.repaint();
    }
  }

  /**
   * Gestion du redimensionnement de l'étiquette du temps actuel lors du redimensionnement de la
   * fenêtre ou du temps qui change
   */
  private void handleTimeLblResize() {
    panelCurrentTime.setBounds(
        GAP,
        (int)
            (getHeight()
                - 2 * GAP
                - GAP / 2
                - lblCurrentTime.getPreferredSize().getHeight()
                - simulationButtonPane.getPreferredSize().getHeight()),
        (int) lblCurrentTime.getPreferredSize().getWidth(),
        (int) lblCurrentTime.getPreferredSize().getHeight() + GAP / 2);
    this.panelCurrentTime.repaint();
  }

  /**
   * Gestion du redimensionnement du panneau de boutons de simulation lors du redimensionnement de
   * la fenêtre
   */
  private void handleButtonPaneResize() {
    simulationButtonPane.setBounds(
        GAP,
        (int) (getHeight() - GAP - simulationButtonPane.getPreferredSize().getHeight()),
        (int) simulationButtonPane.getPreferredSize().getWidth(),
        (int) simulationButtonPane.getPreferredSize().getHeight());

    buttonPaneHelpButtonComponent.setBounds(
        (int) (GAP + simulationButtonPane.getPreferredSize().getWidth() + GAP / 2),
        (int) (getHeight() - GAP - buttonPaneHelpButtonComponent.getPreferredSize().getHeight()),
        (int) buttonPaneHelpButtonComponent.getPreferredSize().getWidth(),
        (int) buttonPaneHelpButtonComponent.getPreferredSize().getHeight());
  }

  /**
   * Gestion du redimensionnement du panneau d'entrée de simulation lors du redimensionnement de la
   * fenêtre
   */
  private void handleInputPaneResize() {
    simulationInputPane.setMaxComponentHeight(getHeight() - 2 * GAP);
    simulationInputPane.setBounds(
        getWidth() - GAP - AbstractInputPane.PREFERRED_DIMENSION.width,
        GAP,
        AbstractInputPane.PREFERRED_DIMENSION.width,
        simulationInputPane.isMinimized()
            ? simulationInputPane.getMinimisedHeight()
            : (simulationInputPane.getPreferredSize().height < (getHeight() - 2 * GAP)
                ? simulationInputPane.getPreferredSize().height
                : (getHeight() - 2 * GAP)));

    simulationInputPane.updateUI();
  }

  /**
   * Gestion du redimensionnement du bouton de la pipette lors du redimensionnement de la fenêtre
   */
  private void handlePickerButtonResize() {
    if (simulationInputPane.getSize().height + 3 * GAP + btnPicker.getHeight() < getHeight()) {
      btnPicker.setBounds(
          (int) (getWidth() - GAP - PICKER_BUTTON_SIZE.width),
          (int) (getHeight() - GAP - PICKER_BUTTON_SIZE.height),
          (int) PICKER_BUTTON_SIZE.getWidth(),
          (int) PICKER_BUTTON_SIZE.getHeight());
      pickerHelpButtonComponent.setBounds(
          (int)
              (getWidth()
                  - GAP / 2
                  - PICKER_BUTTON_SIZE.width
                  - GAP
                  - pickerHelpButtonComponent.getPreferredSize().getWidth()),
          (int) (getHeight() - GAP - pickerHelpButtonComponent.getPreferredSize().getHeight()),
          (int) pickerHelpButtonComponent.getPreferredSize().getWidth(),
          (int) pickerHelpButtonComponent.getPreferredSize().getHeight());
    } else {
      btnPicker.setBounds(
          (int)
              (getWidth()
                  - GAP
                  - AbstractInputPane.PREFERRED_DIMENSION.width
                  - GAP
                  - PICKER_BUTTON_SIZE.width),
          (int) (getHeight() - GAP - PICKER_BUTTON_SIZE.height),
          (int) PICKER_BUTTON_SIZE.getWidth(),
          (int) PICKER_BUTTON_SIZE.getHeight());
      pickerHelpButtonComponent.setBounds(
          (int)
              (getWidth()
                  - GAP
                  - AbstractInputPane.PREFERRED_DIMENSION.width
                  - GAP
                  - PICKER_BUTTON_SIZE.width
                  - GAP / 2
                  - pickerHelpButtonComponent.getPreferredSize().getWidth()),
          (int) (getHeight() - GAP - pickerHelpButtonComponent.getPreferredSize().getHeight()),
          (int) pickerHelpButtonComponent.getPreferredSize().getWidth(),
          (int) pickerHelpButtonComponent.getPreferredSize().getHeight());
    }
  }

  /**
   * Gestion du redimensionnement du panneau d'inspection de simulation afin de garder sa position
   * épinglée au pixel précis de la simulation
   */
  private void handleInspectPaneReposition() {
    int[] cellPosition = simulationInspectPane.getCellPosition();
    int[] cellPositionRelativeToPanel =
        simulationCanvasPane.getCanvasPositionFromPixelValues(cellPosition);
    simulationInspectPane.setBounds(
        cellPositionRelativeToPanel[0],
        cellPositionRelativeToPanel[1],
        simulationInspectPane.getWidth(),
        simulationInspectPane.getHeight());
  }

  /**
   * Gestion du repositionnement du panneau de légende de simulation lors du redimensionnement de la
   * fenêtre, afin de s'assurer qu'il reste dans les limites du panneau de simulation
   */
  private void handleLegendPaneReposition() {
    if (getWidth() == 0 || getHeight() == 0) {
      return;
    }
    if (simulationLegendPane.getX() + simulationLegendPane.getWidth() > getWidth() - GAP) {
      simulationLegendPane.setBounds(
          getWidth() - simulationLegendPane.getWidth() - GAP,
          simulationLegendPane.getY(),
          simulationLegendPane.getWidth(),
          simulationLegendPane.getHeight());
    }
    if (simulationLegendPane.getY() + simulationLegendPane.getHeight() > getHeight() - GAP) {
      simulationLegendPane.setBounds(
          simulationLegendPane.getX(),
          getHeight() - simulationLegendPane.getHeight() - GAP,
          simulationLegendPane.getWidth(),
          simulationLegendPane.getHeight());
    }
  }

  /** Gestion de la redimension du panneau de simulation */
  private void handleResize() {
    handleCanvasPaneResize();
    handleTimeLblResize();
    handleButtonPaneResize();
    handleInputPaneResize();
    handlePickerButtonResize();
    handleLegendPaneReposition();
  }
}
