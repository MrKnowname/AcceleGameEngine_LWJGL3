package com.accele.engine;

import java.util.HashMap;
import java.util.Map;

import com.accele.engine.gfx.Model;
import com.accele.engine.gfx.Shader;
import com.accele.engine.gfx.Texture;
import com.accele.engine.io.Input;
import com.accele.engine.property.Property;
import com.accele.engine.state.State;

public final class Registry {

	private Map<String, Indexable> entries;
	private Map<String, State> states;
	private Map<String, Shader> shaders;
	private Map<String, Model> models;
	private Map<String, Texture> textures;
	private Map<String, Input> inputs;
	private Map<String, Property> properties;
	private Map<String, SubRegistry> subRegistries;
	
	protected Registry() {
		this.entries = new HashMap<>();
		this.states = new HashMap<>();
		this.shaders = new HashMap<>();
		this.models = new HashMap<>();
		this.textures = new HashMap<>();
		this.inputs = new HashMap<>();
		this.properties = new HashMap<>();
		this.subRegistries = new HashMap<>();
	}
	
	private boolean register(Indexable entry) {
		if (entries.containsKey(entry.getRegistryId()))
			return false;
		entries.put(entry.getRegistryId(), entry);
		return true;
	}
	
	public <T extends State> void register(T state) {
		if (!register((Indexable) state)) {
			System.err.println("Could not register state \"" + state.getRegistryId() + "\".");
			return;
		}
		
		states.put(state.getLocalizedId(), state);
	}
	
	public <T extends Shader> void register(T shader) {
		if (!register((Indexable) shader)) {
			System.err.println("Could not register shader \"" + shader.getRegistryId() + "\".");
			return;
		}
		
		shaders.put(shader.getLocalizedId(), shader);
	}
	
	public <T extends Model> void register(T model) {
		if (!register((Indexable) model)) {
			System.err.println("Could not register model \"" + model.getRegistryId() + "\".");
			return;
		}
		
		models.put(model.getLocalizedId(), model);
	}
	
	public <T extends Texture> void register(T texture) {
		if (!register((Indexable) texture)) {
			System.err.println("Could not register texture \"" + texture.getRegistryId() + "\".");
			return;
		}
		
		textures.put(texture.getLocalizedId(), texture);
	}
	
	public <T extends Input> void register(T input) {
		if (!register((Indexable) input)) {
			System.err.println("Could not register input \"" + input.getRegistryId() + "\".");
			return;
		}
		
		inputs.put(input.getLocalizedId(), input);
	}
	
	public <T extends Property> void register(T property) {
		if (!register((Indexable) property)) {
			System.err.println("Could not register property \"" + property.getRegistryId() + "\".");
			return;
		}
		
		properties.put(property.getLocalizedId(), property);
	}
	
	public <T extends SubRegistry> void register(T subRegistry) {
		if (!register((Indexable) subRegistry)) {
			System.err.println("Could not register sub-registry \"" + subRegistry.getRegistryId() + "\".");
			return;
		}
		
		subRegistries.put(subRegistry.getLocalizedId(), subRegistry);
	}
	
	public Indexable get(String registryId) {
		return entries.get(registryId);
	}
	
	public State getState(String localizedId) {
		return states.get(localizedId);
	}
	
	public Shader getShader(String localizedId) {
		return shaders.get(localizedId);
	}
	
	public Model getModel(String localizedId) {
		return models.get(localizedId);
	}
	
	public Texture getTexture(String localizedId) {
		return textures.get(localizedId);
	}
	
	public Input getInput(String localizedId) {
		return inputs.get(localizedId);
	}
	
	public Property getProperty(String localizedId) {
		return properties.get(localizedId);
	}
	
	public SubRegistry getSubRegistry(String localizedId) {
		return subRegistries.get(localizedId);
	}
	
	public Indexable[] getAll() {
		return entries.values().toArray(new Indexable[entries.size()]);
	}
	
	public State[] getStates() {
		return states.values().toArray(new State[states.size()]);
	}
	
	public Shader[] getShaders() {
		return shaders.values().toArray(new Shader[shaders.size()]);
	}
	
	public Model[] getModels() {
		return models.values().toArray(new Model[models.size()]);
	}
	
	public Texture[] getTextures() {
		return textures.values().toArray(new Texture[textures.size()]);
	}
	
	public Input[] getInputs() {
		return inputs.values().toArray(new Input[inputs.size()]);
	}
	
	public Property[] getProperties() {
		return properties.values().toArray(new Property[properties.size()]);
	}
	
	public SubRegistry[] getSubRegistries() {
		return subRegistries.values().toArray(new SubRegistry[subRegistries.size()]);
	}
	
}
