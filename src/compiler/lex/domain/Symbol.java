package compiler.lex.domain;
/**
 * @author yang
 * @version 1.0
 * @created 13-11ÔÂ-2016 9:29:56
 */
public class Symbol {

	private String attributevalue;
	private int lineNumber;
	private int linePostion;
	private String tokenType;
	
	public Symbol(){

	}

	public Symbol(String attributevalue, int lineNumber, int linePostion,
			String tokenType) {
		super();
		this.attributevalue = attributevalue;
		this.lineNumber = lineNumber;
		this.linePostion = linePostion;
		this.tokenType = tokenType;
	}

	public String getAttributevalue() {
		return attributevalue;
	}

	public void setAttributevalue(String attributevalue) {
		this.attributevalue = attributevalue;
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

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}



}