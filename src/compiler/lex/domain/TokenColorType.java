package compiler.lex.domain;

public enum TokenColorType {
		GREEN(1,"green"),PURPLE(2,"purple"),BLUE(3,"blue"),BLACK(4,"black"),RED(5,"red");
		int colorNum;
		String colorStr;
		private TokenColorType(int colorNum, String colorStr) {
			this.colorNum = colorNum;
			this.colorStr = colorStr;
		}
		public int getColorNum() {
			return colorNum;
		}
		public void setColorNum(int colorNum) {
			this.colorNum = colorNum;
		}
		public String getColorStr() {
			return colorStr;
		}
		public void setColorStr(String colorStr) {
			this.colorStr = colorStr;
		}
		
}
