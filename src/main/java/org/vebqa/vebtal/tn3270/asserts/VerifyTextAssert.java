package org.vebqa.vebtal.tn3270.asserts;

import org.assertj.core.api.AbstractAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.model.SpecLine;
import org.vebqa.vebtal.tn3270.commands.Verifytext;

import net.sf.f3270.junit.rules.TerminalResource;

/**
 * Special assertion class - inherits from AbstractAssert!
 * 
 * @author doerges
 *
 */
public class VerifyTextAssert extends AbstractAssert<VerifyTextAssert, TerminalResource> {

	private static final Logger logger = LoggerFactory.getLogger(VerifyTextAssert.class);

	private String textToFind = null;
	private int x = 0;
	private int y = 0;

	/**
	 * Constructor assertion class, PDF filename ist the object we want to make
	 * assertions on.
	 * 
	 * @param aTerminal a terminal connected to the system under test
	 */
	public VerifyTextAssert(TerminalResource aTerminal) {
		super(aTerminal, VerifyTextAssert.class);
	}

	/**
	 * A fluent entry point to our specific assertion class, use it with static
	 * import.
	 * 
	 * @param aTerminal a terminal connected to the system under test
	 * @return new object
	 */
	public static VerifyTextAssert assertThat(TerminalResource aTerminal) {
		return new VerifyTextAssert(aTerminal);
	}

	/**
	 * Part of assertion - configure
	 * 
	 * @param someText some text we are expecting
	 * @return self
	 */
	public VerifyTextAssert hasText(String someText) {
		this.textToFind = someText;
		return this;
	}

	public VerifyTextAssert atPosition(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	/**
	 * A specific assertion
	 * 
	 * @return self
	 */
	public VerifyTextAssert check() {
		// check that we really have a terminal ressource
		isNotNull();

		// use keyword implementation here
		Verifytext keyword = new Verifytext("verifyText", SpecLine.build().setColumn(this.x).setRow(this.y).getLine(),
				this.textToFind);
		Response tResp = keyword.executeImpl(this.actual.getDriver());
		if (tResp.getCode() == Response.FAILED) {
			failWithMessage("Expected text <%s> not found in the page.", this.textToFind);
		}

		return this;
	}
}