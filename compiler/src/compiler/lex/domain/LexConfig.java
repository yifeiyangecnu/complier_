package compiler.lex.domain;

import java.util.Arrays;

import compiler.lex.Exception.MyException;

public class LexConfig {
		static String[] keywords={"else","if","int","real","then","while"};
		static String[] operators={"+","-","*","/","=","==","<","<=",">",">=","!="};
		static String[] delimiters={"(",")","{","}",";"};
		public static  boolean isKeyWord(String word)
		{
			Arrays.sort(keywords);
			return Arrays.binarySearch(keywords, word)>=0;
		}
		
		public static boolean isOpeartor(String word)
		{
			Arrays.sort(operators);
			return Arrays.binarySearch(operators, word)>=0;
		}
		
		public static boolean isDelimiter(String word)
		{
			Arrays.sort(delimiters);
			return Arrays.binarySearch(delimiters, word)>=0;
		}
		
		public static boolean isIdentifier(String word)
		{
//			if(null==word)
//				throw new MyException("wordŒ™null");
		return 	DFA_REGEX.match(word, "[A-Z]|[a-z]a*b*c*d*e*f*g*h*i*j*k*l*m*n*o*p*q*r*s*t*u*v*w*x*y*z*"
				+ "A*B*C*D*E*F*G*H*I*J*K*L*M*N*O*P*Q*R*S*T*U*V*W*X*Y*Z*0*1*2*3*4*5*6*7*8*9*");
//			return word.matches("[a-zA-Z]+[a-zA-Z0-9]*");
		}
		public static boolean isInt(String word) 
		{
//			if(null==word)
//				throw new MyException("wordŒ™null");
			return word.matches("0*1*2*3*4*5*6*7*8*9*");
//			return DFA_REGEX.match(word, "[0-9]+");

		}
		
		public static boolean isReal(String word)
		{
//			if(null==word)
//				throw new MyException("wordŒ™null");
			return DFA_REGEX.match(word, "[0-9]"+DFA_REGEX.NUM+"E|e+|-|[0-9]"+DFA_REGEX.NUM+"."+DFA_REGEX.NUM+"E|e+|-|[0-9]"+DFA_REGEX.NUM)
			||DFA_REGEX.match(word, "[0-9]"+DFA_REGEX.NUM+".[0-9]"+DFA_REGEX.NUM)
			||DFA_REGEX.match(word, "[0-9]"+DFA_REGEX.NUM+"."+DFA_REGEX.NUM+"E|e+|-|[0-9]"+DFA_REGEX.NUM)
			||DFA_REGEX.match(word, "[0-9]"+DFA_REGEX.NUM+"E|e+|-|[0-9]"+DFA_REGEX.NUM+".[0-9]"+DFA_REGEX.NUM)
			||DFA_REGEX.match("12e+1", "[0-9]"+DFA_REGEX.NUM+"E|e+|-|[0-9]"+DFA_REGEX.NUM);
//			return word.matches("[0-9]+([Ee][\\+\\-]?[0-9]+)|[0-9]+\\.[0-9]+[[Ee][\\+\\-]?[0-9]+]?");

		}
		
		private static boolean isExponent(String word) throws MyException
		{
			if(null==word)
				throw new MyException("wordŒ™null");
			return word.matches("[Ee][\\+\\-]?[0-9]+");
		}
		public static boolean isKeywordOrOperatorOrDelimeiter(String word)
		{
			return isOpeartor(word)||isDelimiter(word)||isKeyWord(word);
		}
		public static boolean isIntOrRealOrIdentifier(String word) 
		{
			return (isInt(word)||isReal(word)||isIdentifier(word))&&!isKeywordOrOperatorOrDelimeiter(word);
		}
		public static void main(String args[]) throws MyException
		{
			//isKeyWord≤‚ ‘
			println(isKeyWord("real"));
			println(!isKeyWord("reall"));
			println(isKeyWord("int"));
			println(!isKeyWord("adsfd"));
			//isOpeartor≤‚ ‘
			println(isOpeartor("+"));
			println(isOpeartor("-"));
			println(!isOpeartor("++"));
			//isDelimiter≤‚ ‘
			println(isDelimiter(";"));
			//isReal≤‚ ‘
			println(isReal("1.5")); 
			println(isReal("1555.005669"));
			println(isReal("2E6"));
			//isExponent≤‚ ‘
			println(isExponent("e+1"));
			//isKeywordOrOperatorOrDelimeiter≤‚ ‘
			println(!isKeywordOrOperatorOrDelimeiter(""));
		}
		
		
		static void println(Object str)
		{
			System.out.println(str);
		}
}
