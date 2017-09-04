package com.accele.engine;

import java.util.HashMap;
import java.util.Map;

public abstract class SubRegistry implements Indexable {

	protected String registryId;
	protected String localizedId;
	protected Map<String, Indexable> entries;
	
	public SubRegistry(String registryId, String localizedId) {
		this.registryId = registryId;
		this.localizedId = localizedId;
		this.entries = new HashMap<>();
	}
	
	protected boolean canRegister(Indexable entry) {
		if (entries.containsKey(entry.getRegistryId()))
			return false;
		entries.put(entry.getRegistryId(), entry);
		return true;
	}
	
	public abstract <T extends Indexable> void register(T entry);
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	@Override
	public String getLocalizedId() {
		return localizedId;
	}
	
	public Indexable get(String registryId) {
		return entries.get(registryId);
	}
	
	public Indexable[] getEntries() {
		return entries.values().toArray(new Indexable[entries.size()]);
	}
	
}
