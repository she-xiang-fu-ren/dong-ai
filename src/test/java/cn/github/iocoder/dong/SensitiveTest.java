package cn.github.iocoder.dong;

import com.github.houbb.sensitive.word.core.SensitiveWordHelper;

public class SensitiveTest {
    public static void main(String[] args) {
        final String text = "五星红旗迎风飘扬，毛主席的画像屹立在天安门前。";

        boolean contains = SensitiveWordHelper.contains(text);

    }
}
