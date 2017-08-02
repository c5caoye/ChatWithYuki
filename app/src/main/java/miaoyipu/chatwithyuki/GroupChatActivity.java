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

import miaoyipu.chatwithyuki.Models.GroupMessage;

import static miaoyipu.chatwithyuki.Utility.postGroupMessage;
import static miaoyipu.chatwithyuki.Utility.postNewMessage;

public class GroupChatActivity extends AppCompatActivity {
    private static final String TAG = "GROUP";
    private FirebaseListAdapter<GroupMessage> adapter;
    private DatabaseReference database;
    private FirebaseUser currentUser;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        context = this.getApplicationContext();
        database = FirebaseDatabase.getInstance().getReference();
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
                postGroupMessage(database, currentUser, input.getText().toString());
                input.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(GroupChatActivity.this, "You have signed out.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
        }
        return true;
    }

    private void displayChatMessages() {
        ListView msgList = (ListView) findViewById(R.id.main_msgList);
        DatabaseReference dbref = database.child("groupMessages");

        adapter = new FirebaseListAdapter<GroupMessage>(this, GroupMessage.class, R.layout.messages, dbref.limitToLast(100)) {
            @Override
            protected void populateView(View v, GroupMessage model, int position) {
                TextView msgText = (TextView)v.findViewById(R.id.messages_msgContent);
                TextView msgUser = (TextView)v.findViewById(R.id.messages_userId);
                TextView msgTime = (TextView)v.findViewById(R.id.messages_timeStamp);

                msgText.setText(model.getMsgText());
                msgUser.setText(model.getAuthorName());
                Date date = new Date(model.getMsgTime());
                msgTime.setText(Utility.dateFormat.format(date));
            }
        };
        msgList.setAdapter(adapter);
    }

}
