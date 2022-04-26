package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"SELECT id, title, thumbnail_url, author, publisher, publish_date,isbn,EXPOSITION FROM books ORDER BY title",
				new BookInfoRowMapper());

		return getedBookList;
	}

	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT * FROM books where id =" + bookId;

		BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());
		return bookDetailsInfo;
	}

	public void registBook(BookDetailsInfo bookInfo) {

		String sql = "INSERT INTO books (title, author,publisher,publish_date,thumbnail_url,thumbnail_name,EXPOSITION,isbn,upd_date,reg_date) VALUES ('"
				+ bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
				+ bookInfo.getPublish_date() + "','" + bookInfo.getThumbnailUrl() + "','" + bookInfo.getThumbnailName()
				+ "','" + bookInfo.getEXPOSITION() + "','" + bookInfo.getIsbn() + "'," + "'now()'," + "'now()');";

		jdbcTemplate.update(sql);
	}

	public void deleteBook(Integer bookId) {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "DELETE FROM books WHERE id = " + bookId + ";";
		jdbcTemplate.update(sql);
	}

	//
	// @return 最新の書籍情報を取得
	public int MaxId() {
		String sql = "SELECT MAX(id) FROM books";
		int MaxId = jdbcTemplate.queryForObject(sql, int.class);
		return MaxId;
	}
}
