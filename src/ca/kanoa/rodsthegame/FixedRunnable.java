package ca.kanoa.rodsthegame;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class FixedRunnable extends BukkitRunnable {

	public boolean canceled = false;
	
	/**
	 * Cancel this task so it will not run
	 */
	@Override
	public void cancel() {
		this.canceled = true;
	}
	
	/**
	 * Check to see weather this task is cancelled
	 * @return If this task is cancelled
	 */
	public boolean isCanceled() {
		return this.canceled;
	}
	
	/**
	 * Code to be ran when task is called
	 */
	public abstract void loop();
	
	/**
	 * DO NOT OVERRIDE
	 */
	@Override
	public void run() {
		if (!canceled)
			this.loop();
	}
	
}
