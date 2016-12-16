package compiler.lex.domain;
import java.util.HashMap;
import java.util.Map;

/**
 * 匹配正则表达式
 * @author yang
 *现可支持 | * ^三种符号 2016/11/26
 */
public class DFA_REGEX {
	
	static String NUM="0*1*2*3*4*5*6*7*8*9*";
	public static void main(String args[]) throws DFAException 
	{
//		debug(hashCode(0,'a')==97);
//		debug(hashCode(1,'a')==353);
//		debug(match("ac","a|bc"));
//		debug(!match("abc","a|bc"));
//		debug(match("bc","a|bc"));
//		debug(match("bccccccccccc","bc*"));
//		debug(match("b","bc*"));
//		debug(match("bccccccccc","bc+"));
//		debug(match("bcccccccccd","bc+d"));
//		debug(match("cd","a*c+d"));
//		debug(match("acd","a*c+d"));
//		debug(match("aaaaaaaccccccccccd","a*c+d"));
//		debug(match("a","a*"));
//		debug(match("","a*"));
//		debug(match("a","^"));
//		debug(match("abc","^b*c|d"));
//		debug(match("a+aa","a\\+a+"));
//		debug(match("a","a(ab)*"));
//		debug(match("aabab","a(ab)*"));
//		debug(match("aab","a(ab)+"));
//		debug(match("aabab","a(ab)+"));
//		debug(match("aababcdaa","a(ab)+(cd)*aa"));
//		debug(match("c","(ab)|c"));
//		debug(match("ab","(ab)|c"));
//		debug(preHandle("[abc]"));
//		debug(preHandle("[abc]").equals("a|b|c"));
		debug(preHandle("[a-f]g|e").equals("a|b|c|d|e|fg|e"));
//		debug(preHandle("[a-f123]"));
//		debug(preHandle("[a-f123]").equals("a|b|c|d|e|f|1|2|3"));
//		try {
//			debug(preHandle("[abc").equals("a|b|c"));
//		} catch (DFAException e) {
//			e.printStackTrace();
//
//		}
		debug(match("a12", "[A-Z]|[a-z]a*b*c*d*e*f*g*h*i*j*k*l*m*n*o*p*q*r*s*t*u*v*w*x*y*z*"
				+ "A*B*C*D*E*F*G*H*I*J*K*L*M*N*O*P*Q*R*S*T*U*V*W*X*Y*Z*"+NUM));
		debug(match("12",NUM));
		debug(match("12e+1.e1", "[0-9]"+NUM+"E|e+|-|[0-9]"+NUM+"."+NUM+"E|e+|-|[0-9]"+NUM));		
		debug(match("12.1", "[0-9]"+NUM+".[0-9]"+NUM));
		debug(match("12.1e1", "[0-9]"+NUM+"."+NUM+"E|e+|-|[0-9]"+NUM));
		debug(match("12e+1.1", "[0-9]"+NUM+"E|e+|-|[0-9]"+NUM+".[0-9]"+NUM));
		debug(match("12e+1", "[0-9]"+NUM+"E|e+|-|[0-9]"+NUM));
//		debug(match("//12hello", "\b*//^*"));
		
	}
	static boolean match(String str,String regex) 
	{
		Map<Integer,Integer> dfa=new HashMap<Integer, Integer>();
		int finalState=DFA(regex,dfa);
		int initialState=0;
		for(int i=0;i<str.length();++i)
		{
			if(!dfa.containsKey(hashCode(initialState, str.charAt(i))))
				return false;
			else
				initialState=dfa.get(hashCode(initialState, str.charAt(i)));
		}
		if(initialState==finalState)
			return true;
		else
			return false;
	}
	static int DFA(String regex,Map<Integer,Integer> dfa) 
	{
		try {
			regex=preHandle(regex);
		} catch (DFAException e) {
			e.printStackTrace();
		}
		debug(regex+"的构造如下:\n");
		int initialState=0;
		for(int i=0;i<regex.length();++i)
		{
			if(regex.charAt(i)=='\\')
			{
				dfa.put(hashCode(initialState, regex.charAt(i+1)), ++initialState);
				dfaStateMove(initialState-1,regex.charAt(i+1),initialState);
				i+=1;
			}
			else
			{
				if(i<regex.length()-1&&isSpecial(regex.charAt(i+1)))
				{
					switch(regex.charAt(i+1))
					{	
					case '|':
						if(regex.charAt(i)=='^')
						{
							StringBuilder sb=new StringBuilder();
							for(int k=0;k<256;++k)
							{
								sb.append((char)k);
							}
							regex=regex.substring(0,i)+sb.toString()+regex.substring(i+1);
						}
						dfa.put(hashCode(initialState, regex.charAt(i)), initialState+1);
						dfaStateMove(initialState,regex.charAt(i),initialState+1);
						do{
						dfa.put(hashCode(initialState,regex.charAt(i+2)), initialState+1);
						dfaStateMove(initialState,regex.charAt(i+2),initialState+1);
						i+=2;}while(regex.charAt(i+1)=='|');
						++initialState;
						break;
					case '*':
						if(regex.charAt(i)=='^')
						{
							for(int k=0;k<256;k++)
							{
								dfa.put(hashCode(initialState, (char)k), initialState);
								dfaStateMove(initialState,(char)k,initialState);
							}	
							i+=1;
						}
						else{
						dfa.put(hashCode(initialState, regex.charAt(i)), initialState);
							dfaStateMove(initialState,regex.charAt(i),initialState);
							i+=1;
						}
							break;
					case '+':
						dfa.put(hashCode(initialState, regex.charAt(i)), ++initialState);
						dfaStateMove(initialState-1,regex.charAt(i),initialState);
						dfa.put(hashCode(initialState, regex.charAt(i)), initialState);
						dfaStateMove(initialState,regex.charAt(i),initialState);
						i+=1;
						break;
						default:
							break;
					}
				}
				else
				{
					if(regex.charAt(i)=='^')
					{
						for(int k=0;k<256;k++)
						{
							dfa.put(hashCode(initialState, (char)k), initialState+1);
							dfaStateMove(initialState,(char)k,initialState+1);
						}
						++initialState;
					}
					else if(regex.charAt(i)=='(')
					{
						int rightPatternPOs = regex.indexOf(")", i);
						String str=regex.substring(i+1, rightPatternPOs);
						switch (regex.charAt(rightPatternPOs+1))
						{
						case '*':
							for(int local_i=0;local_i<str.length()-1;++local_i)
							{
								dfa.put(hashCode(initialState,str.charAt(local_i)), ++initialState);
								dfaStateMove(initialState-1,str.charAt(local_i),initialState);
							}
							dfa.put(hashCode(initialState, str.charAt(str.length()-1)), initialState-(str.length()-1));
							dfaStateMove(initialState,str.charAt(str.length()-1), initialState-(str.length()-1));
							initialState-=(str.length()-1);
							i=rightPatternPOs+1;
							break;
						case '+':
							for(int local_i=0;local_i<str.length();++local_i)
							{
								dfa.put(hashCode(initialState,str.charAt(local_i)), ++initialState);
								dfaStateMove(initialState-1,str.charAt(local_i),initialState);
							}
							for(int local_i=0;local_i<str.length()-1;++local_i)
							{
								dfa.put(hashCode(initialState,str.charAt(local_i)), ++initialState);
								dfaStateMove(initialState-1,str.charAt(local_i),initialState);
							}
							dfa.put(hashCode(initialState, str.charAt(str.length()-1)), initialState-(str.length()-1));
							dfaStateMove(initialState,str.charAt(str.length()-1), initialState-(str.length()-1));
							i=rightPatternPOs+1;
							initialState-=(str.length()-1);
							break;
						case '|':
							for(int local_i=0;local_i<str.length()-1;++local_i)
							{
								dfa.put(hashCode(initialState,str.charAt(local_i)), ++initialState);
								dfaStateMove(initialState-1,str.charAt(local_i),initialState);
							}
							if(regex.charAt(rightPatternPOs+2)!='(')
							{
								dfa.put(hashCode(initialState-(str.length()-1), regex.charAt(rightPatternPOs+2)), ++initialState);
								dfaStateMove(initialState-1-(str.length()-1),regex.charAt(rightPatternPOs+2),initialState);
								dfa.put(hashCode(initialState-1, str.charAt(str.length()-1)), initialState);
								dfaStateMove(initialState-1, str.charAt(str.length()-1), initialState);
								i=rightPatternPOs+2;
							}
							else
							{
							}
							break;
						default:
							for(int local_i=0;local_i<str.length();++local_i)
							{
								dfa.put(hashCode(initialState,str.charAt(local_i)), ++initialState);
								dfaStateMove(initialState-1,str.charAt(local_i),initialState);
							}
							i=rightPatternPOs;
						}
					}
					else
					{
						dfa.put(hashCode(initialState, regex.charAt(i)), ++initialState);
						dfaStateMove(initialState-1,regex.charAt(i),initialState);
					}

				}
			}
		}
		return initialState;
	}
	
	static boolean isSpecial(char ch)
	{
		return ch=='|'||ch=='*'||ch=='+';
	}
	
	static int hashCode(int state,char ch)
	{
		state=state<<8;
		return state|(int)ch;
	}
	
	/**
	 * 对正则表达式做预处理，处理[] （）等
	 * @param regex
	 * @return
	 * @throws DFAException 
	 */
	static String preHandle(String regex) throws DFAException
	{
		StringBuilder sb=new StringBuilder();
		int length = regex.length();
		for(int i=0;i<length;++i)
		{
			if(regex.charAt(i)=='[')
			{
				while(++i<length&&regex.charAt(i)!=']')
					{
						if(i+1<length&&regex.charAt(i+1)=='-')
						{
							if(i+2>length||regex.charAt(i+2)==']'||regex.charAt(i)>regex.charAt(i+2))
							{
								throw new DFAException("错误的使用 - ");
							} 
							for(int j=regex.charAt(i);j<=regex.charAt(i+2);++j)
								{
									sb.append((char)j);
									sb.append('|');
								}
							i+=2;
							continue;
						}
//						sb.append(regex.charAt(i)); 
//						sb.append('|');
					}
					if(i==length)
					{
						throw new DFAException("正则表达式有误，缺少匹配的]");
					}
				sb.deleteCharAt(sb.length()-1);//去除最后的|
//				++i;
			}
			else
			{
				sb.append(regex.charAt(i));
			}
		}
		
		return sb.toString();
	}
	
	
	static void debug(Object object)
	{
		System.out.println(object);
	}
	
	static String dfaStateMove(int state,char ch,int anotherState)
	{
		String str=String.format("状态%d...  输入%c ...到达状态%d", state,ch,anotherState);
		debug(str);
		return str;
	}
}