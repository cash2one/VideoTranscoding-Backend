package es.urjc.videotranscoding.core;

import es.urjc.videotranscoding.core.impl.StreamGobbler;
import es.urjc.videotranscoding.entities.Original;
import es.urjc.videotranscoding.exception.FFmpegException;

/**
 * 
 * @author luisca
 * @since 0.5
 */
public interface VideoTranscodingService {
	/**
	 * Send the video to ffmpeg to convert it
	 * 
	 * @param originalVideo
	 *            that contains the conversion needs
	 * @throws FFmpegException
	 */
	void transcodeVideo(Original originalVideo) throws FFmpegException;

	/**
	 * Type of streamGlobber
	 * 
	 * @return errorGobbler
	 */
	StreamGobbler getErrorGobbler();

	/**
	 * Type of streamGlobber
	 * 
	 * @return inputGobbler
	 */
	StreamGobbler getInputGobbler();

	/**
	 * Type of streamGlobber
	 * 
	 * @return outputGobbler
	 */
	StreamGobbler getOutputGobbler();

}
