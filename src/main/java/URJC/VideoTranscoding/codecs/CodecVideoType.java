package URJC.VideoTranscoding.codecs;

public enum CodecVideoType {
	HEVC_2160(" -c:v libx265 -s 3840x2160 -crf 15 -preset slow "), 
	HEVC_1080(" -c:v libx265 -s 1920x1080 -crf 15 -preset slow "),
	HEVC_720(" -c:v libx265 -s 1280x720 -crf 15 -preset slow "),
	HEVC_480(" -c:v libx265 -s 640x480 -crf 15 -preset slow "),
	HEVC_360(" -c:v libx265 -s 480x360 -crf 15 -preset slow "),

	VP9_1080(""), 
	MP4_1080("");
	
	private final String codecVideoType;

	CodecVideoType(String x) {
		this.codecVideoType = x;
	}

	public String getCodecVideoType() {
		return codecVideoType;
	}

}
