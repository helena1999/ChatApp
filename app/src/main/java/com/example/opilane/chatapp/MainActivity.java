package com.example.opilane.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    FloatingActionButton fab;
    RelativeLayout activity_main;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_main, "You have been signed out.", Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE){
            if (requestCode == RESULT_OK) {
                Snackbar.make(activity_main, "Successfully signed in. Welcome!", Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else {
                Snackbar.make(activity_main, "We could not sign you in. Please try again later.", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = (EditText) findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        }
        else {
            Snackbar.make(activity_main, "Welcome" + FirebaseAuth.getInstance().getCurrentUser(), Snackbar.LENGTH_SHORT).show();
            //kui sisse logitud kuvame sõnumeid
            displayChatMessage();
        }
    }
    private void displayChatMessage() {
        ListView listOfMessage = (ListView)findViewById(R.id.list_of_message);
        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
              //saame viited oma nimekirja vaadetele
                TextView messageText, messageUser, messageTime;
                messageText = (TextView)v.findViewById(R.id.message_text);
                messageTime = (TextView)v.findViewById(R.id.message_time);
                messageUser = (TextView)v.findViewById(R.id.message_user);

                messageText.setText(model.getMessageText());
                messageTime.setText(android.text.format.DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
                messageUser.setText(model.getMessageText());


            }
        };

        listOfMessage.setAdapter(adapter);
    }
}
