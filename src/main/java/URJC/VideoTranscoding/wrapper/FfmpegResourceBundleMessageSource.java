package URJC.VideoTranscoding.wrapper;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * @author Fujitsu Technology Solutions S.A.
 */
public class FfmpegResourceBundleMessageSource extends ResourceBundleMessageSource implements Serializable {
	private static final long serialVersionUID = -4830276323423550391L;

	public ResourceBundle getResourceBundleLog4j(String basename, Locale locale) {
		return this.getResourceBundle(basename, locale);
	}
}
