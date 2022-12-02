package models;

public class Tulpe4<K, V, X, Z> { // Tulpe class (used to return 4 lists of coordinates line of sight.
	
	private K first;
	private V second;
	private X third;
	private Z fourth;
	
	public Tulpe4(K first, V second, X third, Z fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}
	

	public void setFirst(K first) { this.first = first; }
	public void setSecond(V second) { this.second = second; }
	public void setThird(X third) { this.third = third; }
	public void setFourth(Z fourth) { this.fourth = fourth; }
	
	public K getFirst() { return first; }
	public V getSecond() { return second; }
	public X getThird() { return third; }
	public Z getFourth() { return fourth; }
}
