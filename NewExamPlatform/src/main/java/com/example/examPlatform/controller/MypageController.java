package com.example.examPlatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/** マイページコントローラ */
@Controller
@RequestMapping("/ExamPlatform/{userName}")
public class MypageController {
	/** マイページを表示 */
	@GetMapping
	public String Mypage(@PathVariable String userName) {
		return "mypage";
	}
}
