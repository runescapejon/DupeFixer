package mr_krab.dupefixer.listeners;

import java.util.List;
import java.util.stream.Collectors;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.text.serializer.TextSerializers;

import mr_krab.dupefixer.DupeFixer;
import ninja.leaping.configurate.ConfigurationNode;

public class InteractItemListenerFluidFixGP {

	private DupeFixer plugin;

	public InteractItemListenerFluidFixGP(DupeFixer plugin) {
		this.plugin = plugin;
	}

	@Listener
	public void fixBucketDupe(InteractItemEvent.Secondary event) {
		if(event.getSource() instanceof Player) {
			Player player = (Player) event.getSource();
			if(!plugin.getProtectPluginsAPI().getGriefPreventionApi().getClaimManager(player.getWorld()).getClaimAt(player.getLocation()).isTrusted(player.getUniqueId())) {
				List<String> itemList = plugin.getRootNode().getNode("FixFluidDupe", "ItemList", "List").getChildrenList().stream().map(ConfigurationNode::getString).collect(Collectors.toList());
				if(itemList.contains(event.getItemStack().createStack().getType().getId())) {
					event.setCancelled(true);
					String position = player.getPosition().toString();
					plugin.getLogger().info(TextSerializers.formattingCode('§').serialize(TextSerializers.FORMATTING_CODE.deserialize(plugin.getRootNode().getNode("FixFluidDupe", "Messages", "Console").getString()
							.replace("%player%", player.getName()).replace("%coordinates%", position))));
					if(plugin.getRootNode().getNode("FixFluidDupe", "Messages", "SendToPlayer").getBoolean()) {
						player.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(plugin.getRootNode().getNode("FixFluidDupe", "Messages", "Player").getString()));
					}
				}
			}
		}
	}
}
