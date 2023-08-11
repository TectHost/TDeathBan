package minealex.tdeathban.listeners;

import org.bukkit.BanList;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import minealex.tdeathban.TDeathBan;
import minealex.tdeathban.VidaJugador;

public class JugadorListener implements Listener {

    private TDeathBan plugin;
    private FileConfiguration messagesConfig;
    private FileConfiguration config;

    public JugadorListener(TDeathBan plugin) {
        this.plugin = plugin;
        this.messagesConfig = plugin.getMessages();
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void alMorir(PlayerDeathEvent event) {
        Player jugador = event.getEntity();
        String uuid = jugador.getUniqueId().toString();
        VidaJugador vidasJ = plugin.getVidaJugador(uuid);

        int vidasNuevas = 0;

        if (vidasJ == null) {
            plugin.agregarVidaJugador(uuid);
            vidasNuevas = plugin.getCantidadMaximaVidas() - 1;
        } else {
            vidasNuevas = vidasJ.getVidas() - 1;
            vidasJ.setVidas(vidasNuevas);
        }

        String deathMessage = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("messages.death_message", "&5TDeathBan &e> &cYou have died"));
        String remainingLivesMessageSingular = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("messages.remaining_lives_singular", "&5TDeathBan &e> &7You have &e{0} &7life left"));
        String remainingLivesMessagePlural = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("messages.remaining_lives_plural", "&5TDeathBan &e> &7You have &e{0} &7lives left"));

        if (vidasNuevas == 0) {
            plugin.removerVidaJugador(uuid);
            jugador.sendMessage(deathMessage);

            int tempBanDurationHours = config.getInt("Config.tempban_duration_hours", 4);
            long banDuration = System.currentTimeMillis() + (tempBanDurationHours * 60 * 60 * 1000);
            String banReason = ChatColor.translateAlternateColorCodes('&', messagesConfig.getString("messages.tempban_reason", "&5TDeathBan &e> &cYou have run out of lives. Tempban for &e" + tempBanDurationHours + " &chours."));
            BanList banList = Bukkit.getBanList(Type.NAME);
            banList.addBan(jugador.getName(), banReason, new java.util.Date(banDuration), "Console");
            jugador.kickPlayer(banReason);
        } else {
            String remainingLivesMessage = vidasNuevas == 1 ? remainingLivesMessageSingular : remainingLivesMessagePlural;
            String formattedMessage = remainingLivesMessage.replace("{0}", String.valueOf(vidasNuevas));
            jugador.sendMessage(formattedMessage);
        }
    }
}
