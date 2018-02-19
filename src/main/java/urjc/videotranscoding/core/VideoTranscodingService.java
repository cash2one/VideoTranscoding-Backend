package urjc.videotranscoding.core;

import urjc.videotranscoding.core.impl.StreamGobblerPersistent;
import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.exception.FFmpegException;

/**
 * 
 * @author luisca
 * @since 0.5
 */
public interface VideoTranscodingService {
	String getPathOfProgram();

	void transcodeVideo(String pathFFMPEG, String folderOutput, OriginalVideo originalVideo) throws FFmpegException;

	StreamGobblerPersistent getErrorGobbler();

	StreamGobblerPersistent getInputGobbler();

	StreamGobblerPersistent getOutputGobbler();

}
