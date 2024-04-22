package com.yichi.expenseTracker.fragment_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yichi.expenseTracker.adapter.RecordPagerAdapter;
import com.yichi.expenseTracker.bean.FeeTypeBean;
import com.yichi.tally.R;
import com.yichi.expenseTracker.bean.TransactionBean;
import com.yichi.expenseTracker.utils.KeyBoardUtils;
import com.yichi.expenseTracker.dialog.NotesDialog;
import com.yichi.expenseTracker.dialog.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class expensesFragment extends Fragment implements View.OnClickListener {

    protected KeyboardView keyboardView;
    protected EditText et_money;
    protected ImageView iv_type;
    protected TextView tv_type;
    protected TextView tv_notes;
    protected TextView tv_time;
    protected GridView gv_type;
    protected List<FeeTypeBean> typeList;
    protected RecordPagerAdapter.TypeBaseAdapter adapter;
    protected TransactionBean transactionBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionBean = new TransactionBean();
        transactionBean.setTypename(getString(R.string.Else));
        transactionBean.setSelectImageId(R.mipmap.ic_else_on);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        initViews(view);
        setInitTime();
        loadDataToGridView();
        setGridViewListener();

        // 接收来自Activity的编辑数据
        Bundle bundle = getArguments();
        if (bundle != null) {
            float money = bundle.getFloat("Money", 0);
            String type = bundle.getString("Type");
            String note = bundle.getString("Note");
            String date = bundle.getString("Date");
            int category = bundle.getInt("Category", 0);

            et_money.setText(String.valueOf(money));
            tv_type.setText(type);
            tv_notes.setText(note);
            tv_time.setText(date);
            updateUIBasedOnCategory(category);
        }

        return view;
    }

    private void initViews(View view) {
        keyboardView = view.findViewById(R.id.keyboard_fragment_record);
        et_money = view.findViewById(R.id.et_fragment_record_money);
        iv_type = view.findViewById(R.id.iv_fragment_record_else);
        gv_type = view.findViewById(R.id.gv_fragment_record);
        tv_type = view.findViewById(R.id.tv_fragment_record_type);
        tv_notes = view.findViewById(R.id.tv_fragment_record_notes);
        tv_time = view.findViewById(R.id.tv_fragment_record_time);

        KeyBoardUtils keyBoardUtils = new KeyBoardUtils(keyboardView, et_money);
        keyBoardUtils.showKeyBoard();
        keyBoardUtils.setOnEnsureListener(() -> {
            String moneyStr = et_money.getText().toString();
            if (TextUtils.isEmpty(moneyStr) || "0".equals(moneyStr)) {
                Toast.makeText(getContext(), R.string.input_cannot_be_empety, Toast.LENGTH_SHORT).show();
                return;
            }
            float money = Float.parseFloat(moneyStr);
            transactionBean.setMoney(money);
            saveTransactionToDB();
            getActivity().finish();
        });

        tv_notes.setOnClickListener(this);
        tv_time.setOnClickListener(this);
    }

    protected abstract void saveTransactionToDB();

    private void updateUIBasedOnCategory(int category) {
        // Additional UI updates based on category
    }

    private void setInitTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'Year 'MM'Month' dd'Day' HH:mm");
        String time = sdf.format(new Date());
        tv_time.setText(time);
        Calendar calendar = Calendar.getInstance();
        transactionBean.setYear(calendar.get(Calendar.YEAR));
        transactionBean.setMonth(calendar.get(Calendar.MONTH) + 1);
        transactionBean.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        transactionBean.setTime(time);
    }

    protected void loadDataToGridView() {
        typeList = new ArrayList<>();
        adapter = new RecordPagerAdapter.TypeBaseAdapter(getContext(), typeList);
        gv_type.setAdapter(adapter);
    }

    protected void setGridViewListener() {
        gv_type.setOnItemClickListener((parent, view, position, id) -> {
            adapter.selectPosition = position;
            adapter.notifyDataSetChanged();
            FeeTypeBean feeTypeBean = typeList.get(position);
            tv_type.setText(feeTypeBean.getTypename());
            iv_type.setImageResource(feeTypeBean.getSelectImageId());
            transactionBean.setTypename(feeTypeBean.getTypename());
            transactionBean.setSelectImageId(feeTypeBean.getSelectImageId());
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_fragment_record_notes) {
            showNoteDialog();
        } else if (id == R.id.tv_fragment_record_time) {
            showTimeDialog();
        }
    }


    private void showTimeDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext());
        timePickerDialog.show();
        timePickerDialog.setOnEnsureListener((time, year, month, day) -> {
            tv_time.setText(time);
            transactionBean.setYear(year);
            transactionBean.setMonth(month);
            transactionBean.setDay(day);
            transactionBean.setTime(time);
        });
    }

    private void showNoteDialog() {
        NotesDialog dialog = new NotesDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(() -> {
            String text = dialog.getEditText();
            if (!TextUtils.isEmpty(text)) {
                tv_notes.setText(text);
                transactionBean.setNote(text);
            }
            dialog.cancel();
        });
    }
}
