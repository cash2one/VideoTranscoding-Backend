package es.urjc.videotranscoding.codecs;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public enum CodecVideo {
	/**
	 * 
	 */
	HEVC_2160(" -c:v libx265 -s 3840x2160 -crf 15 -preset slow "),
	/**
	 * 
	 */
	HEVC_1080(" -c:v libx265 -s 1920x1080 -crf 15 -preset slow "),
	/**
	 * 
	 */
	HEVC_720(" -c:v libx265 -s 1280x720 -crf 15 -preset slow "),
	/**
	 * 
	 */
	HEVC_480(" -c:v libx265 -s 640x480 -crf 15 -preset slow "),
	/**
	 * 
	 */
	HEVC_360(" -c:v libx265 -s 480x360 -crf 15 -preset slow "),
	/**
	 * 
	 */
	VP9_2160(" -c:v libvpx-vp9 -s 3840x2160 -threads 4 -crf 30 -b:v 0 "),
	/**
	 * 
	 */
	VP9_1080(" -c:v libvpx-vp9 -s 1920x1080 -threads 4 -crf 30 -b:v 0 "),
	/**
	 * 
	 */
	VP9_720(" -c:v libvpx-vp9 -s 1280x720 -threads 4 -crf 30 -b:v 0 "),
	/**
	 * 
	 */
	VP9_480(" -c:v libvpx-vp9 -s 640x480 -threads 4 -crf 30 -b:v 0 "),
	/**
	 * 
	 */
	VP9_360(" -c:v libvpx-vp9 -s 480x360 -threads 4 -crf 30 -b:v 0 "),
	/**
	 * 
	 */
	H264_1080(" -c:v libx264 -s 1920x1080 -crf 18 -preset slow "),
	/**
	 * 
	 */
	H264_720(" -c:v libx264 -s 1280x720 -crf 18 -preset slow "),
	/**
	 * 
	 */
	H264_480(" -c:v libx264 -s 640x480 -crf 18 -preset slow "),
	/**
	 * 
	 */
	H264_360(" -c:v libx264 -s 480x360 -crf 18 -preset slow ");
	private final String codecVideoType;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idVideo;

	CodecVideo(String x) {
		this.codecVideoType = x;
	}

	@Override
	public String toString() {
		return codecVideoType;
	}
}
