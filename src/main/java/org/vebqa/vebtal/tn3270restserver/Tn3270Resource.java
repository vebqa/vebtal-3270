package org.vebqa.vebtal.tn3270restserver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.AbstractTestAdaptionResource;
import org.vebqa.vebtal.TestAdaptionResource;
import org.vebqa.vebtal.model.Command;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import net.sf.f3270.Terminal;

public class Tn3270Resource extends AbstractTestAdaptionResource implements TestAdaptionResource {

	private static final Logger logger = LoggerFactory.getLogger(Tn3270Resource.class);

	/**
	 * Terminal
	 */
	private static Terminal driver;

	public Tn3270Resource() {
	}

	public Response execute(Command cmd) {

		TN3270TestAdaptionPlugin.setDisableUserActions(true);

		Response tResponse = new Response();

		Response result = null;
		try {
			Class<?> cmdClass = Class.forName("org.vebqa.vebtal.tn3270." + getCommandClassName(cmd));
			Constructor<?> cons = cmdClass.getConstructor(String.class, String.class, String.class);
			Object cmdObj = cons.newInstance(cmd.getCommand(), cmd.getTarget(), cmd.getValue());

			// get type
			Method mType = cmdClass.getMethod("getType");
			CommandType cmdType = (CommandType) mType.invoke(cmdObj);
			TN3270TestAdaptionPlugin.addCommandToList(cmd, cmdType);

			// execute
			Method m = cmdClass.getDeclaredMethod("executeImpl", Object.class);

			setStart();
			result = (Response) m.invoke(cmdObj, driver);
			setFinished();

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
			tResponse.setCode(Response.FAILED);
			tResponse.setMessage("Cannot resolve findby.");
			TN3270TestAdaptionPlugin.setDisableUserActions(true);
			return tResponse;
		}
		if (result.getCode().equals(Response.FAILED)) {
			TN3270TestAdaptionPlugin.setLatestResult(false, result.getMessage());
		} else {
			// test if driver is connected
			if (driver.isConnected()) {
				String aDump = driver.getScreenText();
				TN3270TestAdaptionPlugin.setLatestResult(true, aDump);
			} else {
				TN3270TestAdaptionPlugin.setLatestResult(true, "not connected to host");
			}
		}

		TN3270TestAdaptionPlugin.setDisableUserActions(true);
		return result;
	}

	public static void setDriver(Terminal aDriver) {
		driver = aDriver;
	}
}
