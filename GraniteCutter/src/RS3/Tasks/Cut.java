package RS3.Tasks;

import RS3.Task;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Item;

import java.util.Random;
import java.util.concurrent.Callable;

public class Cut extends Task{

    final static int GRANITE5KG_ID = 6983;

    public Cut(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.players.local().animation()==-1 && ctx.backpack.select().count()<10;
    }

    @Override
    public void execute() {
        Item graniteToCut = ctx.backpack.select().id(GRANITE5KG_ID).poll();
        Component craftButton = ctx.widgets.widget(1370).component(11);

        graniteToCut.interact("Craft");

        Random rnd = new Random();
        int holder = -1;
        while (holder == -1) {
            if (craftButton.valid()) {
                Condition.sleep((rnd.nextInt(150)));
                craftButton.click();
                holder = 2;
            }
        }

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation()!=-1;
            }
        }, 100, 16);
    }
}
