package URJC.VideoTranscoding.models;

public class Transcode{
	private String Progress;
	private String DurationVideoTime;
	private String FileSize;
	private String Speed;
	private String BitRate;

	public Transcode(){
		// TODO Auto-generated constructor stub
	}

	public String getProgress(){
		return Progress;
	}

	public void setProgress(String progress){
		Progress = progress;
	}

	public String getDurationVideoTime(){
		return DurationVideoTime;
	}

	public void setDurationVideoTime(String durationVideoTime){
		DurationVideoTime = durationVideoTime;
	}

	public String getFileSize(){
		return FileSize;
	}

	public void setFileSize(String fileSize){
		FileSize = fileSize;
	}

	public String getSpeed(){
		return Speed;
	}

	public void setSpeed(String speed){
		Speed = speed;
	}

	public String getBitRate(){
		return BitRate;
	}

	public void setBitRate(String bitRate){
		BitRate = bitRate;
	}
}
