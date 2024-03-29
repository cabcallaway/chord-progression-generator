package com.example.cpg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

//import com.example.cpg.sql.DatabaseHelper;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.cpg.Connectors.UserService;
import com.example.cpg.dao.UserDao;
import com.example.cpg.model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.room.Room;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cpg.helpers.InputValidation;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;
    private static final String SCOPES = "user-read-recently-played,user-library-modify,user-read-email,user-read-private";
    private AppDatabase database;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private InputValidation inputValidation;
    //private DatabaseHelper databaseHelper;

    Bundle extras = new Bundle();

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: DELETE AFTER DEBUGGING
        //Intent main = new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(main);

        // Set the app's color theme to night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
        msharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(this);
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        //nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        database = AppDatabase.getInstance(getApplicationContext());
        userDao = database.getUserDao();
        inputValidation = new InputValidation(activity);
    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.spotifybutton:
                AuthenticationRequest.Builder builder =
                        new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
                builder.setScopes(new String[]{SCOPES});
                AuthenticationRequest request = builder.build();

                AuthenticationClient.openLoginActivity(LoginActivity.this, REQUEST_CODE, request);
        }
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (userDao.checkUser(textInputEditTextEmail.getText().toString().trim()) == 1) {

            //Get the hashed password stored in the database
            String hashedPass = userDao.getPasswordByEmail(textInputEditTextEmail.getText().toString().trim());

            //Compare to the given password provided by the user.
            if (BCrypt.checkpw(textInputEditTextPassword.getText().toString().trim(), hashedPass)) {
                Intent accountsIntent = new Intent(getApplicationContext(), MainActivity.class);
                extras.putString("email", textInputEditTextEmail.getText().toString().trim());
                accountsIntent.putExtras(extras);
                emptyInputEditText();
                startActivity(accountsIntent);

            } else {
                // Toast to show wrong password
                Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
            }

        } else {
            // Toast to show incorrect email
            Toast.makeText(getApplicationContext(), "No user is registered with that email.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
