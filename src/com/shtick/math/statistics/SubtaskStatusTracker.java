/**
 * 
 */
package com.shtick.math.statistics;

/**
 * @author scox
 *
 */
public class SubtaskStatusTracker implements StatusTracker{
	private StatusTracker parentTracker;
	private String parentTask;
	private double parentOverallProgress;

	/**
	 * @param parentTracker
	 * @param parentTask
	 * @param parentOverallProgress
	 */
	public SubtaskStatusTracker(StatusTracker parentTracker, String parentTask, double parentOverallProgress) {
		super();
		this.parentTracker = parentTracker;
		this.parentTask = parentTask;
		this.parentOverallProgress = parentOverallProgress;
		parentTracker.updateStatus(parentTask, 0, parentOverallProgress);
	}

	public void updateStatus(String currentTask, double taskProgress, double overallProgress) {
		parentTracker.updateStatus(parentTask+": "+currentTask, overallProgress, parentOverallProgress);
	}
}
