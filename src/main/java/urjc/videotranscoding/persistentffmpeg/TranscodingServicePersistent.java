package urjc.videotranscoding.persistentffmpeg;

import java.io.File;
import java.nio.file.Path;

import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.exception.FFmpegException;

/**
 * 
 * @author luisca
 *
 */
public interface TranscodingServicePersistent {

	void transcode(File pathFFMPEG, Path folderOutput, OriginalVideo originalVideo)
			throws FFmpegException;

	public StreamGobblerPersistent getErrorGobbler();

	public StreamGobblerPersistent getInputGobbler();

	public StreamGobblerPersistent getOutputGobbler();

}
