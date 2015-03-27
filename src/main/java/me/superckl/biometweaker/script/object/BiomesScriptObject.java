package me.superckl.biometweaker.script.object;

import me.superckl.biometweaker.config.Config;
import me.superckl.biometweaker.core.ModBiomeTweakerCore;
import me.superckl.biometweaker.script.ScriptHandler;
import me.superckl.biometweaker.script.ScriptParser;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiomeFlower;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveSpawn.Type;
import me.superckl.biometweaker.script.command.ScriptCommandRemoveAllSpawns;
import me.superckl.biometweaker.script.command.ScriptCommandAddRemoveBiome;
import me.superckl.biometweaker.script.command.ScriptCommandSetBiomeProperty;

import com.google.gson.JsonPrimitive;

public class BiomesScriptObject extends ScriptObject{

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
		}else if(call.startsWith("removeFlower(")){
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
		else if(call.startsWith("addSpawn(")){
			final String[] args = ScriptParser.trimAll(ScriptParser.parseArguments(call));
			if(args.length < 5){
				ModBiomeTweakerCore.logger.error("Biome set command requires more than 4 arguments, while "+args.length+" were found! Call: "+call);
				return;
			}
			if(!ScriptParser.isStringArg(args[args.length-4])){
				ModBiomeTweakerCore.logger.error("Found non-String argument "+args[0]+" where a String is required: "+call);
				return;
			}
			if(!ScriptParser.isPositiveInteger(args[args.length-3]) || !ScriptParser.isPositiveInteger(args[args.length-2]) || !ScriptParser.isPositiveInteger(args[args.length-1])){
				ModBiomeTweakerCore.logger.error("Found non-integer argument where integer required: "+call);
				return;
			}
			final int weight = Integer.parseInt(args[args.length-3]), minCount = Integer.parseInt(args[args.length-2]), maxCount = Integer.parseInt(args[args.length-1]);
			final Type type = Type.valueOf(ScriptParser.extractStringArg(args[args.length-4]));
			if(type == null){
				ModBiomeTweakerCore.logger.error("Failed to parse spawn list type: "+args[args.length - 4]);
				return;
			}
			for(int i = 0; i < (args.length - 4); i++){
				if(!ScriptParser.isStringArg(args[i]) && !handler.getShortcuts().containsKey(args[i])){
					ModBiomeTweakerCore.logger.error("Found non-String argument "+args[i]+" where a String is required: "+call);
					continue;
				}
				this.addSpawn(handler.getShortcuts().containsKey(args[i]) ? handler.getShortcuts().get(args[i]):ScriptParser.extractStringArg(args[i]), type, weight, minCount, maxCount);
			}
		}else if(call.startsWith("removeSpawn(")){
			final String[] args = ScriptParser.trimAll(ScriptParser.parseArguments(call));
			if(args.length < 2){
				ModBiomeTweakerCore.logger.error("Biome set command requires more than 1 argument, while "+args.length+" were found! Call: "+call);
				return;
			}
			if(!ScriptParser.isStringArg(args[args.length-1])){
				ModBiomeTweakerCore.logger.error("Found non-String argument "+args[0]+" where a String is required: "+call);
				return;
			}
			final Type type = Type.valueOf(ScriptParser.extractStringArg(args[args.length-1]));
			if(type == null){
				ModBiomeTweakerCore.logger.error("Failed to parse spawn list type: "+args[args.length - 4]);
				return;
			}
			for(int i = 0; i < (args.length - 4); i++){
				if(!ScriptParser.isStringArg(args[i]) && !handler.getShortcuts().containsKey(args[i])){
					ModBiomeTweakerCore.logger.error("Found non-String argument "+args[i]+" where a String is required: "+call);
					continue;
				}
				this.removeSpawn(handler.getShortcuts().containsKey(args[i]) ? handler.getShortcuts().get(args[i]):ScriptParser.extractStringArg(args[i]), type);
			}
		}else if(call.startsWith("removeAllSpawns(")){
			final String[] args = ScriptParser.trimAll(ScriptParser.parseArguments(call));
			if(args.length != 1){
				ModBiomeTweakerCore.logger.error("Biome set command requires 1 argument, while "+args.length+" were found! Call: "+call);
				return;
			}
			final Type type = Type.valueOf(ScriptParser.extractStringArg(args[0]));
			this.removeAllSpawns(type);
		}else
			ModBiomeTweakerCore.logger.error("Failed to find meaning in command "+call+". It will be ignored.");
	}

	private void set(final String key, final String value){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandSetBiomeProperty(biome, key, new JsonPrimitive(value)));;
	}

	private void remove(){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandAddRemoveBiome(biome));
	}

	private void addFlower(final String block, final String meta, final String weight){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandAddRemoveBiomeFlower(biome, block, Integer.parseInt(meta), Integer.parseInt(weight)));
	}

	private void removeFlower(final String block, final String meta){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandAddRemoveBiomeFlower(biome, block, Integer.parseInt(meta)));
	}

	private void addSpawn(final String clazz, final Type type, final int weight, final int minCount, final int maxCount){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandAddRemoveSpawn(biome, type, clazz, weight, minCount, maxCount));
	}

	private void removeSpawn(final String clazz, final Type type){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandAddRemoveSpawn(biome, type, clazz, 0, 0, 0));
	}
	private void removeAllSpawns(final Type type){
		for(final int biome:this.biomes)
			Config.INSTANCE.addCommand(new ScriptCommandRemoveAllSpawns(biome, type));
	}

	@Override
	public void populateCommands() {
		// TODO Auto-generated method stub
		
	}

}
