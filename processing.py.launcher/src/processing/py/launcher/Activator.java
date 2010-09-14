/*******************************************************************************
 * This program and the accompanying materials are made available under the 
 * terms of the Common Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.opensource.org/licenses/cpl1.0.php
 * 
 * Contributors:
 *     Red Robin - initial API
 *     Chris Lonnen - Processing.py implementation 
 *******************************************************************************/
package processing.py.launcher;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "processing.py.launcher";
	
	public static String LAUNCH_CONFIGURATION_TYPE = "processing.py.launcher.jythessing";
	
	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
		super();
		plugin=this;
	}

	/* @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext) */
	public void start(BundleContext context) throws Exception { super.start(context); }

	/* @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext) */
	public void stop(BundleContext context) throws Exception { super.stop(context); }

	/** Returns the shared instance */
	public static Activator getDefault() {
		return plugin;
	}

// ERROR LOGGING
	
	/**
	 * Convenience method for appending a string to the log file.
	 * Don't use this if there is an error.
	 * 
	 * @param message something to append to the log file 
	 */
	public static void logInfo(String message){
		log(IStatus.INFO, IStatus.OK, message, null);
	}
	
	/**
	 * Convenience method for appending an unexpected exception to the log file
	 * 
	 * @param exception some problem
	 */
	public static void logError(Throwable exception){
		logError("Unexpected Exception", exception);
	}
	
	/**
	 * Convenience method for appending an exception with a message
	 * 
	 * @param message a message, preferably something about the problem
	 * @param exception the problem
	 */
	public static void logError(String message, Throwable exception){
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}
	
	/**
	 * Adapter method that creates the appropriate status to be logged
	 * 
	 * @param severity integer code indicating the type of message
	 * @param code plug-in-specific status code
	 * @param message a human readable message
	 */
	public static void log(int severity, int code, String message, Throwable exception){
		log(createStatus(severity, code, message, exception));
	}
	
	/**
	 * Creates a status object to log
	 * 
	 * @param severity integer code indicating the type of message
	 * @param code plug-in-specific status code
	 * @param message a human readable message
	 * @param a low-level exception, or null
	 * @return status object
	 */
	public static IStatus createStatus(int severity, int code, String message, Throwable exception){
		return new Status(severity, PLUGIN_ID, code, message, exception);
	}
	
	/**
	 * Write a status to the log
	 * 
	 * @param status
	 */
	public static void log(IStatus status){
		getDefault().getLog().log(status);
	}
	
}
