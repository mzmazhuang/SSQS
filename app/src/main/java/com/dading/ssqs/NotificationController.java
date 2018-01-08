package com.dading.ssqs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.SparseArray;

import com.dading.ssqs.utils.Logger;

import java.util.ArrayList;

/**
 * @author mazhuang
 *         封装的广播
 *         2017-12-09
 */
public class NotificationController {
    private static int totalEvents = 1;

    public static final int scroll_mask = totalEvents++;
    public static final int today_mask = totalEvents++;
    public static final int early_mask = totalEvents++;
    public static final int footBallFilter = totalEvents++;
    public static final int basketBallFilter = totalEvents++;

    private static final String BROADCAST_NOTIFICATION = "broadcast_notification";
    private static final String BROADCAST_NOTIFICATION_ID = "id";
    private static final String BROADCAST_NOTIFICATION_ARGS = "args";


    private final SparseArray<ArrayList<Object>> observers = new SparseArray<>();
    private final SparseArray<ArrayList<Object>> removeAfterBroadcast = new SparseArray<>();
    private final SparseArray<ArrayList<Object>> addAfterBroadcast = new SparseArray<>();
    private final ArrayList<DelayedPost> delayedPosts = new ArrayList<>(10);

    private int broadcasting = 0;
    private boolean animationInProgress;
    private LocalBroadcastManager broadcastManager;
    private BroadcastReceiver broadcastReceiver;

    private int[] allowedNotifications;

    public interface NotificationControllerDelegate {
        void didReceivedNotification(int id, String... args);
    }

    private class DelayedPost {

        private DelayedPost(int id, String[] args) {
            this.id = id;
            this.args = args;
        }

        private final int id;
        private final String[] args;
    }

    private static volatile NotificationController Instance = null;


    public void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(SSQSApplication.getContext());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_NOTIFICATION);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(BROADCAST_NOTIFICATION)) {
                    int id = intent.getIntExtra(BROADCAST_NOTIFICATION_ID, 0);
                    if (id > 0) {
                        String[] args = intent.getStringArrayExtra(BROADCAST_NOTIFICATION_ARGS);
                        NotificationController.getInstance().postNotification(id, args);
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }


    public static NotificationController getInstance() {
        NotificationController localInstance = Instance;
        if (localInstance == null) {
            synchronized (NotificationController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new NotificationController();
                }
            }
        }
        return localInstance;
    }

    private NotificationController() {
        broadcastManager = LocalBroadcastManager.getInstance(SSQSApplication.getContext());
    }

    public void setAllowedNotificationsDutingAnimation(int notifications[]) {
        allowedNotifications = notifications;
    }

    public void setAnimationInProgress(boolean value) {
        animationInProgress = value;
        if (!animationInProgress && !delayedPosts.isEmpty()) {
            for (DelayedPost delayedPost : delayedPosts) {
                postNotificationInternal(delayedPost.id, true, delayedPost.args);
            }
            delayedPosts.clear();
        }
    }

    public boolean isAnimationInProgress() {
        return animationInProgress;
    }

    public void postNotification(int id, String... args) {
        if (Thread.currentThread() == SSQSApplication.getHandler().getLooper().getThread()) {
            boolean allowDuringAnimation = false;
            if (allowedNotifications != null) {
                for (int allowedNotification : allowedNotifications) {
                    if (allowedNotification == id) {
                        allowDuringAnimation = true;
                        break;
                    }
                }
            }
            postNotificationInternal(id, allowDuringAnimation, args);
        } else {
            Intent intent = new Intent(BROADCAST_NOTIFICATION);
            intent.putExtra(BROADCAST_NOTIFICATION_ID, id);
            intent.putExtra(BROADCAST_NOTIFICATION_ARGS, args);
            broadcastManager.sendBroadcast(intent);
        }
    }

    private void postNotificationInternal(int id, boolean allowDuringAnimation, String... args) {
        if (Thread.currentThread() != SSQSApplication.getHandler().getLooper().getThread()) {
            throw new RuntimeException("postNotification allowed only from MAIN thread");
        }

        if (!allowDuringAnimation && animationInProgress) {
            DelayedPost delayedPost = new DelayedPost(id, args);
            delayedPosts.add(delayedPost);
            Logger.INSTANCE.e("Notification", "delay post notification " + id + " with args count = " + args.length);
            return;
        }
        broadcasting++;
        ArrayList<Object> objects = observers.get(id);
        if (objects != null && !objects.isEmpty()) {
            for (int a = 0; a < objects.size(); a++) {
                Object obj = objects.get(a);
                ((NotificationControllerDelegate) obj).didReceivedNotification(id, args);
            }
        }
        broadcasting--;
        if (broadcasting == 0) {
            if (removeAfterBroadcast.size() != 0) {
                for (int a = 0; a < removeAfterBroadcast.size(); a++) {
                    int key = removeAfterBroadcast.keyAt(a);
                    ArrayList<Object> arrayList = removeAfterBroadcast.get(key);
                    for (int b = 0; b < arrayList.size(); b++) {
                        removeObserver(arrayList.get(b), key);
                    }
                }
                removeAfterBroadcast.clear();
            }
            if (addAfterBroadcast.size() != 0) {
                for (int a = 0; a < addAfterBroadcast.size(); a++) {
                    int key = addAfterBroadcast.keyAt(a);
                    ArrayList<Object> arrayList = addAfterBroadcast.get(key);
                    for (int b = 0; b < arrayList.size(); b++) {
                        addObserver(arrayList.get(b), key);
                    }
                }
                addAfterBroadcast.clear();
            }
        }
    }

    public void addObserver(Object observer, int id) {
        if (Thread.currentThread() != SSQSApplication.getHandler().getLooper().getThread()) {
            throw new RuntimeException("addObserver allowed only from MAIN thread");
        }
        if (broadcasting != 0) {
            ArrayList<Object> arrayList = addAfterBroadcast.get(id);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                addAfterBroadcast.put(id, arrayList);
            }
            arrayList.add(observer);
            return;
        }
        ArrayList<Object> objects = observers.get(id);
        if (objects == null) {
            observers.put(id, (objects = new ArrayList<>()));
        }
        if (objects.contains(observer)) {
            return;
        }
        objects.add(observer);
    }

    public void removeObserver(Object observer, int id) {
        if (Thread.currentThread() != SSQSApplication.getHandler().getLooper().getThread()) {
            throw new RuntimeException("removeObserver allowed only from MAIN thread");
        }
        if (broadcasting != 0) {
            ArrayList<Object> arrayList = removeAfterBroadcast.get(id);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                removeAfterBroadcast.put(id, arrayList);
            }
            arrayList.add(observer);
            return;
        }
        ArrayList<Object> objects = observers.get(id);
        if (objects != null) {
            objects.remove(observer);
        }
    }

    public void removeObserver(int id) {
        if (Thread.currentThread() != SSQSApplication.getHandler().getLooper().getThread()) {
            throw new RuntimeException("removeObserver allowed only from MAIN thread");
        }
        if (broadcasting != 0) {
            ArrayList<Object> arrayList = removeAfterBroadcast.get(id);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                removeAfterBroadcast.put(id, arrayList);
            }
            arrayList.clear();
            return;
        }
        ArrayList<Object> objects = observers.get(id);
        if (objects != null) {
            objects.clear();
        }
    }

}
