package URJC.VideoTranscoding.codecs;

public enum ContainerType {
	MP4(".mp4"), MKV(".mkv"), WEBM(".webm");
	final String containerType;

	ContainerType(String x) {
		this.containerType = x;
	}

	public String getContainerType() {
		return containerType;
	}



}
