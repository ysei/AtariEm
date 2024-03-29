package uk.org.wookey.atari.pluginhandler;

public interface CorePluginInterface {
	public final static int interfaceVersion = 1;
	
	public enum PluginType {
		IOPLUGIN,
		MACROPLUGIN,
	}
	
	public boolean activate();
	public void deactivate();
	public boolean supports(PluginType t);
	public String getName();
	public void setName(String n);
	
	public String getVersionString();
}
