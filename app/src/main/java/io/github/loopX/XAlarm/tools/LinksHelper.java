package io.github.loopX.XAlarm.tools;

import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.widget.TextView;

public class LinksHelper {

    /**
     * Enable links and remove underline
     * @param view
     * @param isWithUnderLine true to remove underline
     */
    public static void enableLinks(TextView view, Boolean isWithUnderLine) {
        if (view != null) {
            view.setMovementMethod(LinkMovementMethod.getInstance());
            if(!isWithUnderLine) {
                stripUnderlines(view);
            }
        }
    }

    /**
     * Remove underline
     * @param textView
     */
    public static void stripUnderlines(TextView textView) {
        Spannable s = (Spannable) textView.getText();
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);

        for (URLSpan span: spans) {
            int start = s.getSpanStart(span);
            int end = s.getSpanEnd(span);
            s.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            s.setSpan(span, start, end, 0);
        }

        textView.setText(s);
    }

    // Customized URLSpan which has no under line
    private static class URLSpanNoUnderline extends URLSpan {

        public URLSpanNoUnderline(String url) {
            super(url);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

    }

}
