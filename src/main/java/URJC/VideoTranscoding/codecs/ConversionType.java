package urjc.VideoTranscoding.codecs;

/**
 * Video file extension, CodecVideo and Resolution, CodecAudio
 * 
 * @author luisca
 */
public enum ConversionType {
	/**
	 * MKV file extension, VideoCodec h265 on 2160, AudioCodec copy
	 */
	MKV_HEVC2160_COPY(Container.MKV, CodecVideo.HEVC_2160, CodecAudio.COPY),
	/**
	 * MKV file extension, VideoCodec h265 on 1080, AudioCodec copy
	 */
	MKV_HEVC1080_COPY(Container.MKV, CodecVideo.HEVC_1080, CodecAudio.COPY),
	/**
	 * MKV file extension, VideoCodec h265 on 720, AudioCodec copy
	 */
	MKV_HEVC720_COPY(Container.MKV, CodecVideo.HEVC_720, CodecAudio.COPY),
	/**
	 * MKV file extension, VideoCodec h265 on 480, AudioCodec copy
	 */
	MKV_HEVC480_COPY(Container.MKV, CodecVideo.HEVC_480, CodecAudio.COPY),
	/**
	 * MKV file extension, VideoCodec h265 on 360, AudioCodec copy
	 */
	MKV_HEVC360_COPY(Container.MKV, CodecVideo.HEVC_360, CodecAudio.COPY),
	/**
	 * MKV file extension, VideoCodec h264 on 1080, AudioCodec copy
	 */
	MKV_H2641080_COPY(Container.MKV, CodecVideo.H264_1080, CodecAudio.COPY),
	/**
	 * 
	 */
	MKV_H264720_COPY(Container.MKV, CodecVideo.H264_720, CodecAudio.COPY),
	/**
	 * 
	 */
	MKV_H264480_COPY(Container.MKV, CodecVideo.H264_480, CodecAudio.COPY),
	/**
	 * 
	 */
	MKV_H264360_COPY(Container.MKV, CodecVideo.H264_360, CodecAudio.COPY),
	/**
	 * WEBM file extension, VideoCodec VP9 on 1080, AudioCodec LIVBORVIS
	 */
	WEBM_VP91080_VORBIS(Container.WEBM, CodecVideo.VP9_1080, CodecAudio.LIBVORVIS),
	/**
	 * WEBM file extension, VideoCodec VP9 on 720, AudioCodec LIVBORVIS
	 */
	WEBM_VP9720_VORBIS(Container.WEBM, CodecVideo.VP9_720, CodecAudio.LIBVORVIS),
	/**
	 * WEBM file extension, VideoCodec VP9 on 480, AudioCodec LIVBORVIS
	 */
	WEBM_VP9480_VORBIS(Container.WEBM, CodecVideo.VP9_480, CodecAudio.LIBVORVIS),
	/**
	 * WEBM file extension, VideoCodec VP9 on 360, AudioCodec LIVBORVIS
	 */
	WEBM_VP9360_VORBIS(Container.WEBM, CodecVideo.VP9_360, CodecAudio.LIBVORVIS);
	private final Container containerType;
	private final CodecVideo codecVideoType;
	private final CodecAudio codecAudioType;

	private ConversionType(Container x, CodecVideo y, CodecAudio z) {
		this.containerType = x;
		this.codecVideoType = y;
		this.codecAudioType = z;
	}

	public String getContainerType() {
		return containerType.toString();
	}

	public String getCodecVideoType() {
		return codecVideoType.toString();
	}

	public String getCodecAudioType() {
		return codecAudioType.toString();
	}
}
