package es.urjc.videotranscoding.core;

import java.util.concurrent.ExecutionException;

import es.urjc.videotranscoding.core.impl.StreamGobbler;
import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.exception.FFmpegException;

/**
 * 
 * @author luisca
 * @since 0.5
 */
public interface VideoTranscodingService {
	/**
	 * 
	 * @param originalVideo
	 * @throws FFmpegException
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 */
	void transcodeVideo(OriginalVideo originalVideo) throws FFmpegException;

	StreamGobbler getErrorGobbler();

	StreamGobbler getInputGobbler();

	StreamGobbler getOutputGobbler();

}
