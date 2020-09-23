package mz.ac.unilurio.repository.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }
}
