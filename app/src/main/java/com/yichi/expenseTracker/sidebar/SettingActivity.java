package com.yichi.expenseTracker.sidebar;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import com.yichi.expenseTracker.MainActivity;
import com.yichi.tally.R;

import java.util.Arrays;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner languageSpinner; // 用于语言选择的下拉菜单
    private final String LANGUAGE_KEY = "current_language"; // SharedPreferences中保存当前语言设置的键

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateResources(this, getCurrentLanguage()); // 在活动启动时根据保存的设置更新语言
        setContentView(R.layout.activity_setting); // 设置布局文件

        ImageView iv_back = findViewById(R.id.iv_setting_activity_back);
        TextView tv_clear = findViewById(R.id.tv_setting_activity_clear);
        languageSpinner = findViewById(R.id.language_spinner);

        tv_clear.setOnClickListener(this);
        iv_back.setOnClickListener(this);

        setupLanguageSpinner(); // 初始化语言选择下拉框
    }

    private void setupLanguageSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_names, android.R.layout.simple_spinner_item); // 使用语言名称数组
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // 设置当前语言为默认选项
        String[] languageCodes = getResources().getStringArray(R.array.language_codes);
        String currentLanguage = getCurrentLanguage();
        int defaultPosition = Arrays.asList(languageCodes).indexOf(currentLanguage); // 查找当前语言代码在数组中的索引
        if (defaultPosition == -1) defaultPosition = 0; // 如果找不到，使用默认语言
        languageSpinner.setSelection(defaultPosition);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguageCode = languageCodes[position]; // 根据选择的位置获取语言代码
                switchLanguage(selectedLanguageCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void switchLanguage(String languageCode) {
        // 获取当前已设置的语言
        String currentLanguage = getCurrentLanguage();

        // 如果当前语言与选择的语言相同，不做任何操作
        if (currentLanguage.equals(languageCode)) {
            return;
        }

        // 更新资源和保存语言设置
        updateResources(this, languageCode);
        saveLanguageSetting(languageCode);

        // 重新启动应用到主页面
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // 清除任务栈，并启动新任务
        startActivity(intent);
    }

    private void saveLanguageSetting(String languageCode) {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LANGUAGE_KEY, languageCode); // 保存语言设置
        editor.apply(); // 提交修改
    }

    private String getCurrentLanguage() {
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        return preferences.getString(LANGUAGE_KEY, "en"); // 获取当前语言，默认为英语
    }

    public static void updateResources(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale); // 设置新的默认语言
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale); // 更新配置
        } else {
            config.locale = locale;
        }
        res.updateConfiguration(config, res.getDisplayMetrics()); // 应用新配置
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_setting_activity_back) {
            finish(); // 结束当前活动，返回上一个活动
        } else if (id == R.id.tv_setting_activity_clear) {
            showDeleteDialog(); // 显示删除数据确认对话框
        }
    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.warning_content_delete_all_record1) + "\n" + getString(R.string.warning_content_delete_all_record2))
                .setPositiveButton(getString(R.string.cancel), null) // 设置取消按钮
                .setNegativeButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 确认后执行删除所有记录的逻辑
                        Toast.makeText(SettingActivity.this, getString(R.string.successfully_deleted), Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create().show(); // 显示对话框
    }
}
