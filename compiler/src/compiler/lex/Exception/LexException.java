package compiler.lex.Exception;

import java.util.List;

import compiler.lex.domain.LexError;

public class LexException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<LexError> errors;
	int errorCount;
	public LexException(List<LexError> errors, int errorCount) {
		this.errors = errors;
		this.errorCount = errorCount;
	}
	public List<LexError> getErrors() {
		return errors;
	}
	public void setErrors(List<LexError> errors) {
		this.errors = errors;
	}
	public int getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	
}
