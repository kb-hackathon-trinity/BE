package org.trinity.be.auth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Log4j
@Configuration
@ComponentScan(basePackages = {"org.trinity.be"})
@RequiredArgsConstructor
public class SecurtiyConfig extends WebSecurityConfigurerAdapter{


}
