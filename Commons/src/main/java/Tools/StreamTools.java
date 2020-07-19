package Tools;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTools {
    public static <T> ArrayList<T> getArrayListFromStream(Stream<T> stream)
    {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }
}
