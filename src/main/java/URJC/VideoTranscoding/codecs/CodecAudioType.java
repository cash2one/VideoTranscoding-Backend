package URJC.VideoTranscoding.codecs;

public enum CodecAudioType {
	AAC(" -c:a aac"), OGG(""), LIVBORVIS(""), COPY(" -c:a copy");
	private final String codecAudioType;

	CodecAudioType(String x) {
		this.codecAudioType = x;
	}

	public String getCodecAudioType() {
		return codecAudioType;
	}

}
