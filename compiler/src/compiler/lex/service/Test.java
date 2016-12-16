package compiler.lex.service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import compiler.lex.Exception.LexException;
import compiler.lex.Exception.MyException;
import compiler.lex.domain.LexError;

public class Test {

	public static void main(String[] args) throws FileNotFoundException, MyException, LexException {
		TokenService tokenService=new TokenServiceImpl();
		List<LexError> errors=new ArrayList<>();
		List<String> tokens=tokenService.getTokenListFromFile("D:/test.toy", errors);
		for(String str:tokens)
		{
			System.out.println(str);
		}
	}
}
