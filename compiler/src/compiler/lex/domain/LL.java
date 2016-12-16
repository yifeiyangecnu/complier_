package compiler.lex.domain;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Stack;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import java.util.Set;

public class LL 
{
    
    
	private LinkedHashMap<String,String> LLmap=new LinkedHashMap<String, String>();
	private Table<String,String,String>  LLanaTable = HashBasedTable.create();
    public ArrayList<String>nonterminals=new ArrayList<String>();//非终结符
    public List<String> terminals=new ArrayList<>();
    ArrayList<String>values = new ArrayList<String>();
    public int row=0;
    public int column=0;
	
    
    public LL()
	{
		LLmap.put("program","compoundstmt");
		LLmap.put("stmt","ifstmt | whilestmt | assgstmt | compoundstmt");
		LLmap.put("compoundstmt","{ stmts }");
		LLmap.put("stmts","stmt stmts | ε");
		LLmap.put("ifstmt","if ( boolexpr ) then stmt else stmt");
		LLmap.put("whilestmt","while ( boolexpr ) stmt");
		LLmap.put("assgstmt","ID = arithexpr ;");
		LLmap.put("boolexpr","arithexpr boolop arithexpr");
		LLmap.put("boolop","< | > | <= | >= | ==");
		LLmap.put("arithexpr","multexpr arithexprprime");
		LLmap.put("arithexprprime","+ multexpr arithexprprime | - multexpr arithexprprime | ε");
		LLmap.put("multexpr","simpleexpr multexprprime");
		LLmap.put("multexprprime","* simpleexpr multexprprime | / simpleexpr multexprprime | ε");
		LLmap.put("simpleexpr","ID | NUM | ( arithexpr )");
		terminals.add("|");
		terminals.add("{");
		terminals.add("}");
//		terminals.add("ε");
		terminals.add("if");
		terminals.add("(");
		terminals.add(")");
		terminals.add("then");
		terminals.add("else");
		terminals.add("while");		
		terminals.add("ID");
		terminals.add("=");
		terminals.add("<");
		terminals.add(">");
		terminals.add(">=");
		terminals.add("<=");
		terminals.add("==");
		terminals.add("+");
		terminals.add("-");
		terminals.add("*");
		terminals.add("/");
		terminals.add("NUM");
	}
    //消除左递归
	public List<String> simplify()
	{
		List<String> simpProductions=new ArrayList<>();
		 simplify(LLmap);
		 Iterator<Map.Entry<String,String>>entries=LLmap.entrySet().iterator();
			logInfo("消除左递归后的产生式为:\n");
			while(entries.hasNext())
			{
			    Map.Entry<String,String>entry=entries.next();
			    logInfo(entry.getKey()+"-->"+entry.getValue()+"\n");
			    simpProductions.add(entry.getKey()+"-->"+entry.getValue()+"\n");
			}
			return simpProductions;
	}
	protected HashMap<String, String> simplify(LinkedHashMap<String, String> LLmap)
	{
		LinkedHashMap<String, String> simplifymap=new LinkedHashMap<String, String>();
		
		ArrayList<String> KeyList = new ArrayList<String>(); 
		
		Set<Entry<String, String>> set = LLmap.entrySet(); 
		Iterator<Entry<String, String>> i = set.iterator();
		while(i.hasNext())
		{
			Map.Entry<String, String> entry=(Map.Entry<String, String>)i.next();
			KeyList.add(entry.getKey());
		}
		for(int x=0;x<KeyList.size();x++)
		{
			String key=KeyList.get(x);
			String value=(String) LLmap.get(key).toString();
			if(!value.contains(key))continue;
			else
			{
				if(!value.contains("|"))
				{
					int tokenposition=value.indexOf(key);
					if(tokenposition!=0)continue;
					else
					{
						if(value.substring(key.length(), key.length()+1)!=" ")continue;//同样前缀字符串
						else 
						{
							String newkeyvalue,key_,value_,afterkey;
							afterkey=value.substring(key.length()+1,value.length()-1);
							newkeyvalue=key+"'";//key的新产生式
							key_=newkeyvalue;
							value_=afterkey+" "+newkeyvalue+" "+"|"+" "+"ε";
							LLmap.put(key, newkeyvalue);
							LLmap.put(key_, value_);//消除了key的左递归
						}
					}
				}
				else
				{
					ArrayList<String>production=new ArrayList<String>();//不含key和含key不在开头的产生式
					ArrayList<String>production_=new ArrayList<String>();//含key且在开头的产生式
					
					while(value.contains("|"))
					{
					    int linepos=value.indexOf("|");
					    if(linepos==0)break;
					    String cutout=value.substring(0,linepos);
					    if(!cutout.contains(key))
					    {
					    	production.add(cutout);
					    }
					    else
					    {
					    	int tokenposition=value.indexOf(key);
							if(tokenposition!=0)production.add(cutout);//第一产生式产生了空格
							else production_.add(cutout);
					    }
						value=value.substring(linepos+1,value.length())+"|";
					}

					for(int m=0;m<production.size();m++)
					{
						if(production_.size()!=0)
						production.set(m,production.get(m)+" "+key+"'");
					}
					for(int m=0;m<production_.size();m++)
					{
						production_.set(m,production_.get(m).substring(key.length(),production_.get(m).length())+" "+key+"'");
					}
					String finalproduction="",finalproduction_="",key_="";
					key_=key+"'";
					for(int m=0;m<production.size();m++)
					{
						finalproduction=finalproduction+production.get(m)+"|";
					}
					finalproduction=finalproduction.substring(0,finalproduction.length()-1);//从1开始截,消除了右部第一个产生式的空格
					if(production_.size()!=0)
					{
						for(int m=0;m<production_.size();m++)
							{
								finalproduction_=finalproduction_+production_.get(m)+"|";
							}
						finalproduction_=finalproduction_.substring(0,finalproduction_.length()-2);
						finalproduction_=finalproduction+" "+"|"+" "+"ε";
						LLmap.put(key_, finalproduction_);
					}					
					
					LLmap.put(key, finalproduction);				
				}
			}
		}
		simplifymap=LLmap;
		return (HashMap<String, String>) simplifymap;
	}
	
	public void first()
	{
		Set<Entry<String, String>> set = LLmap.entrySet(); 
		Iterator<Entry<String, String>> it = set.iterator();
		while(it.hasNext())
		{
			Map.Entry<String, String> entry=(Map.Entry<String, String>)it.next();
			nonterminals.add(entry.getKey());
		}	
		for(int i=0;i<nonterminals.size();i++)
		{
			
			String nonterminalelem=nonterminals.get(i);
			String production =  LLmap.get(nonterminalelem);
			logInfo("\n\n\n-----------开始计算"+nonterminalelem+"的first--------------\n");
			first(nonterminalelem,production,nonterminalelem+"-->"+production);
			
		}
		print();
	}
	
	protected void first(String start,String production,String father){
		 if (production.contains("|")){
			  for (String str :production.split("\\|")){
				 if (LLmap.get(start).contains(str))   //会出现进入递归后才需要判断“|”的情况，因此要分开
				  {
					 int nospace = 0;while(str.charAt(nospace)==' ') nospace++;
					 str=str.substring(nospace,str.length()); //此为写入LLanaTable的特殊情况，虽然在后续中会处理空格，此时需要且只能去掉首空格
					 first(start,str,start+"-->"+str);
				  }
				 else
				  first(start,str,father);
			 }
		}
	     else   //不包含|,即要么为单一生生成式，要么为|分解后的生成式
	       {
	    	 for (String str : production.split(" ")){
	    		 if (!str.equals("")){         //split空格有时会生成“”
	    			 if (!nonterminals.contains(str)){
	    				 LLanaTable.put(start, str, father);
	    				 break;
	    			 }
	    			 else                           {
	    			  String tmp_production = LLmap.get(str);
	    			  first(start,tmp_production,father);
	    			  break;
	    		     }
	    		 }  
	    	 }
	        }
		}
	public ArrayList<String> getfirst(String ntm)
	{
	    ArrayList<String> res = new ArrayList<>();
		Set<String>terminals = LLanaTable.columnKeySet();
		Iterator<String>i = terminals.iterator();
		 logInfo("该非终结符的first有\n");
	     while(i.hasNext())
	     {
	    	 String tmp = i.next();
	    	if (LLanaTable.get(ntm, tmp)!=null )
	    	{
	    		res.add(tmp);
	    		logInfo(tmp);
	    	}
	     }
	     return res;
	}
	private void print()
	{
		
		///输出first
		logInfo("各非终结符的first以及对应生成式:\n");
		Set<Cell<String, String, String>> ana_Set  = LLanaTable.cellSet() ;
	    Iterator<Cell<String, String, String>> ana_iter = ana_Set.iterator();
	    while(ana_iter.hasNext()){
	    	logInfo(ana_iter.next());
	    	
	    }
	}
	public void follow()
	{
		logInfo("\n\n\n-----------开始计算follow--------------\n");
		Set<Entry<String, String>> set = LLmap.entrySet(); 
		Iterator<Entry<String, String>> it = set.iterator();
		while(it.hasNext())
		{
			
			Map.Entry<String, String> entry=(Map.Entry<String, String>)it.next();
			values.add(entry.getKey()+" "+entry.getValue());
		}	
		for (String str :nonterminals)
		{
			if (LLanaTable.get(str, "ε")!=null)
			{
				logInfo("\n\n\n\n\n"+str+"包含ε，开始计算follow\n");
			    follow(str,str);
			    print_follow(str);
			}
		}
		
		
		
	}
	//father主持迭代，mother记录源头
	private  void follow (String str,String mother){
		
		logInfo("\n\n\n\n\n" +"开始计算follow("+str+")\n");
		int count = 1;
       for (int i = 0;i < values.size() ;i++){ 
    	  //values数组包含了生成式全部
    	   String cut = values.get(i).substring(values.get(i).indexOf(" ")+1, values.get(i).length());
    	   //cut为右半部分
    	   if (cut.contains(str) )
    	   {  
    		   logInfo("\n"+str+"----相关生成式"+count+++"\n");   
    		   logInfo(cut);
    		   String father = values.get(i).substring(0, values.get(i).indexOf(" "));
    		   String[] parts = cut.split(" ");
       		   for (int j = 0 ;j<parts.length ; j++)
    		   {
    			   if (parts[j].equals(str))
    			   {
    				   
    				  if (j==parts.length-1 )
    				  {
    					  if (!father.equals(mother))
  				    	{
  				    	 logInfo("后面为空，开始计算follow("+father+")\n");  
  				    	 follow(father,mother);
  				    	}
  				    	else
  				    	 logInfo("非终结符计算自我的follow，此迭代终结\n");
    				  }
    				  else 
    				  {  
    				    int p = j; //这里不直接使用j，因为非终结符可能出现多次;
    				    p++;       //为了方便，直接默认间隔为一个空格，如果后面有要求可以在输入时便将所有多个空格间隔改为单空格,也可在此处更改
    				    String tmp = parts[p];
    				    if (tmp.equals("|") || tmp.equals("ε"))
    				    {
    				    	if (!father.equals(mother))
    				    	{
    				    	 logInfo("后面为空，开始计算follow("+father+")\n");  
    				    	 follow(father,mother);
    				    	}
    				    	else
    				    	 logInfo("非终结符计算自我的follow，此迭代终结\n");
    				    }
    				    else 
    				    {
    				    	if (nonterminals.contains(tmp))
    				    	{
    				    		logInfo("后面为非终结符，将first("+tmp+")加入follow\n");
    				    		ArrayList<String> firsts = getfirst(tmp);
    				    		 for (String first : firsts)
    				    		 {
    				    			 if (!LLanaTable.contains(mother, first))
    				    			 {
    				    			  LLanaTable.put(mother, first, mother+"-->ε");
    				    			  logInfo("将"+mother+"-->ε"+"加入到("+mother+","+first+")中\n");
    				    			 }
    				    			 else
        				    		 {
        				    			 logInfo("此follow值已计算\n");
        				    		 }
    				    		 }
    				    	}
    				    	else
    				    	{
    				    		 String first  = tmp ;
    				    		 if (!LLanaTable.contains(mother,first))
				    			 {
				    			   LLanaTable.put(mother, first, mother+"-->ε");
				    			   logInfo("将"+mother+"-->ε"+"加入到("+mother+","+first+")中\n");
				    			 }
    				    		 else
    				    		 {
    				    			 logInfo("此follow值已计算\n");
    				    		 }
    				    		
    				    	}
    				       
    				    }
    				    
    				  }
    			   }
    			   
    		   }
    		   
    	   }
    	   
    	   
       }
       
	}
    public void print_follow(String ntm)
    {
    	Map<String,String>tmp = LLanaTable.rowMap().get(ntm);
    	Set<Entry<String, String>> set = tmp.entrySet(); 
		Iterator<Entry<String, String>> it = set.iterator();
		String res = "";
		while(it.hasNext())
		{
	      Map.Entry<String,String>king=it.next();
		  if ( king.getValue().equals(ntm+"-->"+"ε") && !king.getKey().equals("ε"))
		   {
			  res = res+king.getKey()+",";
		   }
		}	
    	logInfo("follow("+ntm+")=    "+res.substring(0,res.length()-1)+"\n");
    	
    }
	
	public List<String> print_table()
	{
		Set<String>nonterminalset=LLanaTable.rowKeySet();
		row=nonterminals.size();
		column=terminals.size();
		List<String> productions=new ArrayList<>();
		for(int i=0;i<row*column;++i)
		{
			productions.add("");
		}
//		for(String str:nonterminals)
//			System.err.println(str);
		Iterator<String>nonit=nonterminalset.iterator();
		
		while(nonit.hasNext())
		{
			String nonterminal=nonit.next();
			logInfo(nonterminal+"   ");
			Map<String,String>terminalAndproduction=LLanaTable.row(nonterminal);
			Set<Entry<String,String>>tp=terminalAndproduction.entrySet();
			Iterator<Entry<String,String>>tpit=tp.iterator();
			while(tpit.hasNext())
			{
				Map.Entry<String, String> entry=(Map.Entry<String, String>)tpit.next();
				String terminal=entry.getKey();
				String production=entry.getValue();
				if(!terminal.equals("ε"))
				{
					productions.set(nonterminals.indexOf(nonterminal)*column+terminals.indexOf(terminal), production);
				}
				logInfo(terminal+" PRODUCTION: "+production+"   ");
				int arrowpos=production.indexOf("-->");
				String productionelem=production.substring(arrowpos+3, production.length());
				//log(productionelem+"    2222");
				String temp[]=new String[productionelem.length()];
				temp=productionelem.split(" ");
				//for(int i=0;i<temp.length;i++)
					//log(temp[i]+"22211");
			}
			logInfo("\n");
		}
		return productions;
	}
	
	public void analysis() throws IOException
	{
		//输入系统
		Scanner s = new Scanner(System.in); 
	     logInfo("请输入字符串：\n"); 
	    int linecount=1;//统计行数
	    String sentence="";
		while (true) 
		{
			String currentstr=s.nextLine();
			if (currentstr.equals("q")) break; 
			sentence = sentence+currentstr+'\n';
			linecount++;
		}
		sentence = sentence+"$";
		logInfo("将要分析的句子为:\n ");
		BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(sentence.getBytes(Charset.forName("utf8"))),Charset.forName("utf8")));
		String line;//每一行的句子
		ArrayList<String>analysisstr=new ArrayList<String>();
		while((line=br.readLine())!=null)
		{
			String []a=new String[line.length()];
			a=line.split(" ");
			for(int i=0;i<a.length;i++)
			{
				if(!a[i].trim().isEmpty())
					analysisstr.add(a[i]);
			}
		}
		for(int i=0;i<analysisstr.size();i++)
			logInfo(analysisstr.get(i)+"\n");
		analysis(analysisstr);       
	}
	public List<String> analysis(List<String> analysisstr)
	{
		List<String> errors=new ArrayList<>();
		Stack<String>strstack=new Stack<String>();
		strstack.push("$");
		strstack.push("program");//非终结符栈
			
		logInfo("栈:                                 "+"输入:                                   "+"动作:                              \n");
		int linecount=1;
		int analysisId=0;
		String analysisElem=analysisstr.get(analysisId);
		while(analysisId!=analysisstr.size()-1)
		{
			//logln(analysisId+"      ~!!!!!!!!222");
			//栈信息
			for(int x=0;x<strstack.size();x++)
			{
				logInfo(strstack.get(x));
			}
			logInfo("                           ");
			
			//输入信息
			for(int x=analysisId;x<analysisstr.size();x++)
			{
				logInfo(analysisstr.get(x));                          
			}
			logInfo("                           ");
			
			
			String nonterminal=strstack.peek();
			if(!LLanaTable.containsRow(nonterminal))//栈顶有匹配的终结符时，弹出，并继续下一个字符的分析，若该终结符不匹配，亦是弹出
		     {
			   if(nonterminal.equals(analysisElem))
			  {
				matchaction(analysisElem);
				analysisId++;
				analysisElem=analysisstr.get(analysisId);
				strstack.pop();
				if(nonterminal.equals(";")||nonterminal.equals("{")||nonterminal.equals("}"))linecount++;
				continue;
			 }
			   else 
			   {
				   strstack.pop();
			   }
		     }
			Map<String,String>terminalAndproduction=LLanaTable.row(nonterminal);
			Set<Entry<String,String>>tp=terminalAndproduction.entrySet();
			Iterator<Entry<String,String>>tpit=tp.iterator();
			while(tpit.hasNext())//遍历当前的非终结符的产生式
			{
				Map.Entry<String, String> entry=(Map.Entry<String, String>)tpit.next();
				String terminal=entry.getKey();//first,follow
				String production=entry.getValue();////first,follow
				int arrowpos=production.indexOf("-->");
				String productionelem=production.substring(arrowpos+3, production.length());//截取产生式
				if(!analysisElem.equals(terminal))
				{
					if(!tpit.hasNext())//当遍历完当前非终结符的产生式还没有对应的终结符时，报错，并跳过当前的终结符
					{
						int size=strstack.size();
						String beforetop="";
						if(size-2>=0)
							{
							 beforetop=strstack.get(size-2);
                            if(analysisElem.equals(beforetop))
                        	  {
                        	    strstack.pop();
                        	    jumptop(nonterminal);
                        	    continue;
                        	  }
							}
						if(analysisElem.equals("int")||analysisElem.equals("real"))
							{
							logInfo("变量声明: "+analysisElem+"\n");
							}		
						else
							{
							error(linecount,analysisElem, errors);
							}
						analysisId++;
						analysisElem=analysisstr.get(analysisId);
					}
					else continue;
				}
				else
				{//当存在匹配的终结符时，就把该终结符对应的产生式压入栈中
					strstack.pop();
					String rightstr[]=new String[productionelem.length()];
					rightstr=productionelem.split(" ");
					for(int l=rightstr.length;l>0;l--)
						{
						    if(rightstr[l-1].equals("ε"))continue;
						    else strstack.push(rightstr[l-1]);
						}
					outputaction(production);	
					break;
				}
			}
		}
		for(int x=0;x<strstack.size();x++)
		{
			logInfo(strstack.get(x));
		}
		logInfo("                           ");
		
		//输入信息
		for(int x=analysisId;x<analysisstr.size();x++)
		{
			logInfo(analysisstr.get(x));
		}
		logInfo("                           ");
		return errors;
		}
	
	
	protected void jumptop(String str)
    {
    	logInfo("栈顶之下的一个字符匹配跳过当前栈顶字符: "+str+"\n");
    }
	protected void outputaction(String str)
	{
		logInfo("输出 "+str);
		logInfo("                              \n");
	}
	protected void matchaction(String str)
	{
		logInfo("匹配 "+str);
		logInfo("                              \n");
	}
    protected void error(int i ,String str, List<String> errors)
    {
    	logErr("第"+i+"行"+str+" 出错，跳过当前字符\n");
    	errors.add("第"+i+"行"+str+" 出错，跳过当前字符");
    }
    
    final String logPath="d:/log/";
    final String infoLogPath=logPath+"info/";
    final String errLogPath=logPath+"err/";
    void logInfo(Object object)
    {
    	System.out.print(object);
//	 	log(infoLogPath,"logInfo_",object.toString());    	
    }
    
    void logErr(Object object)
    {
	 	System.err.print(object);
	// 	log(errLogPath,"logErr_",object.toString());
    }
    
    void log(String pathName,String logName,String str)
    {
    	Date date=new Date();
	 	String filename = logName+date.getTime()/60000+".txt";
		File[] files=new File(pathName).listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(null==name)
					return false;
				return !name.equals(filename);
				
			}
		});
		for(File f:files)
		{
			f.delete();
		}
		try {
			FileWriter fileWriter=new FileWriter(new File(pathName+filename),true);
			fileWriter.write(str);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
