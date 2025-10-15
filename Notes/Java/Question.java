public class RemoveDuplicatesFromSortedArray
{
	public static void main(String[] args) {
		int arr[] = { 1, 2, 2, 3, 4, 4, 4, 5, 5 };
        int n = arr.length;
 
        // removeDuplicates() returns new size of array
        n = removeDuplicates(arr, n);
 
        // Print updated array
        for (int i = 0; i < n; i++)
            System.out.print(arr[i] + " ");
	}
	static int removeDuplicates(int arr[], int n){
	    int cur= 0;
	    int next =0;
	    while(next<arr.length)
	    {
	        if(arr[next]==arr[cur])
	        {
	            while(next<arr.length-1 && arr[next+1]==arr[next])
	            {
	              next++;  
	            }
	            if(next!=n-1)
	            {
	            arr[cur+1]=arr[next+1];
	            }
	        }
	        cur++;
	        next++;
	    }
	    int res = cur;
	    while(cur<arr.length)
	     arr[cur++]=0;
	   
	    return res;
	    
	}
	

}
