package urjc.videotranscoding.wrapper;

import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSourceAware;

public interface MessageResolveService extends MessageSourceAware {
	String getMessage(String key, Object[] argumentsForKey, Locale locale);
	Map<String, String> getMessages(Locale locale);
}