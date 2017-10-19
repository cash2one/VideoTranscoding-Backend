package URJC.VideoTranscoding.codecs;

/**
 * Video file extension, CodecVideo and Resolucion, CodecAudio
 * 
 * @author luisca
 *
 */
public enum ConversionType {
	/**
	 * MKV file extension, VideoCodec h265 on 2160, AudioCodec copy
	 */
	MKV_HEVC2160_COPY(ContainerType.MKV, CodecVideoType.HEVC_2160, CodecAudioType.COPY),
	/**
	 * MKV file extension, VideoCodec h265 on 1080, AudioCodec copy
	 */
	MKV_HEVC1080_COPY(ContainerType.MKV, CodecVideoType.HEVC_1080, CodecAudioType.COPY),
	/**
	 * MKV file extension, VideoCodec h265 on 720, AudioCodec copy
	 */
	MKV_HEVC720_COPY(ContainerType.MKV, CodecVideoType.HEVC_720, CodecAudioType.COPY),
	/**
	 * MKV file extension, VideoCodec h265 on 480, AudioCodec copy
	 */
	MKV_HEVC480_COPY(ContainerType.MKV, CodecVideoType.HEVC_480, CodecAudioType.COPY),
	/**
	 * MKV file extension, VideoCodec h265 on 360, AudioCodec copy
	 */
	MKV_HEVC360_COPY(ContainerType.MKV, CodecVideoType.HEVC_360, CodecAudioType.COPY),
	/**
	 * MKV file extension, VideoCodec h265 on 360, AudioCodec copy
	 */
	WEBM_VP91080_VORBIS(ContainerType.WEBM, CodecVideoType.HEVC_360, CodecAudioType.COPY);

	private final ContainerType containerType;
	private final CodecVideoType codecVideoType;
	private final CodecAudioType codecAudioType;

	private ConversionType(ContainerType x, CodecVideoType y, CodecAudioType z) {
		this.containerType = x;
		this.codecVideoType = y;
		this.codecAudioType = z;
	}

	public String getContainerType() {
		return containerType.getContainerType();
	}

	public String getCodecVideoType() {
		return codecVideoType.getCodecVideoType();
	}

	public String getCodecAudioType() {
		return codecAudioType.getCodecAudioType();
	}

}
