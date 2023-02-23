package com.onesignal.flutter;

import android.util.Log;

import com.onesignal.user.subscriptions.ISubscription;
import com.onesignal.user.subscriptions.IPushSubscription;

import com.onesignal.inAppMessages.IInAppMessage;
import com.onesignal.inAppMessages.IInAppMessageClickResult;

import com.onesignal.notifications.INotification;
 import com.onesignal.notifications.INotificationClickResult;
 import com.onesignal.notifications.INotificationReceivedEvent;
 import com.onesignal.notifications.INotificationReceivedEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

class OneSignalSerializer {

    static HashMap<String, Object> convertNotificationToMap(INotification notification) throws JSONException {
        HashMap<String, Object> hash = new HashMap<>();

        
        hash.put("androidNotificationId", notification.getAndroidNotificationId());

        if (notification.getGroupedNotifications() != null) {
            hash.put("groupKey", notification.getGroupKey());
            hash.put("groupMessage", notification.getGroupMessage());
            hash.put("groupedNotifications", notification.getGroupedNotifications());
        }


        hash.put("notificationId", notification.getNotificationId());
        hash.put("title", notification.getTitle());

        if (notification.getBody() != null)
            hash.put("body", notification.getBody());
        if (notification.getSmallIcon() != null)
            hash.put("smallIcon", notification.getSmallIcon());
        if (notification.getLargeIcon() != null)
            hash.put("largeIcon", notification.getLargeIcon());
        if (notification.getBigPicture() != null)
            hash.put("bigPicture", notification.getBigPicture());
        if (notification.getSmallIconAccentColor() != null)
            hash.put("smallIconAccentColor", notification.getSmallIconAccentColor());
        if (notification.getLaunchURL() != null)
            hash.put("launchUrl", notification.getLaunchURL());
        if (notification.getSound() != null)
            hash.put("sound", notification.getSound());
        if (notification.getLedColor() != null)
            hash.put("ledColor", notification.getLedColor());
        hash.put("lockScreenVisibility", notification.getLockScreenVisibility());
        if (notification.getGroupKey() != null)
            hash.put("groupKey", notification.getGroupKey());
        if (notification.getGroupMessage() != null)
            hash.put("groupMessage", notification.getGroupMessage());
        if (notification.getFromProjectNumber() != null)
            hash.put("fromProjectNumber", notification.getFromProjectNumber());
        if (notification.getCollapseId() != null)
            hash.put("collapseId", notification.getCollapseId());
        hash.put("priority", notification.getPriority());
        if (notification.getAdditionalData() != null && notification.getAdditionalData().length() > 0)
            hash.put("additionalData", convertJSONObjectToHashMap(notification.getAdditionalData()));
        if (notification.getActionButtons() != null) {
            hash.put("actionButtons", notification.getActionButtons());
        }
        // TODO : rawPayload
        // hash.put("rawPayload", notification.getRawPayload());
        return hash;
    }

    static HashMap<String, Object> convertNotificationClickedResultToMap(INotificationClickResult openResult) throws JSONException {
        HashMap<String, Object> hash = new HashMap<>();

        hash.put("actionId", openResult.getAction().getActionId());
        hash.put("type", openResult.getAction().getType());
        hash.put("title", openResult.getNotification().getTitle());
        hash.put("message", openResult.getNotification().getBody());
        hash.put("additionalData", openResult.getNotification().getAdditionalData());
        return hash;
    }

    static HashMap<String, Object> convertPermissionChanged(boolean state) {
        HashMap<String, Object> permission = new HashMap<>();

        permission.put("areNotificationsEnabled", state);

        return permission;
    }


    static HashMap<String, Object> convertInAppMessageClickedActionToMap(IInAppMessageClickResult result) {
        HashMap<String, Object> hash = new HashMap<>();

        hash.put("click_name", result.getAction().getClickName());
        hash.put("click_url", result.getAction().getClickUrl());
        hash.put("first_click", result.getAction().isFirstClick());
        hash.put("closes_message", result.getAction().getClosesMessage());

        return hash;
    }

    static HashMap<String, Object> convertInAppMessageToMap(IInAppMessage message) {
        HashMap<String, Object> hash = new HashMap<>();

        hash.put("message_id", message.getMessageId());

        return hash;
    }

    static HashMap<String, Object> convertOnSubscriptionChanged(IPushSubscription state) {
        HashMap<String, Object> hash = new HashMap<>();
        

        hash.put("token", state.getToken());
        hash.put("pushId", state.getId());
        hash.put("optedIn", state.getOptedIn());

        return hash;
    }


    static HashMap<String, Object> convertJSONObjectToHashMap(JSONObject object) throws JSONException {
        HashMap<String, Object> hash = new HashMap<>();

        if (object == null || object == JSONObject.NULL)
           return hash;

        Iterator<String> keys = object.keys();

        while (keys.hasNext()) {
            String key = keys.next();

            if (object.isNull(key))
                continue;

            Object val = object.get(key);

            if (val instanceof JSONArray) {
                val = convertJSONArrayToList((JSONArray)val);
            } else if (val instanceof JSONObject) {
                val = convertJSONObjectToHashMap((JSONObject)val);
            }

            hash.put(key, val);
        }

        return hash;
    }

    private static List<Object> convertJSONArrayToList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            Object val = array.get(i);

            if (val instanceof JSONArray)
                val = OneSignalSerializer.convertJSONArrayToList((JSONArray)val);
            else if (val instanceof JSONObject)
                val = convertJSONObjectToHashMap((JSONObject)val);

            list.add(val);
        }

        return list;
    }
}
