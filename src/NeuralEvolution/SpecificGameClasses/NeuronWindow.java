package NeuralEvolution.SpecificGameClasses;

import NeuralEvolution.BodyClasses.Bact;
import NeuralEvolution.GameClasses.Workhorse;
import NeuralEvolution.GameClasses.World;
import NeuralEvolution.NeuronClasses.NeuralNode;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Sumner
 */
public class NeuronWindow extends Canvas {
    private final BufferStrategy strategy;
    private Workhorse wh;

    private static final Color maroon = new Color(128,0,0);
    private static final Color red = new Color(255,0,0);
    private static final Color darkgreen = new Color(0,128,0);
    private static final Color green = new Color(0,255,0);
    private static final Color darkblue = new Color(0,0,128);
    private static final Color blue = new Color(0,0,255);
    
    public NeuronWindow(Workhorse wh){
        JFrame myFrame = new JFrame("Neurons");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel myPanel = (JPanel) myFrame.getContentPane();
        myPanel.setPreferredSize(new Dimension(500,500));
        myPanel.setLayout(null);
        this.setBounds(0,0,500,500);
        myPanel.add(this);
        this.setIgnoreRepaint(true);
        myFrame.pack();
        myFrame.setResizable(false);
        myFrame.setVisible(true); //usual
        this.requestFocus();
        this.createBufferStrategy(2);
        strategy = this.getBufferStrategy();
        this.wh = wh;
    }
    
    HashMap<NeuralNode,NeuralNodeContainer> poss = null;
    Bact lastSelBact;
    public void drawWindow(){
        Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
        World myWorld = wh.getWorldController().getWorld();
        g.setColor(Color.white); g.fillRect(0, 0, myWorld.getWindowWidth(), myWorld.getWindowHeight());

        Bact selBact = myWorld.getSelBact();
        if (selBact==null){
            g.setColor(Color.BLACK);
            g.drawString("No selected Bact", 0, 10);
        } else if (poss==null || !selBact.equals(lastSelBact)){
            poss = new HashMap<>();
            NeuralNode[] outputs = selBact.getOutputs();
            HashSet<NeuralNode> seen = new HashSet<>();
            ArrayList<NeuralNode> next = new ArrayList<>(Arrays.asList(outputs));
            ArrayList<NeuralNode> nextnext = new ArrayList<>();
            int x_pos;
            int y_pos = this.getHeight()-40;
            boolean first = true;
            while (next.size()>0){
                int spacing = (this.getWidth()-40) / next.size();
                x_pos = 20;
                y_pos -= 40;
                for (NeuralNode n : next){
                    if (seen.contains(n))
                        continue;
                    if (n.getInputs().isEmpty() && first)
                        continue;
                    NeuralNodeContainer nnc = new NeuralNodeContainer(n,x_pos,y_pos,first);
                    poss.put(n,nnc);
                    x_pos += spacing;
                    if (n.getAcceptsInput()){
                        nextnext.addAll(n.getInputs());
                        nnc.nns.addAll(n.getInputs());
                    }
                    seen.add(n);
                }
                next.clear();
                next.addAll(nextnext);
                nextnext.clear();
                first = false;
            }
        }
        if (poss!=null && selBact!=null) {
            g.setColor(Color.BLACK);
            g.drawString("Genes:  "+selBact.getDNA().length, 0, 10);
            g.drawString("Parent: "+selBact.getParent(), 0, 20);
            g.drawString("DNA Hash:"+Arrays.toString(selBact.getDNA()).hashCode(), 0, 30);
            g.drawString("Mut Rate:"+selBact.getMutRate(), 0, 40);
            Collection<NeuralNodeContainer> nncs = poss.values();
            for (NeuralNodeContainer nnc : nncs){
                nnc.draw(poss,g);
            }
        }
        lastSelBact = selBact;

        g.dispose(); strategy.show();
    }
    
    public class NeuralNodeContainer{
        public ArrayList<NeuralNode> nns = new ArrayList<>();
        NeuralNode n;
        int x,y;
        boolean first;
        public NeuralNodeContainer(NeuralNode n, int x, int y, boolean first){
            this.n = n;
            this.x = x; this.y = y;
            this.first = first;
        }
        public void addInput(NeuralNode nnc){
            nns.add(nnc);
        }
        public void draw(HashMap<NeuralNode,NeuralNodeContainer> nncs, Graphics2D g){
            if (n.getState()==0){
                if (first)
                    g.setColor(maroon);
                else if (n.getAcceptsInput())
                    g.setColor(darkgreen);
                else
                    g.setColor(darkblue);
            }else{
                if (first)
                    g.setColor(red);
                else if (n.getAcceptsInput())
                    g.setColor(green);
                else
                    g.setColor(blue);
            }
            for (NeuralNode n : nns){
                NeuralNodeContainer o_n = nncs.get(n);
                int mid_x = (o_n.x - x)/2 + x;
                int mid_y = (o_n.y - y)/2 + y;

                g.drawLine(x+10, y+10, o_n.x+10, o_n.y+10);
            }
            g.fillOval(x, y, 20, 20);
            g.setColor(Color.BLACK);
            g.drawString(""+n.getState(), x+1, y+15);
            g.drawString(n.getID(), x, y);
            g.drawString(n.opCode(), x, y+30);
        }
    }
}
    
