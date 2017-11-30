package com.klip.android.broadcastbestpractice;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    public static final String LOGIN_INFO = "login_info";
    public static final String IS_REMENBER = "isRemember";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";

    private SharedPreferences sharedPreferences;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button loginButton;
    private CheckBox checkBox;
    private Boolean isRemember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(LOGIN_INFO, MODE_PRIVATE);
        isRemember = sharedPreferences.getBoolean(IS_REMENBER, false);
        String account = sharedPreferences.getString(ACCOUNT, "");
        String password = sharedPreferences.getString(PASSWORD, "");
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        loginButton = (Button) findViewById(R.id.login_button);
        if (isRemember) {
            checkBox.setChecked(true);
            accountEdit.setText(account);
            passwordEdit.setText(password);
        }
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 什么也不输入时，accountEdit.getText()也不是null
                // accountEdit.getText().toString()的值是""
                // 所以不需要判断为空的判读
//                if (accountEdit.getText() != null && passwordEdit.getText() != null) {
                // .trim()去掉空格和回车
                String account = accountEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                if ("".equals(account)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.account_is_empty), Toast.LENGTH_LONG).show();
                } else if ("".equals(password)) {
                    Toast.makeText(LoginActivity.this, getString(R.string.password_is_empty), Toast.LENGTH_LONG).show();
                } else {
                    // 判断输入数据和从数据库里拉回的数据是否匹配
                    if ("admin".equals(account) && "admin".equals(password)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "account or password is wrong", Toast.LENGTH_LONG).show();
                    }
                }
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (checkBox.isChecked()) {
                    editor.putBoolean(IS_REMENBER, true);
                    editor.putString(ACCOUNT, account);
                    editor.putString(PASSWORD, password);
                } else {
                    editor.putBoolean(IS_REMENBER, false);
                    editor.putString(ACCOUNT, "");
                    editor.putString(PASSWORD, "");
                }
                editor.apply();
//                }
            }
        });
    }
}
