import reactor.util.StringUtils;

public class GeneralTest {
    public static void main(String... args) {
        String test = null;
        System.out.println("test.isEmpty() = " + StringUtils.hasLength((test + "").trim()));
        test = "";
        System.out.println("test.isEmpty() = " + StringUtils.hasLength(test));

    }
}
