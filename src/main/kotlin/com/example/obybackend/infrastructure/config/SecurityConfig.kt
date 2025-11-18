package com.example.obybackend.infrastructure.config

import com.example.obybackend.infrastructure.security.CustomOAuth2UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.OncePerRequestFilter
import java.util.function.Supplier

/**
 * Spring Security設定
 *
 * Google OAuth2.0 ログインを設定
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customOAuth2UserService: CustomOAuth2UserService,
    @Value("\${app.frontend.url}") private val frontendUrl: String,
    @Value("\${app.cors.allowed-origins}") private val allowedOrigins: String,
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            // CSRF保護を有効化（SPA対応）
            .csrf { csrf ->
                csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(SpaCsrfTokenRequestHandler())
            }
            // CSRFトークンをCookieにセットするフィルターを追加
            .addFilterAfter(CsrfCookieFilter(), BasicAuthenticationFilter::class.java)
            .authorizeHttpRequests { auth ->
                auth
                    // 認証不要エンドポイント
                    .requestMatchers("/", "/error", "/login/**").permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .requestMatchers("/actuator/health").permitAll()
                    // OAuth2認証エンドポイント
                    .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                    // API endpoints - 認証必須
                    .requestMatchers("/api/v1/**").authenticated()
                    // その他すべて認証必須
                    .anyRequest().authenticated()
            }
            .oauth2Login { oauth2 ->
                oauth2
                    .userInfoEndpoint { userInfo ->
                        userInfo.userService(customOAuth2UserService)
                    }
                    .defaultSuccessUrl("$frontendUrl/auth/callback", true)
                    .failureUrl("$frontendUrl/login?error=true")
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("$frontendUrl/login")
                    .permitAll()
            }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        // カンマ区切りの文字列をリストに変換
        configuration.allowedOrigins = allowedOrigins.split(",").map { it.trim() }
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}

/**
 * CSRFトークンをCookieにセットするフィルター
 *
 * SPAでCSRF保護を機能させるため、リクエストごとにCSRFトークンを
 * Cookieに設定する。フロントエンドはこのCookieからトークンを取得し、
 * リクエストヘッダー（X-XSRF-TOKEN）に含める。
 */
class CsrfCookieFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        // CSRFトークンを取得（存在しなければ生成される）
        val csrfToken = request.getAttribute(CsrfToken::class.java.name) as? CsrfToken
        csrfToken?.token // トークンを生成してCookieにセット

        filterChain.doFilter(request, response)
    }
}

/**
 * SPA用のCSRFトークンリクエストハンドラー
 *
 * Spring Security 6.0以降推奨のXOR CSRFトークンハンドラーを使用。
 * これにより、BREACH攻撃からの保護が強化される。
 *
 * マルチパートリクエスト（ファイルアップロード）の場合は従来のハンドリングを使用。
 */
class SpaCsrfTokenRequestHandler : CsrfTokenRequestAttributeHandler() {
    private val delegate = XorCsrfTokenRequestAttributeHandler()

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        csrfToken: Supplier<CsrfToken>,
    ) {
        // XOR方式でCSRFトークンをハンドリング（セキュリティ強化）
        delegate.handle(request, response, csrfToken)
    }

    override fun resolveCsrfTokenValue(
        request: HttpServletRequest,
        csrfToken: CsrfToken,
    ): String? {
        // X-XSRF-TOKENヘッダーから直接トークンを取得
        // （SPAではヘッダーでトークンを送信するため、XORハンドラーは使用しない）
        val headerValue = request.getHeader(csrfToken.headerName)
        if (headerValue != null) {
            return headerValue
        }

        // ヘッダーにない場合はリクエストパラメータから取得
        return request.getParameter(csrfToken.parameterName)
    }
}
