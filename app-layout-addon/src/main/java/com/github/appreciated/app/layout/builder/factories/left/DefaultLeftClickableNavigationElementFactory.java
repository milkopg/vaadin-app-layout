package com.github.appreciated.app.layout.builder.factories.left;

import com.github.appreciated.app.layout.builder.elements.ClickableNavigationElement;
import com.github.appreciated.app.layout.builder.interfaces.ComponentFactory;
import com.github.appreciated.app.layout.component.button.NavigationButton;
import com.vaadin.ui.Component;

public class DefaultLeftClickableNavigationElementFactory implements ComponentFactory<Component, ClickableNavigationElement> {
    @Override
    public Component get(ClickableNavigationElement element) {
        NavigationButton button = new NavigationButton(element.getName(), element.getIcon());
        button.addClickListener(element.getListener());
        return button;
    }
}
