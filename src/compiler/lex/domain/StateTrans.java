package compiler.lex.domain;


public class StateTrans implements Comparable<StateTrans>{
	char input;
	int state;
	public char getInput() {
		return input;
	}
	public void setInput(char input) {
		this.input = input;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
	public StateTrans(char input, int state) {
		super();
		this.input = input;
		this.state = state;
	}
	
	
	
	
	@Override
	public int hashCode() {
		return (state<<8)|(int)input;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateTrans other = (StateTrans) obj;
		if (input != other.input)
			return false;
		if (state != other.state)
			return false;
		return true;
	}
	@Override
	public int compareTo(StateTrans arg0) {
		return Integer.valueOf(this.state).compareTo(Integer.valueOf(arg0.state));
	}
	
}
