package env;

import arc.struct.*;
import arc.util.*;
import mindustry.io.*;
import mindustry.io.SaveFileReader.*;
import mindustry.mod.*;
import mindustry.world.meta.*;

import java.io.*;

import static arc.Core.*;
import static mindustry.Vars.*;

/** Main mod class and {@link Env} allocator. */
public class EnvAlloc extends Mod implements CustomChunk{
    /** Must correspond to {@link Env}'s maximum taken flag. */
    private static int start = Env.oxygen;
    private static final int vanilla = (start << 1) - 1;

    private static final ObjectIntMap<String> ids = new ObjectIntMap<>();
    private static final IntMap<String> names = new IntMap<>();

    /** Instantiates the mod and sets up the allocator. */
    public EnvAlloc(){
        app.post(() -> {
            if(!mods.getMod(EnvAlloc.class).enabled()) return;

            // add custom saves chunk for synchronizing env flags
            SaveVersion.addCustomChunk("env-alloc-state", this);
        });
    }

    /**
     * Allocates a new {@link Env} flag. Do not call after {@linkplain Mod#loadContent() content loading}.
     * @param name Name of the {@linkplain Env flag}, should be prefixed with mod name for uniqueness.
     * @return     The integer flag. Unstable across game runs.
     */
    public static int create(String name){
        if(ids.containsKey(name)){
            throw new IllegalArgumentException(String.format("Env `%s` is already registered; if you didn't do this, pick another unique name", name));
        }else{
            int id = start <<= 1;
            if(id == 0){
                throw new IllegalStateException("Too much env flags");
            }else{
                ids.put(name, id);
                names.put(id, name);
                return id;
            }
        }
    }

    @Override
    public void write(DataOutput stream) throws IOException{
        stream.writeInt(0);

        int mask = state.rules.env & ~vanilla;
        for(int i = 0; i < 32; i++){
            int bit = (mask >>> i) & 1;
            if(bit == 1){
                int id = 1 << i;
                var name = names.get(id);
                if(name == null){
                    Log.err("[EnvAlloc] ID `@` is used outside of EnvAlloc, things will break when loading the save. " +
                                "Consult mod authors to use this library instead!", id);
                    continue;
                }

                stream.writeBoolean(true);
                stream.writeUTF(name);
            }
        }

        stream.writeBoolean(false);
    }

    @Override
    public void read(DataInput stream) throws IOException{
        Log.debug("[EnvAlloc] Found `env-alloc-state` chunk, synchronizing env flags...");

        int ver = stream.readInt();
        if(ver == 0){
            int mask = 0;
            while(stream.readBoolean()){
                var name = stream.readUTF();
                int id = ids.get(name);
                if(id == 0){
                    throw new IOException(String.format("[EnvAlloc] Env flag `%s` not found", name));
                }

                Log.debug("[EnvAlloc] Mapping `@` flag", name);
                mask |= id;
            }

            state.rules.env = (state.rules.env & vanilla) | mask;
        }else{
            throw new IOException(String.format("[EnvAlloc] Unsupported `env-alloc-state` version `%s`", ver));
        }
    }
}
