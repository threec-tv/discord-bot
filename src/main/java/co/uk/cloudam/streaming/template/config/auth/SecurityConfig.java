//package co.uk.cloudam.streaming.template.config.auth;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
//import org.springframework.security.oauth2.core.OAuth2TokenValidator;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.security.oauth2.jwt.JwtDecoders;
//import org.springframework.security.oauth2.jwt.JwtValidators;
//import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//
//import java.util.Collection;
//
////DISABLED
////@EnableWebSecurity
////@EnableGlobalMethodSecurity(prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    public static final String AUTHORITY_PREFIX = "PERMISSION_";
//    @Value("${auth0.audience}")
//    private String audience;
//    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
//    private String issuer;
//
//    @Bean
//    JwtDecoder jwtDecoder() {
//        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
//            JwtDecoders.fromOidcIssuerLocation(issuer);
//
//        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
//        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
//        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
//
//        jwtDecoder.setJwtValidator(withAudience);
//
//        return jwtDecoder;
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//            .authorizeRequests()
//            .mvcMatchers("/actuator/**").hasAuthority("PERMISSION_read:actuator")
//            .and()
//            .oauth2ResourceServer(oauth2ResourceServer ->
//                                      oauth2ResourceServer
//                                          .jwt(jwt ->
//                                                   jwt.jwtAuthenticationConverter(getJwtAuthenticationConverter())));
//
//    }
//
//    private Converter<Jwt, AbstractAuthenticationToken> getJwtAuthenticationConverter() {
//        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(getJwtGrantedAuthoritiesConverter());
//        return jwtAuthenticationConverter;
//    }
//
//    private Converter<Jwt, Collection<GrantedAuthority>> getJwtGrantedAuthoritiesConverter() {
//        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
//        converter.setAuthorityPrefix(AUTHORITY_PREFIX);
//        converter.setAuthoritiesClaimName("permissions"); // from JWT token
//        return converter;
//    }
//
//}
