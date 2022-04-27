import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
public class BijectionGroup{
    //Interface for Bijections (primarily for the getter functions).
    public interface Bijection<T, Y> extends Function<T, Y> {
        String name();
        List<T> getDomain();
        List<T> getRange();
    }

    //Generates all possible permuations of a list.
    private static <T> List<List<T>> generatePermutations(List<T> original) {
        List<List<T>> result = new ArrayList<>();
        if (original.isEmpty()) {
            result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        T lastElement = original.remove(original.size()-1);
        List<List<T>> permutations = generatePermutations(original);
        for (List<T> permutation:permutations) {
            for (int i = 0; i < permutation.size()+1; i++) {
                List<T> temp = new ArrayList<>(permutation);
                temp.add(i, lastElement);
                result.add(temp);
            }
        }
        return result;
    }

    //Finds all bijections of a finite set.
    public static <T> List<Bijection<T,T>> bijectionsOf(Set<T> domain) {
        List<T> domainList = new ArrayList<>(domain);
        List<Bijection<T,T>> listOfBijections = new ArrayList<>();
        List<T> tempList = new ArrayList<>(domainList);
        List<List<T>> permutationsList = generatePermutations(tempList);

        //For every permutation of the list, create a Bijection (Function) that maps the domain to the permutation (range).
        for(int i = 0; i < permutationsList.size();i++){
            final int finalI = i;
            Bijection<T, T> bijection = new Bijection<T, T>() {
                final List<T> domain = domainList;
                final List<T> range = permutationsList.get(finalI);

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

                //Get index of element in domain and return corresponding element in range.
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
            final List<Bijection<T,T>> bijections = bijectionsOf(items);

            //Creates a Bijection that is a composite function of two bijections.
            //Maps the element using the first bijection and then maps the result using the second bijection.
            @Override
            public Bijection<T, T> binaryOperation(Bijection<T, T> one, Bijection<T, T> other) {
                Bijection<T, T> inverse = new Bijection<T, T>() {
                    final List<T> domain_one = one.getDomain();
                    final List<T> range_one = one.getRange();
                    final List<T> domain_two = one.getDomain();
                    final List<T> range_two = one.getRange();

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

                    //Get index of element in domain and return corresponding element in range.
                    //Repeat for both functions to perform proper composite function.
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

            //Returns a bijection that essentially maps its domain to itself.
            @Override
            public Bijection<T, T> identity() {
                Bijection<T, T> identity = new Bijection<T, T>() {
                    final List<T> domain = new ArrayList<>(items);

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
                    //Get index of element in domain and return corresponding element in range.
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

            //Returns a bijection which maps the range to the domain.
            @Override
            public Bijection<T, T> inverseOf(Bijection<T, T> bijection) {
                Bijection<T, T> inverse = new Bijection<T, T>() {
                    //Swap domain and range. Works because it is a bijection.
                    final List<T> domain = bijection.getRange();
                    final List<T> range = bijection.getDomain();

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

                    //Get index of element in domain and return corresponding element in range.
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