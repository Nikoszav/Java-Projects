package VForPhotoLC.VForPhotoLC.Config;

import VForPhotoLC.VForPhotoLC.services.PhotoUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


        private PhotoUserDetail photoUserDetail;
        @Autowired
        public SecurityConfig(PhotoUserDetail photoUserDetail){
                this.photoUserDetail=photoUserDetail;
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception{
            auth
                    .authenticationProvider(authenticationProvider());
        }
        @Override
            public void configure(HttpSecurity http) throws Exception{

                http
                    .authorizeRequests()
                    .antMatchers("/greece","/spain","/uk","/profile")
                    .authenticated()
                    .and()
                    .formLogin();


                http.csrf().disable();
                http.headers().frameOptions().disable();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider authProvider
                    = new DaoAuthenticationProvider();
            authProvider.setUserDetailsService(photoUserDetail);
            authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
            return authProvider;
        }

}
