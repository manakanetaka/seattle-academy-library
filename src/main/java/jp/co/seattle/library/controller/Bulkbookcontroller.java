package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class Bulkbookcontroller {
	final static Logger logger = LoggerFactory.getLogger(Bulkbookcontroller.class);
	@Autowired
	private BooksService booksService;

	@Transactional
	@RequestMapping(value = "/bulkbook", method = RequestMethod.GET)
	public String Book(Model model) {
		return "bulkbook";
	}

	@Transactional
	@RequestMapping(value = "/uplode", method = RequestMethod.POST)
	public String bulkbook(Locale locale, @RequestParam("file") MultipartFile file, Model model) {
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

			String line = null;
			List<BookDetailsInfo> bookList = new ArrayList<BookDetailsInfo>();
			List<String> errorLine = new ArrayList<String>();
			int count = 0;

			while ((line = br.readLine()) != null) {
				final String[] data = line.split(",", -1);
				count++;

				BookDetailsInfo bookInfo = new BookDetailsInfo();
				bookInfo.setTitle(data[0]);
				bookInfo.setAuthor(data[1]);
				bookInfo.setPublisher(data[2]);
				bookInfo.setPublish_date(data[3]);
				bookInfo.setIsbn(data[4]);
				// bookInfo.setEXPOSITION(data[5]);

				boolean empty = data[0].isEmpty() || data[1].isEmpty() || data[2].isEmpty() || data[3].isEmpty();

				boolean day = data[3].matches("^[0-9]{8}+$");

				boolean number = !data[4].equals("") && !data[4].matches("^[0-9]{10}|[0-9]{13}+$");
				if (empty || !day || number) {
					errorLine.add(count + "行目の書籍登録時にエラーが発生しました");
				}
				bookList.add(bookInfo);
			}
			if (bookList.size() == 0) {
				model.addAttribute("errorBulk", "CSVファイルが選択されていない、もしくは中身がありません");
				return "bulkbook";
			}
			if (0 < errorLine.size()) {
				model.addAttribute("errorbulk", errorLine);
				return "bulkbook";
			}

			for (int i = 0; i < bookList.size(); i++) {
				// 書籍情報をbooklistから取得しbookinfoに代入
				BookDetailsInfo bookInfo = bookList.get(i);
				// 書籍情報を登録
				booksService.registBook(bookInfo);
			}

			model.addAttribute("bookList", booksService.getBookList());
			return "home";
		} catch (Exception e) {
			throw new RuntimeException("ファイルが読み込めません", e);
		}
	}
}
