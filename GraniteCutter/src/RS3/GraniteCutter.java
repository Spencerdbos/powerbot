package RS3;

import RS3.Tasks.Bank;
import RS3.Tasks.Cut;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name ="Spencer's Granite Cutter V2", description ="Cut Granite, Make Money", properties = "Author = Spencer; Topic = 999; Client = 6")

public class GraniteCutter extends PollingScript<org.powerbot.script.rt6.ClientContext> implements PaintListener{

    List<Task> taskList = new ArrayList<Task>();
    int startExp = 0;

    @Override
    public void start(){
        taskList.add(new Bank(ctx));
        taskList.add(new Cut(ctx));
        startExp = ctx.skills.experience(Constants.SKILLS_CRAFTING);
    }
    @Override
    public void poll() {
        for(Task task : taskList){
            if(task.activate()){
                task.execute();
                break;
            }
        }
    }

    @Override
    public void repaint(Graphics graphics){
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000*60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60) % 24);

        int expGained = ctx.skills.experience(Constants.SKILLS_CRAFTING)-startExp;

        Graphics2D g = (Graphics2D)graphics;

        g.setColor(new Color(0,0,0,180));
        g.fillRect(3,3, 153, 100);

        g.setColor(new Color(255,255,255));
        g.drawRect(3,3, 153, 100);

        g.drawString("Spencer's Granite Cutter", 10, 20);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 10, 40);
        g.drawString("Exp/Hour: " + (int)(expGained * (3600000D / milliseconds)), 10, 60);
        g.drawString("Pieces Cut/Hour: " +(int)((expGained * (3600000D / milliseconds))/10), 10, 80);
        g.drawString("GP/Hour: " +(((int)((expGained * (3600000D / milliseconds))/10))*1821), 10, 100);
    }
}