package uz.app.bot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Util {

    public static Set<String> buttonMessages() {
        return new HashSet<>(Arrays.asList(
                "➕ Add product",
                "📂 Add category",
                "🛠 Manage orders",
                "📊 Get statistics",
                "🛠 Manage Categories and Products 📂"
        ));
    }
}
