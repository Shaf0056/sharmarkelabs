package com.example.sharmarkelabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity {

    /**
     * This holds the text at the center of the screen
     */
    private TextView passtext = null;
    /**
     * This field takes the password input
     */
    private EditText pass = null;
    /**
     * This button lets the user log in and checks the password
     */
    private Button loginbtn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView passtext = findViewById(R.id.textView);
        EditText pass = findViewById(R.id.editTextTextPassword);
        Button loginbtn = findViewById(R.id.login);

        loginbtn.setOnClickListener(clk->{
            String password = pass.getText().toString();
            checkPasswordComplexity(password);
            boolean requirementMet = checkPasswordComplexity(password);
            if(requirementMet){
                passtext.setText("Your password meets the requirements");
            } else {
                passtext.setText("You shall not pass!");
            }
        });


    }

    /**
     * this function checks if the password entered matches the requirements such as :
     * containing an upper case letter, symbol etc...
     * @param password The string object we are checking
     * @return Returns true if the password is complex enough.
     */
    boolean checkPasswordComplexity(String password) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                foundUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                foundLowerCase = true;
            } else if (Character.isDigit(c)) {
                foundNumber = true;
            } else if (isSpecialCharacter(c)) {
                foundSpecial = true;
            }
        }

        if (!foundUpperCase) {

            Toast.makeText(getApplicationContext(), "your password is missing an upper case letter", Toast.LENGTH_SHORT).show();

            return false;

        } else if (!foundLowerCase) {
            Toast.makeText(getApplicationContext(), "missing a lower case number", Toast.LENGTH_SHORT).show();

            return false;

        } else if (!foundNumber) {
            Toast.makeText(getApplicationContext(), "missing a number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!foundSpecial) {
            Toast.makeText(getApplicationContext(), "missing special characters", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true; //only get here if they're all true

    }

    /**
     * this function checks if any of these special characters are used in the password.
     * @param c the parameter C is the
     * @return true if it matches one of the special characters and false if it doesnt
     */
    boolean isSpecialCharacter(char c)

    {
        switch (c){
            case '#':
            case '?':
            case '*':
            case '@':
            case '%':
            case '^':
            case '!':
            case '&':
                return true;
            default:
                return false;
        }


    }}
