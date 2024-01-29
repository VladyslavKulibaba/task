package com.task.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class AemPracticeModelTest {

    private final AemContext ctx = new AemContext(ResourceResolverType.JCR_MOCK);

    private AemPracticeModel model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        ctx.load().json("/com/task/core/models/practice/AemPracticeModel.json",
                "/content");

        Resource resource = ctx.resourceResolver().getResource("/content/container/jcr:content/root/responsivegrid/practice");
        ctx.currentResource(resource);
        model = resource.adaptTo(AemPracticeModel.class);
    }

    @Test
    void testGetNavigationItemsShouldReturnCorrectItems() {
        assertEquals(2, model.getNavigationItems().size());
        assertEquals("TEST_Title 1", model.getNavigationItems().get(0).getTitle());
        assertEquals("/content/pagePath1", model.getNavigationItems().get(0).getPagePath());
    }

    @Test
    void testModelShouldReturnCorrectTitle() {
        assertEquals("Test title", model.getTitle());
    }

    @Test
    void testModelShouldReturnCorrectImagePath() {
        assertEquals("/content/dam/test", model.getImagePath());
    }

    @Test
    void testModelShouldReturnCorrectBackground() {
        assertTrue(model.isBackgroundImage());
    }
}
