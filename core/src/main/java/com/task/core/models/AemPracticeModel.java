package com.task.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ChildResource;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AemPracticeModel {

    @ValueMapValue
    private String imagePath;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private boolean backgroundImage;

    @Inject
    @Via(type = ChildResource.class)
    private List<NavigationItem> navigationItems = new ArrayList<>();

    @Getter
    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class NavigationItem {

        public static final String PREFIX = "TEST_";

        @ValueMapValue
        private String title;

        @ValueMapValue
        private String pagePath;

        @PostConstruct
        private void init() {
            Optional.ofNullable(title).ifPresent(title -> this.title = PREFIX + title);
        }
    }

}
