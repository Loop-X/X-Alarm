package io.github.loop_x.yummywakeup.module.UnlockTypeModule.alarmType;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.loop_x.yummywakeup.R;
import io.github.loop_x.yummywakeup.module.AlarmModule.AlarmAlertFullScreen;
import io.github.loop_x.yummywakeup.tools.CalculationFormula;

public class MathAlarm extends UnlockFragment {

    private int[] formula;
    private int result;
    private String input;
    private TextView tvFormula;
    private EditText etCalculResult;
    private ImageView ivFlagResult;
    private Button btnCloseAlarm;
    private OnAlarmAction mListener;

    public MathAlarm() {}

    public static MathAlarm newInstance() {
        return new MathAlarm();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_unlock_math_alarm;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (OnAlarmAction) activity;
    }

    @Override
    public void onViewInitial() {
        tvFormula = (TextView) findViewById(R.id.tv_formula);
        etCalculResult = (EditText) findViewById(R.id.et_calcul_result);
        ivFlagResult = (ImageView) findViewById(R.id.iv_flag_result);
        btnCloseAlarm = (Button) findViewById(R.id.btn_calcul_close_alarm);
        btnCloseAlarm.setEnabled(false);
        btnCloseAlarm.setVisibility(View.GONE);

        formula = CalculationFormula.generateFormula(); // Generate formula
        result = CalculationFormula.getFormulaResult(formula); // Get formula's result
        tvFormula.setText(CalculationFormula.getFormulaString(formula) + " = "); // Show formula on textView

        initListener();
    }

    @Override
    public void onRefreshData() {

    }

    public void initListener() {
        etCalculResult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // If typed number is equal to result, then enable image and button
                if (!TextUtils.isEmpty(etCalculResult.getText())) {

                    input = etCalculResult.getText().toString();

                    if (input.contains("-") && input.length() > 1) {
                        if(input.startsWith("-") && !input.substring(1).contains("-")) {
                            if (Integer.parseInt(input.substring(1)) == -1 * result) {
                                btnCloseAlarm.setVisibility(View.VISIBLE);
                                btnCloseAlarm.setEnabled(true);
                                ivFlagResult.setBackgroundResource(R.drawable.icon_active);
                            }
                        }
                    } else if (!input.contains("-")) {
                        if (Integer.parseInt(input) == result) {
                            btnCloseAlarm.setVisibility(View.VISIBLE);
                            btnCloseAlarm.setEnabled(true);
                            ivFlagResult.setBackgroundResource(R.drawable.icon_active);
                        }
                    } else {
                        btnCloseAlarm.setVisibility(View.GONE);
                        btnCloseAlarm.setEnabled(false);
                        ivFlagResult.setBackgroundResource(R.drawable.icon_inactive);
                    }
                }
            }
    });
        btnCloseAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.closeAlarm();
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean checkUnlockAlarm() {
        return false;
    }
}
