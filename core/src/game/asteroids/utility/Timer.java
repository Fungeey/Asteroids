package game.asteroids.utility;

import java.util.ArrayList;

public class Timer {
	private static final ArrayList<Timer> timers = new ArrayList<Timer>();

	private float duration;
	private Runnable callBack;

	public static Timer startNew(float duration, Runnable callBack){
		Timer timer = new Timer(duration, callBack);
		timers.add(timer);
		return timer;
	}

	public void clear(){
		timers.remove(this);
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
		System.out.println(timers.size());
		for(int i = 0; i < timers.size(); i++){
			timers.get(i).update(delta);
		}
	}
}
