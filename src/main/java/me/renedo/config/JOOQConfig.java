package me.renedo.config;

import javax.sql.DataSource;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JOOQConfig {
    private final DataSource ds;

    public JOOQConfig(DataSource ds) {
        this.ds = ds;
    }

    /**
     * Add custom jOOQ configuration.
     * <p>
     * The {@link DefaultConfigurationCustomizer} type has been added in Spring Boot
     * 2.5 to facilitate customising the out of the box provided jOOQ
     */
    @Bean
    public DefaultConfigurationCustomizer configurationCustomizer() {
        return c -> c.settings()
            .withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED);
    }

    @Bean
    public DSLContext ctx() {
        return DSL.using(ds, SQLDialect.H2, new Settings().withRenderQuotedNames(RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED));
    }
}
