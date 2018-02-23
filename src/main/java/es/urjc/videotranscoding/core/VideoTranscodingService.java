package es.urjc.videotranscoding.core;

import es.urjc.videotranscoding.core.impl.StreamGobbler;
import es.urjc.videotranscoding.entities.OriginalVideo;
import es.urjc.videotranscoding.exception.FFmpegException;

/**
 * 
 * @author luisca
 * @since 0.5
 */
public interface VideoTranscodingService {
	

	// TODO se puede quitar lo del pathFFMPEG ya que YO controlo la aplicación y se
	// cada cosa. SOLO deberia bastar con el original video para las conversioes

	void transcodeVideo( OriginalVideo originalVideo) throws FFmpegException;

	StreamGobbler getErrorGobbler();

	StreamGobbler getInputGobbler();

	StreamGobbler getOutputGobbler();

}
