package com.klip.android.broadcastbestpractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login_button);
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
//                }
            }
        });
    }
}
