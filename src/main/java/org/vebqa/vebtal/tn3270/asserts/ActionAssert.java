package org.vebqa.vebtal.tn3270.asserts;

import org.assertj.core.api.AbstractAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.model.SpecLine;
import org.vebqa.vebtal.tn3270.commands.Keypress;
import org.vebqa.vebtal.tn3270.commands.Type;

import net.sf.f3270.junit.rules.TerminalResource;

/**
 * Special assertion class - inherits from AbstractAssert!
 * 
 * @author doerges
 *
 */
public class ActionAssert extends AbstractAssert<VerifyTextAssert, TerminalResource> {

	private static final Logger logger = LoggerFactory.getLogger(ActionAssert.class);

	private String textToFind = null;
	private int x = 0;
	private int y = 0;
	private String label = null;

	/**
	 * Constructor assertion class, PDF filename ist the object we want to make
	 * assertions on.
	 * 
	 * @param aTerminal a terminal connected to the system under test
	 */
	public ActionAssert(TerminalResource aTerminal) {
		super(aTerminal, ActionAssert.class);
		this.textToFind = null;
		this.x = 0;
		this.y = 0;
		this.label = null;
	}

	/**
	 * A fluent entry point to our specific assertion class, use it with static
	 * import.
	 * 
	 * @param aTerminal a terminal connected to the system under test
	 * @return new object
	 */
	public static ActionAssert assertThat(TerminalResource aTerminal) {
		return new ActionAssert(aTerminal);
	}

	/**
	 * Part of assertion - configure
	 * 
	 * @param someChars some text we are expecting
	 */
	public void doType(String someChars) {
		// check that we really have a terminal ressource
		isNotNull();

		if (this.x > 0 || this.y > 0) {
			failWithMessage("Only label allowed as identifier for <type>");
		}
		
		// use keyword implementation here
		Type keyword = new Type("type", SpecLine.build().setLabel(this.label).getLine(), someChars);
		Response tResp = keyword.executeImpl(this.actual.getDriver());
		if (tResp.getCode() == Response.FAILED) {
			failWithMessage("Cannot type characters <%s>.", someChars);
		}
	}

	public void doKeypress(String aKeyDef) {
		// check that we really have a terminal ressource
		isNotNull();

		if ((this.x > 0 && this.y == 0) || (this.x == 0 && this.y > 0)) {
			failWithMessage("When position is specified we need row and column.");
		}

		// use keyword implementation here
		Keypress keyword = new Keypress("keyPress", "", aKeyDef);
		Response tResp = keyword.executeImpl(this.actual.getDriver());
		if (tResp.getCode() == Response.FAILED) {
			failWithMessage("Cannot press key <%s>.", aKeyDef);
		}
	}

	public ActionAssert atPositionX(int x) {
		this.x = x;
		return this;
	}

	public ActionAssert atPositionY(int y) {
		this.y = y;
		return this;
	}

	public ActionAssert forLabel(String aLabel) {
		this.label = aLabel;
		return this;
	}
}