package gith;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static java.lang.reflect.Array.newInstance;

/**
 * Created by Elena on 10.08.2017.
 */
public class BrokenLine extends JFrame {
    private JButton buttonLine = new JButton("Create broken line");
    static BrokenLine ssp;
    public static CoordinatesRandom[] arrayCoordinate;
    static final int N = 11;

    static class DrawDialog extends JDialog{
        int[] arrX = new int[N];
        int[] arrY = new int[N];

        DrawDialog(){
            setSize(new Dimension(200, 200));
            setTitle("The broken line");
            add(new BrokenLine.DrawDialog.DrawArr());
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        }
        class DrawArr extends JPanel{

            public DrawArr(){
                setArr();
            }
            public void setArr() {
                arrayCoordinate = GeneratedCoord.array(new CoordinatesRandom[N], CoordinatesRandom.generator());

                System.out.println("before sorting:");
                System.out.println(Arrays.toString(arrayCoordinate));

                Arrays.sort(arrayCoordinate, new SortedByX1());
                System.out.println("sorted by X:");
                System.out.println(Arrays.toString(arrayCoordinate));

                solutionAtan();

                Arrays.sort(arrayCoordinate, new SortedByAtan1());
                System.out.println("sorted by atan2:");
                System.out.println(Arrays.toString(arrayCoordinate));
            }
            public void solutionAtan(){
                for (int i = 0; i < arrayCoordinate.length; i++){
                    if (i == 0){
                        arrayCoordinate[i].atan = -20;
                    }
                    else {
                        arrayCoordinate[i].atan = Math.atan2(arrayCoordinate[i].j - arrayCoordinate[0].j, arrayCoordinate[i].i - arrayCoordinate[0].i);
                    }
                }
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.RED);
                for (int i = 0; i < arrayCoordinate.length; i++){
                    arrX[i] = arrayCoordinate[i].i;
                    arrY[i] = arrayCoordinate[i].j;
                }
                g.drawPolygon(arrX, arrY, N);
            }
        }
    }

    public BrokenLine(){
        super("Task 4 (The closed broken line)");
        JPanel p = new JPanel();
        p.add(buttonLine);
        add(p, BorderLayout.NORTH);
        buttonLine.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog d = new BrokenLine.DrawDialog();
                d.setVisible(true);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200,100);
        setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ssp = new BrokenLine();

            }
        });
    }
}
class SortedByX1 implements Comparator<CoordinatesRandom> {
    @Override
    public int compare(CoordinatesRandom o1, CoordinatesRandom o2) {
        return (o1.i < o2.i ? -1 : (o1.i == o2.i ? 0 : 1));
    }
}
class SortedByAtan1 implements Comparator<CoordinatesRandom>{
    @Override
    public int compare(CoordinatesRandom o1, CoordinatesRandom o2) {
        return (o1.atan < o2.atan ? -1 : (o1.atan == o2.atan ? 0 : 1));

    }
}
class CoordinatesRandom{
    int i;
    int j;
    double atan;

    public CoordinatesRandom(int i, int j) {
        this.i = i;
        this.j = j;
        atan = 0;
    }
    public String toString(){
        String result = "[" + i + ", " + j + "]";
        return result;
    }

    private static Random r = new Random(47);
    public static Generate<CoordinatesRandom> generator(){
        return new Generate<CoordinatesRandom>(){
            public CoordinatesRandom next(){
                return new CoordinatesRandom(r.nextInt(100), r.nextInt(100));
            }
        };
    }
}
interface Generate<T>{
    T next();
}
class CollectionD<T> extends ArrayList<T> {
    public CollectionD(Generate<T> gen, int quantity) {
        for (int i = 0; i < quantity; i++)
            add(gen.next());
    }

    public static <T> CollectionD<T> list(Generate<T> gen, int quantity){
        return new CollectionD<T>(gen, quantity);
    }

}
class GeneratedCoord{
    public static <T> T[] array(T[] a, Generate<T> gen){
        return new CollectionD<T>(gen, a.length).toArray(a);

    }
    @SuppressWarnings("unchecked")
    public static <T> T[] array(Class<T> type, Generate<T> gen, int size){
        T[] a = (T[])newInstance(type, size);
        return new CollectionD<T>(gen, size).toArray(a);
    }
}
