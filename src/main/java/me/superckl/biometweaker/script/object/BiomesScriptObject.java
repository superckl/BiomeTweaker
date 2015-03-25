package me.superckl.biometweaker.script.object;

import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.command.ScriptCommandAddBiomeFlower;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveBiome;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.ScriptCommandSetBiomeProperty;

import com.google.gson.JsonPrimitive;

public class BiomesScriptObject implements IScriptObject{

	private final int[] biomes;

	public BiomesScriptObject(final int ... biomes) {
		this.biomes = biomes;
	}

	@Override
	public void handleCall(final String call, final ScriptHandler handler){
		if(call.startsWith("set(")){
			final String[] args = ScriptParser.trimAll(ScriptParser.parseArguments(call));
			if(args.length < 2){
				ModBiomeTweakerCore.logger.error("Biome set command requires more than 1 argument, while "+args.length+" were found! Call: "+call);
				return;
			}
			final String value = args[args.length - 1];
			for(int i = 0; i < (args.length - 1); i++){
				if(!ScriptParser.isStringArg(args[i]) && !handler.getShortcuts().containsKey(args[i])){
					ModBiomeTweakerCore.logger.error("Found non-String argument "+args[i]+" where a String is required: "+call);
					continue;
				}
				this.set(handler.getShortcuts().containsKey(args[i]) ? handler.getShortcuts().get(args[i]):ScriptParser.extractStringArg(args[i]), value);
			}
		}else if(call.startsWith("addFlower(")){
			final String[] args = ScriptParser.trimAll(ScriptParser.parseArguments(call));
			if(args.length != 3){
				ModBiomeTweakerCore.logger.error("Biome addFlower command requires 3 arguments, while "+args.length+" were found! Call: "+call);
				return;
			}
			if(!ScriptParser.isStringArg(args[0])){
				ModBiomeTweakerCore.logger.error("Found non-String argument "+args[0]+" where a String is required: "+call);
				return;
			}
			if(!ScriptParser.isPositiveInteger(args[1]) || !ScriptParser.isPositiveInteger(args[2])){
				ModBiomeTweakerCore.logger.error("Found non-integer argument where integer required: "+call);
				return;
			}
			this.addFlower(ScriptParser.extractStringArg(args[0]), args[1], args[2]);
		}else if(call.startsWith("removeFlower")){
			final String[] args = ScriptParser.trimAll(ScriptParser.parseArguments(call));
			if(args.length != 2){
				ModBiomeTweakerCore.logger.error("Biome addFlower command requires 2 arguments, while "+args.length+" were found! Call: "+call);
				return;
			}
			if(!ScriptParser.isStringArg(args[0])){
				ModBiomeTweakerCore.logger.error("Found non-String argument "+args[0]+" where a String is required: "+call);
				return;
			}
			if(!ScriptParser.isPositiveInteger(args[1])){
				ModBiomeTweakerCore.logger.error("Found non-integer argument where integer required: "+call);
				return;
			}
			this.removeFlower(ScriptParser.extractStringArg(args[0]), args[1]);
		}else if(call.equals("remove()"))
			this.remove();
		else
			ModBiomeTweakerCore.logger.error("Failed to find meaning in command "+call+". It will be ignored.");
	}

	private void set(final String key, final String value){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandSetBiomeProperty(biome, key, new JsonPrimitive(value)));;
	}

	private void remove(){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandRemoveBiome(biome));
	}

	private void addFlower(final String block, final String meta, final String weight){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandAddBiomeFlower(biome, block, Integer.parseInt(meta), Integer.parseInt(weight)));
	}

	private void removeFlower(final String block, final String meta){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandRemoveBiomeFlower(biome, block, Integer.parseInt(meta)));
	}

}
