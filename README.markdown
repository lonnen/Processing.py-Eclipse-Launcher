# Processing.py Eclipse Launcher Plug-in #

A simple Eclipse plug-in that launches *.py files as [processing.py](http://github.com/jdf/processing.py) sketches.
[Chris Lonnen](http://chrislonnen.com) &lt; [chris.lonnen@gmail.com](mailto:chris.lonnen@gmail.com)&gt;

## Please note that this plug-in will require a working [processing.py](http://github.com/jdf/processing.py) installation in your machine.

## Getting Started ##

First -- Go get processing.py and set it up. Remember where it is installed, _specifically_ making sure the folder contains processing-py.jar
    
Inside Eclipse go to Help -> Install New Software...
Add a new update site (Add... on the upper right) with the name "Processing.py launcher update site." and location - http://github.com/Lonnen/Processing.py-Eclipse-Launcher/raw/master/processing.py.plugin.updateSite/site.xml
Select the Processing.Py_Launcher plug in and choose Next. 
You will be prompted to review and accept the Eclipse Public License v 1.0. Proceed if you accept the terms.
The software contains unsigned content. Select ok if you trust the source (the update site from my github master is probably trustworthy).
Restart Eclipse if prompted.
Under File -> Preferences find Processing.py and indicate the folder where Processing.py is installed.
Add any additional command line arguments you desire ( --present for full screen. It should accept anything Processing does).
Save the preferences.
Right click on a *.py sketch in your workspace and select Run as... -> Processing.py Sketch
Error messages and standard out should appear in your Eclipse editor console.
	
## Whahuh? ##

This plug-in removes the necessity of command line interaction, and of keeping a terminal open.

Please play with this and [report bugs](http://github.com/Lonnen/Processing.py-Eclipse-Launcher/issues), I will try to patch these in a timely fashion.
I also accept feature requests, but know that those may linger for a long time without being acted on.