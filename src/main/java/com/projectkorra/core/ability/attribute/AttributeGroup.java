package com.projectkorra.core.ability.attribute;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AttributeGroup {

	public static final AttributeGroup DAMAGE = createBase(Attribute.DAMAGE);
	public static final AttributeGroup SPEED = createBase(Attribute.SPEED);
	public static final AttributeGroup RANGE = createBase(Attribute.RANGE);
	public static final AttributeGroup SELECT_RANGE = createBase(Attribute.SELECT_RANGE);
	public static final AttributeGroup CHARGE_TIME = createBase(Attribute.CHARGE_TIME);
	public static final AttributeGroup COOLDOWN = createBase(Attribute.COOLDOWN);
	public static final AttributeGroup DURATION = createBase(Attribute.DURATION);
	public static final AttributeGroup SIZE = new AttributeGroup("size").addAll("radius", "length", "width", "height", "min_radius", "max_radius", "max_height", "min_height", "min_length", "max_length", "min_width", "max_width");
	public static final AttributeGroup BURN = new AttributeGroup("burn");
	public static final AttributeGroup STAMINA = new AttributeGroup("stamina").add("stamina_cost").add("stamina_drain");
	public static final AttributeGroup RESISTANCE = new AttributeGroup("resistance");
	public static final AttributeGroup HEALTH = new AttributeGroup("health");
	
	private String name;
	private Set<String> attributes = new HashSet<>();

	private AttributeGroup(String name) {
		this.name = name.toLowerCase();
		this.attributes.add(this.name);
	}
	
	public String getName() {
		return name;
	}
	
	public Set<String> getAttributes() {
		return Collections.unmodifiableSet(attributes);
	}
	
	public AttributeGroup add(String attribute) {
		attributes.add(attribute.toLowerCase());
		return this;
	}
	
	public AttributeGroup addAll(String...attributes) {
		for (String attribute : attributes) {
			this.attributes.add(attribute.toLowerCase());
		}
		return this;
	}
	
	public AttributeGroup remove(String attribute) {
		attributes.remove(attribute.toLowerCase());
		return this;
	}
	
	public AttributeGroup removeAll(String...attributes) {
		for (String attribute : attributes) {
			this.attributes.remove(attribute.toLowerCase());
		}
		return this;
	}
	
	private static AttributeGroup createBase(String attribute) {
		return new AttributeGroup(attribute).add("max_" + attribute).add("min_" + attribute);
	}
}
