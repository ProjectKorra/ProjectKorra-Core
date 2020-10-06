package com.projectkorra.core.system.skill;

public class SkillConnection {

	public static enum Type {
		PARENT, CHILD;
	}
	
	private Skill skill;
	private Type connection;
	
	public SkillConnection(Skill skill, Type connection) {
		this.skill = skill;
		this.connection = connection;
	}
	
	public Skill getSkill() {
		return skill;
	}
	
	public Type getType() {
		return connection;
	}
	
	@Override
	public int hashCode() {
		return skill.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof SkillConnection) {
			return this.skill == ((SkillConnection)other).skill;
		}
		
		return false;
	}
	
	public boolean matches(SkillConnection other) {
		return this.skill == other.skill && this.connection == other.connection;
	}
}
