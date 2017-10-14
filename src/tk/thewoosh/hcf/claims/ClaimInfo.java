package tk.thewoosh.hcf.claims;

import tk.thewoosh.hcf.faction.Faction;

public class ClaimInfo {

	private final ClaimType type;
	private final Faction faction;
	
	public ClaimInfo(ClaimType type, Faction faction) {
		this.type = type;
		this.faction = faction;
	}
	
	public ClaimInfo(ClaimType type) {
		this(type, null);
	}
	
	public String getFaction() {
		return faction != null ? faction.getName() : "";
	}
	
	public Faction faction() {
		return faction;
	}
	
	public ClaimType getType() {
		return type;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof ClaimInfo ? ((ClaimInfo) obj).getType() == type || ((ClaimInfo) obj).faction() == faction : false;
	}
	
}
