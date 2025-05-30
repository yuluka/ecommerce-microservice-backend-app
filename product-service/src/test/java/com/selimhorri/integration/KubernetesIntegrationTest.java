package com.selimhorri.integration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;

public class KubernetesIntegrationTest {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    context,
                    "spring.profiles.active=test",
                    "spring.config.name=application");
        }
    }
}
