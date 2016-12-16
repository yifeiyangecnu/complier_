package compiler.lex.domain;

import java.util.Arrays;

import compiler.lex.Exception.MyException;

public enum TokenType  {
	IF("if","if"),ELSE("else","else"),INT("int","int"),REAL("real","real"),THEN("then","then"),WHILE("while","while"),
	PLUS("+","+"),SUB("-","-"),TIMES("*","*"),DIVIDES("/","/"),ASSIGN("=","="),
	EQUALS("==","=="),LESS_THAN("<","<"),GREATE_THAN(">",">"),
	LESS_THAN_OR_EQUALS("<=","<="),GREATE_THAN_OR_EQUALS(">=",">="),
	NOT_EQUALS("!=","!="),
	LPAREN("(","("),RPAREN(")",")"),
	LBRACE("{","{"),RBRACE("}","}"),SEMICOLON(";",";"),
	IDENTIFIER("id","ID"),NUM("num","NUM"),UNFAIR("unfair","UNFAIR")/*不符合词法*/,
	TESTTOKEN(String.valueOf((char)127),"");//保持最大，用于比较
	private String tokenName;
	private String token;
	private TokenType(String token,String tokenName)
	{
			this.token=token;
			this.tokenName=tokenName;
	} 
	
	public static String getTokenNameByToken(String token) throws MyException
	{
		TokenType.TESTTOKEN.setToken(String.valueOf((char)127));//保持最大
		TokenType[] tokenTypes = TokenType.values();
		Arrays.sort(tokenTypes, (arg0,arg1)->{
			return arg0.token.compareTo(arg1.token);
		});
		int pos=Arrays.binarySearch(tokenTypes, myTestToken(token), (arg0,arg1)->{
			return arg0.token.compareTo(arg1.token);
		});
		if(pos<0||pos==tokenTypes.length-1)
			throw new MyException("未定义的token:"+token);
		else
			return tokenTypes[pos].tokenName;
	}
	
	public static TokenType myTestToken(String token)
	{
		TokenType.TESTTOKEN.setToken(token);
		return TokenType.TESTTOKEN;
	}
	public String getTokenName() {
		return tokenName;
	}
	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	
	public static void main(String args[]) throws MyException
	{
		System.out.println( getTokenNameByToken(";"));
		System.out.println( getTokenNameByToken("}"));
		System.out.println(String.valueOf((char)127));
	}
}
