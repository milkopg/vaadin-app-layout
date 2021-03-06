package com.github.appreciated.app.layout.interceptor;

import java.text.Normalizer;

public class DefaultViewNameInterceptor implements ViewNameInterceptor {
    @Override
    public String get(String info) {
        info = Normalizer.normalize(info, Normalizer.Form.NFD);
        info = info.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return info;
    }
}
