package es.urjc.videotranscoding.codecs;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConversionTypeBasic {
	public enum Types {
		WEB("web"), MOVIL("movil"), VLC("vlc");

		private final String text;

		Types(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}

	}

	private static List<ConversionType> MOVIL = Arrays.asList(ConversionType.MKV_H264360_COPY);
	private static List<ConversionType> WEB = Arrays.asList(ConversionType.MKV_H264360_COPY);
	private static List<ConversionType> VLC = Arrays.asList(ConversionType.MKV_H264360_COPY);

	public static List<String> getAllTypesBasic() {
		List<String> enumNames = Stream.of(Types.values()).map(Enum::name).collect(Collectors.toList());
		return enumNames;
	};

	public static List<ConversionType> getConversion(Types conversion) {
		switch (conversion) {
		case MOVIL:
			return MOVIL;

		case WEB:
			return WEB;

		case VLC:
			return VLC;

		}
		return null;

	}

}
