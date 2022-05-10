package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.RentalsService;

//import jp.co.seattle.library.service;
/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 * 
 */
@Controller
public class ReturnBookController {
	final static Logger logger = LoggerFactory.getLogger(ReturnBookController.class);

	@Autowired
	private BooksService booksService;
	@Autowired
	private RentalsService rentalbook;

	@RequestMapping(value = "/ReturnBook", method = RequestMethod.POST)
	public String ReturnBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
		logger.info("Welcome Return! The client locale is {}.", locale);

		if (rentalbook.selectRentalBook(bookId) < 0) {
			model.addAttribute("errorrent", "貸出しされていません。");
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		} else {
			rentalbook.ReturnbookInfo(bookId);
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		}
		return "details";
	}
}