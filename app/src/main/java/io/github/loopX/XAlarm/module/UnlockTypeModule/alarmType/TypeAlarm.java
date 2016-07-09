package io.github.loopX.XAlarm.module.UnlockTypeModule.alarmType;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.github.loopX.XAlarm.BuildConfig;
import io.github.loopX.XAlarm.R;
import io.github.loopX.XAlarm.tools.ToastMaster;
import io.github.loopX.XAlarm.view.YummyEditText;
import io.github.loopX.XAlarm.view.YummyTextView;

public class TypeAlarm extends UnlockFragment {

    private YummyTextView tvSentenceToType;
    private YummyEditText etTypeSentence;

    private OnAlarmAction mListener;

    private InputMethodManager mInputMethodManager;

    private Activity mContext;
    private Timer mTimer = null;

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            mListener.closeAlarm();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.fragment_unlock_type_alarm;
    }

    @Override
    public void onViewInitial() {
        tvSentenceToType = (YummyTextView) findViewById(R.id.tv_sentence_to_type);
        etTypeSentence = (YummyEditText) findViewById(R.id.et_type_sentence);

        etTypeSentence.setFocusable(true);
        etTypeSentence.setFocusableInTouchMode(true);
        etTypeSentence.requestFocus();

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        etTypeSentence.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(etTypeSentence.getText())) {
                    if(etTypeSentence.getText().toString().toLowerCase().equals(
                            tvSentenceToType.getText().toString().toLowerCase())) {
                        ToastMaster.setToast(Toast.makeText(getActivity(),
                                getString(R.string.puzzle_complete),
                                Toast.LENGTH_SHORT));
                        ToastMaster.showToast();

                        if(mTimer == null) {
                            mTimer = new Timer(true);
                            mTimer.schedule(task, 1200);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onRefreshData() {
        Random random = new Random();
        int resId = random.nextInt(16);
        tvSentenceToType.setText(
                getResources().getIdentifier(
                        "type_alarm_sentence_" + resId,
                        "string",
                        BuildConfig.APPLICATION_ID));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        mListener = (OnAlarmAction) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(mListener != null) {
            mListener.closeAlarm();
            mListener = null;
        }

        if(mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    public boolean checkUnlockAlarm() {
        return false;
    }
}
