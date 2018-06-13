package com.wadexhong.boxscore.modelhelper.firebasemodel;

import android.net.Uri;

/**
 * Created by wade8 on 2018/6/14.
 */

public interface UploadExcelFileCallBack {

    void onSuccess(Uri uri);

    void onFailure(String message);
}
