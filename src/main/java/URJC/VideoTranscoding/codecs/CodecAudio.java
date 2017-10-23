package URJC.VideoTranscoding.codecs;

public enum CodecAudio{
	/**
	 * 
	 */
	AAC(" -c:a libfdk_aac "),
	/**
	 * NO IMPLEMENTADO
	 */
	OGG(""),
	/**
	 * 
	 */
	LIBVORVIS(" -codec:a libvorbis "),
	/**
	 * 
	 */
	LIVOPUS(" -codec:a libopus "),
	/*
	 * 
	 */
	COPY(" -c:a copy ");
	private final String codecAudioType;

	CodecAudio(String x){
		this.codecAudioType = x;
	}

	public String getCodecAudioType(){
		return codecAudioType;
	}
}
