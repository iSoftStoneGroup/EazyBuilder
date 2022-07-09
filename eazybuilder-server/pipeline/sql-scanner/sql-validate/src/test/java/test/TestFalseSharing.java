package test;

import java.util.concurrent.CountDownLatch;

/**
 * 
 * 
without padding:
duration = 4154041594
duration = 4548287696
duration = 4913581428
duration = 4899760951
duration = 4956310421


padding:
duration = 2179604159
duration = 2318824569
duration = 2947247615
duration = 2739751041
duration = 2929680436


@Contended
duration = 1117142356
duration = 1526198912
duration = 1248551659
duration = 1668061280
duration = 1408059581

 * 
 * 

 *
 */
public class TestFalseSharing implements Runnable{
	    public static int NUM_THREADS = 4;
	    public final static long ITERATIONS=120L * 1000L * 1000L;
	    private final int index;
	    public static CountDownLatch start_latch;
	    public static CountDownLatch end_latch;
	    //测试对象
	    private static VolatileLong[] data = new VolatileLong [NUM_THREADS];
	    static {
	    	for (int i = 0; i < data.length; i++) {
	    		data[i] = new VolatileLong ();
	    	}
	    }
	    
	    public TestFalseSharing(final int arrayIndex) {
	        this.index = arrayIndex;
	    }
	    public static void main(final String[] args) throws Exception {
	    	for(int i=0;i<5;i++) {
	    		start_latch=new CountDownLatch(NUM_THREADS);
	    		end_latch=new CountDownLatch(NUM_THREADS);
	    		long start = System.nanoTime();
	    		runTest();
	    		System.out.println("duration = " + (System.nanoTime() - start)); 
	    	}
	    }
	    private static void runTest() throws InterruptedException {
	        Thread[] threads = new Thread[NUM_THREADS];
	        for (int i = 0; i < threads.length; i++) {
	            threads[i] = new Thread(new TestFalseSharing(i));
	            threads[i].start();
	            start_latch.countDown();
	        }
	        end_latch.await();
	    }
	    public void run() {
	    	try {
				//wait for start signal
	    		start_latch.await();
				//do test iteration
				long i = ITERATIONS + 1;
				while (0 != --i) {
					data[index].value = i;
				}
				//
				end_latch.countDown();
	    	} catch (InterruptedException e) {
	    		e.printStackTrace();
	    	}
	    }
	    @sun.misc.Contended
	    public final static class VolatileLong  {
	        public volatile long value = 0L;
//	        public long p1, p2, p3, p4, p5, p6;  //cach line padding   
	    }
}
