package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 * booksテーブルに関する処理を実装する
 * 
 */
@Service
public class BooksService {
	final static Logger logger = LoggerFactory.getLogger(BooksService.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	/**
	 * 書籍リストを取得する
	 * 
	 * @return書籍リスト
	 */

	public List<BookInfo> getBookList() {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> getedBookList = jdbcTemplate.query(
				"SELECT id, title, thumbnail_url, author, publisher, publish_date,isbn,EXPOSITION FROM books ORDER BY title",
				new BookInfoRowMapper());

		return getedBookList;
	}

	/**
	 * 書籍IDに基づく書籍詳細情報を取得する ＠param bookId 書籍ID
	 * 
	 * @return 書籍情報
	 *
	 */

	public BookDetailsInfo getBookInfo(int bookId) {

		// JSPに渡すデータを設定する
		String sql = "SELECT books.id,books.title,books.author,books.publisher,books.publish_date,books.thumbnail_url,books.thumbnail_name,books.reg_date,books.upd_date,books.exposition,books.isbn,rentals.book_id, "
				+ "case " + "When book_id > 0  then '貸し出し中' " + "else '貸し出し可' " + " END AS status" + " FROM books "
				+ "LEFT OUTER JOIN rentals " + "ON books.id = rentals.book_id " + "WHERE books.id = " + bookId;

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

	/**
	 * 書籍を登録する ＠param bookInfo 書籍情報
	 * 
	 */

	public void deleteBook(Integer bookId) {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "DELETE FROM books WHERE id = " + bookId + ";";
		jdbcTemplate.update(sql);
	}

	/**
	 * 書籍を登録する ＠param bookInfo 書籍情報
	 */

	public int MaxId() {
		String sql = "SELECT MAX(id) FROM books";

		int MaxId = jdbcTemplate.queryForObject(sql, int.class);
		return MaxId;
	}

	public void UpBook(BookDetailsInfo bookInfo) {

		String sql = "UPDATE books SET title ='" + bookInfo.getTitle() + "',author='" + bookInfo.getAuthor()
				+ "',publisher='" + bookInfo.getPublisher() + "',publish_date='" + bookInfo.getPublish_date()
				+ "',thumbnail_url='" + bookInfo.getThumbnailUrl() + "',thumbnail_name='" + bookInfo.getThumbnailName()
				+ "',upd_date=now(),EXPOSITION='" + bookInfo.getEXPOSITION() + "',isbn='" + bookInfo.getIsbn()
				+ "'WHERE id= '" + bookInfo.getBookId() + "';";
		jdbcTemplate.update(sql);

	}

	public String uploadbulkbook(String bulkbook, MultipartFile file) {

		return null;
	}

	public List<BookInfo> searchBookList(String key) {

		// TODO 取得したい情報を取得するようにSQLを修正
		List<BookInfo> searchBookList = jdbcTemplate.query(
				"select title,id,author,publisher,publish_date,thumbnail_url from books where title LIKE '%"+ key + "%';",
				new BookInfoRowMapper());

		return searchBookList;

	

}
}
