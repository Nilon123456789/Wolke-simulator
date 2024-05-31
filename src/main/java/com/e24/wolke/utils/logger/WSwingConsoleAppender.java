package com.e24.wolke.utils.logger;

import com.e24.wolke.utils.images.WColor.WColorType;
import java.io.Serializable;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * WSwingAppender
 *
 * <p>Appender pour sauvegarder les logs pour une console Swing
 *
 * @author Nilon123456789
 */
@Plugin(
    name = "WSwingConsoleAppender",
    category = Core.CATEGORY_NAME,
    elementType = Appender.ELEMENT_TYPE)
public class WSwingConsoleAppender extends AbstractAppender {

  /**
   * Constructeur.
   *
   * @param name Le nom du Appender
   * @param filter Le Filter a associer avec le Appender
   * @param layout Le Layout a utiliser pour le formattage de l'evenement
   * @param ignoreExceptions Si vrai, exceptions seront logged et suppressees. Si faux, les erreurs
   *     seront logged puis passees a l'application
   * @param properties Proprietes Property
   * @since 2.11.2
   */
  protected WSwingConsoleAppender(
      final String name,
      final Filter filter,
      final Layout<? extends Serializable> layout,
      final boolean ignoreExceptions,
      final Property[] properties) {
    super(name, filter, layout, ignoreExceptions, properties);
  }

  /** {@inheritDoc} */
  @Override
  public void append(LogEvent event) {
    String formattedLog = new String(getLayout().toByteArray(event));
    formattedLog = convertToHtml(formattedLog);
    WConsole.append(new WLogEvent(formattedLog, event.toImmutable()));
  }

  /**
   * Convertit un log en HTML
   *
   * @param log log
   * @return log log en HTML
   */
  private static String convertToHtml(String log) {
    StringBuilder sb = new StringBuilder("<p>");

    for (WColorType col : WColorType.values()) {
      log = log.replace(col.getAnsiCode(), col.getColorName());
    }

    sb.append(log);

    sb.append("</p>");
    return sb.toString();
  }

  /**
   * Méthode d'usine pour créer un WSwingConsoleAppender
   *
   * @param name nom
   * @param layout layout
   * @param filter filtre
   * @param otherAttribute autre attribut
   * @return WSwingConsoleAppender
   */
  @PluginFactory
  public static WSwingConsoleAppender createAppender(
      @PluginAttribute("name") String name,
      @PluginElement("Layout") Layout<? extends Serializable> layout,
      @PluginElement("Filter") final Filter filter,
      @PluginAttribute("otherAttribute") String otherAttribute) {
    if (name == null) {
      LOGGER.error("No name provided for WSwingConsoleAppender");
      return null;
    }
    if (layout == null) {
      layout = PatternLayout.createDefaultLayout();
    }
    return new WSwingConsoleAppender(name, filter, layout, true, Property.EMPTY_ARRAY);
  }
}
