package tfc.dynamic_weaponary.API.Events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.Event;

import java.io.File;

public class SetupConfigsEvent extends Event {
	public final Dist dist;
	public final File path;
	
	public SetupConfigsEvent(Dist dist, File file) {
		super();
		this.dist = dist;
		this.path = file;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getClass().equals(obj.getClass());
	}
	
	@Override
	public boolean isCancelable() {
		return false;
	}
	
	public static class MakeFiles extends SetupConfigsEvent {
		public MakeFiles(Dist dist, File file) {
			super(dist, file);
		}
		
		@Override
		public boolean isCancelable() {
			return super.isCancelable();
		}
	}
	
	public static class ChangeFiles extends SetupConfigsEvent {
		public ChangeFiles(Dist dist, File file) {
			super(dist, file);
		}
		
		@Override
		public boolean isCancelable() {
			return super.isCancelable();
		}
	}
}
