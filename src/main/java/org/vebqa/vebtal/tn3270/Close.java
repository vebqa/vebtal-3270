package org.vebqa.vebtal.tn3270;

import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import net.sf.f3270.Terminal;

public class Close extends AbstractCommand {

	public Close(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Terminal driver) {
		Response tResp = new Response();
		driver.disconnect();
		tResp.setCode("0");
		tResp.setMessage("Successfully disconnected from host");
		
		return tResp;
	}

}
