package org.domainexpert.survivor.test;

/**
 * Figure 8 of the survivor's guide
 */
public class ContainerUser {
	
	public static void main(String[] args) {
		new ContainerUser().go();
	}
	
	public void go() {
		Container c1 = new Container();
		Item i1 = new Item();
		c1.setItem(i1);
		
		Container c2 = new Container();
		Item i2 = new Item();
		c2.setItem(i2);
		
		@SuppressWarnings("unused")
		Container c3 = c2;
	}
}
