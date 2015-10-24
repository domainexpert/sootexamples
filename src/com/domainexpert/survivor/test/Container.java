package com.domainexpert.survivor.test;

/**
 * Figure 6 of the survivor's guide
 */
public class Container {
	private Item item = new Item();

	void setItem(Item item) {
		this.item = item;
	}

	Item getItem() {
		return this.item;
	}
}
