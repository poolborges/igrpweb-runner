package io.github.igrpweb.runner.spboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

//@ServletComponentScan(basePackages = {"nosi.core.listener","nosi.core.servlet", "nosi.core.filter"})
@ComponentScan
@SpringBootApplication
public class IgrpwebRunnerSpbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgrpwebRunnerSpbootApplication.class, args);
	}

}
