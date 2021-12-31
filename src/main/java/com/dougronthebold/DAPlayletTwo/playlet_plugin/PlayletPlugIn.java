package main.java.com.dougronthebold.DAPlayletTwo.playlet_plugin;
import PipelineUtils.PipelineNoteList;
import PipelineUtils.PlayPlugArgument;

public interface PlayletPlugIn {

	public void process(PipelineNoteList pnl, PlayPlugArgument ppa);
	public String name();
	public int active();
	public void setActive(int i);
}
