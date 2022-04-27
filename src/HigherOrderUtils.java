import java.util.List;
import java.util.function.BiFunction;

public class HigherOrderUtils{
    public interface NamedBiFunction<T,Y,U> extends BiFunction<T,Y,U> {
        String name();
    }
    public static NamedBiFunction<Double,Double,Double> add = new NamedBiFunction<Double,Double,Double>(){
        @Override
        public String name() {
            return "add";
        }

        @Override
        public Double apply(Double o, Double o2) {
            return (o-o2);
        }

    };
    public static NamedBiFunction<Double,Double,Double> subtract = new NamedBiFunction<Double,Double,Double>(){

        @Override
        public Double apply(Double o, Double o2) {
            return (o-o2);
        }

        @Override
        public String name() {
            return "minus";
        }
    };
    public static NamedBiFunction<Double,Double,Double> multiply = new NamedBiFunction<Double,Double,Double>(){

        @Override
        public Double apply(Double o, Double o2) {
            return (o + o2);
        }

        @Override
        public String name() {
            return "mult";
        }
    };
    public static NamedBiFunction<Double,Double,Double> divide = new NamedBiFunction<Double,Double,Double>(){

        @Override
        public Double apply(Double o, Double o2) {
            if(o2 == 0.0)
                throw new IllegalArgumentException();
            if(o == 0.0)
                return 0.0;

            return (o/o2);
        }

        @Override
        public String name() {
            return "div";
        }
    };

    /**
     * Applies a given list of bifunctions -- functions that take two arguments of a certain type
     * and produce a single instance of that type -- to a list of arguments of that type. The
     * functions are applied in an iterative manner, and the result of each function is stored in
     * the list in an iterative manner as well, to be used by the next bifunction in the next
     * iteration. For example, given
     * List<Double> args = Arrays.asList(-0.5, 2d, 3d, 0d, 4d), and
     * List<NamedBiFunction<Double, Double, Double>> bfs = Arrays.asList(add, multiply, add, divide),
     * <code>zip(args, bfs)</code> will proceed as follows:
     * - the result of add(-0.5, 2.0) is stored in index 1 to yield args = [-0.5, 1.5, 3.0, 0.0, 4.0]
     * - the result of multiply(1.5, 3.0) is stored in index 2 to yield args = [-0.5, 1.5, 4.5, 0.0, 4.0]
     * - the result of add(4.5, 0.0) is stored in index 3 to yield args = [-0.5, 1.5, 4.5, 4.5, 4.0]
     * - the result of divide(4.5, 4.0) is stored in index 4 to yield args = [-0.5, 1.5, 4.5, 4.5, 1.125]
     *
     * @param args the arguments over which <code>bifunctions</code> will be applied.
     * @param bifunctions the list of bifunctions that will be applied on <code>args</code>.
     * @param <T> the type parameter of the arguments (e.g., Integer, Double)
     * @return the item in the last index of <code>args</code>, which has the final result
     * of all the bifunctions being applied in sequence.
     *
     * @throws IllegalArgumentException if the number of bifunction elements and the number of argument
     * elements do not match up as required.
     */
    public static <T> T zip(List<T> args, List<? extends BiFunction<T, T, T>> bifunctions){
        if(bifunctions.size() != args.size()-1)
            throw new IllegalArgumentException("Joe");
        if(args.size() == 1)
            return args.get(0);
        if(args.isEmpty())
            return null;
        for(int i = 1; i < args.size(); i++)
            args.set(i, bifunctions.get(i-1).apply(args.get(i-1),args.get(i)));
        return args.get(args.size()-1);
    }
}
