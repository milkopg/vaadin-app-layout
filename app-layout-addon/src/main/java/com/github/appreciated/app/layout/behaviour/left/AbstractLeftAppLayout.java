package com.github.appreciated.app.layout.behaviour.left;

import com.github.appreciated.app.layout.behaviour.AppLayoutComponent;
import com.github.appreciated.app.layout.behaviour.listener.AppLayoutResizeListener;
import com.github.appreciated.app.layout.builder.design.AppLayoutDesign;
import com.github.appreciated.app.layout.builder.elements.*;
import com.github.appreciated.app.layout.builder.factories.left.DefaultLeftClickableNavigationElementFactory;
import com.github.appreciated.app.layout.builder.factories.left.DefaultLeftNavigationBadgeElementComponentFactory;
import com.github.appreciated.app.layout.builder.factories.left.DefaultLeftSectionElementComponentFactory;
import com.github.appreciated.app.layout.builder.factories.left.DefaultLeftSubmenuNavigationElementFactory;
import com.github.appreciated.app.layout.builder.factories.top.DefaultTopClickableNavigationElementFactory;
import com.github.appreciated.app.layout.builder.factories.top.DefaultTopNavigationBadgeElementComponentFactory;
import com.github.appreciated.app.layout.builder.factories.top.DefaultTopSectionElementComponentFactory;
import com.github.appreciated.app.layout.builder.factories.top.DefaultTopSubmenuNavigationElementFactory;
import com.github.appreciated.app.layout.builder.interfaces.ComponentFactory;
import com.github.appreciated.app.layout.builder.interfaces.NavigationElementComponent;
import com.github.appreciated.app.layout.component.layout.VerticalFlexBoxLayout;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.IOException;
import java.util.List;

import static com.github.appreciated.app.layout.builder.design.Styles.APP_LAYOUT;

/**
 * The {@link AbstractLeftAppLayout} is the supposed to be the base of any {@link AppLayoutComponent} with a "Left Behaviour".
 */

public abstract class AbstractLeftAppLayout extends CustomLayout implements AppLayoutComponent, AppLayoutResizeListener.AppLayoutResizedListener {

    private final Panel contentPanel = new Panel();

    private final VerticalLayout menuHeaderHolder = new VerticalLayout();
    private final VerticalLayout menuElementHolder = new VerticalLayout();
    private final VerticalLayout menuFooterHolder = new VerticalLayout();

    private final VerticalFlexBoxLayout menuHolder = new VerticalFlexBoxLayout(menuHeaderHolder, menuElementHolder, menuFooterHolder);

    private final HorizontalLayout appBar = new HorizontalLayout();
    private final HorizontalLayout appBarElementWrapper = new HorizontalLayout();
    private final HorizontalLayout appBarElementContainer = new HorizontalLayout();
    private Component title = new Label("");
    private final HorizontalLayout titleWrapper = new HorizontalLayout(title);
    private List<NavigatorNavigationElement> list;
    private ComponentFactory<NavigationElementComponent, NavigatorNavigationElement> drawerNavigationElementProvider = new DefaultLeftNavigationBadgeElementComponentFactory();
    private ComponentFactory<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> drawerSubmenuElementProvider = new DefaultLeftSubmenuNavigationElementFactory();
    private ComponentFactory<Component, SectionNavigationElement> drawerSectionElementProvider = new DefaultLeftSectionElementComponentFactory();
    private ComponentFactory<Component, ClickableNavigationElement> drawerClickableElementProvider = new DefaultLeftClickableNavigationElementFactory();
    private ComponentFactory<NavigationElementComponent, NavigatorNavigationElement> topNavigationElementProvider = new DefaultTopNavigationBadgeElementComponentFactory();
    private ComponentFactory<Component, SectionNavigationElement> topSectionElementProvider = new DefaultTopSectionElementComponentFactory();
    private ComponentFactory<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> topSubmenuElementProvider = new DefaultTopSubmenuNavigationElementFactory();
    private ComponentFactory<Component, ClickableNavigationElement> topClickableElementProvider = new DefaultTopClickableNavigationElementFactory();

    public AbstractLeftAppLayout(String filename) throws IOException {
        super(AbstractLeftAppLayout.class.getResourceAsStream(filename));
        setSizeFull();
        contentPanel.setSizeFull();
        contentPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        menuHolder.setSizeFull();
        menuHolder.grow(menuElementHolder);
        menuHolder.setOverflowAuto(true);

        menuHeaderHolder.setVisible(false);
        menuFooterHolder.setVisible(false);
        menuHeaderHolder.setMargin(false);
        menuElementHolder.setMargin(new MarginInfo(true, false));
        menuFooterHolder.setMargin(new MarginInfo(false, false, true, false));
        menuElementHolder.setWidth(100, Unit.PERCENTAGE);
        addStyleNames("app-layout-behaviour-" + getStyleName(), APP_LAYOUT);
        addComponent(contentPanel, "content");
        addComponent(menuHolder, "menu-elements");
        addComponent(appBar, "app-bar-elements");
        appBar.addComponents(titleWrapper, new AppLayoutResizeListener(this), appBarElementWrapper);
        appBar.setExpandRatio(appBarElementWrapper, 1);
        appBar.setWidth(100, Unit.PERCENTAGE);
        appBar.setHeight(100, Unit.PERCENTAGE);
        appBarElementWrapper.setSpacing(false);
        appBarElementWrapper.setSizeFull();
        appBarElementWrapper.addComponentAsFirst(appBarElementContainer);
        appBarElementContainer.setHeight(100, Unit.PERCENTAGE);
        appBarElementWrapper.setComponentAlignment(appBarElementContainer, Alignment.TOP_RIGHT);
        titleWrapper.setHeight(100, Unit.PERCENTAGE);
        titleWrapper.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
    }

    public abstract String getStyleName();

    @Override
    public void setNavigatorNavigationElements(List<NavigatorNavigationElement> list) {
        this.list = list;
    }

    @Override
    public void refreshNavigationElementInfo() {
        if (list != null) {
            list.forEach(NavigatorNavigationElement::refreshInfo);
        }
    }

    public void addAppBarElement(Component component) {
        appBarElementContainer.addComponent(component);
        appBarElementContainer.setComponentAlignment(component, Alignment.MIDDLE_RIGHT);
    }

    public void setDesign(AppLayoutDesign design) {
        this.addStyleName(design.getStyleName());
    }

    public HorizontalLayout getAppBar() {
        return appBar;
    }

    public HorizontalLayout getAppBarElementWrapper() {
        return appBarElementWrapper;
    }

    public Component getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (this.title instanceof Label) {
            ((Label) this.title).setValue(title);
        }
    }

    @Override
    public Component getTitleComponent() {
        return title;
    }

    public void setTitleComponent(Component compoent) {
        titleWrapper.replaceComponent(this.title, compoent);
        this.title = compoent;
        titleWrapper.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
    }

    @Override
    public Panel getContentHolder() {
        return contentPanel;
    }

    public HorizontalLayout getTitleWrapper() {
        return titleWrapper;
    }

    public VerticalLayout getMenuElementHolder() {
        return menuElementHolder;
    }

    public VerticalLayout getMenuFooterHolder() {
        return menuFooterHolder;
    }

    public VerticalLayout getMenuHeaderHolder() {
        return menuHeaderHolder;
    }

    public Layout getMenuHolder() {
        return menuHolder;
    }

    public void addAppBarIcon(Component appBarIconComponent) {
        titleWrapper.addComponentAsFirst(appBarIconComponent);
        titleWrapper.setComponentAlignment(appBarIconComponent, Alignment.MIDDLE_LEFT);
    }

    @Override
    public void addNavigationElement(AbstractNavigationElement component) {
        component.setProvider(this);
        addToDrawer(component.getComponent());
    }

    @Override
    public ComponentFactory<Component, SectionNavigationElement> getDrawerSectionElementProvider() {
        return drawerSectionElementProvider;
    }

    @Override
    public void setDrawerSectionElementProvider(ComponentFactory<Component, SectionNavigationElement> provider) {
        drawerSectionElementProvider = provider;
    }

    @Override
    public ComponentFactory<Component, SectionNavigationElement> getTopSectionElementProvider() {
        return topSectionElementProvider;
    }

    @Override
    public void setTopSectionElementProvider(ComponentFactory<Component, SectionNavigationElement> provider) {
        topSectionElementProvider = provider;
    }

    @Override
    public ComponentFactory<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> getDrawerSubmenuElementProvider() {
        return drawerSubmenuElementProvider;
    }

    @Override
    public void setDrawerSubmenuElementProvider(ComponentFactory<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> provider) {
        drawerSubmenuElementProvider = provider;
    }

    @Override
    public ComponentFactory<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> getTopSubmenuElementProvider() {
        return topSubmenuElementProvider;
    }

    @Override
    public void setTopSubmenuElementProvider(ComponentFactory<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> provider) {
        topSubmenuElementProvider = provider;
    }

    @Override
    public ComponentFactory<NavigationElementComponent, NavigatorNavigationElement> getDrawerNavigationElementProvider() {
        return drawerNavigationElementProvider;
    }

    @Override
    public void setDrawerNavigationElementProvider(ComponentFactory<NavigationElementComponent, NavigatorNavigationElement> provider) {
        drawerNavigationElementProvider = provider;
    }

    @Override
    public ComponentFactory<NavigationElementComponent, NavigatorNavigationElement> getTopNavigationElementProvider() {
        return topNavigationElementProvider;
    }

    @Override
    public void setTopNavigationElementProvider(ComponentFactory<NavigationElementComponent, NavigatorNavigationElement> provider) {
        topNavigationElementProvider = provider;
    }

    @Override
    public ComponentFactory<Component, ClickableNavigationElement> getTopClickableElementProvider() {
        return topClickableElementProvider;
    }

    @Override
    public void setTopClickableElementProvider(ComponentFactory<Component, ClickableNavigationElement> topClickableElementProvider) {
        this.topClickableElementProvider = topClickableElementProvider;
    }

    @Override
    public ComponentFactory<Component, ClickableNavigationElement> getDrawerClickableElementProvider() {
        return drawerClickableElementProvider;
    }

    @Override
    public void setDrawerClickableElementProvider(ComponentFactory<Component, ClickableNavigationElement> drawerClickableElementProvider) {
        this.drawerClickableElementProvider = drawerClickableElementProvider;
    }

    @Override
    public void addNavigationFooterElement(AbstractNavigationElement component) {
        component.setProvider(this);
        addToDrawerFooter(component.getComponent());
    }

    @Override
    public void addNavigationHeaderElement(AbstractNavigationElement component) {
        component.setProvider(this);
        addToDrawerHeader(component.getComponent());
    }

    @Override
    public void addToDrawer(Component component) {
        menuElementHolder.addComponent(component);
    }

    @Override
    public void addToDrawerFooter(Component component) {
        menuFooterHolder.setVisible(true);
        menuFooterHolder.addComponent(component);
    }

    @Override
    public void addToDrawerHeader(Component component) {
        menuHeaderHolder.setVisible(true);
        menuHeaderHolder.addComponent(component);
    }

    @Override
    public void addToTop(Component component) {
        throw new UnsupportedOperationException("The Left Layout does not support this operation");
    }

    @Override
    public void addToTopFooter(Component component) {
        throw new UnsupportedOperationException("The Left Layout does not support this operation");
    }

    @Override
    public void addToTopHeader(Component component) {
        throw new UnsupportedOperationException("The Left Layout does not support this operation");
    }

    @Override
    public void onAppLayoutResized() {
        getUI().access(() -> getUI().markAsDirty());
    }
}
