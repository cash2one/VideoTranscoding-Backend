package urjc.videotranscoding.core;

import java.io.File;
import java.nio.file.Path;

import urjc.videotranscoding.core.impl.StreamGobblerPersistent;
import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.exception.FFmpegException;

/**
 * 
 * @author luisca
 *
 */
public interface VideoTranscodingService {

	void transcode(File pathFFMPEG, Path folderOutput, OriginalVideo originalVideo)
			throws FFmpegException;

	public StreamGobblerPersistent getErrorGobbler();

	public StreamGobblerPersistent getInputGobbler();

	public StreamGobblerPersistent getOutputGobbler();

}
