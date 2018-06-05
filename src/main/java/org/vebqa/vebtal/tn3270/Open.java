package org.vebqa.vebtal.tn3270;

import java.io.File;

import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.Tn3270Resource;

import net.sf.f3270.HostCharset;
import net.sf.f3270.Terminal;
import net.sf.f3270.TerminalModel;
import net.sf.f3270.TerminalType;

public class Open extends AbstractCommand {

	public Open(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	public Response executeImpl(Terminal driver) {
		// find s3270 terminal emulator
		File dir = new File("s3270/client/ws3270.exe");

		// Refactor: Context
		// target = value
		// column, row, labelText
		String host = "localhost";
		int port = 23;
		String type = "TYPE_3279";
		String model = "MODE_80_24";
		String charset = "BRACKET";
		
		// Beispiel: target= host=ctbtest;port=992;codepage=1141;ssltype=sslv3
		String[] someToken = target.split(";");

		for (String aToken : someToken) {
			// Needs an equal
			String[] parts = aToken.split("=");
			switch (parts[0]) {
			case "host":
				host = String.valueOf(parts[1]);
				break;
			case "port":
				port = Integer.valueOf(parts[1]);
				break;
			case "type":
				type = String.valueOf(parts[1]);
				break;
			case "model":
				model = String.valueOf(parts[1]);
				break;
			case "charset":
				charset = String.valueOf(parts[1]);
			}
		}
		
		driver = new Terminal(dir.getAbsolutePath(), host, Integer.valueOf(port),
				TerminalType.valueOf(String.valueOf(type)), TerminalModel.valueOf(String.valueOf(model)), HostCharset.valueOf(charset), false);
		driver.connect();

		Tn3270Resource.setDriver(driver);
		
		Response tResponse = new Response();
		tResponse.setCode("0");
		return tResponse;
	}

}
