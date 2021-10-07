package com.williambl.rideableenderpearl;

import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RideableEnderPearl.MODID)
public class RideableEnderPearl {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "rideableenderpearl";

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    private static final RegistryObject<Item> RIDEABLE_ENDER_PEARL_ITEM = ITEMS.register("rideable_ender_pearl", () -> new RideableEnderPearlItem(new Item.Properties().maxStackSize(16).group(ItemGroup.TRANSPORTATION)));

    public RideableEnderPearl() {
        MinecraftForge.EVENT_BUS.register(this);
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static class RideableEnderPearlItem extends Item {
        public RideableEnderPearlItem(Item.Properties builder) {
            super(builder);
        }

        public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
            ItemStack itemstack = playerIn.getHeldItem(handIn);
            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            playerIn.getCooldownTracker().setCooldown(this, 20);
            if (!worldIn.isRemote) {
                EnderPearlEntity enderpearlentity = new EnderPearlEntity(worldIn, playerIn);
                enderpearlentity.setItem(itemstack);
                enderpearlentity.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
                worldIn.addEntity(enderpearlentity);
                playerIn.startRiding(enderpearlentity);
            }

            playerIn.addStat(Stats.ITEM_USED.get(this));
            if (!playerIn.abilities.isCreativeMode) {
                itemstack.shrink(1);
            }

            return ActionResult.func_233538_a_(itemstack, worldIn.isRemote());
        }
    }
}
