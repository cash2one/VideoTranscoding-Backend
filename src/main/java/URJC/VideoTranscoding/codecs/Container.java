package URJC.VideoTranscoding.codecs;

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
	AVI(".avi");
	final String containerType;

	Container(String x){
		this.containerType = x;
	}

	public String getContainerType(){
		return containerType;
	}
}
