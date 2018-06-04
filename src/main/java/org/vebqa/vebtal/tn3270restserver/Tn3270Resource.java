package org.vebqa.vebtal.tn3270restserver;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.TestAdaptionResource;
import org.vebqa.vebtal.model.Command;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.model.TN3270Session;

import net.sf.f3270.HostCharset;
import net.sf.f3270.Terminal;
import net.sf.f3270.TerminalModel;
import net.sf.f3270.TerminalType;

public class Tn3270Resource implements TestAdaptionResource {

	private static final Logger logger = LoggerFactory.getLogger(Tn3270Resource.class);

	/**
	 * Terminal
	 */
	private static Terminal driver;

	public Tn3270Resource() {
	}
	
	public Response execute(Command cmd) {
		TN3270TestAdaptionPlugin.addCommandToList(cmd);

		Response tResponse = new Response();

		// Test - to be refactored
		// Command instanziieren
		// erst alles klein schreiben
		String tCmd = cmd.getCommand().toLowerCase().trim();
		// erster Buchstabe gross
		String cmdFL = tCmd.substring(0, 1).toUpperCase();
		String cmdRest = tCmd.substring(1);
		tCmd = cmdFL + cmdRest;

		String tClass = "org.vebqa.vebtal.tn3270." + tCmd;
		Response result = null;
		try {
			Class<?> cmdClass = Class.forName(tClass);
			Constructor<?> cons = cmdClass.getConstructor(String.class, String.class, String.class);
			Object cmdObj = cons.newInstance(cmd.getCommand(), cmd.getTarget(), cmd.getValue());
			Method m = cmdClass.getDeclaredMethod("executeImpl", Terminal.class);
			result = (Response) m.invoke(cmdObj, driver);
		} catch (ClassNotFoundException e) {
			logger.error("Command implementation class not found!", e);
		} catch (NoSuchMethodException e) {
			logger.error("Execution method in command implementation class not found!", e);
		} catch (SecurityException e) {
			logger.error("Security exception", e);
		} catch (InstantiationException e) {
			logger.error("Cannot instantiate command implementation class!", e);
		} catch (IllegalAccessException e) {
			logger.error("Cannot access implementation class!", e);
		} catch (IllegalArgumentException e) {
			logger.error("Wrong arguments!", e);
		} catch (InvocationTargetException e) {
			logger.error("Error while invoking class!", e);
		}

		if (result == null) {
			tResponse.setCode("1");
			tResponse.setMessage("Cannot resolve findby.");
			return tResponse;
		}
		if (result.getCode() != "0") {
			TN3270TestAdaptionPlugin.setLatestResult(false, result.getMessage());
		} else {
			// test if driver is connected
			if (driver.isConnected()) {
				String aDump = driver.getScreenText();
				// Password inside?
				if (tCmd.toLowerCase().indexOf("password") > 0) {
					aDump = aDump.replaceAll(cmd.getValue(), "**********");
				}
				TN3270TestAdaptionPlugin.setLatestResult(true, aDump);
			} else {
				TN3270TestAdaptionPlugin.setLatestResult(true, "not connected to host");
			}
		}
		return result;
	}

	public static void setDriver(Terminal aDriver) {
		driver = aDriver;
	}
}
