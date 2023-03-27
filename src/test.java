import java.util.*;

public class test {
    public static void main(String[] args) {
        List<t> list =new ArrayList<>();
        list.add(new t(1));
        list.add(new t(3));
        list.add(new t(2));
        list = list.stream().sorted(Comparator.comparingInt(a->-a.val)).toList();
        for (t t : list) {
            System.out.println(t.val);
        }
    }
    private static class t{
        int val;
        public t(int val){
            this.val = val;
        }
    }
}
