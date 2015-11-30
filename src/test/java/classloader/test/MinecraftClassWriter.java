package classloader.test;

import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.Launch;
import squeek.asmhelper.me.superckl.biometweaker.ObfHelper;

public class MinecraftClassWriter extends ClassWriter{

	public MinecraftClassWriter(int flags) {
		super(flags);
	}

	@Override
	protected String getCommonSuperClass(final String type1, final String type2)
	{
		Class<?> c, d;
        ClassLoader classLoader = Launch.classLoader;
        try {
            c = Class.forName(type1.replace('/', '.'), false, classLoader);
            d = Class.forName(type2.replace('/', '.'), false, classLoader);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
        if (c.isAssignableFrom(d)) {
            return type1;
        }
        if (d.isAssignableFrom(c)) {
            return type2;
        }
        if (c.isInterface() || d.isInterface()) {
            return "java/lang/Object";
        } else {
            do {
                c = c.getSuperclass();
            } while (!c.isAssignableFrom(d));
            return c.getName().replace('.', '/');
        }
	}
}
