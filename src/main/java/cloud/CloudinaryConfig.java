package cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.Map;

public class CloudinaryConfig {
    private static final String CLOUD_NAME = "dchi76opz";
    private static final String API_KEY = "625223392633453";
    private static final String API_SECRET = "o9itY2xiaJMVu0pY660gYEfaX0I";

    private static Cloudinary cloudinary;

    static {
        try {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", CLOUD_NAME,
                    "api_key", API_KEY,
                    "api_secret", API_SECRET,
                    "secure", true
            ));
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Cloudinary", e);
        }
    }

    public static Cloudinary getCloudinary() {
        if (cloudinary == null) {
            throw new IllegalStateException("Cloudinary not initialized");
        }
        return cloudinary;
    }
}