package es.urjc.videotranscoding.codecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConversionTypeBasic {
	public enum Types {
		WEB("web"), MOVIL("movil"), VLC("vlc"), ALL("");

		private final String text;

		Types(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}

	}

	// TODO FINAL LISTS OF CONVERSIONS
	private static List<ConversionType> MOVIL = Arrays.asList(ConversionType.MKV_H264480_COPY);
	private static List<ConversionType> WEB = Arrays.asList(ConversionType.MKV_H264720_COPY);
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
		case ALL:
			List<ConversionType> allList = new ArrayList<>();
			allList.addAll(VLC);
			allList.addAll(WEB);
			allList.addAll(MOVIL);
			return allList;
		}
		return null;

	}

}
