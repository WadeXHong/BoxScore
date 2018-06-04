package com.wadexhong.boxscore.modelhelper.firebasemodel;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wadexhong.boxscore.Constants;
import com.wadexhong.boxscore.dialog.ProgressBarDialog;
import com.wadexhong.boxscore.modelhelper.firebasemodel.task.OnCreateDataBaseTask;

/**
 * Created by wade8 on 2018/5/29.
 */

public class Get {
    private static final String TAG = Get.class.getSimpleName();

    private static Get mInstance;

    private Get(){}

    public static Get getInstance(){
        if (mInstance == null) mInstance = new Get();
            return mInstance;
    }


    public static void onCreate() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(Constants.FireBaseConstant.USERS).child(FirebaseAuth.getInstance().getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                new OnCreateDataBaseTask(dataSnapshot).execute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ProgressBarDialog.hideProgressBarDialog();
            }
        });


    }
}



//
//            DatabaseReference refTest = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid()).child("teamList");
//            refTest.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snap:dataSnapshot.getChildren()) {
//                        Log.i("key", snap.getKey());
//                        Log.i("value", (String) snap.getValue());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });



//            DatabaseReference refTest = FirebaseDatabase.getInstance().getReference("teams").child(FirebaseAuth.getInstance().getUid());
//            refTest.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot snap:dataSnapshot.getChildren()) {
//                        Log.i("key", snap.getKey());
//
//                        for (DataSnapshot snapChild:snap.child("players").getChildren()){
//                            Log.i("kk", snapChild.getKey());
//                        }
//
////                        Log.i("value", (String) snap.getValue());
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });