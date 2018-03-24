package cn.it8090.solrdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
/*@EnableAutoConfiguration*/
@ComponentScan(basePackages = "cn.it8090.solrdemo")
public class SolrdemoApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(SolrdemoApplication.class, args);
	}



}
