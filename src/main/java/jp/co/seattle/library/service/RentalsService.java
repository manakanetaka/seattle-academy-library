package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 * 
 */
@Service
public class RentalsService {
	final static Logger logger = LoggerFactory.getLogger(RentalsService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * 書籍を登録する 
	 * ＠param bookId 書籍番号
	 */
	public void getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "INSERT INTO rentals (book_id) VALUES (" + bookId + ")";
		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を取得する 
	 * ＠param bookId 書籍番号
	 * @return selectRentalBook
	 */

	public int selectRentalBook(int bookId) {
		// TODO SQL生成
		String sql = "SELECT book_id From rentals WHERE book_id=" + bookId;

		try {
			int selectRentalBook = jdbcTemplate.queryForObject(sql, int.class);
			return selectRentalBook;
		} catch (Exception e) {
			return -1;
		}
	}
}
