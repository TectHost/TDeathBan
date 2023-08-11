package minealex.tdeathban;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import minealex.tdeathban.commands.Commands;
import minealex.tdeathban.listeners.JugadorListener;
import minealex.tdeathban.placeholders.PlaceholderAPIVidas;

public class TDeathBan extends JavaPlugin {
    
    PluginDescriptionFile pdfFile = getDescription();
    public String version = pdfFile.getVersion();
    private FileConfiguration players = null;
    private File playersFile = null;
    private FileConfiguration messages = null;
    private File messagesFile = null;
    private FileConfiguration config = null;
    private File configFile = null;

    private ArrayList<VidaJugador> vidas = new ArrayList<VidaJugador>();
    private int cantidadMaximaVidas;

    public void onEnable() {
        cantidadMaximaVidas = 3;
        registerEvents();
        registerPlayers();
        registerMessages();
        registerConfig(); // Llamada para registrar el archivo config.yml
        cargarJugadores();
        getCommand("tdb").setExecutor(new Commands(this));
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIVidas(this).register();
        }
    }

    public void onDisable() {
        guardarJugadores();
    }

    public int getCantidadMaximaVidas() {
        return this.cantidadMaximaVidas;
    }

    public VidaJugador getVidaJugador(String uuid) {
        for (VidaJugador vida : vidas) {
            if (vida.getUuid().equals(uuid)) {
                return vida;
            }
        }
        return null;
    }

    public void agregarVidaJugador(String uuid) {
        vidas.add(new VidaJugador(uuid, cantidadMaximaVidas - 1));
    }

    public void removerVidaJugador(String uuid) {
        for (int i = 0; i < vidas.size(); i++) {
            if (vidas.get(i).getUuid().equals(uuid)) {
                vidas.remove(i);
                return;
            }
        }
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new JugadorListener(this), this);
    }

    public void registerPlayers() {
        playersFile = new File(this.getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            this.saveResource("players.yml", false);
        }
    }

    public void guardarJugadores() {
        FileConfiguration players = getPlayers();
        players.set("Players", null);
        for (VidaJugador v : vidas) {
            players.set("Players." + v.getUuid() + ".vidas", v.getVidas());
        }
        savePlayers();
    }

    public void cargarJugadores() {
        FileConfiguration players = getPlayers();
        if (players.contains("Players")) {
            for (String uuid : players.getConfigurationSection("Players").getKeys(false)) {
                int vidas = Integer.valueOf(players.getString("Players." + uuid + ".vidas"));
                VidaJugador v = new VidaJugador(uuid, vidas);
                this.vidas.add(v);
            }
        }
    }

    public void savePlayers() {
        try {
            players.save(playersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getPlayers() {
        if (players == null) {
            reloadPlayers();
        }
        return players;
    }

    public void reloadPlayers() {
        if (players == null) {
            playersFile = new File(getDataFolder(), "players.yml");
        }
        players = YamlConfiguration.loadConfiguration(playersFile);

        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(this.getResource("players.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                players.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void registerMessages() {
        messagesFile = new File(this.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            this.saveResource("messages.yml", false);
        }
    }

    public void reloadMessages() {
        if (messages == null) {
            messagesFile = new File(getDataFolder(), "messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                messages.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getMessages() {
        if (messages == null) {
            reloadMessages();
        }
        return messages;
    }

    public void registerConfig() {
        configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            this.saveResource("config.yml", false);
        }
    }

    public void reloadConfig() {
        if (config == null) {
            configFile = new File(getDataFolder(), "config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(this.getResource("config.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                config.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }
}
