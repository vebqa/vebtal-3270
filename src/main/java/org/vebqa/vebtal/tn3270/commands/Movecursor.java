package org.vebqa.vebtal.tn3270.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

import net.sf.f3270.Terminal;

@Keyword(module = TN3270TestAdaptionPlugin.ID, command = "moveCursor", description = "move cursor to specific position", hintTarget = "row=;col=", hintValue = "")
public class Movecursor extends AbstractCommand {

	public Movecursor(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		Terminal driver = (Terminal) aDriver;
		Response tResp = new Response();

		int row = 1;
		int col = 1;

		// konvention: row=x
		String[] someToken = target.split(";");
		for (String aToken : someToken) {
			String[] parts = aToken.split("=");
			switch (parts[0]) {
			case "row":
				row = Integer.valueOf(parts[1]);
				break;
			case "col":
				col = Integer.valueOf(parts[1]);
				break;
			default:
				break;
			}
		}

		driver.moveCursor(row -1, col -1); // zero based!
		
		tResp.setCode(Response.PASSED);

		return tResp;
	}
}
