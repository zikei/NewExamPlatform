package com.example.examPlatform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.examPlatform.entity.Account;
import com.example.examPlatform.repository.AccountRepository;

@SpringBootApplication
public class NewExamPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewExamPlatformApplication.class, args);
//		SpringApplication.run(NewExamPlatformApplication.class, args).getBean(NewExamPlatformApplication.class).execute();
	}
	
	@Autowired
    AccountRepository accountRepo;

    @Autowired
    PasswordEncoder passwordEncoder;
	
	private void execute() {
		Account user = new Account(null, "user", "password", "", true);
		executeInsert(user);
	}
	
	private void executeInsert(Account user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = accountRepo.save(user);
		
		System.out.println("登録したデータ：" + user);
	}
}