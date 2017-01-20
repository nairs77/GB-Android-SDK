package com.gebros.platform.auth.ui;

import android.text.InputFilter;
import android.text.Spanned;

import java.io.UnsupportedEncodingException;

/**
 * Created by jce_platform on 2016. 5. 31..
 */
public class ByteLengthFilter implements InputFilter {

    private String mCharset;
    protected int mMaxByte;

    public ByteLengthFilter(int maxbyte, String charset) {

        this.mMaxByte = maxbyte;
        this.mCharset = charset;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        // 변경 후 예상되는 문자열
        String expected = new String();
        expected += dest.subSequence(0, dstart);
        expected += source.subSequence(start, end);
        expected += dest.subSequence(dend, dest.length());
        int keep = calculateMaxLength(expected) - (dest.length() - (dend - dstart));
        if (keep < 0) // keep -값이 나올경우를 대비한 방어코드
        {
            keep = 0;
        }
        int Rekeep = keep;

        if (keep <= 0 && Rekeep <= 0) {
            return ""; // source 입력 불가(원래 문자열 변경 없음)
        } else if (keep >= end - start) {
            return null; // keep original. source 그대로 허용
        } else {
            if (dest.length() == 0 && Rekeep <= 0) // 기존의 내용이 없고, 붙여넣기 하는 문자바이트가
            // 71바이트를 넘을경우
            {
                return source.subSequence(start, start + keep);
            } else if (Rekeep <= 0) // 엔터가 들어갈 경우 keep이 0이 되어버리는 경우가 있음
            {
                return source.subSequence(start, start + (source.length() - 1));
            } else {
                return source.subSequence(start, start + Rekeep); // source중
                // 일부만입력 허용
            }
        }
    }

    /**
     * 입력가능한 최대 문자 길이(최대 바이트 길이와 다름!).
     */
    protected int calculateMaxLength(String expected) {
        int expectedByte = getByteLength(expected);
        if (expectedByte == 0) {
            return 0;
        }
        return mMaxByte - (getByteLength(expected) - expected.length());
    }

    /**
     * 문자열의 바이트 길이. 인코딩 문자셋에 따라 바이트 길이 달라짐.
     *
     * @param str
     * @return
     */
    private int getByteLength(String str) {
        try {
            return str.getBytes(mCharset).length;
        } catch (UnsupportedEncodingException e) {
        }

        return 0;
    }
}
