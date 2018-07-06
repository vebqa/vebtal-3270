package org.vebqa.vebtal.tn3270;

import org.vebqa.vebtal.GuiManager;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.sut.SutStatus;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

import net.sf.f3270.Terminal;

public class Close extends AbstractCommand {

	public Close(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		Terminal driver = (Terminal)aDriver;
		Response tResp = new Response();
		driver.disconnect();
		tResp.setCode("0");
		tResp.setMessage("Successfully disconnected from host");
		
		GuiManager.getinstance().setTabStatus(TN3270TestAdaptionPlugin.ID, SutStatus.DISCONNECTED);
		
		return tResp;
	}

	@Override
	public CommandType getType() {
		return this.type;
	}
}
