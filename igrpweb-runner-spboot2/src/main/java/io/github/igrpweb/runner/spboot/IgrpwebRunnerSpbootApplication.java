package io.github.igrpweb.runner.spboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan(basePackages = {"nosi.core.listener","nosi.core.servlet", "nosi.core.filter"})
@SpringBootApplication
public class IgrpwebRunnerSpbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgrpwebRunnerSpbootApplication.class, args);
	}

}
