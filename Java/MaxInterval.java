package Java;
import java.util.*;

public class MaxInterval {

    public static int maxInterval(int t, ArrayList<Integer> start, ArrayList<Integer> end, int k) {
		// Write your code here.

		ArrayList <Integer> interval = new ArrayList();
		int i =0;
		while(i<start.size())
		{
			if(i==0)
			{
				if(start.get(0)>0)
				interval.add(start.get(0));
			}
			else
			{
				if(start.get(i)-end.get(i-1)>0)
				interval.add(start.get(i)-end.get(i-1));
			}
			i++;
			
		}
		if(t-end.get(i-1)>0)
		interval.add(t-end.get(i-1));
		int sum = 0;
		int max = 0;
		for(int j=0;j<interval.size();j++)
		{
			if(j<k+1){
			sum+=interval.get(j);
			max= Math.max(max, sum);
			}
			else
			{
				
				sum = sum + interval.get(j)- interval.get(j-k-1);
				max= Math.max(max, sum);
				}
		}
        System.out.println("interval - "+interval);
		return max;
		
	}
    public static void main(String arges[]) {
        ArrayList<Integer> start = new ArrayList(Arrays.asList(2,4,8));
        ArrayList<Integer> end = new ArrayList(Arrays.asList(4,5,10));

        // interval.add(new ArrayList<Integer>(Arrays.asList(1, 3)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(2, 7)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(3, 5)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(1, 8)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(9, 10)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(5, 11)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(11, 12)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(7,10)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(12, 12)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(14, 17)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(9, 10)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(5, 11)));

        System.out.println("response - " +maxInterval(11,start,end,0));

    }
    
}
