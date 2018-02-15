package urjc.videotranscoding.wrapper;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.web.context.support.XmlWebApplicationContext;
//@Service
public class MessageResolverServiceImpl implements MessageResolveService {
	private static final Logger LOGGER = LogManager
			.getLogger(MessageResolverServiceImpl.class);

	private MessageSource messageSource;

	@Override
	public void setMessageSource(MessageSource messageSource) {
		LOGGER.info("Messages i18n injected");
		this.messageSource = messageSource;
	}

	public String getMessage(String key, Object[] arguments, Locale locale) {
		String message = "";
		try {
			message = messageSource.getMessage(key, arguments, locale);
		} catch (NoSuchMessageException e) {
			message = key;
			LOGGER.warn("No message found: " + key);
		}
		return message;
	}

	public Map<String, String> getMessages(Locale locale) {
		Properties properties = ((XmlWebApplicationContext) messageSource)
				.getBean("messageSource",
						ExposedResourceMessageBundleSource.class)
				.getMessages(locale);
		Map<String, String> messagesMap = new HashMap<String, String>();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			if (entry.getKey() != null && entry.getValue() != null) {
				messagesMap.put(entry.getKey().toString(),
						entry.getValue().toString());
			}
		}
		return messagesMap;
	}
}
