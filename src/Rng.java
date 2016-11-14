import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import java.io.File;
import java.io.IOException;

/**
 * Created by Matias on 12/11/2016.
 */
public class Rng {
    private double normalRandom2;
    private boolean second=true;
    double j = 4;
    double k = 400;
    double m = 2147483647;
    public  int n=20;
    public  double antics[] = new double[6000];

    public  void ini() {
        n=(int)k+1;
        for(int i=0;i<1000;i++)
            antics[i]=i;
    }

    private void copy(){
        for(int i=0;i<k;i++)
            antics[i] = antics[n - (int) k + i];
        n=(int)k;
    }

    public Rng(){
        this.ini();
    }

    public Rng(double j,double k, double m){
        this.j=4;
        this.k=k;
        this.m=m;
    }

    /**
     * Verifica que el array utilizado para guardar los k anteriores valores
     * no llegue a su limite de lo contrario copia los ultimos k vaores al
     * inicio del array e inicia n=k
     * @return  U(0,1) proximo número aleatorio entre 0 1 con distribucion uniforme
     */
    public double nextUniform(){
        if(n==antics.length)copy();
        return this.getNextUniform();
    };

    /**
     * Obtiene  el proximo numero aleatorio con distribucion uniforme,
     * el agoritmo fue obtenido de
     * http://wiki.fib.upc.es/sim/index.php/Fibonacci_%22lagged%22
     * @return proximo número aleatorio
     */
    private   double getNextUniform() {
        double a,b;
        a=antics[n-(int)j];
        b=antics[n-(int)k];
        antics[n] = ((a * b) % m);
        n++;
        return antics[n-1]/m;
    }



    /**
     * retorna el siguiente número aleatorio, una trandormación anterior
     * se retorna el segundo número resultado de la transformación
     * de lo contrario se realiza la transformación
     *
     * @return número aleatorio en la ditribucion normal
     */
   public double nextNormal(){
       this.second=!this.second;
       return this.second?this.normalRandom2:calcNextNormal();
   }
    /**
     * Obtiene el próximo nuemro aleatorio con di
     */
   private  double calcNextNormal(){
       this.second=!this.second;
       double x1, x2, w;
       do {
           x1 = 2.0 * this.nextUniform() - 1.0;
           x2 = 2.0 * this.nextUniform() - 1.0;
           w = x1 * x1 + x2 * x2;
       } while ( w >= 1.0 );
       w = Math.sqrt( (-2.0 * Math.log( w ) ) / w );
       this.normalRandom2=x2*w;
       return x1 * w;
   }


    public static void main(String[] args) {
        double[] value = new double[100000];
        double[] value1 = new double[100000];

        Rng generator = new Rng();
        double time= System.nanoTime();
        for (int i=1; i < 100000; i++){
            value[i] = generator.nextUniform();
            value1[i] = generator.nextNormal();

        }
        time=System.nanoTime()-time;
        System.out.println(time);
            int number = 100;
            HistogramDataset dataset = new HistogramDataset();
            dataset.setType(HistogramType.FREQUENCY);
            dataset.addSeries("Histogram",value,number);
        HistogramDataset dataset1 = new HistogramDataset();
        dataset1.setType(HistogramType.FREQUENCY);
        dataset1.addSeries("Histogram",value1,number);
            String plotTitle = "Histogram";
            String xaxis = "number";
            String yaxis = "value";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = false;
            boolean urls = false;
            JFreeChart chart = ChartFactory.createHistogram( plotTitle, xaxis, yaxis,
                    dataset, orientation, show, toolTips, urls);
            JFreeChart chart1 = ChartFactory.createHistogram( plotTitle, xaxis, yaxis,
                dataset1, orientation, show, toolTips, urls);
            int width = 800;
            int height = 300;
            try {
                ChartUtilities.saveChartAsJPEG(new File("histogramUniforme.PNG"), chart, width, height);
                ChartUtilities.saveChartAsJPEG(new File("histogramNormal.PNG"), chart1, width, height);
            } catch (IOException e) {}

    }


}
