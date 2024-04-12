package dev.rosewood.rosegarden.manager;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.command.framework.BaseRoseCommand;
import dev.rosewood.rosegarden.command.framework.RoseCommandWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class AbstractCommandManager extends Manager {

    private final List<RoseCommandWrapper> commandWrappers;

    public AbstractCommandManager(RosePlugin rosePlugin) {
        super(rosePlugin);

        this.commandWrappers = new ArrayList<>();
        this.getRootCommands().stream()
                .map(x -> x.apply(this.rosePlugin))
                .map(x -> new RoseCommandWrapper(this.rosePlugin, x))
                .forEach(this.commandWrappers::add);
    }

    @Override
    public void reload() {
        this.commandWrappers.forEach(RoseCommandWrapper::register);
        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }

    @Override
    public void disable() {
        this.commandWrappers.forEach(RoseCommandWrapper::unregister);
        Bukkit.getOnlinePlayers().forEach(Player::updateCommands);
    }

    public abstract List<Function<RosePlugin, BaseRoseCommand>> getRootCommands();

}
