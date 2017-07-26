package miaoyipu.chatwithyuki;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import miaoyipu.chatwithyuki.Models.ChatMessage;
import miaoyipu.chatwithyuki.Models.User;

import static miaoyipu.chatwithyuki.Utility.YUKIUID;
import static miaoyipu.chatwithyuki.Utility.dbSetUser;
import static miaoyipu.chatwithyuki.Utility.postNewMessage;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAIN";
    private static final int SIGN_IN_REQUEST_CODE = 1;
    private static final java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yy (HH:mm:ss)");
    private FirebaseListAdapter<ChatMessage> adapter;
    private DatabaseReference database;
    private FirebaseUser currentUser;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this.getApplicationContext();
        checkUserState();
        database= FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setFab();
        displayChatMessages();
    }

    private void setFab() {
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.main_sendFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText)findViewById(R.id.main_textInput);
                RequestQueueSingleton.postNewMessage(context, database, input.getText().toString(), currentUser); // Post message to chat bot.
                postNewMessage(database, currentUser, input.getText().toString(), getString(R.string.yukiUid)); // Post message to database.

                input.setText("");
            }
        });
    }

    /* Check whether user is already signed in. */
    private void checkUserState() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null ) {
            // Start sign in/up activity
            startActivityForResult(
                    AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE
            );
        } else {
            // User already signed in
            Toast.makeText(this,
                    R.string.welcomeMessage +
                            FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getDisplayName()
                    + R.string.welcomePostFix,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    /* Once the user has signed in, MainActivity will receive a result in the form of an Intent.
        If the result's code is RESULT_OK, it means sign in successful.
        call finish() otherwise to close the app.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (requestCode == RESULT_OK) {
                Toast.makeText(this, R.string.signInSuccess, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.signInFail, Toast.LENGTH_LONG).show();
                finish(); // close the app
            }
        }

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        addNewUser();
//        displayChatMessages();
    }

    /**
     * Add the current user to the database as a User object under users
     * if not exist already.
     */
    private void addNewUser() {
        User newUser = new User(currentUser.getUid(), currentUser.getDisplayName());
        dbSetUser(database, newUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "You have signed out.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
        }
        return true;
    }

    private void displayChatMessages() {
        ListView msgList = (ListView)findViewById(R.id.main_msgList);

        DatabaseReference dbref = database.child("users")
                                            .child(currentUser.getUid())
                                            .child("chats")
                                            .child(YUKIUID);

        Log.d(TAG, dbref.toString());
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.messages, dbref) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView msgText = (TextView)v.findViewById(R.id.messages_msgContent);
                TextView msgUser = (TextView)v.findViewById(R.id.messages_userId);
                TextView msgTime = (TextView)v.findViewById(R.id.messages_timeStamp);

                msgText.setText(model.getMsgText());
                msgUser.setText(model.getAuthorName());
                Date date = new Date(model.getMsgTime());
                msgTime.setText(dateFormat.format(date));
            }
        };
        msgList.setAdapter(adapter);
    }
}
