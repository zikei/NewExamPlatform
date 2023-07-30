package com.example.examPlatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/** ホームコントローラ */
@Controller
@RequestMapping("/ExamPlatform/Report")
public class ReportController {
	/** レポート画面表示 */
	@GetMapping("/{reportId}/Act")
	public String ReportView(@PathVariable Integer reportId, Model model) {
		
		return "report";
	}
}