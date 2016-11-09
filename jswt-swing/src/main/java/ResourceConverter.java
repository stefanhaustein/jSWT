import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

// TODO: Dir based, include file names in outer map.
public class ResourceConverter {

    public static String quote(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch(c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    File inputRoot;
    File outputRoot;
    boolean firstEntry = true;

    ResourceConverter(File inputRoot, File outputRoot) {
        this.inputRoot = inputRoot;
        this.outputRoot = outputRoot;
    }

    void addEntry(String name, Writer writer) throws IOException{
        if (firstEntry) {
            firstEntry = false;
        } else {
            writer.write(",");
        }
        writer.write("\n " + quote(name) + ": ");
    }

    void convertProperties(File file, Writer writer) throws IOException {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(new FileInputStream(file), "utf-8"));

        writer.write("{");
        boolean first = true;
        for (String name: properties.stringPropertyNames()) {
            if (!first) {
                writer.write(",");
            } else {
                first = false;
            }
            writer.write("\n  ");
            writer.write(quote(name));
            writer.write(": ");
            writer.write(quote(properties.getProperty(name)));
        }
        writer.write("\n }");
    }

    void convertDir(String path, Writer writer) throws IOException {
        File dir = new File(inputRoot, path);

        for (File file: dir.listFiles()) {
            String name = file.getName();
            String fullName = path.isEmpty() ? name : (path + "/" + name);
            if (file.isDirectory()) {
                convertDir(path + "/" + name, writer);
            } else if (name.endsWith(".properties")) {
                addEntry(fullName.substring(0, fullName.lastIndexOf('.')), writer);
                convertProperties(file, writer);
            } else if (name.endsWith(".jpg") || name.endsWith(".gif") || name.endsWith(".png") ||
                    name.endsWith(".jpeg")) {
                copyFile(fullName);
            }
        }
    }

    private void copyFile(String path) throws IOException {
        File src = new File(inputRoot, path);
        File dst = new File(outputRoot, path);
        dst.getParentFile().mkdirs();
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dst);

        byte[] buf = new byte[8096];
        while (true) {
            int count = is.read(buf);
            if (count <= 0) {
                break;
            }
            os.write(buf, 0, count);
        }
        is.close();
        os.close();
    }

    void run() throws IOException {
        Writer writer = new OutputStreamWriter(new FileOutputStream(new File(outputRoot, "gwt_resources.js")));
        writer.write("window.gwtResources = {\n");

        convertDir("", writer);

        writer.write("\n};\n");
        writer.close();
    }


    public static void main(String[] args) throws IOException {

        String home = System.getProperty("user.home");
        String inputRoot = home + "/src/jSWT/jswt-demo-core/src/main/resources";
        String outputRoot = home + "/src/jSWT/jswt-demo-gwt/src/main/webapp";

        new ResourceConverter(new File(inputRoot), new File(outputRoot)).run();
    }

}
