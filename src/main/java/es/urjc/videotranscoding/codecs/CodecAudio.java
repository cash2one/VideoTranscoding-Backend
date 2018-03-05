package es.urjc.videotranscoding.codecs;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public enum CodecAudio {

	/**
	 * Audio AAC
	 */
	AAC(" -c:a libfdk_aac "),
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
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idAudio;

	CodecAudio(String x) {
		this.codecAudioType = x;
	}

	public String toString() {
		return codecAudioType;
	}
}
