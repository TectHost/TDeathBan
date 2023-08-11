package minealex.tdeathban.placeholders;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import minealex.tdeathban.TDeathBan;
import minealex.tdeathban.VidaJugador;

public class PlaceholderAPIVidas extends PlaceholderExpansion{
		 
	    private TDeathBan plugin;
	 
	    public PlaceholderAPIVidas(TDeathBan plugin) {
	    	this.plugin = plugin;
	    }
	 
	    @Override
	    public boolean persist(){
	        return true;
	    }
	    
	    @Override
	    public boolean canRegister(){
	        return true;
	    }
	 
	    @Override
	    public String getAuthor(){
	        return "Mine_Alex";
	    }
	 
	    @Override
	    public String getIdentifier(){
	        return "tdeathban";
	    }
	 
	    @Override
	    public String getVersion(){
	        return plugin.getDescription().getVersion();
	    }
	    
	    @Override
	    public String onPlaceholderRequest(Player player, String identifier){
	 
	        if(player == null){
	            return "";
	        }
	 
	        if(identifier.equals("lifes")){
	        	VidaJugador vidasJ = plugin.getVidaJugador(player.getUniqueId().toString());
	        	if(vidasJ == null) {
	        		return plugin.getCantidadMaximaVidas()+"";
	        	}else {
	        		return vidasJ.getVidas()+"";
	        	}
	        }
	        return null;
	    }
}
