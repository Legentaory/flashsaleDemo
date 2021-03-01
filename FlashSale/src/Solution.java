import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    /**
     * @param n: The number of nodes
     * @param starts: One point of the edge
     * @param ends: Another point of the edge
     * @param lens: The length of the edge
     * @return: Return the length of longest path on the tree.
     */
    public int longestPath(int n, int[] starts, int[] ends, int[] lens) {
        // Write your code here
        if(starts == null || ends == null || lens == null){
            return 0;
        }
        Map<Integer, List<int[]>> grapgh = new HashMap<>();
        for(int i = 0; i < starts.length; i++){
            if(!grapgh.containsKey(starts[i])){
                grapgh.put(starts[i], new ArrayList<int[]>());
            }
            if(!grapgh.containsKey(ends[i])){
                grapgh.put(ends[i], new ArrayList<int[]>());
            }
            grapgh.get(starts[i]).add(new int[]{ends[i], lens[i]});
            grapgh.get(ends[i]).add(new int[]{starts[i], lens[i]});
        }

        int longest = 0;
        int ii = 0;
        System.out.println(grapgh.size());
        for(Integer node: grapgh.keySet()){
            Map<Integer, Integer> memo = new HashMap<>();
            longest = Math.max(DFS(node, grapgh, memo), longest);
            System.out.println("log" + longest + "    "+ ii);
            ii++;
        }

        return longest;
    }

    private int DFS(int node,  Map<Integer, List<int[]>> grapgh, Map<Integer, Integer> memo){
        if(memo.containsKey(node)){
            return -1;
        }
        memo.put(node, 0);
        for(int[] neighbor: grapgh.get(node)){
            int longest = DFS(neighbor[0], grapgh, memo);
            if(longest == -1){
                memo.put(node, Math.max(longest, memo.get(node)));
            }else{
                memo.put(node, Math.max(longest + neighbor[1], memo.get(node)));}
        }
        return memo.get(node);
    }
}