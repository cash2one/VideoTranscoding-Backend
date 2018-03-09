package es.urjc.videotranscoding.codecs;

/**
 * @author luisca
 */
public enum Container{
	/**
	 * 
	 */
	MP4(".mp4"),
	/**
	 * 
	 */
	MKV(".mkv"),
	/**
	 *     
	 */
	WEBM(".webm"),
	/**
	 * 
	 */
	AVI(".avi"),
	/**
	 * 
	 */
	FLV(".flv");
	final String containerType;


	Container(String x){
		this.containerType = x;
	}

	public String toString(){
		return containerType;
	}
}
