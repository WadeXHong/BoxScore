package com.bardxhong.boxscore.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bardxhong.boxscore.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.bardxhong.boxscore.BoxScore;
import com.bardxhong.boxscore.BoxScoreContract;
import com.bardxhong.boxscore.BoxScorePresenter;
import com.bardxhong.boxscore.Constants;
import com.bardxhong.boxscore.R;
import com.bardxhong.boxscore.dialog.ProgressBarDialog;
import com.bardxhong.boxscore.gameboxscore.GameBoxScoreActivity;
import com.bardxhong.boxscore.gamehistory.GameHistoryActivity;
import com.bardxhong.boxscore.objects.GameInfo;
import com.bardxhong.boxscore.setting.SettingActivity;
import com.bardxhong.boxscore.startgame.StartGameActivity;
import com.bardxhong.boxscore.teammanage.TeamManageActivity;

public class BoxScoreActivity extends AppCompatActivity implements BoxScoreContract.View {

    private final String TAG = BoxScoreActivity.class.getSimpleName();

    public final int REQUEST_CODE_SETTING = 0;
    public final int REQUEST_CODE_INAPPUPDATE = 87;

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

    private TextView mVersionText;

    private boolean mIsUserNameLegal = false;
    private boolean mIsPassWordLegal = false;
    private boolean isTestDialogShow = false;

    /**
     * 可從 api 或 remoteConfig 之類的東西動態決定
     */
    private int mockServerResponseAppUpdateType = AppUpdateType.FLEXIBLE;

    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState installState) {
            switch (installState.installStatus()) {
                case InstallStatus.DOWNLOADING:
                    Log.i(TAG, "[onStateUpdate] DOWNLOADING");
                    Toast.makeText(BoxScoreActivity.this, "DOWNLOADING", Toast.LENGTH_SHORT).show();
                    break;
                case InstallStatus.DOWNLOADED:
                    Log.i(TAG, "[onStateUpdate] DOWNLOADED");
                    Toast.makeText(BoxScoreActivity.this, "DOWNLOADED", Toast.LENGTH_SHORT).show();

                    popupSnackBarForCompleteUpdate();

                    break;
                case InstallStatus.PENDING:
                    Log.i(TAG, "[onStateUpdate] PENDING");
                    Toast.makeText(BoxScoreActivity.this, "PENDING", Toast.LENGTH_SHORT).show();
                    break;
                case InstallStatus.INSTALLING:
                    Log.i(TAG, "[onStateUpdate] INSTALLING");
                    Toast.makeText(BoxScoreActivity.this, "INSTALLING", Toast.LENGTH_SHORT).show();
                    break;
                case InstallStatus.INSTALLED:
                    Log.i(TAG, "[onStateUpdate] INSTALLED");
                    Toast.makeText(BoxScoreActivity.this, "INSTALLED", Toast.LENGTH_SHORT).show();
                    break;
                case InstallStatus.CANCELED:
                    Log.i(TAG, "[onStateUpdate] CANCELED");
                    Toast.makeText(BoxScoreActivity.this, "CANCELED", Toast.LENGTH_SHORT).show();
                    break;
                case InstallStatus.FAILED:
                    Log.i(TAG, "[onStateUpdate] FAILED");
                    Toast.makeText(BoxScoreActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                    break;
                case InstallStatus.UNKNOWN:
                    Log.i(TAG, "[onStateUpdate] UNKNOWN");
                    Toast.makeText(BoxScoreActivity.this, "UNKNOWN", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "[Lifecycle] onCreate");
        setContentView(R.layout.activity_box_score);
        BoxScore.sIsOnClickAllowed = false;

        mContext = this;

        appUpdateManager = AppUpdateManagerFactory.create(this);
        if (mockServerResponseAppUpdateType == AppUpdateType.FLEXIBLE) {
            handleInAppUpdate(mockServerResponseAppUpdateType);
        }

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
        mVersionText = findViewById(R.id.activity_boxscore_version);

        mVersionText.setText(String.valueOf(BuildConfig.VERSION_CODE));

        setOnClickListenerToView();
        addTextChangeListenerToEditText();

        init();
    }

    /**
     * 檢查token，決定首頁顯示畫面種類。
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "[Lifecycle] onStart");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            showMainUi(View.GONE, View.VISIBLE);
        } else {
            showMainUi(View.VISIBLE, View.GONE);
            BoxScore.sIsOnClickAllowed = true;
        }

    }

    /**
     * 重新設定亮度，確保設定值更改後有被套用。
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "[Lifecycle] onResume");
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = BoxScore.sBrightness;
        getWindow().setAttributes(layoutParams);

        if (mockServerResponseAppUpdateType == AppUpdateType.IMMEDIATE) {
            handleInAppUpdate(mockServerResponseAppUpdateType);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "[Lifecycle] onPause");
        if (mockServerResponseAppUpdateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "[Lifecycle] onStop");
    }

    /**
     * 避免singlton的ProgressBarDialog持有同一個context
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "[Lifecycle] onDestroy");
        ProgressBarDialog.setNull();
        if (mockServerResponseAppUpdateType == AppUpdateType.FLEXIBLE) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }


    private void handleInAppUpdate(int updateType) {
        appUpdateManager.registerListener(installStateUpdatedListener);
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                logAppUpdateInfo(appUpdateInfo);

                int availability = appUpdateInfo.updateAvailability();
                int status = appUpdateInfo.installStatus();

                if (availability == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(updateType)) {

                    startUpdateFlowForResult(appUpdateManager, appUpdateInfo, updateType);

                } else if (availability == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {

                    if (status == InstallStatus.DOWNLOADING
                            || status == InstallStatus.PENDING) {

                        Log.i(TAG, "[AppUpdateInfo]: resume downloading");

                        if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                            // 視是否需要讓下載中重起 app 的使用者看到更新狀態
//                            startUpdateFlowForResult(appUpdateManager, appUpdateInfo, AppUpdateType.FLEXIBLE);
                        } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                            // 是否要阻擋下載中可以繼續使用 app 的行為
                            startUpdateFlowForResult(appUpdateManager, appUpdateInfo, AppUpdateType.IMMEDIATE);
                        } else {
                            throw new RuntimeException("你484忘記開網路ㄌ");
                        }

                    /*
                    * 理論上這個狀態只有 flexible 重啟 app 會踩到,
                    * 因為 immediate 在 onPause unregister listener, download 玩會自動安裝
                    * flexible 除非刷掉, 重啟, 或不保留, 原則上不會重新走到 onCreate
                    * */
                    } else if (status == InstallStatus.DOWNLOADED) {
                        Log.i(TAG, "[AppUpdateInfo]: resume to install");
                        if (updateType == AppUpdateType.FLEXIBLE) {
                            // 鷹派 or 鴿派
//                            appUpdateManager.completeUpdate();
                            popupSnackBarForCompleteUpdate();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "[AppUpdateInfo] onFailure: called, " + e.getMessage());
            }
        });
    }

    private void showSampleOptionDialog(
            AppUpdateInfo appUpdateInfo,
            DialogInterface.OnClickListener immediate,
            DialogInterface.OnClickListener flexible
    ) {
        new AlertDialog.Builder(BoxScoreActivity.this)
                .setMessage("請選擇你要更新的種類")
                .setPositiveButton("IMMEDIATE", immediate)
                .setNeutralButton("FLEXIBLE", flexible)
                .setCancelable(false)
                .show();
    }

    private void logAppUpdateInfo(AppUpdateInfo appUpdateInfo) {
        Log.i(TAG,
                "[AppUpdateInfo] onSuccess: called, updateAvailability: " + appUpdateInfo.updateAvailability());
        Log.i(TAG, "[AppUpdateInfo] onSuccess: called, packageName: " + appUpdateInfo.packageName());
        Log.i(TAG, "[AppUpdateInfo] onSuccess: called, availableVersionCode: "
                + appUpdateInfo.availableVersionCode());
        Log.i(TAG, "[AppUpdateInfo] onSuccess: called, installStatus: " + appUpdateInfo.installStatus());
        Log.i(TAG, "[AppUpdateInfo] onSuccess: called, isUpdateTypeAllowed IMMEDIATE: "
                + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE));
        Log.i(TAG, "[AppUpdateInfo] onSuccess: called, isUpdateTypeAllowed FLEXIBLE: "
                + appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE));
    }

    private void startUpdateFlowForResult(AppUpdateManager manager, AppUpdateInfo info, int type) {
        try {
            manager.startUpdateFlowForResult(
                    info,
                    type,
                    BoxScoreActivity.this,
                    REQUEST_CODE_INAPPUPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            Log.e(TAG, "[AppUpdateInfo] onSuccess: error", e);
        }
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.activity_boxscore_main_layout),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate()
                .addOnSuccessListener(aVoid -> Log.i(TAG, "onStateUpdate: success"))
                .addOnFailureListener(aVoid -> Log.e(TAG, "onStateUpdate: error"))
                .addOnCompleteListener(aVoid -> Log.i(TAG, "onStateUpdate: complete")));
        snackbar.setActionTextColor(
                getResources().getColor(R.color.blue));
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SETTING) {
            mPassWordEditText.setText("");
        } else if (requestCode == REQUEST_CODE_INAPPUPDATE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "[onActivityResult] success");
            } else if (resultCode == RESULT_CANCELED){
                Log.d(TAG, "[onActivityResult] canceled");
            } else {
                Log.d(TAG, "[onActivityResult] wtf");
            }
        }
    }

    private void setOnClickListenerToView() {
        mStartGameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BoxScore.isOnClickAllowedAndSetTimer()) {

                    Log.i(TAG, "StartGame pressed");
                    mPresenter.pressStartGame();
                }
            }
        });
        mTeamManageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BoxScore.isOnClickAllowedAndSetTimer()) {

                    Log.i(TAG, "TeamManage pressed");
                    startActivity(new Intent(BoxScoreActivity.this, TeamManageActivity.class));
                }

            }
        });
        mGameHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BoxScore.isOnClickAllowedAndSetTimer()) {
                    Log.i(TAG, "GmaeHistory pressed");
                    startActivity(new Intent(BoxScoreActivity.this, GameHistoryActivity.class));
                }
            }
        });
        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BoxScore.isOnClickAllowedAndSetTimer()) {
                    Log.i(TAG, "Setting pressed");

                    startActivityForResult(
                              new Intent(BoxScoreActivity.this, SettingActivity.class)
                                        .putExtra(Constants.ExtraNames.SETTING_BOOLEAN_IS_SHOW_LOGOUT, true)
                              , REQUEST_CODE_SETTING);
                }
            }
        });
        mLogInLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BoxScore.isOnClickAllowedAndSetTimer()) {

                    showProgressBarDialog(getString(R.string.progressbar_login));

                    String userName = mUserNameEditText.getText().toString();
                    String passWord = mPassWordEditText.getText().toString();

                    if (!userName.trim().equals("") && !passWord.trim().equals("")) {

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(mUserNameEditText.getText().toString(), mPassWordEditText.getText().toString())
                                  .addOnCompleteListener(BoxScoreActivity.this, new OnCompleteListener<AuthResult>() {
                                      @Override
                                      public void onComplete(@NonNull Task<AuthResult> task) {

                                          if (task.isSuccessful()) {

                                              showProgressBarDialog(getString(R.string.progressbar_loading));
                                              mPresenter.updateDbFromFireBase();
                                              showToast(getString(R.string.login_message_success));
                                              showMainUi(View.GONE, View.VISIBLE);

                                          } else {

                                              try {
                                                  throw task.getException();
                                              } catch (FirebaseAuthWeakPasswordException e) {

                                              } catch (FirebaseAuthInvalidCredentialsException e) {
                                                  showToast(getString(R.string.login_message_wrong_password));

                                              } catch (FirebaseAuthUserCollisionException e) {

                                              } catch (Exception e) {
                                                  showToast(getString(R.string.login_message_account_not_exist));
                                              }

                                              ProgressBarDialog.hideProgressBarDialog();

                                          }
                                      }
                                  });
                    } else {
                        showToast(getString(R.string.login_message_invalid));
                        ProgressBarDialog.hideProgressBarDialog();

                    }
                }
            }
        });
        mSignUpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (BoxScore.isOnClickAllowedAndSetTimer()) {

                    if (mIsUserNameLegal && mIsPassWordLegal) {

                        showProgressBarDialog(getString(R.string.progressbar_signup));
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mUserNameEditText.getText().toString(), mPassWordEditText.getText().toString())
                                  .addOnCompleteListener(BoxScoreActivity.this, new OnCompleteListener<AuthResult>() {
                                      @Override
                                      public void onComplete(@NonNull Task<AuthResult> task) {

                                          if (task.isSuccessful()) {

                                              mPassWordEditText.setText("");
                                              showMainUi(View.GONE, View.VISIBLE);
                                              showToast(getString(R.string.success));

                                          } else {

                                              try {
                                                  throw task.getException();
                                              } catch (FirebaseAuthUserCollisionException e) {
                                                  showToast(getString(R.string.sign_up_account_already_exist));

                                              } catch (Exception e) {
                                                  showToast(e.toString());
                                              }

                                          }
                                          ProgressBarDialog.hideProgressBarDialog();
                                      }
                                  });

                    } else if (!mIsUserNameLegal) {

                        showToast(getString(R.string.sign_up_account_invalid));

                    } else {

                        showToast(getString(R.string.sign_up_password_too_short));

                    }
                }
            }
        });
    }

    private void addTextChangeListenerToEditText() {
        mUserNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0 && s.charAt(s.length() - 1) == '\u0020')
                    s.delete(s.length() - 1, s.length());
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
    }


    private void init() {
        Log.i(TAG, "BoxScoreActivity.init");
        setStatusBar(this);

        ChangeBounds bounds = new ChangeBounds();
        bounds.setDuration(750);
        getWindow().setSharedElementEnterTransition(bounds);

        mPresenter = new BoxScorePresenter(this);

        delayForAnimation();
    }

    private void setStatusBar(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /**
     * 等transition動畫跑完再開始從FireBase 同步資料
     */
    private void delayForAnimation() {

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

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(BoxScoreContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showMainUi(int logInViewVisibility, int functionViewVisibility) {

        mUserNameLayout.setVisibility(logInViewVisibility);
        mPassWordLayout.setVisibility(logInViewVisibility);
        mLogInLayout.setVisibility(logInViewVisibility);
        mSignUpLayout.setVisibility(logInViewVisibility);


        mStartGameLayout.setVisibility(functionViewVisibility);
        mTeamManageLayout.setVisibility(functionViewVisibility);
        mGameHistoryLayout.setVisibility(functionViewVisibility);
        mSettingLayout.setVisibility(functionViewVisibility);
    }

    /**
     * 點擊開始遊戲後若presenter判斷sharedpreferences內有比賽顯示此詢問訊息
     * PositiveButton will let user back to the previous game,
     * NeuralButton will save the previous game and  start new one,
     * NegativeButton will delete the previous game from database and start new one.
     *
     * @param opponentName opponentName saved in SharedPreferences
     */
    @Override
    public void askResumeGame(String opponentName) {

        new AlertDialog.Builder(this, R.style.OrangeDialog).setTitle(R.string.ask_resume_game_title)
                  .setMessage(getString(R.string.ask_resume_game_message, opponentName))
                  .setPositiveButton(getString(R.string.ask_resume_game_button_resume), new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          transToGameBoxScore();
                          Log.d(TAG, "pressed Resume");
                          dialog.dismiss();
                      }
                  })
                  .setNegativeButton(getString(R.string.ask_resume_game_button_save_and_start), new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          Log.d(TAG, "pressed End and start new");
                          mPresenter.saveAndEndCurrentGame();
                          transToStartGame();
                          dialog.dismiss();
                      }
                  })
                  .setNeutralButton(getString(R.string.ask_resume_game_button_discard), new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          Log.d(TAG, "pressed Discard and start new");
                          //ToDo callPresenter to delete previous gameData in DB
                          mPresenter.removeGameDataInDataBase();
                          mPresenter.removeGameDataSharedPreferences();
                          transToStartGame();
                          dialog.dismiss();
                      }
                  }).show();
    }

    @Override
    public void transToStartGame() {
        startActivity(new Intent(mContext, StartGameActivity.class));
    }

    @Override
    public void transToGameBoxScore() {
        startActivity(new Intent(this, GameBoxScoreActivity.class).putExtra("GameInfo", new GameInfo()).putExtra("isResume", true));
    }

    @Override
    public void showProgressBarDialog(String message) {
        ProgressBarDialog.getInstance(this).showProgressBarDialog(message);
    }

    @Override
    public void onBackPressed() {
        mLogo.setTransitionName(null);
        super.onBackPressed();
    }

}
