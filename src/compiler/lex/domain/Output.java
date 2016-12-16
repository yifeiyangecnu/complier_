package compiler.lex.domain;

public class Output {
		String tokenValue;
		TokenColorType tokenColor;
		int lineNumber;
		
		public Output(String tokenValue, TokenColorType tokenColor,
				int lineNumber) {
			super();
			this.tokenValue = tokenValue;
			this.tokenColor = tokenColor;
			this.lineNumber = lineNumber;
		}
		public String getTokenValue() {
			return tokenValue;
		}
		public void setTokenValue(String tokenValue) {
			this.tokenValue = tokenValue;
		}
		public int getLineNumber() {
			return lineNumber;
		}
		public void setLineNumber(int lineNumber) {
			this.lineNumber = lineNumber;
		}
		public Output(String tokenName, TokenColorType tokenColor) {
			super();
			this.tokenValue = tokenName;
			this.tokenColor = tokenColor;
		}
		public String getTokenName() {
			return tokenValue;
		}
		public void setTokenName(String tokenName) {
			this.tokenValue = tokenName;
		}
		public TokenColorType getTokenColor() {
			return tokenColor;
		}
		public void setTokenColor(TokenColorType tokenColor) {
			this.tokenColor = tokenColor;
		}
		
}
