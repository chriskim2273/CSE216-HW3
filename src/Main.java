import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void testBijectionGroup(){
        Set<Integer> a_few = Stream.of(1,2,3).collect(Collectors.toSet());
// you have to figure out the data type in the line below
        List<BijectionGroup.Bijection<Integer,Integer>> bijections = BijectionGroup.bijectionsOf(a_few);
        bijections.forEach(aBijection -> {
            a_few.forEach(n -> System.out.printf("%d --> %d; ", n, aBijection.apply(n)));
            System.out.println();
        });

        //Set<Integer> a_few = Stream.of(1, 2, 3).collect(Collectors.toSet());
// you have to figure out the data types in the lines below
        System.out.println();
        System.out.println();

        Group<BijectionGroup.Bijection<Integer,Integer>> g = BijectionGroup.bijectionGroup(a_few);
        BijectionGroup.Bijection<Integer,Integer> f1 = BijectionGroup.bijectionsOf(a_few).stream().findFirst().get();
        BijectionGroup.Bijection<Integer,Integer> f2 = g.inverseOf(f1);
        BijectionGroup.Bijection<Integer,Integer> id = g.identity();

        a_few.forEach(n -> System.out.printf("%d --> %d; ",n, f1.apply(n)));
        System.out.println();
        a_few.forEach(n -> System.out.printf("%d --> %d; ",n, f2.apply(n)));
        System.out.println();
        a_few.forEach(n -> System.out.printf("%d --> %d; ",n, id.apply(n)));
    }
    public static void testHigherOrderUtils(){
        List<Double> numbers = Arrays.asList(-0.5, 2d, 3d, 0d, 4d); // documentation example
        List<HigherOrderUtils.NamedBiFunction<Double, Double, Double>> operations = Arrays.asList(HigherOrderUtils.add, HigherOrderUtils.multiply, HigherOrderUtils.subtract, HigherOrderUtils.divide);
        Double d = HigherOrderUtils.zip(numbers, operations); // expected correct value: 1.125
        // different use case, not with NamedBiFunction objects
        List<String> strings = Arrays.asList("a", "n", "t");
        // note the syntax of this lambda expression
        BiFunction<String, String, String> concat = (s, t) -> s + t;
        String s = HigherOrderUtils.zip(strings, Arrays.asList(concat, concat)); // expected correct value: "ant"
        System.out.println(d);
        System.out.println(s);
    }
    public static void testSimpleUtils(){
        ArrayList<Integer> al = new ArrayList<>();
        for(int i = 10; i >= 1; i--){
            al.add(i);
        }
        System.out.println("MIN: " + SimpleUtils.least(al, true));

        Map<String, String> books = new HashMap<>();
        books.put(
                "978-0201633610", "Design patterns : elements of reusable object-oriented software");
        books.put(
                "978-1617291999", "Java 8 in Action: Lambdas, Streams, and functional-style programming");
        books.put("978-0134685991", "Effective Java");

        for(String s : SimpleUtils.flatten(books))
            System.out.println(s);
    }
    public static void main(String... args) {
        testSimpleUtils();
        testHigherOrderUtils();
        testBijectionGroup();
    }

}
