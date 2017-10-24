package pruebas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class pruebas2{
	public static void main(String[] args){
		String texto = "frame= 1486 fps= 65 q=-0.0 size=    4738kB time=00:01:02.32 bitrate= 622.7kbits/s speed=2.74x";
		Pattern p = Pattern.compile(
					".*size= *(\\d+)kB.*time= *(\\d\\d):(\\d\\d):(\\d\\d\\.\\d\\d).*bitrate= *(\\d+\\.\\d)+kbits/s *speed= *(\\d+.\\d+)x.*");
		Matcher m = p.matcher(texto);
		if(!m.matches())
			System.out.println("Algo no me kuadra xikiyo");
		else{
			System.out.println("Size: " + m.group(1) + "kB");
			System.out.println("Time (Hours): " + m.group(2));
			System.out.println("Time (Minutes): " + m.group(3));
			System.out.println("Time (Seconds): " + m.group(4));
			System.out.println("Bitrate: " + m.group(5) + "kbits/s");
			System.out.println("Speed: " + m.group(6) + "x");
		}
	}
}
