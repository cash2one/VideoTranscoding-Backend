package es.urjc.videotranscoding.codecs;

/**
 * @author luisca
 */
public enum Container {
	/**
	 * Mp4 extension
	 */
	MP4(".mp4"),
	/**
	 * Mkv extension
	 */
	MKV(".mkv"),
	/**
	 * Webm extension
	 */
	WEBM(".webm"),
	/**
	 * Avi extension
	 */
	AVI(".avi"),
	/**
	 * Flv extension
	 */
	FLV(".flv");
	final String containerType;

	Container(String x) {
		this.containerType = x;
	}

	public String toString() {
		return containerType;
	}
}
