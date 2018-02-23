package es.urjc.videotranscoding.codecs;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idContainer;

	Container(String x){
		this.containerType = x;
	}

	public String toString(){
		return containerType;
	}
}
