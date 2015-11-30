package classloader.test;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;

@SortingIndex(1001)
@Name("ClassLoaderTest")
public class ClassLoaderTest implements IFMLLoadingPlugin, IClassTransformer{

	private final Logger log = LogManager.getLogger("ClassLoaderTest");
	
	@Override
	public String[] getASMTransformerClass() {
		return new String[] {this.getClass().getName()};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if(transformedName.equals("net.minecraft.world.biome.BiomeGenBase")){
			ClassLoader mcLoader = Launch.classLoader;
			ClassLoader asmLoader = ClassWriter.class.getClassLoader();
			this.log.info(mcLoader == asmLoader);
			try {
				ClassReader reader = new ClassReader(basicClass);
				ClassNode cNode = new ClassNode();
				reader.accept(cNode, 0);
				ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
				cNode.accept(writer);
				writer.toByteArray();
				this.log.info("Huh, it worked without the fix.");
			} catch (Exception e) {
				this.log.info("Yep, that's broken.");
				e.printStackTrace();
			}
			
			try {
				ClassReader reader = new ClassReader(basicClass);
				ClassNode cNode = new ClassNode();
				reader.accept(cNode, 0);
				ClassWriter writer = new MinecraftClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
				cNode.accept(writer);
				writer.toByteArray();
				this.log.info("Ah, now it works with the right ClassLoader...");
			} catch (Exception e) {
				this.log.info("Huh, it's broken with the fix.");
				e.printStackTrace();
			}
		}
		return basicClass;
	}

}
