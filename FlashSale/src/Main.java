import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Solution s = new Solution();

        File file = new File("src/testdatav2_1469_data_100.txt");
        Scanner scanner = new Scanner(file);

        int[] start = new int[99999];
        int[] end = new int[99999];
        int[] len = new int[99999];
        int index = 0;
        int count = 0;
        while(scanner.hasNextLine()) {
            count++;
            String s1 = scanner.next();
            switch (count){
                case 1:
                    String[] s1n = s1.split("[^\\d]+");
                    System.out.println(Arrays.toString(s1n));
                    int i = 0;
                    for(String ss: s1n){
                        if(ss.length() == 0){
                            continue;
                        }
                        start[i] = Integer.parseInt(ss);
                        i++;
                    }
                    break;
                case 2:
                    String[] s2n = s1.split("[^\\d]+");
                    System.out.println(Arrays.toString(s2n));
                    int j = 0;
                    for(String ss: s2n){
                        if(ss.length() == 0){
                            continue;
                        }
                        end[j] = Integer.parseInt(ss);
                        j++;
                    }
                    break;
                case 3:
                    String[] s3n = s1.split("[^\\d]+");
                    int k = 0;
                    for(String ss: s3n){
                        if(ss.length() == 0){
                            continue;
                        }
                        len[k] = Integer.parseInt(ss);
                        k++;
                    }
                    break;
            }
            index++;
        }
        System.out.println(count);
        System.out.println(end[99998]);

        int longest = s.longestPath(100000,start,end,len);
        System.out.println(longest);

    }
}
