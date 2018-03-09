package es.urjc.videotranscoding.codecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConversionTypeBasic {
	public enum Types {
		WEB("Web - For all Web Navigators"), MOVIL("Movil - Android and iOS"), VLC("Vlc - For Computers"), ALL("All - Web, Movil and Vlc");

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
	private static List<ConversionType> MOVIL = Arrays.asList(ConversionType.MP4_H264360_AAC,
			ConversionType.MP4_H264480_AAC, ConversionType.MP4_H264720_AAC, ConversionType.MP4_H2641080_AAC);
	private static List<ConversionType> WEB = Arrays.asList(ConversionType.WEBM_VP91080_VORBIS,
			ConversionType.WEBM_VP9360_VORBIS, ConversionType.WEBM_VP9480_VORBIS, ConversionType.WEBM_VP9720_VORBIS);
	private static List<ConversionType> VLC = Arrays.asList(ConversionType.MKV_HEVC1080_COPY,
			ConversionType.MKV_HEVC2160_COPY, ConversionType.MKV_HEVC720_COPY, ConversionType.MKV_HEVC480_COPY,
			ConversionType.MKV_HEVC360_COPY);

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
