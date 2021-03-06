package com.github.appreciated.app.layout.builder.steps;

import com.github.appreciated.app.layout.builder.AppLayoutConfiguration;
import com.github.appreciated.app.layout.builder.CDIAppLayoutBuilder;
import com.vaadin.navigator.ViewProvider;

import java.util.function.Supplier;

public class CDIBuilderNavigatorPreamble extends AbstractBuilderPreamble<CDIAppLayoutBuilder> {

    public CDIBuilderNavigatorPreamble(CDIAppLayoutBuilder builder) {
        super(builder);
    }

    /**
     * Force the user with this API to set the NavigatorProducer since without it the AppLayout (with CDI) won't work
     *
     * @param producer
     * @return
     */
    public CDIBuilderViewNameProviderPreamble withNavigator(AppLayoutConfiguration.NavigatorProducer producer) {
        return new CDIBuilderViewNameProviderPreamble(getBuilder().withNavigator(producer));
    }

    /**
     * Force the user with this API to set the ViewProvider Supplier since without it the AppLayout (with CDI) won't work
     *
     * @param supplier
     * @return
     */
    public CDIBuilderViewNameProviderPreamble withViewProvider(Supplier<ViewProvider> supplier) {
        return new CDIBuilderViewNameProviderPreamble(getBuilder().withViewProvider(supplier));
    }
}
