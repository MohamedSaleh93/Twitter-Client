package com.twitter.client.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.twitter.client.R;
import com.twitter.client.utilities.Statics;
import com.twitter.client.sharedpref.SharedPreferenceManager;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * @author MohamedSaleh on 6/24/2017.
 */

public class SplashScreen extends Activity {

    private TwitterLoginButton twitterLoginButton;
    private SharedPreferenceManager sharedPreferenceManager;
    private final static int ALPHA_ANIMATION_DURATION = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferenceManager = SharedPreferenceManager.getInstance();
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitterLoginButton);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterAuthConfig authConfig = TwitterCore.getInstance().getAuthConfig();
                TwitterAuthToken authToken = result.data.getAuthToken();
                sharedPreferenceManager.putString(Statics.AUTH_TOKEN_KEY, authToken.token);
                sharedPreferenceManager.putString(Statics.AUTH_SECRET_KEY, authToken.secret);
                sharedPreferenceManager.putString(Statics.USER_NAME_KEY, result.data.getUserName());
                sharedPreferenceManager.putLong(Statics.USER_ID_KEY, result.data.getUserId());
                sharedPreferenceManager.putBoolean(Statics.USER_LOGGED_IN_KEY, true);

                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(SplashScreen.this, getString(R.string.login_failed) , Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.splashLogo).animate().alpha(1f).setDuration(ALPHA_ANIMATION_DURATION)
                .setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (sharedPreferenceManager.getBoolean(Statics.USER_LOGGED_IN_KEY, false)) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    twitterLoginButton.animate().alpha(1f).setDuration(ALPHA_ANIMATION_DURATION).start();
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
