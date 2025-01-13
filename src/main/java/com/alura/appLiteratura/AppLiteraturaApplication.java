package com.alura.appLiteratura;

import com.alura.appLiteratura.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppLiteraturaApplication implements CommandLineRunner {

	@Autowired
	private final Principal principal;

	public AppLiteraturaApplication(Principal principal){
		this.principal = principal;
	}


	public static void main(String[] args) {
		SpringApplication.run(AppLiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		principal.mostrarMenu();

	}
}
