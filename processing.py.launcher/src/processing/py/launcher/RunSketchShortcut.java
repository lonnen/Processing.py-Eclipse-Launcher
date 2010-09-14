package processing.py.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

/** Launches a *.py file as a Processing.py sketch in the Jython interpreter. */
public class RunSketchShortcut implements ILaunchShortcut {

	public static final String ID_LAUNCH_CONFIGURATION_TYPE = "processing.py.LaunchConfigurationType";

	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection){
			Object object = ((IStructuredSelection) selection).getFirstElement();
			if(object instanceof IAdaptable){
				IResource resource = (IResource) ((IAdaptable)object).getAdapter(IResource.class);
				if (resource != null){
					launch(resource, mode);
					return;
				}
			}
		}
		Activator.logError("Launch Error: Could not get a resource from the selection", null);
	}

	/** Run simple checks, and if the resource passes, launch it in the given mode. */
	public void launch(IResource resource, String mode) {
		if( resource == null )
			Activator.logError("Launch Error: Resource was null", null);
		else if (! ("py".equalsIgnoreCase(resource.getFileExtension())))
			Activator.logError("Launch Error: Bad File Extension", null);
		else
			launch((IFile)resource, mode);
	}


	public void launch(IFile file, String mode) {
		if (!verifyMode(mode)) return;
		
		ILaunchConfiguration configuration = createLaunchConfiguration(file);
		if (configuration == null) {		
			Activator.logError("Launch Error: Launch configuration could not be created", null);
			return;
		}
		
		DebugUITools.launch(configuration, mode);
	}

	/** Had to implement. Untested, may not work. */
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		IFile file = (IFile)input.getAdapter(IFile.class);
		if(file == null){
			Activator.logError("Launch Error: Could not adapt the editor contents to a file", null);
			return;
		}
		launch(file,mode);
	}
	
	/** Verify that the mode is supported. */
	protected boolean verifyMode(String mode){
		if (mode.equals(ILaunchManager.RUN_MODE)) return true;		
		Activator.logError("Launch Error: Processing.py launcher only supports \"run\" mode.",null);
		return false;
	}

	/** Create and return the default launch config for the file. */
	protected ILaunchConfiguration createLaunchConfiguration(IFile file) {
		if (!file.isAccessible()) return null;
		
		String jarPath = Activator.getDefault().getPreferenceStore().getString(JycessingPreferences.DIR_PATH);
		String moreArgs = Activator.getDefault().getPreferenceStore().getString(JycessingPreferences.ADD_ARGS);
		
		File jarFolder = new File(jarPath);
		File jarFile = new File(jarFolder, "processing-py.jar");
		
		if(!jarFile.exists()){
			Activator.logError("Launch aborted. Please indicates a valid Processing.py location in the preferences menu.", null);
			return null;
		}

		ILaunchConfiguration config = null;
		try{
			IRuntimeClasspathEntry jarFileClasspathEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(jarFile.getCanonicalPath()));
			List classpath = new ArrayList();
			classpath.add(jarFileClasspathEntry.getMemento());
			
			ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();					
			ILaunchConfigurationType configType = lm.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
			ILaunchConfigurationWorkingCopy workingCopy = configType.newInstance(null, file.getName());	
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "jycessing.Runner");
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, jarFolder.getCanonicalPath());
			workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, ( moreArgs + " " + file.getLocation().toOSString()));

			config = workingCopy; // instead of workingCopy.doSave() to prevent it from being stored
			
		} catch (Exception e){
			Activator.logError("Launch Error: An exception occured while creating a launch configuration.", e);
		}
		return config;
	}
	
}
