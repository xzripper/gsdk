package gsdk.glib.ginet;

import static java.util.Arrays.stream;

import java.util.Base64;

/**
 * Utility for encoding/decoding transfer data.
 */
public class GinetTransferableObjectUtility{
    private static final String SPLIT = "'";
    private static final String SINGLE_QUOTE_C = "sqv1";
    private static final String SIGNATURE_C = "VTrfO1-";

    /**
     * Assembles string for objects and encrypts it.
     *
     * @param objects Objects.
     */
    @SafeVarargs
    public static <T> String assemble(T ...objects) {
        StringBuilder assembled = new StringBuilder();

        for(T object : objects) {
            String strObject = object.toString().replace(SPLIT, SINGLE_QUOTE_C);

            assembled.append(String.format("%s%s", strObject, strObject.equals(objects[objects.length - 1]) ? "" : SPLIT));
        }

        return SIGNATURE_C + Base64.getEncoder().encodeToString(assembled.reverse().toString().getBytes());
    }

    /**
     * Decrypts assembled objects and disassembles them.
     *
     * @param assembled Assembled string.
     */
    public static String[] disassemble(String assembled) {
        if(!isAssembled(assembled)) return new String[] {};

        assembled = new String(Base64.getDecoder().decode(assembled.substring(SIGNATURE_C.length())));

        String[] objects = new String[(int) assembled.codePoints().filter(chr -> chr == SPLIT.charAt(0)).count() + 1];

        String[] disassembled = assembled.split(SPLIT);

        for(int i=0; i < disassembled.length; i++) {
            String object_ =  new StringBuilder(disassembled[i]).reverse().toString().replace(SINGLE_QUOTE_C, SPLIT);

            if(!object_.isEmpty()) {
                objects[i] = object_;
            }
        }

        objects = stream(objects)
            .filter(string -> string != null && !string.isEmpty())
            .toArray(String[]::new);

        return objects;
    }

    /**
     * Is string was assembled.

     * @param assembled String.
     */
    public static boolean isAssembled(String assembled) {
        return assembled.startsWith(SIGNATURE_C) && assembled.length() >= SIGNATURE_C.length() + 3;
    }
}
