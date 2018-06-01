package org.vebqa.vebtal.tn3270;

import java.io.File;

import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

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

		// target: 127.0.0.1:23
		String aHost = this.target.substring(0, this.target.indexOf(":"));
		String aPort = this.target.substring(this.target.indexOf(":") + 1, this.target.length());
		
		driver = new Terminal(dir.getAbsolutePath(), aHost, Integer.valueOf(aPort),
				TerminalType.TYPE_3279, TerminalModel.MODE_80_24, HostCharset.BRACKET, false);
		driver.connect();

		Response tResponse = new Response();
		tResponse.setCode("0");
		TN3270TestAdaptionPlugin.setLatestResult(true, driver.getScreenText());
		return tResponse;
	}

}
