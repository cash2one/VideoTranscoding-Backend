package es.urjc.videotranscoding.codecs;

public enum CodecAudio {

	/**
	 * Audio AAC
	 */
	AAC(" -c:a aac -b:a 320k "),
	/**
	 * Audio LibVorbis
	 */
	LIBVORVIS(" -codec:a libvorbis "),
	/**
	 * Audio LivOpus
	 */
	LIVOPUS(" -codec:a libopus "),
	/*
	 * Copy Audio
	 */
	COPY(" -c:a copy ");
	private final String codecAudioType;


	CodecAudio(String x) {
		this.codecAudioType = x;
	}

	public String toString() {
		return codecAudioType;
	}
}
