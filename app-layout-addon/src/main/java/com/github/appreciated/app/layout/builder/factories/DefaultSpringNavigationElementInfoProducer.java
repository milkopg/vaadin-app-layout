package com.github.appreciated.app.layout.builder.factories;

import com.vaadin.navigator.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

public class DefaultSpringNavigationElementInfoProducer extends BasicViewInfoProducer {

    public DefaultSpringNavigationElementInfoProducer() {
        withViewNameProvider(info -> getSpringViewName(info));
    }

    /**
     * This is a workaround to avoid code duplication and also don't want Spring as a dependency in the app-layout,
     * since there are users which are not using spring.
     *
     * @param aClass
     * @return
     */
    private Optional<String> getSpringViewName(Class<? extends View> aClass) {
        Annotation a = Arrays.stream(aClass.getAnnotations())
                .filter(annotation -> annotation.annotationType().getName().equals("com.vaadin.spring.annotation.SpringView"))
                .findFirst()
                .get();
        String result = null;
        try {
            Method m = a.getClass().getMethod("name");
            result = (String) m.invoke(a);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(result);
    }
}