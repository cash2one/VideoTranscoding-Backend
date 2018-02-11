package urjc.videotranscoding.persistentffmpeg;

import java.io.File;
import java.nio.file.Path;

import urjc.videotranscoding.entities.OriginalVideo;
import urjc.videotranscoding.exception.FFmpegException;
import urjc.videotranscoding.ffmpeg.StreamGobbler;

/**
 * 
 * @author luisca
 *
 */
public interface TranscodingServicePersistent {

	void transcode(File pathFFMPEG, File fileInput, Path folderOutput, OriginalVideo originalVideo)
			throws FFmpegException;

	public StreamGobbler getErrorGobbler();

	public StreamGobbler getInputGobbler();

	public StreamGobbler getOutputGobbler();

}
