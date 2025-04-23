package cloud;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import javax.servlet.http.Part;
import java.io.*;
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
    public String convertMediaToUrl(Part filePart) throws IOException {
        // Validate input
        if (filePart == null || filePart.getSize() == 0) {
            throw new IOException("No file uploaded or file is empty.");
        }

        // Get and validate filename
        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IOException("Invalid file name");
        }

        // Sanitize filename
        fileName = fileName.replaceAll("[^\\w.-]", "_");

        // Create temp file
        File tempFile = null;
        try {
            tempFile = File.createTempFile("cloud_", "_" + fileName);

            // Copy file content
            try (InputStream in = filePart.getInputStream();
                 OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // Prepare upload options
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                    "resource_type", "auto",
                    "public_id", "courses/" + System.currentTimeMillis() + "_" + fileName,
                    "folder", "courses",
                    "overwrite", false,
                    "unique_filename", true
            );

            // Upload to Cloudinary
            Map<?, ?> uploadResult = CloudinaryConfig.getCloudinary()
                    .uploader()
                    .upload(tempFile, uploadOptions);

            // Get secure URL
            String fileUrl = (String) uploadResult.get("secure_url");
            if (fileUrl == null || fileUrl.isEmpty()) {
                throw new IOException("Upload succeeded but no URL returned");
            }

            return fileUrl;
        } finally {
            // Clean up temp file
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    public String convertPdfToUrl(Part file) throws IOException {
        // Validate input
        if (file == null || file.getSize() == 0) {
            throw new IOException("No file uploaded or file is empty.");
        }

        // Get and validate filename
        String fileName = getSubmittedFileName(file);
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IOException("Invalid file name");
        }

        // Sanitize filename - chỉ giữ lại chữ cái, số và dấu gạch ngang
        fileName = fileName.replaceAll("[^a-zA-Z0-9-]", "_");

        // Loại bỏ đuôi .pdf nếu có để tránh trùng lặp
        if (fileName.toLowerCase().endsWith(".pdf")) {
            fileName = fileName.substring(0, fileName.length() - 4);
        }

        // Thêm đuôi .pdf vào tên file
        fileName = fileName + ".pdf";

        // Create temp file
        File tempFile = null;
        try {
            tempFile = File.createTempFile("cloud_", "_" + fileName);

            // Copy file content
            try (InputStream in = file.getInputStream();
                 OutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            // Prepare upload options for PDF
            Map<String, Object> uploadOptions = ObjectUtils.asMap(
                "resource_type", "auto",
                "public_id", "pdfs/" + System.currentTimeMillis() + "_" + fileName.toLowerCase(),
                "use_filename", true,
                "unique_filename", true,
                "overwrite", true,
                "format", "pdf",
                "type", "authenticated",
                "access_mode", "authenticated"
            );

            // Upload to Cloudinary
            Map<?, ?> uploadResult = cloudinary.uploader()
                    .upload(tempFile, uploadOptions);

            // Get secure URL
            String fileUrl = (String) uploadResult.get("secure_url");
            if (fileUrl == null || fileUrl.isEmpty()) {
                throw new IOException("Upload succeeded but no URL returned");
            }

            return fileUrl;
        } finally {
            // Clean up temp file
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    private String getSubmittedFileName(Part part) {
        String header = part.getHeader("content-disposition");
        if (header == null) return null;
        for (String headerPart : header.split(";")) {
            if (headerPart.trim().startsWith("filename")) {
                return headerPart.substring(headerPart.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

}