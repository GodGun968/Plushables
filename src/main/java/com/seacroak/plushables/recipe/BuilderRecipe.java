package com.seacroak.plushables.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class BuilderRecipe implements Recipe<SimpleInventory> {

    private final Identifier id;
    private final ItemStack output;
    private final DefaultedList<Ingredient> recipeItems;

    public BuilderRecipe(Identifier id, ItemStack output, DefaultedList<Ingredient> recipeItems) {
        this.id = id;
        this.output = output;
        this.recipeItems = recipeItems;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        if (recipeItems.get(0).test(inventory.getStack(0))) {
            if (recipeItems.get(2).test(inventory.getStack(3))) {
                return recipeItems.get(1).test(inventory.getStack(1));
            }
        }
        return false;
    }

    @Override
    public ItemStack craft(SimpleInventory inventory) {
        return output;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    public DefaultedList<Ingredient> getRecipeItems() {
        return this.recipeItems;
    }

    @Override
    public ItemStack getOutput() {
        return output.copy();
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Type implements RecipeType<BuilderRecipe> {
        private Type() {
        }

        public static final Type INSTANCE = new Type();
        public static final String ID = "builder";
    }

    public static class Serializer implements RecipeSerializer<BuilderRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final String ID = "builder";
        // this is the name given in the json file

        @Override
        public BuilderRecipe read(Identifier id, JsonObject json) {
            ItemStack output = ShapedRecipe.outputFromJson(JsonHelper.getObject(json, "output"));

            JsonArray ingredients = JsonHelper.getArray(json, "ingredients");
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(3, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new BuilderRecipe(id, output, inputs);
        }

        @Override
        public BuilderRecipe read(Identifier id, PacketByteBuf buf) {
            DefaultedList<Ingredient> inputs = DefaultedList.ofSize(buf.readInt(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromPacket(buf));
            }
            ItemStack output = buf.readItemStack();
            return new BuilderRecipe(id, output, inputs);
        }

        @Override
        public void write(PacketByteBuf buf, BuilderRecipe recipe) {

            buf.writeInt(recipe.getRecipeItems().size());
            for (Ingredient ing : recipe.getRecipeItems()) {
                ing.write(buf);
            }
            buf.writeItemStack(recipe.getOutput());
        }
    }

}
