package com.productiveedge.content_mgmt_automation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ProcessTest {
    public static void main(String[] args) {
        String jarPath = "C:/Users/Сергей/.m2/repository/com/productiveedge/automation/content-mgmt-automation-jar/1.0-SNAPSHOT/content-mgmt-automation-jar-1.0-SNAPSHOT-jar-with-dependencies.jar";
        String processCommand = "java -jar " + jarPath;

        Scanner scanner = new Scanner(System.in);
        System.out.println("" +
                "1) Созадать папки\n" +
                "2) Grab All links\n" +
                "3) Take screenshot\n");
        int res = scanner.nextInt();
        switch (res) {
            case 1:
                processCommand = processCommand +
                        " command=CREATE_FOLDERS" +
                        " root_folder_path=C://folder";
                break;
            case 2:
                processCommand = processCommand +
                        " command=GRAB_ALL_LINKS" +
                        " domain_name=productiveedge.com" +
                        " url_protocol=https" +
                        " url_port=80" +
                        " maximum_amount_internal_url_to_process=10" +
                        " allow_redirect=false" +
                        " url=https://www.productiveedge.com/";
                break;
            case 3:
                processCommand = processCommand +
                        " command=TAKE_SCREENSHOT" +
                        " browser_name=chrome" +
                        " browser_path=false" +
                        " url=https://www.productiveedge.com/";
                break;
        }
        Process proc;
        try {
            proc = Runtime.getRuntime().exec(processCommand);
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()), 1);
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getInputStream()), 1);
            String line;
            while ((line = inputReader.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = errorReader.readLine()) != null) {
                System.out.println(line);
            }

            inputReader.close();
            errorReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
