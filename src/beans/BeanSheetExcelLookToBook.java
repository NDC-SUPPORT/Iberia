package beans;

public class BeanSheetExcelLookToBook {
	
	private String user = "";
	private Long look = new Long(0);
	private Long book = new Long(0);

	public BeanSheetExcelLookToBook() {
		
	}
		
	public BeanSheetExcelLookToBook(String user, Long look, Long book) 
	{
		this.user = user;
		this.look = look;
		this.book = book;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Long getLook() {
		return look;
	}

	public void setLook(Long look) {
		this.look = look;
	}

	public Long getBook() {
		return book;
	}

	public void setBook(Long book) {
		this.book = book;
	}
}
