import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
public class BijectionGroup{
    public interface Bijection<T, Y> extends Function<T, Y> {
        String name();

        List<T> getDomain();

        List<T> getRange();
    }
    private static <T> List<List<T>> generatePerm(List<T> original) {
        List<List<T>> result = new ArrayList<>();
        if (original.isEmpty()) {
            result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        T lastElement = original.remove(original.size()-1);
        List<List<T>> permutations = generatePerm(original);
        for (List<T> permutation:permutations) {
            for (int i = 0; i < permutation.size()+1; i++) {
                List<T> temp = new ArrayList<>(permutation);
                temp.add(i, lastElement);
                result.add(temp);
            }
        }
        return result;
    }

    public static <T> List<Bijection> bijectionsOf(Set<T> domain) {
        List<T> domainList = new ArrayList<>(domain);
        List<Bijection> listOfBijections = new ArrayList<>();
        List<T> tempList = new ArrayList<>(domainList);
        List<List<T>> permutationsList = generatePerm(tempList);

        for(int i = 0; i < permutationsList.size();i++){
            final int finalI = i;
            Bijection<T, T> bijection = new Bijection<T, T>() {
                List<T> domain = domainList;
                List<T> range = permutationsList.get(finalI);

                @Override
                public List<T> getDomain(){
                    return domain;
                }
                @Override
                public List<T> getRange(){
                    return range;
                }

                @Override
                public String name(){
                    return "bijection";
                }
                @Override
                public T apply(T o) {
                    int index = domain.indexOf(o);
                    //Error
                    if(index == -1)
                        throw new IllegalArgumentException("Not in Domain");

                    return range.get(index);
                }
            };
            listOfBijections.add(bijection);
        }

        return listOfBijections;
    }

    ////////////////////////////////

    public static <T> Group<Bijection<T,T>> bijectionGroup(Set<T> items) {
        Group<Bijection<T,T>> bijectiongroup = new Group<Bijection<T, T>>() {
            List<Bijection> bijections = bijectionsOf(items);
            @Override
            public Bijection<T, T> binaryOperation(Bijection<T, T> one, Bijection<T, T> other) {
                Bijection<T, T> inverse = new Bijection<T, T>() {
                    List<T> domain_one = one.getDomain();
                    List<T> range_one = one.getRange();
                    List<T> domain_two = one.getDomain();
                    List<T> range_two = one.getRange();

                    @Override
                    public List<T> getDomain(){
                        return domain_one;
                    }
                    @Override
                    public List<T> getRange(){
                        return range_two;
                    }

                    @Override
                    public String name(){
                        return "composite";
                    }
                    @Override
                    public T apply(T o) {
                        int index = domain_one.indexOf(o);
                        if(index == -1)
                            throw new IllegalArgumentException("Not in Domain");
                        index = domain_two.indexOf(range_one.get(index));
                        if(index == -1)
                            throw new IllegalArgumentException("Not in Domain");
                        return range_two.get(index);
                    }
                };
                return inverse;
            }

            @Override
            public Bijection<T, T> identity() {
                Bijection<T, T> identity = new Bijection<T, T>() {
                    List<T> domain = new ArrayList<>(items);

                    @Override
                    public List<T> getDomain(){
                        return domain;
                    }
                    @Override
                    public List<T> getRange(){
                        return domain;
                    }

                    @Override
                    public String name(){
                        return "identity";
                    }
                    @Override
                    public T apply(T o) {
                        int index = domain.indexOf(o);
                        //Error
                        if(index == -1)
                            throw new IllegalArgumentException("Not in Domain");

                        return domain.get(index);
                    }
                };
                return identity;
            }

            @Override
            public Bijection<T, T> inverseOf(Bijection<T, T> bijection) {
                Bijection<T, T> inverse = new Bijection<T, T>() {
                    List<T> domain = bijection.getRange();
                    List<T> range = bijection.getDomain();

                    @Override
                    public List<T> getDomain(){
                        return domain;
                    }
                    @Override
                    public List<T> getRange(){
                        return range;
                    }

                    @Override
                    public String name(){
                        return "inverse";
                    }
                    @Override
                    public T apply(T o) {
                        int index = domain.indexOf(o);
                        //Error
                        if(index == -1)
                            throw new IllegalArgumentException("Not in Domain");

                        return range.get(index);
                    }
                };
                return inverse;
            }
        };
        return bijectiongroup;
    }
}