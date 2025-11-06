package com.example.obybackend.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.ObjectPostProcessor
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.session.HttpSessionEventPublisher
import org.springframework.security.web.session.SessionInformationExpiredStrategy

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
                session.maximumSessions(1)
                    .sessionRegistry(sessionRegistry())
                    .maxSessionsPreventsLogin(false)
                    .expiredSessionStrategy(sessionInformationExpiredStrategy())
            }
            .authorizeHttpRequests { authorize ->
                authorize
                    .withObjectPostProcessor(
                        object : ObjectPostProcessor<DefaultWebSecurityExpressionHandler> {
                            override fun <O : DefaultWebSecurityExpressionHandler> postProcess(handler: O): O {
                                handler.setRoleHierarchy(roleHierarchy())
                                return handler
                            }
                        },
                    )
                    .requestMatchers("/", "/favicon.ico", "/error", "/actuator/health").permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
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

    @Bean
    fun sessionRegistry(): SessionRegistry = SessionRegistryImpl()

    @Bean
    fun sessionInformationExpiredStrategy(): SessionInformationExpiredStrategy =
        SessionInformationExpiredStrategy { event ->
            // 明示的に401を返してフロントエンド側でセッション期限切れを検知しやすくする
            val response = event.response
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.writer.write("Session expired")
        }

    @Bean
    fun roleHierarchy(): RoleHierarchy =
        RoleHierarchyImpl().apply {
            // ROLE_ADMIN > ROLE_USER の順序を定義して、管理者が一般権限も自動的に持つようにする
            setHierarchy("ROLE_ADMIN > ROLE_USER")
        }
}
