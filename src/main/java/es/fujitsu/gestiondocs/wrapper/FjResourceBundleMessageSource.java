package es.fujitsu.gestiondocs.wrapper;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @author Fujitsu Technology Solutions S.A.
 */
public class FjResourceBundleMessageSource extends ResourceBundleMessageSource implements Serializable {
	private static final long serialVersionUID = -4830276323423550391L;

	public ResourceBundle getFjResourceBundle(String basename, Locale locale) {
		return this.getResourceBundle(basename, locale);
	}
}
