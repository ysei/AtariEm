package uk.org.wookey.atari.pluginhandler;

import uk.org.wookey.atari.utils.Logger;

public class CorePlugin implements CorePluginInterface {
	private Logger _logger = new Logger("CorePlugin");
	protected String version = "1.0.0";
	protected String name;
	
	public CorePlugin() {
		name = null;
	}
	
	@Override
	public boolean supports(PluginType t) {
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String n) {
		name = n;
		_logger = new Logger(name);
	}

	@Override
	public boolean activate() {
		_logger.logInfo("Activating plugin " + getName());
		
		return true;
	}

	@Override
	public void deactivate() {
		_logger.logInfo("Deactivating plugin " + getName());
	}

	@Override
	public String getVersionString() {
		return name + " " + version;
	}	
}
