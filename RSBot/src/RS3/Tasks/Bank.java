package RS3.Tasks;

import RS3.Task;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.util.concurrent.Callable;

public class Bank extends Task{

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.backpack.select().count() >= 10);
    }

    @Override
    public void execute() {
        if (ctx.bank.opened()) {
            final int inventCount = ctx.backpack.select().count();
            Component loadoutButton1 = ctx.widgets.widget(762).component(37);
            int holder = -1;
            while (holder == -1) {
                if (loadoutButton1.valid()) {
                    if (loadoutButton1.click()){
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.backpack.select().count() != inventCount;
                            }
                        },50, 16);
                    }

                    holder = 2;
                }
            }
        }
        else{
            if(ctx.bank.inViewport()) {
                if(ctx.bank.open()){
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    },50, 16);
                }
            }
            else{
                ctx.camera.turnTo(ctx.bank.nearest());
            }
        }
    }
}
