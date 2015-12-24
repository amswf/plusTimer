package com.google.example.gms.nativeexample;

import android.os.Message;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

/**
 * Created by amy on 15/12/23.
 */
public class PlusTimer {
    private Timer timer;
    private HashMap hashMap;
    private int index;
    private Boolean isStop = true;
    
    public void addTask(int delay,TimerTask callback){
        if(timer == null){
            hashMap = new HashMap();
            timer = new Timer(true);
        }
        HashMap delayMap = new HashMap();
        delayMap.put("index",0);
        delayMap.put("callback",callback);
        delayMap.put("delay",delay);
        delayMap.put("hasRun",false);
        index++;
        hashMap.put(index+"",delayMap);
        if(isStop){
            timer.schedule(task,0,100);
            isStop = false;
        }
    }

    TimerTask task = new TimerTask() {
        public void run () {
            PlusTimer.this.update();
        }
    };

    private void update(){
        Iterator iter = hashMap.entrySet().iterator();
        if(!iter.hasNext()){
            isStop = true;
            timer.cancel();
        }
        while (iter.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            HashMap delayMap = (HashMap)entry.getValue();
            int index = (int)delayMap.get("index");
            index+=100;
            delayMap.put("index",index);
            int delay = (int)delayMap.get("delay");
            Boolean hasRun = (Boolean)delayMap.get("hasRun");
            if(index >= delay && hasRun == false) {
                delayMap.put("hasRun",true);
                TimerTask task= (TimerTask)delayMap.get("callback");
                stopTask(task);
                task.run();
            }
        }
    }

    public void stopTask(TimerTask callback){
        Iterator iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
            HashMap.Entry entry = (HashMap.Entry) iter.next();
            String key = (String)entry.getKey();
            HashMap delayMap = (HashMap)entry.getValue();
            TimerTask task = (TimerTask)delayMap.get("callback");
            if(callback == task){
                hashMap.remove(key);
            }
        }
    }

    public void stopAll(){
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
    }
}
