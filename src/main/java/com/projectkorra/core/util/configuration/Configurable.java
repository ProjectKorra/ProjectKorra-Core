package com.projectkorra.core.util.configuration;

public interface Configurable {

	/**
	 * Name for the file of this configurable object, will always be placed in the
	 * folder named by {@link Configurable#getFolderName()}
	 * 
	 * @return the file name
	 */
	public String getFileName();

	/**
	 * Name for the folder this configurable object resides in, will always be
	 * preceded by <code>ProjectKorra/configuration</code>
	 * 
	 * @return the folder name
	 */
	public String getFolderName();

	/**
	 * Method called after being processed by {@link Config#process(Configurable)}
	 */
	public void postProcessed();
}
