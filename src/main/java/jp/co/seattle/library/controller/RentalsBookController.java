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
public class RentalsBookController {
	final static Logger logger = LoggerFactory.getLogger(DeleteBookController.class);

	@Autowired
	private RentalsService rentalbook;
	@Autowired
	private BooksService booksService;

	@RequestMapping(value = "/RentBook", method = RequestMethod.POST)
	public String rentalsBook(Locale locale, @RequestParam("bookId") Integer bookId, Model model) {
		logger.info("Welcome Rent! The client locale is {}.", locale);

		if (rentalbook.selectRentalBook(bookId) > 0) {
			model.addAttribute("errorrent", "貸出し済みです。");
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		} else {
			rentalbook.getBookInfo(bookId);
			model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

		}
		return "details";
	}

}
