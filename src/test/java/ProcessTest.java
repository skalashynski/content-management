import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ProcessTest {
    public static void main(String[] args) {
        String jarPath = "C:/Users/Сергей/.m2/repository/com/productiveedge/automation/content-mgmt-automation-jar/1.0-SNAPSHOT/content-mgmt-automation-jar-1.0-SNAPSHOT.jar";
        String processCommand = "java -jar " + jarPath;

        Scanner scanner = new Scanner(System.in);
        System.out.println("" +
                "1) Созадать папки\n" +
                "2) Grab All links\n");
        int res = scanner.nextInt();
        switch (res) {
            case 1:
                processCommand = processCommand +
                        " command=CREATE_FOLDERS" +
                        " ROOT_FOLDER_PATH=C://folder";
                break;
            case 2:
                processCommand = processCommand +
                        " ";
                break;
        }
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(processCommand);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()), 1);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
