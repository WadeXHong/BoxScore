package com.wadexhong.boxscore.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.wadexhong.boxscore.BoxScore;
import com.wadexhong.boxscore.BoxScoreContract;
import com.wadexhong.boxscore.BoxScorePresenter;
import com.wadexhong.boxscore.R;
import com.wadexhong.boxscore.dialog.ProgressBarDialog;
import com.wadexhong.boxscore.gameboxscore.GameBoxScoreActivity;
import com.wadexhong.boxscore.gamehistory.GameHistoryActivity;
import com.wadexhong.boxscore.objects.GameInfo;
import com.wadexhong.boxscore.setting.SettingActivity;
import com.wadexhong.boxscore.startgame.StartGameActivity;
import com.wadexhong.boxscore.teammanage.TeamManageActivity;

public class BoxScoreActivity extends AppCompatActivity implements BoxScoreContract.View {
    
    private static final String  TAG = BoxScoreActivity.class.getSimpleName();

    public static final int REQUEST_CODE_SETTING = 0;

    private Context mContext;

    private BoxScoreContract.Presenter mPresenter;

    private ConstraintLayout mMainLayout;
    private LinearLayout mStartGameLayout;
    private LinearLayout mTeamManageLayout;
    private LinearLayout mGameHistoryLayout;
    private LinearLayout mSettingLayout;

    private LinearLayout mUserNameLayout;
    private LinearLayout mPassWordLayout;
    private LinearLayout mLogInLayout;
    private LinearLayout mSignUpLayout;

    private EditText mUserNameEditText;
    private EditText mPassWordEditText;

    private ImageView mLogo;

    private boolean mIsLogIn = false; //TODO
    private boolean mIsUserNameLegal = false;
    private boolean mIsPassWordLegal = false;


    @Override
    protected void onResume() {
        super.onResume();
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.screenBrightness = BoxScore.sBrightness;
            getWindow().setAttributes(layoutParams);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            showMainUi(View.GONE, View.VISIBLE);
        }else {
            showMainUi(View.VISIBLE, View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SETTING)
            mPassWordEditText.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProgressBarDialog.setNull();
    }


    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_score);
        mContext = this;
        FirebaseAuth.getInstance();
        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(750);
        getWindow().setSharedElementEnterTransition(bounds);

//        ChangeBounds mBounds = new ChangeBounds();
//        mBounds.setDuration(500);
//        getWindow().setSharedElementReturnTransition(null);


        mMainLayout = findViewById(R.id.activity_boxscore_main_layout);
        mStartGameLayout = findViewById(R.id.activity_boxscore_startgame_layout);
        mTeamManageLayout = findViewById(R.id.activity_boxscore_teammanage_layout);
        mGameHistoryLayout = findViewById(R.id.activity_boxscore_gamehistory_layout);
        mSettingLayout = findViewById(R.id.activity_boxscore_setting_layout);
        mUserNameLayout = findViewById(R.id.activity_boxscore_username_layout);
        mPassWordLayout = findViewById(R.id.activity_boxscore_password_layout);
        mLogInLayout = findViewById(R.id.activity_boxscore_login_layout);
        mSignUpLayout = findViewById(R.id.activity_boxscore_signup_layout);
        mUserNameEditText = findViewById(R.id.activity_boxscore_username_edittext);
        mPassWordEditText = findViewById(R.id.activity_boxscore_password_edittext);
        mLogo = findViewById(R.id.activity_boxscore_logo_imageview);

//        if (mIsLogIn){ //TODO  token判斷
//            showUi(View.GONE, View.VISIBLE);
//        }else {
//            showUi(View.VISIBLE, View.GONE);
//        }

        mStartGameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"StartGame pressed");
                mPresenter.pressStartGame();
            }
        });
        mTeamManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"TeamManage pressed");
                startActivity(new Intent(BoxScoreActivity.this, TeamManageActivity.class));
            }
        });
        mGameHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"GmaeHistory pressed");
                startActivity(new Intent(BoxScoreActivity.this, GameHistoryActivity.class));
            }
        });
        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Setting pressed");
                startActivityForResult(new Intent(BoxScoreActivity.this, SettingActivity.class), REQUEST_CODE_SETTING);
            }
        });
        mLogInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgressBarDialog("登入中...");

                String userName = mUserNameEditText.getText().toString();
                String passWord = mPassWordEditText.getText().toString();

                if (!userName.trim().equals("") && !passWord.trim().equals("")) {

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(mUserNameEditText.getText().toString(), mPassWordEditText.getText().toString())
                              .addOnCompleteListener(BoxScoreActivity.this, new OnCompleteListener<AuthResult>() {
                                  @Override
                                  public void onComplete(@NonNull Task<AuthResult> task) {

                                      if (task.isSuccessful()) {
                                          showProgressBarDialog("資料同步中");
                                          mPresenter.updateDbFromFireBase();
                                          showToast("登入成功");
                                          showMainUi(View.GONE, View.VISIBLE);
                                      } else {
                                          try {
                                              throw task.getException();
                                          } catch(FirebaseAuthWeakPasswordException e) {

                                          } catch(FirebaseAuthInvalidCredentialsException e) {
                                              showToast("密碼錯誤");

                                          } catch(FirebaseAuthUserCollisionException e) {
                                              showToast("其他裝置帳號登入中");

                                          } catch(Exception e) {
                                              showToast("帳號不存在");
                                          }
                                          ProgressBarDialog.hideProgressBarDialog();
                                      }
                                  }
                              });
                }else {
                    showToast("輸入不符合規定");
                    ProgressBarDialog.hideProgressBarDialog();

                }


//                mPresenter.logInFireBase(mUserNameEditText.getText().toString(), mPassWordEditText.getText().toString());
            }
        });
        mSignUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsUserNameLegal && mIsPassWordLegal){
                    showProgressBarDialog("註冊中...");
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(mUserNameEditText.getText().toString(), mPassWordEditText.getText().toString())
                              .addOnCompleteListener(BoxScoreActivity.this, new OnCompleteListener<AuthResult>() {
                                  @Override
                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                      if (task.isSuccessful()) {
                                          mPassWordEditText.setText("");
                                          showMainUi(View.GONE, View.VISIBLE);
                                          showToast("成功");
                                      } else {
                                          try {
                                              throw task.getException();
                                          } catch(FirebaseAuthUserCollisionException e) {
                                              showToast("此帳號已註冊");

                                          } catch(Exception e) {
                                              showToast(e.toString());
                                          }
                                      }
                                      ProgressBarDialog.hideProgressBarDialog();
                                  }
                              });
                }else if (!mIsUserNameLegal){
                    showToast("帳號不符規定");
                }else {
                    showToast("密碼長度不得小於6位");
                }
            }
        });

        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0 && s.charAt(s.length()-1) == '\u0020') s.delete(s.length()-1, s.length());
                mIsUserNameLegal = Patterns.EMAIL_ADDRESS.matcher(s).matches();
            }
        });

        mPassWordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mIsPassWordLegal = s.length() >= 6;
            }
        });

        init();
    }


    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void init() {
        Log.i(TAG,"BoxScoreActivity.init");
        setStatusBar(this);
        mPresenter = new BoxScorePresenter(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.start();
                    }
                });
            }
        }).start();
    }

    private void setStatusBar(Activity activity) {

        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void setPresenter(BoxScoreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMainUi(int logInViewVisibility, int fuctionViewVisibility) {

        mUserNameLayout.setVisibility(logInViewVisibility);
        mPassWordLayout.setVisibility(logInViewVisibility);
        mLogInLayout.setVisibility(logInViewVisibility);
        mSignUpLayout.setVisibility(logInViewVisibility);


        mStartGameLayout.setVisibility(fuctionViewVisibility);
        mTeamManageLayout.setVisibility(fuctionViewVisibility);
        mGameHistoryLayout.setVisibility(fuctionViewVisibility);
        mSettingLayout.setVisibility(fuctionViewVisibility);
    }

    @Override
    public void askResumeGame(String opponentName) {
        new AlertDialog.Builder(this,R.style.OrangeDialog).setTitle("恢復比賽")
                  .setMessage("上次與\n"+opponentName+" 的比賽記錄尚未結束\n是否恢復比賽？\n\n警告：選擇\n「放棄並開新比賽」\n將刪除紀錄")
                  .setPositiveButton("恢復比賽", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          transToGameBoxScore();//TODO ??????????? How to do ?
                          Log.d(TAG,"pressed Resume");
                          dialog.dismiss();
                      }})
                  .setNegativeButton("結束並開新比賽", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          Log.d(TAG,"pressed End and start new");
                          mPresenter.saveAndEndCurrentGame();
                          transToStartGame();
                          dialog.dismiss();
                      }})
                  .setNeutralButton("放棄並開新比賽", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          Log.d(TAG,"pressed Discard and start new");
                          //ToDo callPresenter to delete previous gameData in DB
                          mPresenter.removeGameDataInDataBase();
                          mPresenter.removeGameDataSharedPreferences();
                          transToStartGame();
                          dialog.dismiss();
                      }}).show();

    }

    @Override
    public void transToStartGame() {
        startActivity(new Intent(mContext, StartGameActivity.class));
    }

    @Override
    public void transToGameBoxScore() {
        startActivity(new Intent(this, GameBoxScoreActivity.class).putExtra("GameInfo",new GameInfo()).putExtra("isResume",true));

    }

    @Override
    public void showProgressBarDialog(String message) {
        ProgressBarDialog.showProgressBarDialog(this, message);
    }

    @Override
    public void onBackPressed() {
        mLogo.setTransitionName(null);
        super.onBackPressed();
    }

}
