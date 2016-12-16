package compiler.lex.domain;

public class LexError {
	int lineNumber;
	int linePostion;
	String invailedWord;
	
	public LexError(int lineNumber, int linePostion, String invailedWord) {
		super();
		this.lineNumber = lineNumber;
		this.linePostion = linePostion;
		this.invailedWord = invailedWord;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public int getLinePostion() {
		return linePostion;
	}
	public void setLinePostion(int linePostion) {
		this.linePostion = linePostion;
	}
	public String getInvailedWord() {
		return invailedWord;
	}
	public void setInvailedWord(String invailedWord) {
		this.invailedWord = invailedWord;
	}
	
}
