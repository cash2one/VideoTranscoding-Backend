package urjc.videotranscoding.codecs;

import java.util.Arrays;
import java.util.List;

public  class ConversionTypeBasic {

	public static enum DEVICE {
		MOVIL(listMovil), WEB_NAVIGATOR(listNavigator), VLC(listVLC);
		private DEVICE(List<ConversionType> list) {
		}

		public List<ConversionType> getDevice(ConversionTypeBasic.DEVICE x) {
			switch (x) {
			case MOVIL:
				return getConversionTypeBasicMovil();
			case VLC:
				return getConversionTypeBasicVLC();
			case WEB_NAVIGATOR:
				return getConversionTypeBasicNavigator();
			default:
				return null;
			}
		}
	}

	private static List<ConversionType> listMovil = Arrays.asList(ConversionType.MKV_H264360_COPY);
	private static List<ConversionType> listNavigator = Arrays.asList(ConversionType.MKV_H264360_COPY);
	private static List<ConversionType> listVLC = Arrays.asList(ConversionType.MKV_H264360_COPY);

	private ConversionTypeBasic(List<ConversionType> listConversions) {
		// TODO Auto-generated constructor stub
	}

	public static List<ConversionType> getConversionTypeBasicMovil() {
		return listMovil;
	}

	public static List<ConversionType> getConversionTypeBasicNavigator() {
		return listNavigator;
	}

	public static List<ConversionType> getConversionTypeBasicVLC() {
		return listVLC;
	}

}
