package miaoyipu.chatwithyuki;

import android.content.res.Resources;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import miaoyipu.chatwithyuki.Models.ChatMessage;
import miaoyipu.chatwithyuki.Models.User;

/**
 * Created by cy804 on 2017-07-26.
 */

public class Utility {
    private static final String TAG = "UTLI";
    public static final String YUKI = "小白白";
    public static final String YUKIUID = "000000";

    private Utility() {} // Prevent the class from being constructed.

    /**
     * Post message to the Firebase database as ChatMessage object.
     * @param database The database posting to
     * @param user User posting the message
     * @param message The message
     */
    public static void postNewMessage(DatabaseReference database, FirebaseUser user, String message, String targetUid) {
        ChatMessage chatMessage = new ChatMessage(
                message,
                user.getDisplayName(),
                user.getUid(),
                targetUid
        );

        dbSetMessage(database, chatMessage, user.getUid(), targetUid);
    }

    /**
     * Post message to Firebase database as ChatMessage object.
     * @param database
     * @param message
     * @param targetUid
     */
    public static void postBotMessage(DatabaseReference database, String message, String targetUid) {
        ChatMessage chatMessage = new ChatMessage(message,
                YUKI,
                YUKIUID,
                targetUid
        );

        dbSetMessage(database, chatMessage, YUKIUID, targetUid);
        dbSetMessage(database, chatMessage, targetUid, YUKIUID);
    }

    /**
     * Set message to the database.
     * @param database
     * @param chatMessage
     * @param uid the author uid
     * @param targetUid
     */
    public static void dbSetMessage(DatabaseReference database, ChatMessage chatMessage, String uid, String targetUid) {
        database.child("users")
                .child(uid)
                .child("chats")
                .child(targetUid)
                .push()
                .setValue(chatMessage);
    }

    public static void dbSetUser(DatabaseReference database, User user) {
        database.child("users")
                .child(user.getId())
                .setValue(user);
    }
}
