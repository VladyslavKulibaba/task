package com.task.core.models;

import com.day.cq.commons.Externalizer;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.settings.SlingSettingsService;

import java.util.Set;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class})
public class InstanceHelper {

	/**
	 * Adobe Cloud Manager CI/CD marks SlingSettingsService as deprecated
	 * and recommends implement custom service to determine current runmode.
	 */
	@OSGiService
	private SlingSettingsService slingSettingsService;

	public boolean isAuthorRunMode() {
		Set<String> runModes = slingSettingsService.getRunModes();
		runModes.forEach(System.out::println);
		return runModes.contains(Externalizer.AUTHOR);
	}
}
