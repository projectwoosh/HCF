package tk.thewoosh.hcf.faction;

public enum FactionRole {

	LEADER("**"), MOD("*"), RECRUIT("");
	
	private final String identifier;
	
	private int id = -1;
	
	public int getId() {
		if (id == -1)
			for (int i = 0; i < values().length; i++)
				if (values()[i] == this)
					this.id = i;
		return id;
	}
	
	private FactionRole(String s) {
		this.identifier = s;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
}
