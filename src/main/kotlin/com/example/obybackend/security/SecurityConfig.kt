package com.example.obybackend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.session.HttpSessionEventPublisher

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val customOidcUserService: CustomOidcUserService,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            }
            .headers { headers ->
                headers.contentSecurityPolicy { csp ->
                    csp.policyDirectives("default-src 'self'")
                }
                headers.frameOptions { it.sameOrigin() }
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                session.sessionFixation { it.migrateSession() }
            }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/", "/favicon.ico", "/error", "/actuator/health").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/messages", "/api/messages/*").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth ->
                oauth.userInfoEndpoint { endpoint ->
                    endpoint.oidcUserService(customOidcUserService)
                }
                oauth.defaultSuccessUrl("/auth/me", true)
            }
            .logout { logout ->
                logout.logoutUrl("/auth/logout")
                logout.clearAuthentication(true)
                logout.invalidateHttpSession(true)
                logout.deleteCookies("JSESSIONID")
                logout.logoutSuccessHandler { _, response, _ ->
                    response.status = 204
                }
            }

        return http.build()
    }

    @Bean
    fun httpSessionEventPublisher(): HttpSessionEventPublisher = HttpSessionEventPublisher()
}
