package es.urjc.videotranscoding.codecs;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Video file extension, CodecVideo and Resolution, CodecAudio
 * 
 * @author luisca
 */
public enum ConversionType {
	/**
 	* MKV/HEVC/COPY
 	*/
	MKV_HEVC2160_COPY(Container.MKV, CodecVideo.HEVC_2160, CodecAudio.COPY),
	MKV_HEVC1080_COPY(Container.MKV, CodecVideo.HEVC_1080, CodecAudio.COPY),
	MKV_HEVC720_COPY(Container.MKV, CodecVideo.HEVC_720, CodecAudio.COPY),
	MKV_HEVC480_COPY(Container.MKV, CodecVideo.HEVC_480, CodecAudio.COPY),
	MKV_HEVC360_COPY(Container.MKV, CodecVideo.HEVC_360, CodecAudio.COPY),
	/**
	 * MKV/H264/COPY
	 */
	MKV_H2641080_COPY(Container.MKV, CodecVideo.H264_1080, CodecAudio.COPY),
	MKV_H264720_COPY(Container.MKV, CodecVideo.H264_720, CodecAudio.COPY),
	MKV_H264480_COPY(Container.MKV, CodecVideo.H264_480, CodecAudio.COPY),
	MKV_H264360_COPY(Container.MKV, CodecVideo.H264_360, CodecAudio.COPY),
	
	/**
	 * MKV/H264/AAC
	 */
	MKV_HEVC2160_AAC(Container.MKV, CodecVideo.HEVC_2160, CodecAudio.AAC),
	MKV_HEVC1080_AAC(Container.MKV, CodecVideo.HEVC_1080, CodecAudio.AAC),
	MKV_HEVC720_AAC(Container.MKV, CodecVideo.HEVC_720, CodecAudio.AAC),
	MKV_HEVC480_AAC(Container.MKV, CodecVideo.HEVC_480, CodecAudio.AAC),
	MKV_HEVC360_AAC(Container.MKV, CodecVideo.HEVC_360, CodecAudio.AAC),
	/**
	 * MKV/H264/AAC
	 */
	MKV_H2641080_AAC(Container.MKV, CodecVideo.H264_1080, CodecAudio.AAC),
	MKV_H264720_AAC(Container.MKV, CodecVideo.H264_720, CodecAudio.AAC),
	MKV_H264480_AAC(Container.MKV, CodecVideo.H264_480, CodecAudio.AAC),
	MKV_H264360_AAC(Container.MKV, CodecVideo.H264_360, CodecAudio.AAC),
	/**
	 * MP4/HEVC/COPY
	 */
	MP4_HEVC2160_COPY(Container.MP4, CodecVideo.HEVC_2160, CodecAudio.COPY),
	MP4_HEVC1080_COPY(Container.MP4, CodecVideo.HEVC_1080, CodecAudio.COPY),
	MP4_HEVC720_COPY(Container.MP4, CodecVideo.HEVC_720, CodecAudio.COPY),
	MP4_HEVC480_COPY(Container.MP4, CodecVideo.HEVC_480, CodecAudio.COPY),
	MP4_HEVC360_COPY(Container.MP4, CodecVideo.HEVC_360, CodecAudio.COPY),
	/**
	 * MP4/H264/COPY
	 */
	MP4_H2641080_COPY(Container.MP4, CodecVideo.H264_1080, CodecAudio.COPY),
	MP4_H264720_COPY(Container.MP4, CodecVideo.H264_720, CodecAudio.COPY),
	MP4_H264480_COPY(Container.MP4, CodecVideo.H264_480, CodecAudio.COPY),
	MP4_H264360_COPY(Container.MP4, CodecVideo.H264_360, CodecAudio.COPY),
	/**
	 * MP4/HEVC/AAC
	 */
	MP4_HEVC2160_AAC(Container.MP4, CodecVideo.HEVC_2160, CodecAudio.AAC),
	MP4_HEVC1080_AAC(Container.MP4, CodecVideo.HEVC_1080, CodecAudio.AAC),
	MP4_HEVC720_AAC(Container.MP4, CodecVideo.HEVC_720, CodecAudio.AAC),
	MP4_HEVC480_AAC(Container.MP4, CodecVideo.HEVC_480, CodecAudio.AAC),
	MP4_HEVC360_AAC(Container.MP4, CodecVideo.HEVC_360, CodecAudio.AAC),
	/**
	 * MP4/H264/AAC
	 */
	MP4_H2641080_AAC(Container.MP4, CodecVideo.H264_1080, CodecAudio.AAC),
	MP4_H264720_AAC(Container.MP4, CodecVideo.H264_720, CodecAudio.AAC),
	MP4_H264480_AAC(Container.MP4, CodecVideo.H264_480, CodecAudio.AAC),
	MP4_H264360_AAC(Container.MP4, CodecVideo.H264_360, CodecAudio.AAC),
	/**
 	* WEBM/VP9/LIVBORVIS
 	*/
	WEBM_VP92160_VORBIS(Container.WEBM, CodecVideo.VP9_2160, CodecAudio.LIBVORVIS),
	WEBM_VP91080_VORBIS(Container.WEBM, CodecVideo.VP9_1080, CodecAudio.LIBVORVIS),
	WEBM_VP9720_VORBIS(Container.WEBM, CodecVideo.VP9_720, CodecAudio.LIBVORVIS),
	WEBM_VP9480_VORBIS(Container.WEBM, CodecVideo.VP9_480, CodecAudio.LIBVORVIS),
	WEBM_VP9360_VORBIS(Container.WEBM, CodecVideo.VP9_360, CodecAudio.LIBVORVIS);
	private final Container containerType;
	private final CodecVideo codecVideoType;
	private final CodecAudio codecAudioType;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idConversionType;

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
