package game.asteroids.utility;

import java.util.ArrayList;

public class Timer {
	private static final ArrayList<Timer> timers = new ArrayList<>();

	private float startDuration;
	private float duration;
	private Runnable callBack;

	public static Timer startNew(float duration, Runnable callBack){
		Timer timer = new Timer(duration, callBack);
		timer.startDuration = duration;
		timers.add(timer);
		return timer;
	}

	public void clear(){
		timers.remove(this);
	}

	public float progress(){
		return duration/startDuration;
	}

	public boolean isRunning(){
		return timers.contains(this);
	}

	private Timer(float duration, Runnable callBack){
		this.duration = duration;
		this.callBack = callBack;
	}

	private void update(float delta){
		duration -= delta;
		if(duration <= 0){
			callBack.run();
			timers.remove(this);
		}
	}

	public static void updateTimers(float delta){
		for(int i = 0; i < timers.size(); i++){
			timers.get(i).update(delta);
		}
	}
	
	public static void clearAll() {
		timers.clear();
	}
}
