package Java;
import java.util.*;

public class Matrix {

    public static List<Integer> spiralPathMatrix(int[][] matrix, int n, int m) {
            // Write you code here.
            List<Integer> res= new ArrayList<>();
            int i=0;
            int j=0;
            while(i<=n/2 || j<=m/2)
            {
           res.addAll(getPath(matrix, i, n, m, i, j));
           i++;
           j++;
            }
            return res;
        }

    public static List<Integer> getPath(int[][] matrix, int start, int n, int m, int i, int j)
    {
        List<Integer> res= new ArrayList<Integer>();

            int end= m;
            while(j<end)
            {
                res.add(matrix[i][j]);
                j++;
            }
            //i = 0;
            //j= m;
            end = n;
            j--;
            i++;
            if(i>=end)
            return res;
            while(i<end)
            {
                res.add(matrix[i][j]);
                i++;
            }
            //i=n;
            //j=m-1
            end--;
            i--;
            j--;
            //i=n-1;
            if(j<start)
            return res;
               while(j>=start)
            {
                res.add(matrix[i][j]);
                j--;
            }
            //i= n-i;
            //j=-1
            j++;
            //j=0;
            start++;
            i--;
            if(i<start)
            return res;
            while(i>=start)
            {
                res.add(matrix[i][j]);
                i--;
            }
            //i=1;
            //j=0
        return res;
    }
}
