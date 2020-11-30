/**
 * 
 */
package com.shtick.math.statistics;

/**
 * @author scox
 *
 */
public interface StatusTracker {

	/**
	 * Implemented by the entity that wants to track progress/status.
	 * Called by the process the is reporting progress/status.
	 * 
	 * @param currentTask
	 * @param taskProgress
	 * @param overallProgress
	 */
	void updateStatus(String currentTask, double taskProgress, double overallProgress);
}
