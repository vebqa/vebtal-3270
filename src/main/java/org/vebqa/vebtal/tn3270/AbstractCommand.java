package org.vebqa.vebtal.tn3270;

import org.vebqa.vebtal.command.ICommand;
import org.vebqa.vebtal.model.Response;

import net.sf.f3270.Terminal;

public abstract class AbstractCommand implements ICommand {
	
	protected final String command;
	protected final String target;
	protected final String value;
	
	public AbstractCommand(String aCommand, String aTarget, String aValue) {
		this.command = aCommand.trim();
		this.target = aTarget.trim();
		this.value = aValue.trim();
	}

	
	protected abstract Response executeImpl(Terminal driver);
}
