package processing.py.launcher;

import java.io.File;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class JycessingPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String DIR_PATH = "directoryPath";
	public static final String ADD_ARGS = "additionalArgs";
	
	private DirectoryFieldEditor dirPath;
	private StringFieldEditor additionalArgs;
	
	public JycessingPreferences(){ 
		super(GRID);
	}
	

	/* Field editors are inflexible, so we cannot put the box on a newline, but they greatly simplify this page. */
	public void createFieldEditors(){
		dirPath = new DirectoryFieldEditor(DIR_PATH, "Processing.py directory:", getFieldEditorParent());
		addField(dirPath);
		additionalArgs = new StringFieldEditor(ADD_ARGS, "Additional Arguments:", getFieldEditorParent());  
		addField(additionalArgs);
	}
	
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Processing.py Configuration");
	}
	
	/* Validate the directory path and enable the buttons. */
	public void checkState(){
		super.checkState();
		setValid(false);
		Path test = new Path(dirPath.getStringValue());
		if(test != null){
			File jycessingDir = new File(test.makeAbsolute().toOSString());
			File jycessingJar = new File(jycessingDir, "processing-py.jar");
			if(jycessingJar.exists()){
				setValid(true);
				setErrorMessage(null);
				return;
			}
		}
		setErrorMessage("processing-py.jar could not be found in the provided directory.");
	}
	
	/* Override to make checkState fire on every field change. By default it only fires for some. */
	public void propertyChange(PropertyChangeEvent event){
		super.propertyChange(event);
		if (event.getProperty().equals(FieldEditor.VALUE)) checkState();
	}
	
}
