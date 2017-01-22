package comp9321.servlet;



public class PairDef <X,Y>{
	public X x; 
	public Y y;

	public PairDef(X x,Y y){
		this.x=x; 
		this.y=y;
	}

	public X getX() {
		return x;
	}

	public Y getY() {
		return y;
	}

}