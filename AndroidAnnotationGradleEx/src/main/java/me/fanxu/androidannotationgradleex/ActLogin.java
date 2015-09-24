package me.fanxu.androidannotationgradleex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.kakao.SessionCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Noroo (2015-09-11 22:13:28):
 */
@EActivity(R.layout.activity_login)
public class ActLogin extends Activity {

    @App
    AA_Application application;

    private SessionCallback callback;

    @ViewById
    LoginButton login_button;

    private CallbackManager callbackManager;

    @AfterViews
    void afterViews() {

        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(ActLogin.this, "페이스북 로그인 성공", LENGTH_SHORT).show();
//                startActivity(new Intent(HelloAndroidActivity.this, MainActivity_.class));
//                finish();
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException e) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);

        callbackManager = CallbackManager.Factory.create();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
