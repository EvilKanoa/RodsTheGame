package ca.kanoa.rodsthegame;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class FixedRunnable extends BukkitRunnable {

	public boolean canceled = false;
	
	@Override
	public void cancel() {
		this.canceled = true;
	}
	
	public boolean isCanceled() {
		return this.canceled;
	}
	
	public abstract void loop();
	
	@Override
	public void run() {
		if (!canceled)
			this.loop();
	}
	
}
