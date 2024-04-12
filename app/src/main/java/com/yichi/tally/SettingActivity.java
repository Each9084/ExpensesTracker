package com.yichi.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yichi.tally.database.DBManager;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ImageView iv_back = findViewById(R.id.iv_setting_activity_back);
        TextView tv_clear = findViewById(R.id.tv_setting_activity_clear);

        tv_clear.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        if (id == R.id.iv_setting_activity_back) {
            finish();
        }else if (id== R.id.tv_setting_activity_clear){
            showDeleteDialog();
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.warning)).setMessage(getString(R.string.warning_content_delete_all_record1)
        +"\n"+getString(R.string.warning_content_delete_all_record2))
                .setPositiveButton(getString(R.string.cancel),null)
                .setNegativeButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager.deleteAll();
                        Toast.makeText(SettingActivity.this,getString(R.string.successfully_deleted)
                                ,Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show();
    }
}