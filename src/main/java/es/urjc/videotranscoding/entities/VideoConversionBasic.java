package es.urjc.videotranscoding.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonView;

import es.urjc.videotranscoding.codecs.ConversionTypeBasic;

/**
 * @author luisca
 * @since 0.5
 */

public class VideoConversionBasic {
	public interface Basic {
	}

	public interface Details {
	}

	/**
	 * VideoConversion id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonView(Basic.class)
	private long videoConversionId;
	/**
	 * Path where is localized the original Video
	 */
	@JsonView(Basic.class)
	private String originalVideo;

	
	//	private  List<Tripla<Boolean, ConversionType, String>> listConversions = new ArrayList<>();

	

	/**
	 * Constructor for Hibernate
	 */
	protected VideoConversionBasic() {
	}

	public VideoConversionBasic(String originalVideo, ConversionTypeBasic.DEVICE... type) {
		this.originalVideo=originalVideo;
		
	}

}
