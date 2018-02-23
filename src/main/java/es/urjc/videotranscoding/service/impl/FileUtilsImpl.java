package es.urjc.videotranscoding.service.impl;

import java.io.File;

import org.springframework.stereotype.Service;

import es.urjc.videotranscoding.service.FileUtils;

@Service
public class FileUtilsImpl implements FileUtils {

	@Override
	public boolean exitsFile(String file) {
		File f = new File(file);
		return f.exists();
	}
	public boolean exitsPath(String path) {
		File f = new File (path);
		return f.isDirectory();
	}
	

}
