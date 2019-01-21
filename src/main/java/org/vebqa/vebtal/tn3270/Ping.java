package org.vebqa.vebtal.tn3270;

import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

public class Ping extends AbstractCommand {

	public Ping(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {

		Response tResponse = new Response();
		tResponse.setCode(Response.PASSED);
		tResponse.setMessage("Service " + TN3270TestAdaptionPlugin.ID + " is up and running.");
		
		return tResponse;
	}
}
