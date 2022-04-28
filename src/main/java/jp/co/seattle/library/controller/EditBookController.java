package jp.co.seattle.library.controller;

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
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller // APIの入り口
public class EditBookController {
	final static Logger logger = LoggerFactory.getLogger(EditBookController.class);

	@Autowired
	private BooksService booksService;

	@Autowired
	private ThumbnailService thumbnailService;

	@Transactional
	@RequestMapping(value = "/editBook", method = RequestMethod.POST)
	public String editBook(Locale locale, @RequestParam("bookId") int bookId, Model model) {
		model.addAttribute("bookInfo", booksService.getBookInfo(bookId));
		return "editBook";
	}

	/**
	 * 書籍情報を登録する
	 * 
	 * @param locale          ロケール情報
	 * @param title           書籍名
	 * @param author          著者名
	 * @param publisher       出版社
	 * @param file            サムネイルファイル
	 * @param publishDate     出版日
	 * @param isbn            ISBN
	 * @param explanatoryText 説明文
	 * @param model           モデル
	 * @return 遷移先画面
	 */

	@Transactional
	@RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
	public String editBook(Locale locale, 
			@RequestParam("bookId")int bookId, 
			@RequestParam("title") String title, 
			@RequestParam("author") String author,
			@RequestParam("publisher") String publisher, 
			@RequestParam("publish_date") String publishDate,
			@RequestParam("isbn") String isbn, 
			@RequestParam("EXPOSITION") String EXPOSITION,
			@RequestParam("thumbnail") MultipartFile file, Model model) {
		logger.info("Welcome updateBooks.java! The client locale is {}.", locale);

		// パラメータで受け取った書籍情報をDtoに格納する。
		BookDetailsInfo bookInfo = new BookDetailsInfo();
		bookInfo.setBookId(bookId);
		bookInfo.setTitle(title);
		bookInfo.setAuthor(author);
		bookInfo.setPublisher(publisher);
		bookInfo.setPublish_date(publishDate);
		bookInfo.setIsbn(isbn);
		bookInfo.setEXPOSITION(EXPOSITION);
		
		

		// クライアントのファイルシステムにある元のファイル名を設定する
		String thumbnail = file.getOriginalFilename();

		if (!file.isEmpty()) {
			try {
				// サムネイル画像をアップロード
				String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
				// URLを取得
				String thumbnailUrl = thumbnailService.getURL(fileName);

				bookInfo.setThumbnailName(fileName);
				bookInfo.setThumbnailUrl(thumbnailUrl);

			} catch (Exception e) {

				// 異常終了時の処理
				logger.error("サムネイルアップロードでエラー発生", e);
				model.addAttribute("bookDetailsInfo", bookInfo);
				return "addBook";
			}
		}

		if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty()) {
			model.addAttribute("errorhissu", "必須項目が未入力です");

		}
		if (!publishDate.matches("^[0-9]{8}+$")) {
			model.addAttribute("errordate", "出版日は半角数字のYYYYMMDD形式で入力してください");

		}
		if (!isbn.matches("^[0-9]{10}||[0-9]{13}+$")) {
			model.addAttribute("errorisbn", "ISBNは半角数字で、１０桁か１３桁で入力してください");

		}
		if ((title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty())
				|| (!publishDate.matches("^[0-9]{8}+$")) || (!isbn.matches("^[0-9]{10}||[0-9]{13}+$"))) {
			model.addAttribute("bookInfo", bookInfo);
			return "editook";
		}
		// 書籍情報を更新する
		booksService.UpBook(bookInfo);

		model.addAttribute("resultMessage", "登録完了");

		// TODO 更新した書籍の詳細情報を表示するように実装
		model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
		return "details";
	}

}
