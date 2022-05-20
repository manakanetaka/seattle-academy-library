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

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class SearchBookController {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);

	@Autowired
	private BooksService booksService;

	@RequestMapping(value = "/searchBook", method = RequestMethod.POST)
	public String searchBookList(Locale locale, @RequestParam("search1") String key,@RequestParam("radio") String radio,Model model) {
		logger.info("Welcome delete! The client locale is {}.", locale);

		if (radio.equals("partmatch")){
			model.addAttribute("bookList", booksService.searchBookList(key));
			
		}else if(radio.equals("match")){
			model.addAttribute("bookList", booksService.matchBookList(key));
		

		}
		return "home";
	}
}
	